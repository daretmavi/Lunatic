package org.evlis.lunamatic;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.evlis.lunamatic.commands.LumaCommand;
import org.evlis.lunamatic.events.*;
import org.evlis.lunamatic.triggers.Scheduler;
import org.evlis.lunamatic.utilities.LangManager;
import org.evlis.lunamatic.utilities.LogHandler;

import java.io.InputStreamReader;
import java.lang.module.ModuleDescriptor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class Lunamatic extends JavaPlugin {
    private static Lunamatic instance;
    public LangManager langManager;

    public TimeSkip timeSkip;
    public PlayerJoin playerJoin;
    public PlayerQuit playerQuit;
    public PlayerSleep playerSleep;
    public EntitySpawn entitySpawn;
    private static final Logger logger = Logger.getLogger(Lunamatic.class.getName());
    private static final String REQUIRED_VERSION = "1.21";
    public static final int REQUIRED_LANG_VER = 2;
    private static final String API_URL ="https://api.modrinth.com/v2/project/lunamatic/version";

    @Override
    public void onEnable() {
        // Begin Initialization
        //Logger logger = getLogger();
        logger.info("Begin plugin initialization...");
        logger.setUseParentHandlers(false); // Disable parent handlers to avoid duplicate logging
        LogHandler handler = new LogHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);

        // Assign instance variable
        instance = this;

        // Get versions
        String serverVersion = getServer().getMinecraftVersion();
        String currentVersion = this.getPluginMeta().getVersion();
        // Check server version, log error if not supported.
        if (!serverVersion.startsWith(REQUIRED_VERSION)) {
            logger.info("Unsupported server version detected! Expected ver: " + REQUIRED_VERSION + ", Your version: " + serverVersion);
        }
        // Config Initialization
        saveDefaultConfig();
        loadGlobalConfig();

        // Load translations
        LangManager.initialize(this, getDataFolder(), GlobalVars.lang);
        langManager = LangManager.getInstance();
        langManager.saveDefaultTranslations();
        langManager.loadTranslations();

        if (!langManager.doesTranslationExist(GlobalVars.lang)) {
            logger.info(GlobalVars.lang + " language does NOT exist! Falling back to the default language (en_US).");
            GlobalVars.lang = "en_US"; // Set to default language
            langManager.loadTranslations(); // Reload translations
        }

        logger.info(langManager.getTranslation("lang_load_success"));

        // Update check
        if (GlobalVars.checkUpdates) {
            checkForUpdates(currentVersion);
        }
        // Class Initialization
        Scheduler schedule = new Scheduler();
        // defer moon state initialization until first server tick
        getServer().getGlobalRegionScheduler().run(this, (server) -> {
            GlobalVars.initializeWorldSettings();
            logger.info(langManager.getTranslation("world_load_success") + GlobalVars.currentMoonStateMap.keySet());
        });
        timeSkip = new TimeSkip();
        playerJoin = new PlayerJoin();
        playerQuit = new PlayerQuit();
        playerSleep = new PlayerSleep();
        entitySpawn = new EntitySpawn();
        Bukkit.getServer().getPluginManager().registerEvents(timeSkip, this);
        Bukkit.getServer().getPluginManager().registerEvents(playerJoin, this);
        Bukkit.getServer().getPluginManager().registerEvents(playerQuit, this);
        Bukkit.getServer().getPluginManager().registerEvents(playerSleep, this);
        Bukkit.getServer().getPluginManager().registerEvents(entitySpawn, this);
        registerCommands();

        schedule.StartMoonSchedule(this);
        // Notify of successful plugin start
        logger.info(langManager.getTranslation("plugin_success_load") + currentVersion);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.info("Lunamatic has been disabled.");
    }

    public void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new LumaCommand(this));
    }

    public void loadGlobalConfig() {
        try {
            // Set default values for missing keys
            getConfig().addDefault("checkForUpdates", true);
            getConfig().addDefault("lang", "en_US");
            getConfig().addDefault("disabledWorlds", new ArrayList<>()); // Default to an empty list
            getConfig().addDefault("fullMoonEnabled", true);
            getConfig().addDefault("newMoonEnabled", true);
            getConfig().addDefault("harvestMoonEnabled", true);
            getConfig().addDefault("harvestMoonSpawnAllay", true);
            getConfig().addDefault("bloodMoonEnabled", true);
            getConfig().addDefault("bloodMoonSpawnVex", true);
            getConfig().addDefault("bloodMoonDieSides", 2);
            getConfig().addDefault("harvestMoonDieSides", 2);

            // Apply defaults if missing
            getConfig().options().copyDefaults(true);
            saveConfig();

            // Load values into GlobalVars
            GlobalVars.checkUpdates = getConfig().getBoolean("checkForUpdates");
            GlobalVars.lang = getConfig().getString("lang");
            GlobalVars.disabledWorlds = getConfig().getStringList("disabledWorlds");
            GlobalVars.fullMoonEnabled = getConfig().getBoolean("fullMoonEnabled");
            GlobalVars.newMoonEnabled = getConfig().getBoolean("newMoonEnabled");
            GlobalVars.harvestMoonEnabled = getConfig().getBoolean("harvestMoonEnabled");
            GlobalVars.harvestMoonSpawnAllay = getConfig().getBoolean("harvestMoonSpawnAllay");
            GlobalVars.bloodMoonEnabled = getConfig().getBoolean("bloodMoonEnabled");
            GlobalVars.bloodMoonSpawnVex = getConfig().getBoolean("bloodMoonSpawnVex");
            GlobalVars.bloodMoonDieSides = getConfig().getInt("bloodMoonDieSides");
            GlobalVars.harvestMoonDieSides = getConfig().getInt("harvestMoonDieSides");
        } catch (Exception e) {
            logger.info("Failed to load configuration! Disabling plugin. Error: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void checkForUpdates(String currentVersionString) {
        logger.info(langManager.getTranslation("update_check"));
        try {
            // Open a connection to the API
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Parse the response
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            // Get the first item in the JSON array
            JsonElement latestVersionElement = jsonArray.get(0);
            String latestVersionString = latestVersionElement.getAsJsonObject().get("version_number").getAsString();

            // Compare versions
            ModuleDescriptor.Version latestVersion = ModuleDescriptor.Version.parse(latestVersionString);
            ModuleDescriptor.Version currentVersion = ModuleDescriptor.Version.parse(currentVersionString);
            if (currentVersion.compareTo(latestVersion) < 0) {
                logger.info(langManager.getTranslation("update_found").replace("%a",latestVersionString).replace("%b",currentVersionString));
            }
            else {
                logger.info(langManager.getTranslation("up_to_date"));
            }

            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            logger.info(langManager.getTranslation("update_error"));
        }
    }

    public static Lunamatic getInstance() {
        return instance;
    }
}
