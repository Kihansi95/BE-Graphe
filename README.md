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

# Output to Excel
Pour pouvoir lancer le teste automatique qui rédige le fichier excel 2010+ (.xslx)
+ Ouvrir Eclipse
+ Choisir les fichier dans lib (commons-collections4-4.1.jar, poi-3.16.jar, poi-ooxml-3.16.jar, poi-ooxml-schemas-3.16.jar, tous ce qui dans ooxml-lib)
+ Clique droite > Build Path > Add to Build Path
+ Maintenant vérifier si les librairies sont dans Referenced Libraries
+ Lancer application.CheckExcel.java et vérifier si le fichier "excel.xlsx" existe