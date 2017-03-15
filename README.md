BE-Graphe
---------------------

# First use
Se mettre dans le dossier "Cible"
+ Cible
  + src/
    |*.java
  + bin/
    |*.class
  |...  

Télécharger projet
```
git clone https://github.com/Kihansi95/BE-Graphe.git
```

Mettre tous dossiers à l'intérieur ici.

Sauvegarder nom, email et mot de passe de la première utilisation
```
git init
git config user.name ""
git config user.email ""
git config credential.helper store
```

# Working
```
git add "nom_fichier" || git add .
git pull
git commit -m "hey I work this"
git push
```

Si conflit
```
git pull
# conflit
git diff --name-only --diff-filter=U
# listing les fichiers en conflit
# régler les parties conflit selon ce conseil: https://help.github.com/articles/resolving-a-merge-conflict-using-the-command-line/#competing-line-change-merge-conflicts
# fini
git add .
git commit -m "Merge conflict dans ces fichiers"
git push
```