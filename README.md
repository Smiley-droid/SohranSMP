# SohranSMP (KamoofSMP) — Portage Paper 26.2

Portage du plugin d'origine (Paper 1.20 / 1.20.1) vers **Paper 26.2** (Minecraft "Chaos Cubed", sortie le 16 juin 2026).

## Ce qui a changé

| Sujet | Avant (1.20) | Maintenant (26.2) |
|---|---|---|
| Java | 8 | **25** (requis par Paper 26.1+) |
| Dépendance Paper | `spigot-api 1.20-R0.1-SNAPSHOT` via hub.spigotmc.org | `paper-api 26.2.build.119-stable` via `repo.papermc.io` |
| `plugin.yml` | `api-version: 1.19` | `api-version: '26.2'` |
| Textes / couleurs | `ChatColor.translateAlternateColorCodes` (`String`) | `net.kyori.adventure.text.Component` (API Adventure) |
| Messages cliquables | `net.md_5.bungee.api.chat.*` (deprecated, en voie de suppression) | `Component.clickEvent(...)` / `HoverEvent.showText(...)` |
| Nom d'affichage / lore des têtes | `SkullMeta.setDisplayName(String)` (deprecated) | `SkullMeta.displayName(Component)` / `lore(List<Component>)` |
| `ItemStack` tête | `new ItemStack(Material.PLAYER_HEAD, 1, (short)3)` (constructeur à durability supprimé) | `new ItemStack(Material.PLAYER_HEAD)` |
| Message de join | `PlayerJoinEvent#setJoinMessage(String)` (deprecated) | `PlayerJoinEvent#joinMessage(Component)` |
| NickAPI | dépendance `system` sur un `.jar` local (`C:/BuildTool/...`), version 6.5.3 | dépendance Maven normale sur le dépôt officiel `xyz.haoshoku.nick:nickapi:7.11` |

## ⚠️ Point d'attention important : NickAPI

Le plugin dépend entièrement de **NickAPI** (par Haoshoku) pour changer le skin/pseudo/UUID d'un joueur.

- Entre la version 6.5.3 (utilisée par le plugin d'origine) et la version 7.x, l'auteur a **entièrement recodé l'API** et **renommé plusieurs méthodes** :
  - `nick(...)` → `setNick(...)`
  - `setGameProfileName(...)` → `setProfileName(...)`
  - `resetGameProfileName(...)` → `resetProfileName(...)`
  - `setSkin`, `setUniqueId`, `resetNick`, `resetSkin`, `resetUniqueId`, `refreshPlayer` : noms inchangés.
- J'ai gardé `NickAPI.isNicked(Player)` et `NickAPI.getOriginalName(Player)` tels quels car ils existaient dans la version d'origine et rien n'indique qu'ils aient été renommés, **mais je n'ai pas pu consulter l'intégralité de la Javadoc à jour** (le site de l'auteur bloque l'accès automatisé). **Vérifiez ces deux appels en premier si la compilation échoue.**
- **Compatibilité 26.2** : au moment de ce portage, la dernière version publique de NickAPI est la **7.11** (31 janvier 2026), officiellement annoncée pour 1.8.8 / 1.20.2+ / 1.21. Minecraft 26.2 étant sorti après (16 juin 2026) et NickAPI travaillant directement au niveau NMS (elle publie une classe `Handler_v1_21_RX` par version), **il n'est pas garanti qu'elle fonctionne sans une mise à jour de l'auteur**. Avant de mettre ça en production :
  1. Vérifiez sur https://www.spigotmc.org/resources/nickapi-1-8-8-1-20-2-1-21.26013/updates si une version postérieure au 26.2 est sortie.
  2. Si non, testez sur un serveur de test — NickAPI a plusieurs retours d'utilisateurs signalant des soucis de compatibilité (bugs `NoSuchElementException` sur les versions toutes fraîches, cf. les avis sur SpigotMC).
- NickAPI **n'est pas open-source et n'est pas sur Maven Central**. J'ai d'abord essayé de pointer le `pom.xml` vers le dépôt Maven officiel de l'auteur (`https://haoshoku.xyz:8081/repository/default/`), **mais ce dépôt s'est révélé injoignable depuis les runners GitHub Actions** (connexion en timeout — testé en conditions réelles via le workflow CI). Le `pom.xml` utilise donc une dépendance `system` pointant vers `libs/NickAPI.jar`, **committé dans le dépôt** (version 7.11, fournie par toi). C'est le même principe que le `pom.xml` d'origine (qui pointait vers un chemin Windows local en dur), en version portable et versionnée.
- **Toutes les méthodes NickAPI utilisées dans le code ont été vérifiées par inspection directe du bytecode du jar fourni** (`setNick`, `setSkin`, `setProfileName`, `setUniqueId`, `refreshPlayer`, `resetNick`, `resetSkin`, `resetProfileName`, `resetUniqueId`, `isNicked`, `getOriginalName`) : elles existent bien telles quelles dans `xyz/haoshoku/nick/api/NickAPI.class`. Le portage est donc fiable sur ce point.

## Autres changements de comportement mineurs

- `HeadEvent` ignore désormais explicitement `EquipmentSlot.OFF_HAND` pour éviter un déclenchement en double de l'interaction (bug latent qui existait déjà avant, mais plus visible sur les versions récentes où l'event `PlayerInteractEvent` se déclenche pour chaque main).
- Le nom du joueur déguisé (utilisé pour l'affichage / la sauvegarde du déguisement) passe désormais par les sérialiseurs Adventure (`LegacyComponentSerializer` / `PlainTextComponentSerializer`) plutôt que `ChatColor.stripColor`, pour un résultat strictement identique sans dépendre d'API dépréciées.

## Build

### En local
```bash
# Nécessite Java 25 (JDK) et Maven installés
mvn clean package
```
Le jar final se trouve dans `target/SohranSMP-2.0.0.jar`.

### Via GitHub Actions (automatique)
Un workflow est fourni dans `.github/workflows/build.yml` :
- Se déclenche sur chaque `push`/`pull_request` vers `main`/`master`, et peut être lancé manuellement (`workflow_dispatch`).
- Installe Java 25 (Temurin), compile avec Maven, et **upload le `.jar` en artifact** de la run (visible dans l'onglet *Actions* du dépôt, téléchargeable pendant 90 jours).
- Si vous poussez un tag de version (ex. `git tag v2.0.0 && git push --tags`), il crée automatiquement une **Release GitHub** avec le `.jar` attaché.

Pour l'activer, il suffit de pousser ce dossier tel quel dans un dépôt GitHub — aucune configuration supplémentaire n'est nécessaire (le `GITHUB_TOKEN` utilisé pour créer la release est fourni automatiquement par GitHub Actions).

## ⚠️ Non testé en conditions réelles sur un serveur

Le `.jar` compile (vérifié via le workflow GitHub Actions du dépôt). En revanche, le comportement
en jeu — notamment la compatibilité de NickAPI 7.11 avec Minecraft 26.2 au niveau NMS — **n'a pas
pu être testé sur un vrai serveur** depuis cet environnement. Teste sur un serveur de
développement avant mise en production.
