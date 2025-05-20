package org.evlis.lunamatic.utilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RandomItems {
    private static class WeightedItem {
        final ItemStack item;
        final double chance; // out of 100

        WeightedItem(ItemStack item, double chance) {
            this.item = item;
            this.chance = chance;
        }
    }

    private static final List<WeightedItem> allayHoldItems = List.of(
            new WeightedItem(new ItemStack(Material.WHEAT), 60.0),
            new WeightedItem(new ItemStack(Material.CARROT), 19.0),
            new WeightedItem(new ItemStack(Material.APPLE), 15.0),
            new WeightedItem(new ItemStack(Material.GOLDEN_CARROT), 3.6),
            new WeightedItem(new ItemStack(Material.GOLDEN_APPLE), 2.2),
            new WeightedItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), 0.2)
    );

    public static ItemStack getAllayHoldItem() {
        double roll = Math.random() * 100;
        double cumulative = 0.0;
        for (WeightedItem entry : allayHoldItems) {
            cumulative += entry.chance;
            if (roll < cumulative) {
                return entry.item;
            }
        }
        return null; // fallback, shouldn't happen if chances total 100
    }
}
