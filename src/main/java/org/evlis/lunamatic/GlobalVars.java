package org.evlis.lunamatic;

import io.papermc.paper.world.MoonPhase;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalVars {
    // Map of all enabled world states
    public static final Map<String, CurrentMoonState> currentMoonStateMap = new ConcurrentHashMap<>();
    // Plugin initialization of all enabled worlds
    public static void initializeWorldSettings() {
        for (World world : Bukkit.getWorlds()) {
            if (!disabledWorlds.contains(world.getName())) {
                currentMoonStateMap.put(world.getName(), new CurrentMoonState());
            }
        }
    }
    // Null safe moon state getter
    public static CurrentMoonState getMoonState(@NotNull World world) {
        return currentMoonStateMap.getOrDefault(world.getName(), new CurrentMoonState());
    }
    // Test flag
    public static Boolean debug = false;
    // Language version increment
    public static Integer lang_vers = 3;
    // Check for updates?
    public static Boolean checkUpdates = true;
    // Default language for the plugin
    public static String lang = "en_US";
    // how far should monsters engage the player from during a blood moon?
    public static final double bloodmoonDetectionRange = 32.0;
    // worlds to exclude entirely from moon effects
    public static List<String> disabledWorlds = List.of();
    // Dice sides for blood & harvest
    public static Integer bloodMoonDieSides = 2;
    public static Integer harvestMoonDieSides = 2;
    // enabled moons:
    public static Boolean fullMoonEnabled = true;
    public static Boolean harvestMoonEnabled = true;
    public static Boolean harvestMoonSpawnAllay = true;
    public static Boolean newMoonEnabled = true;
    public static Boolean bloodMoonEnabled = true;
    public static Boolean bloodMoonSpawnVex = true;
    // map of how many armor pieces to apply
    public static final Map<Difficulty, Integer> difficultyArmorMap = Map.of(
            Difficulty.PEACEFUL, 0,
            Difficulty.EASY, 2,
            Difficulty.NORMAL, 3,
            Difficulty.HARD, 4
    );
    // map of what level potion effect mobs should get
    public static final Map<Difficulty, Integer> difficultyPotionMap = Map.of(
            Difficulty.PEACEFUL, 0,
            Difficulty.EASY, 0,
            Difficulty.NORMAL, 1,
            Difficulty.HARD, 2
    );
    // map for time between current moon phase and the next new moon
    public static final Map<MoonPhase, Integer> newMoonOffset = Map.of(
            MoonPhase.FULL_MOON, 96000,
            MoonPhase.WANING_GIBBOUS, 72000,
            MoonPhase.LAST_QUARTER, 48000,
            MoonPhase.WANING_CRESCENT, 24000,
            MoonPhase.NEW_MOON, 0,
            MoonPhase.WAXING_CRESCENT, 168000,
            MoonPhase.FIRST_QUARTER, 144000,
            MoonPhase.WAXING_GIBBOUS, 120000
    );
    // map for time between current moon phase and the next full moon
    public static final Map<MoonPhase, Integer> fullMoonOffset = Map.of(
            MoonPhase.FULL_MOON, 0,
            MoonPhase.WANING_GIBBOUS, 168000,
            MoonPhase.LAST_QUARTER, 144000,
            MoonPhase.WANING_CRESCENT, 120000,
            MoonPhase.NEW_MOON, 96000,
            MoonPhase.WAXING_CRESCENT, 72000,
            MoonPhase.FIRST_QUARTER, 48000,
            MoonPhase.WAXING_GIBBOUS, 24000
    );
    //============ PER-WORLD SETTINGS ============//
    public static class CurrentMoonState {
        // is there a harvest moon today?
        private boolean harvestMoonToday;
        // is there a harvest moon RIGHT NOW?
        private boolean harvestMoonNow;
        // is there a blood moon today?
        private boolean bloodMoonToday;
        // is there a blood moon RIGHT NOW?
        private boolean bloodMoonNow;
        // Initialize all flags to false
        public CurrentMoonState() {
            harvestMoonToday = false;
            harvestMoonNow = false;
            bloodMoonToday = false;
            bloodMoonNow = false;
        }
        // Getters
        public boolean isHarvestMoonToday() {
            return harvestMoonToday;
        }
        public boolean isHarvestMoonNow() {
            return harvestMoonNow;
        }
        public boolean isBloodMoonToday() {
            return bloodMoonToday;
        }
        public boolean isBloodMoonNow() {
            return bloodMoonNow;
        }
        // Setters
        public void setHarvestMoonToday(boolean harvestMoonToday) {
            this.harvestMoonToday = harvestMoonToday;
        }
        public void setHarvestMoonNow(boolean harvestMoonNow) {
            this.harvestMoonNow = harvestMoonNow;
        }
        public void setBloodMoonToday(boolean bloodMoonToday) {
            this.bloodMoonToday = bloodMoonToday;
        }
        public void setBloodMoonNow(boolean bloodMoonNow) {
            this.bloodMoonNow = bloodMoonNow;
        }
    }
    //============================================//
}
