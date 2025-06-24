package org.evlis.lunamatic.utilities.entities;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EquipArmor {

    public static void equipRandomChainmailArmor(Monster monster, int armorCount) {
        // Define the possible pieces of chainmail armor
        List<ItemStack> chainmailArmor = Arrays.asList(
                new ItemStack(Material.CHAINMAIL_HELMET),
                new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                new ItemStack(Material.CHAINMAIL_LEGGINGS),
                new ItemStack(Material.CHAINMAIL_BOOTS)
        );
        // Shuffle the armor list to randomize the selection
        Collections.shuffle(chainmailArmor);
        // Equip the first 'armorCount' pieces of armor, up to a maximum of 4
        for (int i = 0; i < armorCount && i < 4; i++) {
            ItemStack randomArmor = chainmailArmor.get(i);
            equipArmorPiece(monster, randomArmor);
        }
    }

    private static void equipArmorPiece(LivingEntity entity, ItemStack armor) {
        try {
            if (armor == null || entity == null) {
                throw new NullPointerException("Entity or Armor piece is null");
            }
            switch (armor.getType()) {
                case CHAINMAIL_HELMET:
                    entity.getEquipment().setHelmet(armor);
                    break;
                case CHAINMAIL_CHESTPLATE:
                    entity.getEquipment().setChestplate(armor);
                    break;
                case CHAINMAIL_LEGGINGS:
                    entity.getEquipment().setLeggings(armor);
                    break;
                case CHAINMAIL_BOOTS:
                    entity.getEquipment().setBoots(armor);
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e) {
            System.err.println("Failed to equip armor: " + e.getMessage());
        }
    }
}
