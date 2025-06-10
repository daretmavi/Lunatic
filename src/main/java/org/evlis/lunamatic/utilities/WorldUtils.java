package org.evlis.lunamatic.utilities;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.Lunamatic;

import java.util.logging.Logger;

public class WorldUtils {
    static Plugin plugin = Lunamatic.getInstance();
    static Logger logger = plugin.getLogger();

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
            logger.warning("Spawn event for world '" + worldName + "' failed! World not initialized.");
            return false;
        }
        return true;
    }
}
