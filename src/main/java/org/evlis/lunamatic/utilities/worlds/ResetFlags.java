package org.evlis.lunamatic.utilities.worlds;

import org.bukkit.World;
import org.evlis.lunamatic.GlobalVars;

public class ResetFlags {

    static WorldUtils worldUtils = new WorldUtils();

    public static void resetAll(String worldName) {
        GlobalVars.currentMoonStateMap.get(worldName).setHarvestMoonToday(false);
        GlobalVars.currentMoonStateMap.get(worldName).setHarvestMoonNow(false);
        GlobalVars.currentMoonStateMap.get(worldName).setBloodMoonToday(false);
        GlobalVars.currentMoonStateMap.get(worldName).setBloodMoonNow(false);
    }

    public static void resetTickSpeed(World world) {
        worldUtils.setRandomTickSpeed(world, 3);
    }
}
