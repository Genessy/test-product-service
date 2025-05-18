Build l'application :  
mvn package  
  
Build l'image docker :  
docker image build -t genessy/epsi4a-mspr4-mproduct:0.1 .  
  
Lancer le container :  
docker run -p 8080:8080 genessy/epsi4a-mspr4-mproduct:0.1  
  
Push une image sur docker hub :  
docker push genessy/epsi4a-mspr4-msproduct:0.1

Arrêter et supprimer les ressources créées par docker compose :  
docker compose down  

Lancer le compose :  
docker compose up --build  
  
Lien du Swagger :  
http://localhost:8080/swagger-ui/index.html#/