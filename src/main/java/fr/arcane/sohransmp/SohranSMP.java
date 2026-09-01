package fr.arcane.sohransmp;

import fr.arcane.sohransmp.commands.GiveHeadCommand;
import fr.arcane.sohransmp.commands.UndisguiseCommand;
import fr.arcane.sohransmp.crafting.DisguiseHeadRecipe;
import fr.arcane.sohransmp.events.DisguiseCraftListener;
import fr.arcane.sohransmp.events.DisguiseKeeper;
import fr.arcane.sohransmp.events.HeadEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SohranSMP extends JavaPlugin {

   public static SohranSMP plugin;
   public static FileConfiguration config;
   public static ConfigurationSection PLAYERS_SKIN;

   @Override
   public void onEnable() {
      plugin = this;
      config = this.getConfig();
      if (!config.contains("disguises")) {
         config.createSection("disguises");
      }

      this.saveConfig();
      PLAYERS_SKIN = config.getConfigurationSection("disguises");
      this.saveConfig();

      this.getServer().getPluginManager().registerEvents(new HeadEvent(), this);
      this.getServer().getPluginManager().registerEvents(new DisguiseKeeper(), this);
      this.getServer().getPluginManager().registerEvents(new DisguiseCraftListener(), this);
      DisguiseHeadRecipe.register(this);

      var undisguiseCommand = this.getCommand("undisguise");
      var giveHeadCommand = this.getCommand("givehead");
      if (undisguiseCommand != null) {
         undisguiseCommand.setExecutor(new UndisguiseCommand());
      } else {
         getLogger().warning("La commande /undisguise n'a pas pu être enregistrée !");
      }
      if (giveHeadCommand != null) {
         giveHeadCommand.setExecutor(new GiveHeadCommand());
      } else {
         getLogger().warning("La commande /givehead n'a pas pu être enregistrée !");
      }
   }

   @Override
   public void onDisable() {
      this.saveConfig();
   }

   /**
    * Convertit une chaîne avec des codes couleur legacy ('&amp;a', '&amp;c', ...)
    * en Component Adventure, l'API de texte requise depuis longtemps sur Paper
    * (ChatColor / BaseComponent de BungeeCord sont deprecated et retirés à terme).
    */
   public static Component format(String s) {
      return LegacyComponentSerializer.legacyAmpersand().deserialize(s);
   }
}
