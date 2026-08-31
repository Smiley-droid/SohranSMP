package fr.arcane.sohransmp.events;

import fr.arcane.sohransmp.SohranSMP;
import fr.arcane.sohransmp.disguiseutils.SkinUtil;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.haoshoku.nick.api.NickAPI;

public class DisguiseKeeper implements Listener {

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent e) {
      String name = e.getPlayer().getName();
      if (SohranSMP.PLAYERS_SKIN.contains(name)) {
         SohranSMP.plugin.getLogger().info(name + " rejoint déguisé");
         String disguiseName = String.valueOf(SohranSMP.PLAYERS_SKIN.get(name));
         e.joinMessage(SohranSMP.format("&e" + disguiseName + " joined the game"));
         SkinUtil.disguisePlayer(e.getPlayer(), disguiseName);
      } else {
         SohranSMP.plugin.getLogger().info(name + " n'est pas déguisé");
      }
   }

   @EventHandler
   public void onPlayerLeave(PlayerQuitEvent e) {
      if (NickAPI.isNicked(e.getPlayer())) {
         // On sauvegarde le déguisement courant (nom affiché) sous la clé du vrai nom du joueur
         String currentDisplayName = SkinUtil.stripColor(
               PlainTextComponentSerializer.plainText().serialize(e.getPlayer().displayName())
         );
         SohranSMP.PLAYERS_SKIN.set(NickAPI.getOriginalName(e.getPlayer()), currentDisplayName);
         SohranSMP.plugin.saveConfig();
         SohranSMP.plugin.reloadConfig();
         SohranSMP.plugin.getLogger().info("Déguisement sauvegardé pour " + e.getPlayer().getName());
         // Comme pour le message de connexion, on affiche le pseudo déguisé (et non
         // le vrai pseudo) dans le message de déconnexion.
         e.quitMessage(SohranSMP.format("&e" + currentDisplayName + " left the game"));
      } else {
         SohranSMP.plugin.getLogger().info("Aucun déguisement à sauvegarder pour " + e.getPlayer().getName());
      }
   }
}
