package fr.arcane.sohransmp.commands;

import fr.arcane.sohransmp.SohranSMP;
import fr.arcane.sohransmp.disguiseutils.SkinUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.haoshoku.nick.api.NickAPI;

public class UndisguiseCommand implements CommandExecutor {

   @Override
   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (sender instanceof Player p) {
         if (NickAPI.isNicked(p)) {
            SohranSMP.PLAYERS_SKIN.set(NickAPI.getOriginalName(p), NickAPI.getOriginalName(p));
            SohranSMP.plugin.saveConfig();
            SohranSMP.plugin.reloadConfig();
            SkinUtil.resetPlayer(p);
            p.sendMessage(SohranSMP.format("&aTu as bien retiré ton déguisement"));
         } else {
            p.sendMessage(SohranSMP.format("&cTu n'as pas de déguisement"));
         }
      }
      return false;
   }
}
