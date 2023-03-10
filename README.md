# TALLER5-AREP

### Autor: Yesid Camilo Mora Barbosa

## Desafio

1. El servicio MongoDB es una instancia de MongoDB corriendo en un container de docker en una máquina virtual de EC2

2. LogService es un servicio REST que recibe una cadena, la almacena en la base de datos y responde en un objeto JSON con las 10 ultimas cadenas almacenadas en la base de datos y la fecha en que fueron almacenadas.

3. La aplicación web APP-LB-RoundRobin está compuesta por un cliente web y al menos un servicio REST. El cliente web tiene un campo y un botón y cada vez que el usuario envía un mensaje, este se lo envía al servicio REST y actualiza la pantalla con la información que este le regresa en formato JSON. 
El servicio REST recibe la cadena e implementa un algoritmo de balanceo de cargas de Round Robin, delegando el procesamiento del mensaje y el retorno de la respuesta a cada una de las tres instancias del servicio LogService.

## Arquitectura
![image](https://user-images.githubusercontent.com/98135134/224208535-c7553ca9-c049-4209-8d85-e57d3f7bfba1.png)

## Solución

### Resumen
Este proyecto consiste en implementar una arquitectura de cliente-servidor el cual realiza peticiones a una API que implementa el algoritmo RoundRobin para la distribución de cargas para los logServices que es un controlador que procesa solicitudes extrayendo
datos de colecciones de una la base de datos en mongo.

### Deploy

```
url:  http://ec2-54-174-77-91.compute-1.amazonaws.com:42000/
```

### Implementacion cliente-servidor

EL servidor Round Robin lee los archivos estáticos y se encarga de recibir las peticiones http del cliente.
El servidor LogService por su parte se encarga de recibir las peticiones de RoundRobin y consultar la base de datos.

![image](https://user-images.githubusercontent.com/98135134/224209863-4be6e9c2-60f5-4037-8111-2ca922f92abf.png)

### Round Robin
El round robin expone los metodos get y post para consultar los datos y se conecta con la API de logService con alguna de las 3 instancias al azar.
Para conectarse a la API se debe colocar la IP privada de la instancia de AWS, si se va probar local es el localhost.

![image](https://user-images.githubusercontent.com/98135134/224210499-bb574365-a6d8-4bda-be8e-7c263b02dde7.png)

### Log Service 
El log service se conecta a la base de datos en mongo corriendo en una imagen de docker.
Los servicios expuestos me permiten consultar y agregar datos a una coleccion.

![image](https://user-images.githubusercontent.com/98135134/224210961-e94ac604-22c8-487d-90a0-6c990d838a14.png)

### Mongo
Nos conectamos a la base de datos con el puerto 27017, y creamos una coleccion en una de las bases de datos por defecto.

![image](https://user-images.githubusercontent.com/98135134/224211169-cf074d47-3d40-4653-aac8-798f00c174bb.png)

### DockerFiles

Creamos un archivo Dockerfile para crear la imagen de Log Service
![image](https://user-images.githubusercontent.com/98135134/224211360-7b31945b-21b2-44c4-8fe7-1f5351645947.png)

Creamos un archivo DockerFile2 para crear la imagen de Round Robin
![image](https://user-images.githubusercontent.com/98135134/224211604-5940fdae-f661-49cc-b4b5-6d0bcfd9067d.png)

### Docker compose yml

En este archivo le especificamos a docker los servicios que queramos que nos cree.
Creamos 3 servicios de logService, uno para RoundRobin, y otro para una base de datos de mongo

![image](https://user-images.githubusercontent.com/98135134/224211760-b19d17b3-2389-4391-8dd5-e2a3e324659c.png)

![image](https://user-images.githubusercontent.com/98135134/224211910-fcbc1b2d-ecd2-46f1-a1ff-4db4550b808b.png)

### Docker

Para correr el proyecto en docker usamos los siguientes comandos:

* Construir los servicios:
```
docker-compose build 
```

* Correr los servicios en docker:
```
docker-compose up -d 
```

* Me crea el contenedor con las siguientes imagenes:
![image](https://user-images.githubusercontent.com/98135134/224212510-05d76725-df46-4b0c-9011-ef079fd8f2ec.png)

### DockerHube

Creamos 3 repositorios en Dockerhub para cada servicio (mongo, roundRobin, logService)
![image](https://user-images.githubusercontent.com/98135134/224212816-4682b987-c4d7-47c4-b744-d6e40a3a4c68.png)

Para subirlo usamos los siguientes comandos:

```
docker tag nombre-imagen-en-docker-local camilomora117/nombre-repositorio
```

```
login
```

```
docker push camilomora117/nomnre-repositorio:latest
```

### AWS

Creamos una instancia de Ec2
![image](https://user-images.githubusercontent.com/98135134/224213797-5e66b10a-8b02-494c-b052-de6b743b96d0.png)

Entramos a la consola
![image](https://user-images.githubusercontent.com/98135134/224214502-5b0a701b-ff63-4ffd-b1fc-a5cf7986305d.png)

Instalamos docker
```
sudo yum update -y
sudo yum install docker
```

Corremos las imagenes de dockerhub en aws
```
docker run -d -p 42000:6000 --name roundrobin camilomora117/docker-web
docker run -d -p 34000:6000 --name logservice1 camilomora117/docker-log
docker run -d -p 34001:6000 --name logservice2 camilomora117/docker-log
docker run -d -p 34002:6000 --name logservice3 camilomora117/docker-log
docker run -d -p 27017:6000 --name mongo camilomora117/docker-mongo
```

Verificamos que esten corriendo:
![image](https://user-images.githubusercontent.com/98135134/224216181-1c2c0ba8-5489-4d9e-bc4b-b1591c945c5e.png)

Para terminar abrimos los puertos
![image](https://user-images.githubusercontent.com/98135134/224216494-0b133adc-11b4-44ef-b937-78fcd2c860fe.png)

### Pruebas

Abrimos el link de la instancia:

![image](https://user-images.githubusercontent.com/98135134/224216412-7d939152-da10-435d-818f-cdac118cff1d.png)

Probamos el funcionamiento del get:

![image](https://user-images.githubusercontent.com/98135134/224218101-5c4c97e5-57d7-4ca3-9764-749dab9724ab.png)

Añadimos un nuevo elemento:

![image](https://user-images.githubusercontent.com/98135134/224218250-f4c7a392-8f07-4e9b-9b87-cad0ffdb9019.png)


