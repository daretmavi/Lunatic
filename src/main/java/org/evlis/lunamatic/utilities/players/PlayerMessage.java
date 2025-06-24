package org.evlis.lunamatic.utilities.players;

import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PlayerMessage {
    // Send a BOLD message to all players in a list
    public static void Send(Plugin plugin, List<Player> players, String msg, NamedTextColor color) {
        Component message = Component.text(msg)
                .color(color)
                .decoration(TextDecoration.BOLD, true);
        // Thread-safe for Folia
        RegionScheduler scheduler = plugin.getServer().getRegionScheduler();
        for (Player p : players) {
            scheduler.execute(plugin, p.getLocation(), () -> p.sendMessage(message));
        }
    }
    // Send a BOLD message to a specific player
    public static void Send(Plugin plugin, Player player, String msg, NamedTextColor color) {
        Component message = Component.text(msg)
                .color(color)
                .decoration(TextDecoration.BOLD, true);
        player.sendMessage(message);
    }
}
