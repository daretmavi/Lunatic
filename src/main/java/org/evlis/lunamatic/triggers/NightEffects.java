package org.evlis.lunamatic.triggers;

import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.evlis.lunamatic.utilities.players.PlayerMessage;
import org.evlis.lunamatic.utilities.LangManager;

public class NightEffects {
    public static void ApplyMoonlight(Plugin plugin, Player player, MoonPhase moonPhase, Integer timeTilDawn) {
        LangManager langManager = LangManager.getInstance();
        if (moonPhase == MoonPhase.FULL_MOON) {
            Bukkit.getRegionScheduler().execute(plugin, player.getLocation(), () -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, timeTilDawn, 0));
            });
            PlayerMessage.Send(plugin, player, langManager.getTranslation("lucky_feel"), NamedTextColor.DARK_GREEN);
        } else if (moonPhase == MoonPhase.NEW_MOON) {
            Bukkit.getRegionScheduler().execute(plugin, player.getLocation(), () -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, timeTilDawn, 0));
            });
            PlayerMessage.Send(plugin, player, langManager.getTranslation("wary_feel"), NamedTextColor.DARK_PURPLE);
        }
    }
}
