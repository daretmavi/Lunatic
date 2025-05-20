package org.evlis.lunamatic.utilities;

import org.bukkit.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.EntityType;

import java.util.function.Consumer;

import static org.evlis.lunamatic.utilities.RandomItems.getAllayHoldItem;

public class NightSummons {
    private final Plugin plugin;

    public NightSummons(Plugin plugin) {
        this.plugin = plugin;
    }

    public void SummonVex (Location location, long despawnDelay) {
        tempEntitySpawn(location, EntityType.VEX, Particle.ASH, despawnDelay, entity -> {});
    }
    public void SummonAllay (Location location, long despawnDelay) {
        tempEntitySpawn(location, EntityType.ALLAY, Particle.BUBBLE_POP, despawnDelay, entity -> {
            if (entity instanceof Allay allay) {
                ItemStack item = getAllayHoldItem();
                if (item != null) {
                    allay.getEquipment().setItemInMainHand(item);
                }
            }
        });
    }

    private void tempEntitySpawn(Location location, EntityType creature, Particle despawnParticle, long despawnDelay, Consumer<Entity> callback) {
        Bukkit.getRegionScheduler().execute(plugin, location, () -> {
            Entity entity = location.getWorld().spawnEntity(location, creature);
            if (entity instanceof LivingEntity alive) {
                alive.setRemoveWhenFarAway(true);
            }
            // Call back with the created entity
            callback.accept(entity);

            // Schedule despawn with particle effect
            Bukkit.getRegionScheduler().runDelayed(plugin, location, delayed -> {
                if (!entity.isDead()) {
                    // Spawn particle effects at the entity's despawn location
                    entity.getWorld().spawnParticle(despawnParticle, entity.getLocation(), 24, 0.4, 0.6, 0.4);
                    entity.remove();
                }
            }, despawnDelay);
        });
    }
}
