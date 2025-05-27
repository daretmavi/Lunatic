package org.evlis.lunamatic.triggers;

import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.utilities.PlayerMessage;
import org.evlis.lunamatic.utilities.ResetFlags;
import org.evlis.lunamatic.utilities.WorldEffects;
import org.evlis.lunamatic.utilities.LangManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Scheduler {
    private LangManager getTranslationManager() {
        return LangManager.getInstance(); // Always fetch the latest instance
    }

    @ApiStatus.Experimental
    public static void runGlobalDelayed(Plugin plugin, Runnable task, long delay) {
        plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, t -> task.run(), delay);
    }

    @ApiStatus.Experimental
    private void runWorldDelayed(Plugin plugin, World world, Runnable task, long delay) {
        plugin.getServer().getRegionScheduler().runDelayed(
                plugin,
                world.getSpawnLocation(),
                t -> task.run(),
                delay
        );
    }

    public void StartMoonSchedule(Plugin plugin) {
        GlobalRegionScheduler globalRegionScheduler = plugin.getServer().getGlobalRegionScheduler();
        // get methods for Harvest moon
        WorldEffects worldEffects = new WorldEffects();
        // generate new dice
        Random r = new Random();
        // initialize logger
        Logger logger = plugin.getLogger();
        // get translations
        LangManager lang = getTranslationManager();

        globalRegionScheduler.runAtFixedRate(plugin, (t)-> {
            for (World world : Bukkit.getWorlds()) {
                // Skip disabled worlds
                if (GlobalVars.disabledWorlds.contains(world.getName())) continue;
                // Check if the world has active players
                List<Player> playerList = world.getPlayers();
                if (playerList.isEmpty()) {
                    continue; // Skip worlds with no active players
                }
                if (GlobalVars.disabledWorlds.contains(world.getName())) {
                    continue; // Skip worlds on the disabled list
                }

                long time = world.getTime();
                String worldName = world.getName();
                GlobalVars.CurrentMoonState state = GlobalVars.getMoonState(world);
                // Check if it's the start of the day (0 ticks, 6am)
                if (time >= 0 && time < 20) {
                    if (GlobalVars.debug) {
                        logger.info(getTranslationManager().getTranslation("sched_daydef_reset"));
                    }
                    // Reset defaults every dawn
                    ResetFlags.resetAll(worldName);
                    ResetFlags.resetTickSpeed(world);

                    // get the moon phase tonight
                    @NotNull MoonPhase moonPhase = world.getMoonPhase();
                    // handle debugging flag
                    if (moonPhase == MoonPhase.FULL_MOON && GlobalVars.fullMoonEnabled) {
                        // Do a dice roll to check if we're getting a harvest moon?
                        int chance = r.nextInt(GlobalVars.harvestMoonDieSides);
                        if (chance == 0 && GlobalVars.harvestMoonEnabled) {
                            GlobalVars.currentMoonStateMap.get(worldName).setHarvestMoonToday(true);
                            PlayerMessage.Send(plugin, playerList, getTranslationManager().getTranslation("harvest_moon_tonight"), NamedTextColor.GOLD);
                        } else {
                            PlayerMessage.Send(plugin, playerList, getTranslationManager().getTranslation("full_moon_tonight"), NamedTextColor.YELLOW);
                        }
                    } else if (moonPhase == MoonPhase.NEW_MOON && GlobalVars.newMoonEnabled) {
                        // Do a dice roll to check if the players are THAT unlucky..
                        int chance = r.nextInt(GlobalVars.bloodMoonDieSides);
                        if (chance == 0 && GlobalVars.bloodMoonEnabled) {
                            GlobalVars.currentMoonStateMap.get(worldName).setBloodMoonToday(true);
                            PlayerMessage.Send(plugin, playerList, getTranslationManager().getTranslation("blood_moon_tonight"), NamedTextColor.DARK_RED);
                        } else {
                            PlayerMessage.Send(plugin, playerList, getTranslationManager().getTranslation("new_moon_tonight"), NamedTextColor.DARK_GRAY);
                        }
                    }
                }
                // Execute immediately after sunset starts
                if (time >= 12010 && time < 12030) {
                    if (GlobalVars.currentMoonStateMap.get(worldName).isHarvestMoonToday() || GlobalVars.currentMoonStateMap.get(worldName).isHarvestMoonNow()) {
                        if (GlobalVars.currentMoonStateMap.get(worldName).isHarvestMoonToday() && !GlobalVars.currentMoonStateMap.get(worldName).isHarvestMoonNow()) {
                            GlobalVars.currentMoonStateMap.get(worldName).setHarvestMoonNow(true);
                            // Ensure global var reset
                            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                                ResetFlags.resetTickSpeed(world);
                            }, 24000 - (int)time);
                            plugin.getServer().getScheduler().runTaskLater(plugin, () -> ResetFlags.resetAll(world.getName()), 24000 - (int)time);
                            worldEffects.setRandomTickSpeed(world, 30);
                            worldEffects.setClearSkies(world, (24000 - (int)time));
                            PlayerMessage.Send(plugin, playerList, getTranslationManager().getTranslation("grass_growing"), NamedTextColor.GOLD);
                        } else { // if for some reason both flags are still true, we have an invalid state
                            logger.warning(getTranslationManager().getTranslation("sched_invalid_harv"));
                            GlobalVars.currentMoonStateMap.get(worldName).setHarvestMoonToday(false);
                            GlobalVars.currentMoonStateMap.get(worldName).setHarvestMoonNow(false);
                        }
                    }
                }
                // Execute exactly at the start of night
                if (time >= 12980 && time < 13000) {
                    @NotNull MoonPhase moonPhase = world.getMoonPhase();
                    for (Player p : playerList) {
                        NightEffects.ApplyMoonlight(plugin, p, moonPhase, (24000 - (int)time));
                    }
                    if (GlobalVars.currentMoonStateMap.get(worldName).isBloodMoonToday() || GlobalVars.currentMoonStateMap.get(worldName).isBloodMoonNow()) {
                        if (GlobalVars.currentMoonStateMap.get(worldName).isBloodMoonToday() && !GlobalVars.currentMoonStateMap.get(worldName).isBloodMoonNow()) {
                            GlobalVars.currentMoonStateMap.get(worldName).setBloodMoonNow(true);
                            // Ensure global var reset
                            plugin.getServer().getScheduler().runTaskLater(plugin, () -> ResetFlags.resetAll(world.getName()), 24000 - (int)time);
                        } else { // if for some reason both flags are still true, we have an invalid state
                            logger.warning(getTranslationManager().getTranslation("sched_invalid_blood"));
                            GlobalVars.currentMoonStateMap.get(worldName).setBloodMoonToday(false);
                            GlobalVars.currentMoonStateMap.get(worldName).setBloodMoonNow(false);
                        }
                    }
                }
            }
        }, 1L, 20L); // Check every 20 ticks (1 second)
    }
}
