# NickAPI.jar requis ici

Le dépôt Maven privé de NickAPI (`https://haoshoku.xyz:8081/repository/default/`) s'est
révélé **injoignable depuis les runners GitHub Actions** (connexion en timeout), probablement
suite à une restriction d'accès par IP mise en place par l'auteur. La compilation en CI dépend
donc d'un jar committé directement dans ce dossier.

## Comment l'obtenir

1. Va sur la page SpigotMC de NickAPI :
   https://www.spigotmc.org/resources/nickapi-1-8-8-1-20-2-1-21.26013/
2. Télécharge la version **7.11** (ou plus récente si disponible — vérifie la compatibilité 26.2
   avant, voir le README principal).
3. Renomme le fichier téléchargé en exactement : `NickAPI.jar`
4. Place-le dans ce dossier (`libs/NickAPI.jar`).
5. Commit & push :
   ```bash
   git add libs/NickAPI.jar
   git commit -m "Ajout du jar NickAPI 7.11"
   git push
   ```

Le build GitHub Actions se relancera automatiquement et devrait alors passer la phase de
résolution des dépendances.

## Pourquoi un jar local plutôt qu'une dépendance Maven ?

NickAPI n'est pas publié sur Maven Central et son propre dépôt Maven n'est, en pratique, pas
accessible depuis l'infrastructure GitHub Actions. C'est la même limitation que rencontrait déjà
le `pom.xml` d'origine du plugin (qui pointait vers un chemin Windows local en dur,
`C:/BuildTool/NickAPI/NickAPI-v6.5.3.jar`) — on formalise juste la même approche de façon
portable et versionnée dans le dépôt.

⚠️ Ce dépôt étant privé, committer ce jar ne le rend pas public. Si tu changes la visibilité du
dépôt en public un jour, vérifie d'abord que la licence de NickAPI autorise la redistribution du
jar, sinon retire-le et repasse par un téléchargement manuel côté CI/serveur.
