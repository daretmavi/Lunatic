package org.evlis.lunamatic.utilities.worlds;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.Lunamatic;
import org.evlis.lunamatic.utilities.LangManager;

import java.util.logging.Logger;

public class WorldUtils {
    static Plugin plugin = Lunamatic.getInstance();
    static Logger logger = plugin.getLogger();
    static LangManager langManager = LangManager.getInstance();

    public void setRandomTickSpeed(World world, int tickSpeed) {
        world.setGameRule(org.bukkit.GameRule.RANDOM_TICK_SPEED, tickSpeed);
    }
    public void setClearSkies(World world, int ticksTilDawn) {
        world.setClearWeatherDuration(ticksTilDawn);
    }

    /**
     * Validates if a world is initialized and not disabled.
     *
     * @param worldName The world name to check.
     * @return true if the world is valid and initialized; false otherwise.
     */
    public static boolean isWorldEnabled(String worldName) {
        if (GlobalVars.disabledWorlds.contains(worldName)) {
            return false;
        } else if (!GlobalVars.currentMoonStateMap.containsKey(worldName)) {
            logger.warning("World check for '" + worldName + "' failed! World not initialized. Fixing...");
            GlobalVars.currentMoonStateMap.put(worldName, new GlobalVars.CurrentMoonState());
            logger.info(langManager.getTranslation("world_load_success") + worldName);
            return false;
        }
        return true;
    }
}
