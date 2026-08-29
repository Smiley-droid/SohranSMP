# NickAPI.jar

Le dépôt Maven privé de NickAPI (`https://haoshoku.xyz:8081/repository/default/`) s'est
révélé **injoignable depuis les runners GitHub Actions** (connexion en timeout), probablement
suite à une restriction d'accès par IP mise en place par l'auteur. La compilation en CI dépend
donc de ce jar committé directement dans ce dossier (`NickAPI.jar`, version 7.11).

## Vérification des méthodes utilisées

Toutes les méthodes appelées dans le code du plugin (`SkinUtil`, `HeadEvent`, `DisguiseKeeper`,
`UndisguiseCommand`) ont été vérifiées par inspection directe du bytecode de ce jar :
`setNick`, `setSkin`, `setProfileName`, `setUniqueId`, `refreshPlayer`, `resetNick`, `resetSkin`,
`resetProfileName`, `resetUniqueId`, `isNicked`, `getOriginalName` sont bien toutes présentes
dans `xyz/haoshoku/nick/api/NickAPI.class`.

## Mise à jour future

Si tu passes à une version plus récente de NickAPI (ex. après une éventuelle mise à jour
"officiellement 26.2" par l'auteur) :
1. Télécharge la nouvelle version depuis
   https://www.spigotmc.org/resources/nickapi-1-8-8-1-20-2-1-21.26013/
2. Remplace ce fichier (`libs/NickAPI.jar`)
3. Mets à jour le numéro de version dans `pom.xml` (`<version>7.11</version>`)
4. Commit & push — le build GitHub Actions se relance automatiquement.

⚠️ Ce dépôt étant privé, committer ce jar ne le rend pas public. Si tu changes la visibilité du
dépôt en public un jour, vérifie d'abord que la licence de NickAPI autorise la redistribution du
jar, sinon retire-le et repasse par un téléchargement manuel côté CI/serveur.

