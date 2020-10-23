Consignes :

- Implémentation du protocole HTTP
  - Parsing d'une reuquete faite à la main (par telnet) --> Ok
- GET /toto.txt HTTP/1.1
- host: www.toto.fr
- Ligne vide = fin de la requete

  - Affichage de la requete dans le terminal --> Ok
  - Génération de l'entête HTTP de réponse --> Ok
  - Envoit de la ressource demandée (si la requète est valide) --> Ok
  - Générer quelque cas classique : 200, 400, 404 --> Ok
  - Vérifier le fonctionnement avec un vrai navigateur --> Ok

  VERSIONS :
  java version "15" 2020-09-15
  javac 15