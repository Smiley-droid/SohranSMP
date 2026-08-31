package fr.arcane.sohransmp.commands;

import fr.arcane.sohransmp.SohranSMP;
import fr.arcane.sohransmp.disguiseutils.SkinUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GiveHeadCommand implements CommandExecutor {

   @Override
   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (sender instanceof Player p) {
         if (!p.hasPermission("sohransmp.givehead")) {
            p.sendMessage(SohranSMP.format("&cTu n'as pas la permission d'utiliser cette commande."));
            return true;
         }
         if (args.length >= 1) {
            ItemStack skull = SkinUtil.createHead(args[0]);
            p.getInventory().addItem(skull);
            p.sendMessage(SohranSMP.format("&aVoici la tête de " + args[0]));
         } else {
            p.sendMessage(SohranSMP.format("&cUsage : /givehead <nom d'un joueur>"));
         }
      }
      return false;
   }
}
