package org.evlis.lunamatic.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.Plugin;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.utilities.LangManager;
import org.evlis.lunamatic.utilities.WorldUtils;

import java.util.logging.Logger;

public class WorldLoad implements Listener {
    private final Logger logger;

    public WorldLoad(Plugin plugin) {
        logger = plugin.getLogger();
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        LangManager langManager = LangManager.getInstance();
        World world = event.getWorld();
        String worldName = world.getName();
        //++++++++ SKIP NULL WORLD CHECK HERE ++++++++//
        if (!GlobalVars.disabledWorlds.contains(world.getName())) {
            if (!GlobalVars.currentMoonStateMap.containsKey(world.getName())) {
                GlobalVars.currentMoonStateMap.put(world.getName(), new GlobalVars.CurrentMoonState());
                logger.info(langManager.getTranslation("world_load_success") + world.getName());
            }
        }
    }
}
