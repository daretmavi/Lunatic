package org.evlis.lunamatic.events;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.Lunamatic;
import org.evlis.lunamatic.utilities.NightSummons;
import org.evlis.lunamatic.utilities.WorldUtils;

import java.util.List;
import java.util.logging.Logger;

import static org.evlis.lunamatic.utilities.EquipArmor.equipRandomChainmailArmor;

public class EntitySpawn implements Listener {

    Plugin plugin = Lunamatic.getInstance();
    Logger logger = plugin.getLogger();
    // Custom detection range in blocks (default is 16 for most mobs)
    private static final double BLODMOON_DETECTION_RANGE = 32.0;
    private final NightSummons nightsummon = new NightSummons(plugin);

    int MAX_ALLAYS_PER_WORLD = 20;
    int MAX_VEXES_PER_WORLD = 20;

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();
        String worldName = world.getName();
        //++++++++ ADD NULL WORLD CHECK!!! ++++++++//
        if (!WorldUtils.isWorldEnabled(worldName)) {
            return;
        } // END NULL WORLD CHECK..................
        if (GlobalVars.currentMoonStateMap.get(worldName).isBloodMoonToday() && GlobalVars.currentMoonStateMap.get(worldName).isBloodMoonNow()) {
            Difficulty difficulty = world.getDifficulty();
            long time = world.getTime();
            if (entity instanceof Monster) { // Check if the entity is a hostile mob
                Monster monster = (Monster) entity;
                int ticksTilDawn = 24000 - (int)time;
                if (monster instanceof Creeper && world.getEntitiesByClass(Vex.class).size() < MAX_VEXES_PER_WORLD && GlobalVars.bloodMoonSpawnVex) {
                    event.setCancelled(true);
                    nightsummon.SummonVex(event.getLocation(), ticksTilDawn);
                }
                Location mobLocation = monster.getLocation();
                Player nearestPlayer = findNearestPlayer(mobLocation);
                if (nearestPlayer != null) {
                    // Get monster modifiers based on world difficulty
                    int potionAmp = GlobalVars.difficultyPotionMap.getOrDefault(difficulty, 0);
                    int armorCount = GlobalVars.difficultyArmorMap.getOrDefault(difficulty, 1);
                    // Emmiters to consider: SCULK_SOUL, PORTAL, WITCH, WHITE_ASH
                    monster.getWorld().spawnParticle(Particle.WITCH, mobLocation, 30);
                    // make all monsters faster and expand field of view
                    monster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, ticksTilDawn, potionAmp));
                    monster.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, ticksTilDawn, potionAmp));
                    // equip a random chainmail armor
                    equipRandomChainmailArmor(monster, armorCount);
                    // target nearest player
                    monster.setTarget(nearestPlayer);
                }
            }
        } else if (GlobalVars.currentMoonStateMap.get(worldName).isHarvestMoonToday() && GlobalVars.currentMoonStateMap.get(worldName).isHarvestMoonNow()) {
            long time = world.getTime();
            int ticksTilDawn = 24000 - (int)time;
            // don't allow monster spawning during harvest moon
            if (entity instanceof Monster) {
                Monster monster = (Monster) entity;
                event.setCancelled(true);
                if (monster instanceof Creeper && world.getEntitiesByClass(Allay.class).size() < MAX_ALLAYS_PER_WORLD && GlobalVars.harvestMoonSpawnAllay) {
                    nightsummon.SummonAllay(event.getLocation(), ticksTilDawn);
                }
            } else if (entity instanceof Bee bee) {
                // stop bees from entering hives during harvest moon
                bee.setCannotEnterHiveTicks(ticksTilDawn);
                // give the bee nectar so it can pollinate
                bee.setHasNectar(true);
                // reset bee pollination count
                bee.setCropsGrownSincePollination(0);
            }
        }
    }

    private Player findNearestPlayer(Location mobLocation) {
        List<Player> players = mobLocation.getWorld().getPlayers(); // Get all players in the world

        Player nearestPlayer = null;
        double nearestDistanceSquared = BLODMOON_DETECTION_RANGE * BLODMOON_DETECTION_RANGE;

        for (Player player : players) {
            double distanceSquared = player.getLocation().distanceSquared(mobLocation);
            if (distanceSquared <= nearestDistanceSquared) {
                nearestPlayer = player;
                nearestDistanceSquared = distanceSquared; // Update the closest distance
            }
        }
        return nearestPlayer; // Returns null if no players are within the custom detection range
    }
}
