package fr.arcane.sohransmp.crafting;

import fr.arcane.sohransmp.SohranSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Recette de craft pour la "tête de déguisement" : une tête vierge que
 * n'importe quel joueur (sans besoin de la permission /givehead) peut
 * fabriquer, puis configurer lui-même en la renommant dans une enclume
 * avec le nom du joueur qu'il veut imiter.
 */
public class DisguiseHeadRecipe {

   /** Nom par défaut de l'objet tant qu'il n'a pas été configuré (renommé). */
   public static final String DEFAULT_NAME = "Tête de déguisement";

   /** Clé utilisée pour marquer un item comme étant une tête de déguisement craftée. */
   public static NamespacedKey key(SohranSMP plugin) {
      return new NamespacedKey(plugin, "disguise_head");
   }

   public static void register(SohranSMP plugin) {
      NamespacedKey recipeKey = new NamespacedKey(plugin, "disguise_head_recipe");
      ItemStack result = createBlankHead(plugin);

      ShapelessRecipe recipe = new ShapelessRecipe(recipeKey, result);
      recipe.addIngredient(4, Material.AMETHYST_SHARD);
      // N'importe quelle tête/crâne peut servir de base au craft.
      recipe.addIngredient(new RecipeChoice.MaterialChoice(List.of(
            Material.PLAYER_HEAD,
            Material.ZOMBIE_HEAD,
            Material.SKELETON_SKULL,
            Material.WITHER_SKELETON_SKULL,
            Material.PIGLIN_HEAD,
            Material.DRAGON_HEAD
      )));

      plugin.getServer().addRecipe(recipe);
   }

   private static ItemStack createBlankHead(SohranSMP plugin) {
      ItemStack item = new ItemStack(Material.PLAYER_HEAD);
      ItemMeta meta = item.getItemMeta();
      if (meta instanceof org.bukkit.inventory.meta.SkullMeta skullMeta) {
         // "Kraetos" est un compte Mojang connu dont le skin est un point
         // d'interrogation - utilisé ici comme visuel pour la tête tant
         // qu'elle n'a pas été configurée (renommée) par le joueur.
         org.bukkit.OfflinePlayer kraetos = org.bukkit.Bukkit.getOfflinePlayer("Kraetos");
         if (kraetos.getPlayerProfile() != null) {
            skullMeta.setOwnerProfile(kraetos.getPlayerProfile());
         }
         skullMeta.displayName(Component.text(DEFAULT_NAME, NamedTextColor.LIGHT_PURPLE)
               .decoration(TextDecoration.ITALIC, false));
         skullMeta.lore(List.of(
               Component.text("Renomme cet objet dans une enclume", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
               Component.text("avec le nom du joueur à imiter,", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
               Component.text("puis fais Clic-Droit pour te déguiser.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
         ));
         skullMeta.getPersistentDataContainer().set(key(plugin), PersistentDataType.BOOLEAN, true);
         item.setItemMeta(skullMeta);
      }
      return item;
   }
}
