package org.evlis.lunamatic.utilities.players;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.evlis.lunamatic.GlobalVars;

import java.util.List;

public class TargetPlayer {

    double radius = GlobalVars.bloodmoonDetectionRange;

    public void Attack(Player player) {
        List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
    }
}
