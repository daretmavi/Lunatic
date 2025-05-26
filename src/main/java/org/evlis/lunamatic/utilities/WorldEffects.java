package org.evlis.lunamatic.utilities;

import org.bukkit.World;

public class WorldEffects {
    public void setRandomTickSpeed(World world, int tickSpeed) {
        world.setGameRule(org.bukkit.GameRule.RANDOM_TICK_SPEED, tickSpeed);
    }
    public void setClearSkies(World world, int ticksTilDawn) {
        world.setClearWeatherDuration(ticksTilDawn);
    }
}
