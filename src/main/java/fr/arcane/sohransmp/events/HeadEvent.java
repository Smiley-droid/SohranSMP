package fr.arcane.sohransmp.events;

import fr.arcane.sohransmp.SohranSMP;
import fr.arcane.sohransmp.disguiseutils.SkinUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.haoshoku.nick.api.NickAPI;

public class HeadEvent implements Listener {

   @EventHandler
   public void onInteractWithHead(PlayerInteractEvent e) {
      // Ne traite l'interaction qu'une seule fois (main hand), sinon l'event
      // se déclenche deux fois (main + off hand) sur les versions récentes.
      if (e.getHand() != EquipmentSlot.HAND) {
         return;
      }

      if (e.getItem() != null && e.getItem().getType() == Material.PLAYER_HEAD) {
         Player p = e.getPlayer();
         ItemStack head = e.getItem();
         SkullMeta headMeta = (SkullMeta) head.getItemMeta();
         if (headMeta != null && headMeta.hasDisplayName()) {
            e.setCancelled(true);
            String playerName = PlainTextComponentSerializer.plainText().serialize(headMeta.displayName());
            p.getInventory().remove(head);
            SkinUtil.disguisePlayer(p, playerName);
            p.sendMessage(SohranSMP.format("&aTu es maintenant déguisé en " + playerName));

            Component message = SohranSMP.format("&e&l[CLIQUE ICI]&r &bPour Retirer ton déguisement ou fais la commande /undisguise")
                  .clickEvent(ClickEvent.runCommand("/undisguise"))
                  .hoverEvent(HoverEvent.showText(Component.text("Retirer son déguisement", NamedTextColor.YELLOW)));
            p.sendMessage(message);
         } else {
            p.sendMessage(SohranSMP.format("&cCette tête n'est pas valide"));
         }
      }
   }

   @EventHandler
   public void dropHeadOnDeath(PlayerDeathEvent e) {
      Player victim = e.getEntity();
      SkinUtil.resetPlayer(victim);
      victim.sendMessage(SohranSMP.format("&cTu as perdu ton déguisement en mourrant (rip bozo)"));
      e.getDrops().add(SkinUtil.createHead(NickAPI.getOriginalName(victim)));
   }
}
