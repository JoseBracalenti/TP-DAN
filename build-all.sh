
cd ms-clientes
docker-compose build
docker-compose up -d 
cd ..
cd ms-productos
docker-compose build
docker-compose up -d 
cd ..
cd ms-pedidos
docker-compose build
docker-compose up -d 
cd ..
cd dan-gateway
docker-compose build
docker-compose up -d 
cd ..
cd dan-eureka-srv
docker-compose build
docker-compose up -d
cd ..
cd ms-docker
docker-compose up -d 
