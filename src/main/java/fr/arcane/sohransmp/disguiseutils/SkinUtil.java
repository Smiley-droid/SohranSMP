package fr.arcane.sohransmp.disguiseutils;

import fr.arcane.sohransmp.SohranSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.haoshoku.nick.api.NickAPI;

import java.util.Collections;
import java.util.List;

public class SkinUtil {

   /**
    * Déguise un joueur avec le nom (potentiellement coloré) fourni.
    * Les codes couleur sont retirés avant d'être passés à NickAPI, comme dans la version d'origine.
    */
   public static void disguisePlayer(Player p, String sname) {
      String name = stripColor(sname);

      p.displayName(Component.text(name));

      // NickAPI a été entièrement recodée en version 7.0 : les méthodes ont été renommées.
      // Ancien   -> Nouveau
      // nick               -> setNick
      // setGameProfileName -> setProfileName
      // resetGameProfileName -> resetProfileName
      // (setSkin, setUniqueId, resetNick, resetSkin, resetUniqueId, refreshPlayer inchangés)
      NickAPI.setNick(p, name);
      NickAPI.setSkin(p, name);
      NickAPI.setProfileName(p, name);
      NickAPI.setUniqueId(p, name);
      NickAPI.refreshPlayer(p);
   }

   public static void resetPlayer(Player p) {
      NickAPI.resetNick(p);
      NickAPI.resetSkin(p);
      NickAPI.resetProfileName(p);
      NickAPI.resetUniqueId(p);
      NickAPI.refreshPlayer(p);
      p.displayName(Component.text(p.getName()));
   }

   public static ItemStack createHead(String ownerName) {
      ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
      SkullMeta headMeta = (SkullMeta) skull.getItemMeta();
      OfflinePlayer pa = Bukkit.getOfflinePlayer(ownerName);
      if (headMeta != null && pa.getPlayerProfile() != null) {
         headMeta.setOwnerProfile(pa.getPlayerProfile());
         headMeta.displayName(SohranSMP.format("&e" + ownerName));
         List<Component> lore = Collections.singletonList(
               SohranSMP.format("&8Fais &lClick-Droit&r&8 avec cette tête pour te déguiser en le joueur qu'elle représente")
         );
         headMeta.lore(lore);
         skull.setItemMeta(headMeta);
         return skull;
      } else {
         return skull;
      }
   }

   /**
    * Retire toute mise en forme (couleurs, gras, etc.) d'une chaîne pour ne
    * garder que le texte brut. equivalent moderne de l'ancien ChatColor.stripColor,
    * qui gérait le caractère de code couleur '§' (ChatColor.COLOR_CHAR).
    */
   public static String stripColor(String input) {
      if (input == null) {
         return "";
      }
      Component parsed = LegacyComponentSerializer.legacySection().deserialize(input);
      return PlainTextComponentSerializer.plainText().serialize(parsed);
   }
}
