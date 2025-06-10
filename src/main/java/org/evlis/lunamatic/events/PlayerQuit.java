package org.evlis.lunamatic.events;

import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.utilities.PlayerMessage;
import org.evlis.lunamatic.utilities.WorldUtils;
import org.jetbrains.annotations.NotNull;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();
        //++++++++ ADD NULL WORLD CHECK!!! ++++++++//
        if (!WorldUtils.isWorldEnabled(worldName)) {
            return;
        } // END NULL WORLD CHECK..................
        long time = world.getTime();
        @NotNull MoonPhase moonPhase = world.getMoonPhase();

        if (time >= 12980 && time < 24000) {
            if (moonPhase == MoonPhase.FULL_MOON) {
                // debuff the opportunists
                player.clearActivePotionEffects();
            } else if (moonPhase == MoonPhase.NEW_MOON) {
                // debuff the cowards
                player.clearActivePotionEffects();
            }
        }
    }
}
