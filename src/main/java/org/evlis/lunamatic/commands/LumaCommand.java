package org.evlis.lunamatic.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.utilities.worlds.ResetFlags;
import org.jetbrains.annotations.NotNull;
import org.evlis.lunamatic.utilities.LangManager;
import org.evlis.lunamatic.Lunamatic;
import org.evlis.lunamatic.utilities.players.PlayerMessage;

import java.util.logging.Logger;

@CommandAlias("luma")
public class LumaCommand extends BaseCommand {
    private final Plugin plugin; // stores the reference to your main plugin
    private final Logger logger;

    public LumaCommand(Plugin plugin) {
        this.plugin = plugin;
        logger = plugin.getLogger();
    }

    private LangManager getLangManager() {
        return LangManager.getInstance(); // Always fetch the latest instance
    }

    @Default
    public void defCommand(CommandSender sender) {
        // Display GlobalVars status
        sender.sendMessage(getLangManager().getTranslation("cmd_running") + plugin.getPluginMeta().getVersion());
    }

    @Subcommand("reload")
    @CommandPermission("luma.command.reload")
    @Description("Reloads the plugin configuration")
    public void onReload(CommandSender sender) {
        // Display GlobalVars status
        try {
            plugin.reloadConfig();
            Lunamatic.getInstance().loadGlobalConfig();

            LangManager.initialize(plugin, plugin.getDataFolder(), GlobalVars.lang);
            getLangManager().loadTranslations();

            if (!getLangManager().doesTranslationExist(GlobalVars.lang)) {
                logger.info(GlobalVars.lang + " language does NOT exist! Falling back to the default language (en_US).");
                GlobalVars.lang = "en_US"; // Set to default language
                getLangManager().loadTranslations(); // Reload translations
            }

            logger.info(getLangManager().getTranslation("lang_load_success"));
            sender.sendMessage(getLangManager().getTranslation("cmd_reload_success"));
            if (sender instanceof Player) {
                PlayerMessage.Send(plugin, (Player) sender,getLangManager().getTranslation("cmd_reload_warn"), NamedTextColor.YELLOW);
            } else {
                logger.warning(GlobalVars.lang + getLangManager().getTranslation("cmd_reload_warn"));
            }

        } catch (Exception e) {
            sender.sendMessage(getLangManager().getTranslation("cmd_reload_fail") + e.getMessage());
        }
    }

    @Subcommand("status")
    @CommandPermission("luma.command.status")
    @Description("Displays the status of plugin variables")
    public void onStatus(Player player) {
        // this command is player only!!!
        // get the current world & moon state:
        World world = player.getWorld();
        String worldName = world.getName();
        @NotNull MoonPhase moonPhase = world.getMoonPhase();
        // Display GlobalVars status
        player.sendMessage(getLangManager().getTranslation("cmd_curr_phase") + worldName + ": " + moonPhase);
        if(GlobalVars.currentMoonStateMap.containsKey(worldName)) {
            player.sendMessage(getLangManager().getTranslation("cmd_lang") + GlobalVars.lang);
            player.sendMessage(getLangManager().getTranslation("cmd_blood_moon_enabled") + GlobalVars.bloodMoonEnabled);
            player.sendMessage(getLangManager().getTranslation("cmd_blood_moon_now") + GlobalVars.currentMoonStateMap.get(worldName).isBloodMoonNow());
            player.sendMessage(getLangManager().getTranslation("cmd_blood_moon_today") + GlobalVars.currentMoonStateMap.get(worldName).isBloodMoonToday());
            player.sendMessage(getLangManager().getTranslation("cmd_harv_moon_enabled") + GlobalVars.harvestMoonEnabled);
            player.sendMessage(getLangManager().getTranslation("cmd_harv_moon_now") + GlobalVars.currentMoonStateMap.get(worldName).isHarvestMoonNow());
            player.sendMessage(getLangManager().getTranslation("cmd_harv_moon_today") + GlobalVars.currentMoonStateMap.get(worldName).isHarvestMoonToday());
            player.sendMessage(getLangManager().getTranslation("cmd_disabled_worlds") + String.join(", ", GlobalVars.disabledWorlds));
        } else {
            player.sendMessage(getLangManager().getTranslation("cmd_no_moon_world"));
        }
    }

    @Subcommand("nextmoon")
    @CommandPermission("luma.command.nextmoon")
    @Description("Progresses the moon phase forward by one")
    public void nextMoon(Player player) {
        // this command is player only!!!
        // get the current world & moon state:
        World world = player.getWorld();
        String worldName = world.getName();
        if(GlobalVars.currentMoonStateMap.containsKey(worldName)) {
            ResetFlags.resetAll(worldName);
        }
        // Execute mutation of world time inside a global region scheduler for Folia.
        plugin.getServer().getGlobalRegionScheduler().run(plugin, task -> {
            long currentTime = world.getFullTime();
            logger.info("Current game time: " + currentTime);
            world.setFullTime(currentTime + 24000L);
            logger.info("New game time: " + world.getFullTime());
        });
    }

    @Subcommand("makebloodmoon")
    @CommandPermission("luma.command.makebloodmoon")
    @Description("Changes the current world state to Blood Moon")
    public void makeBloodMoon(Player player) {
        // this command is player only!!!
        // get the current world & moon state:
        World world = player.getWorld();
        String worldName = world.getName();
        @NotNull MoonPhase moonPhase = world.getMoonPhase();
        if(GlobalVars.currentMoonStateMap.containsKey(worldName)) {
            ResetFlags.resetAll(worldName);
            if(moonPhase != MoonPhase.NEW_MOON) {
                ResetFlags.resetAll(worldName);
                long moonskip = (long)GlobalVars.newMoonOffset.getOrDefault(moonPhase, 0);
                logger.info("Skipping ahead by " + moonskip + " seconds.");
                // Execute mutation of world time inside a global region scheduler for Folia.
                plugin.getServer().getGlobalRegionScheduler().run(plugin, task -> {
                    world.setFullTime(world.getFullTime() + moonskip);
                });
            }
            if(!GlobalVars.currentMoonStateMap.get(worldName).isBloodMoonToday()){
                GlobalVars.currentMoonStateMap.get(worldName).setBloodMoonToday(true);
            }
            if(world.getTime() >= 13000 && !GlobalVars.currentMoonStateMap.get(worldName).isBloodMoonNow()) {
                GlobalVars.currentMoonStateMap.get(worldName).setBloodMoonNow(true);
            }
        } else {
            player.sendMessage(getLangManager().getTranslation("cmd_no_moon_world"));
        }
    }

    @Subcommand("makeharvestmoon")
    @CommandPermission("luma.command.makeharvestmoon")
    @Description("Changes the current world state to Harvest Moon")
    public void makeHarvestMoon(Player player) {
        // this command is player only!!!
        // get the current world & moon state:
        World world = player.getWorld();
        String worldName = world.getName();
        @NotNull MoonPhase moonPhase = world.getMoonPhase();
        if(GlobalVars.currentMoonStateMap.containsKey(worldName)) {
            ResetFlags.resetAll(worldName);
            if(moonPhase != MoonPhase.FULL_MOON) {
                long moonskip = (long)GlobalVars.fullMoonOffset.getOrDefault(moonPhase, 0);
                logger.info("Skipping ahead by " + moonskip + " seconds.");
                // Execute mutation of world time inside a global region scheduler for Folia.
                plugin.getServer().getGlobalRegionScheduler().run(plugin, task -> {
                    world.setFullTime(world.getFullTime() + moonskip);
                });
            }
            if(!GlobalVars.currentMoonStateMap.get(worldName).isHarvestMoonToday()){
                GlobalVars.currentMoonStateMap.get(worldName).setHarvestMoonToday(true);
            }
            if(world.getTime() >= 13000 && !GlobalVars.currentMoonStateMap.get(worldName).isHarvestMoonNow()) {
                GlobalVars.currentMoonStateMap.get(worldName).setHarvestMoonNow(true);
            }
        } else {
            player.sendMessage(getLangManager().getTranslation("cmd_no_moon_world"));
        }
    }

    @Subcommand("cyclemoonstates")
    @CommandPermission("luma.command.cyclemoonstates")
    @Description("Cycles through all moon states in default world, TESTING ONLY")
    public void cycleEveryMoonState(CommandSender sender) {
        if (sender instanceof Player) {
            sender.sendMessage("This is a console-only DEBUG command.");
            return;
        }
        World world = Bukkit.getWorlds().getFirst();
        String worldName = world.getName(); // Default world name
        @NotNull MoonPhase moonPhase = world.getMoonPhase();
        // set all flags to false
        ResetFlags.resetAll(worldName);
        // cycle through all moon states
        long moonskip = (long)GlobalVars.newMoonOffset.getOrDefault(moonPhase, 0);
        logger.info("Beginning moon state cycling test...");
        long finalMoonskip1 = moonskip;
        plugin.getServer().getGlobalRegionScheduler().run(plugin, task -> {
            world.setTime(0L);
            world.setFullTime(world.getFullTime() + finalMoonskip1);
            GlobalVars.currentMoonStateMap.get(worldName).setBloodMoonToday(true);
            world.setTime(12980L);
            logger.info("Is the moon red? " + GlobalVars.currentMoonStateMap.get(worldName).isBloodMoonNow());
        });
        moonskip = (long)GlobalVars.fullMoonOffset.getOrDefault(moonPhase, 0);
        long finalMoonskip2 = moonskip;
        plugin.getServer().getGlobalRegionScheduler().run(plugin, task -> {
            world.setTime(0L);
            world.setFullTime(world.getFullTime() + finalMoonskip2);
            GlobalVars.currentMoonStateMap.get(worldName).setHarvestMoonToday(true);
            world.setTime(12010L);
            logger.info("Is it harvest time? " + GlobalVars.currentMoonStateMap.get(worldName).isHarvestMoonNow());
        });
        ResetFlags.resetAll(worldName);
    }
}
