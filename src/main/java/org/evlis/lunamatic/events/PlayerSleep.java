package org.evlis.lunamatic.events;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.Plugin;
import org.evlis.lunamatic.GlobalVars;
import org.evlis.lunamatic.Lunamatic;
import org.evlis.lunamatic.utilities.players.PlayerMessage;
import org.evlis.lunamatic.utilities.worlds.WorldUtils;
import org.evlis.lunamatic.utilities.LangManager;

public class PlayerSleep implements Listener {
    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        LangManager langManager = LangManager.getInstance();
        Plugin plugin = Lunamatic.getInstance();
        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();
        //++++++++ ADD NULL WORLD CHECK!!! ++++++++//
        if (!WorldUtils.isWorldEnabled(worldName)) {
            return;
        } // END NULL WORLD CHECK..................
        if (GlobalVars.currentMoonStateMap.get(world.getName()).isBloodMoonNow() && !GlobalVars.bloodMoonAllowSleep) {
            PlayerMessage.Send(plugin, player, langManager.getTranslation("blood_moon_sleep"), NamedTextColor.RED);
            event.setCancelled(true);
        }
    }
}
