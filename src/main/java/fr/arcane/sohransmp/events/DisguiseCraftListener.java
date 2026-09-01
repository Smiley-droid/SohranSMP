package fr.arcane.sohransmp.events;

import fr.arcane.sohransmp.SohranSMP;
import fr.arcane.sohransmp.crafting.DisguiseHeadRecipe;
import fr.arcane.sohransmp.disguiseutils.SkinUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class DisguiseCraftListener implements Listener {

   @EventHandler
   public void onInteractWithDisguiseHead(PlayerInteractEvent e) {
      if (e.getHand() != EquipmentSlot.HAND) {
         return;
      }

      ItemStack item = e.getItem();
      if (item == null) {
         return;
      }

      ItemMeta meta = item.getItemMeta();
      if (meta == null || !meta.getPersistentDataContainer().has(DisguiseHeadRecipe.key(SohranSMP.plugin), PersistentDataType.BOOLEAN)) {
         return;
      }

      e.setCancelled(true);
      Player p = e.getPlayer();

      if (!meta.hasDisplayName()) {
         p.sendMessage(SohranSMP.format("&cCette tête est invalide."));
         return;
      }

      String currentName = PlainTextComponentSerializer.plainText().serialize(meta.displayName());

      if (currentName.equals(DisguiseHeadRecipe.DEFAULT_NAME)) {
         p.sendMessage(SohranSMP.format("&cTu dois d'abord renommer cette tête dans une enclume avec le nom du joueur à imiter !"));
         return;
      }

      String targetName = currentName;
      // On retire exactement 1 exemplaire de la tête utilisée.
      item.setAmount(item.getAmount() - 1);

      SkinUtil.disguisePlayer(p, targetName);
      p.sendMessage(SohranSMP.format("&aTu es maintenant déguisé en " + targetName));

      Component message = SohranSMP.format("&e&l[CLIQUE ICI]&r &bPour Retirer ton déguisement ou fais la commande /undisguise")
            .clickEvent(ClickEvent.runCommand("/undisguise"))
            .hoverEvent(HoverEvent.showText(Component.text("Retirer son déguisement", NamedTextColor.YELLOW)));
      p.sendMessage(message);
   }
}
