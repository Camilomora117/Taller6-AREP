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

![image](https://user-images.githubusercontent.com/98135134/224521866-4d9279e9-17c7-47d9-9c2d-edb16867f8ce.png)


### Conectar a local

![image](https://user-images.githubusercontent.com/98135134/224522078-4b31bca0-c20d-4de7-95f8-27b565c00190.png)

![image](https://user-images.githubusercontent.com/98135134/224522087-40551feb-8157-418d-a443-ebf189d204f1.png)

![image](https://user-images.githubusercontent.com/98135134/224522115-fd331fe3-d139-4e1f-b0e1-91ee475e6e54.png)


### Round Robin
![image](https://user-images.githubusercontent.com/98135134/224521890-25d7b666-4cd2-4e17-b811-8d2577d1158f.png)



### Mongo
![image](https://user-images.githubusercontent.com/98135134/224521878-4e38ff17-b203-43b6-9e0a-b2829f4b4092.png)

### Puertos

![image](https://user-images.githubusercontent.com/98135134/224521994-78b04b60-bfeb-4f94-9c5a-6d4829e36830.png)


### Ejecutar

![image](https://user-images.githubusercontent.com/98135134/224522018-f2b75d38-2ad3-48a4-bd54-24f3881863db.png)

![image](https://user-images.githubusercontent.com/98135134/224521946-0913afa7-237d-40da-a999-99533b901d08.png)

![image](https://user-images.githubusercontent.com/98135134/224521972-c58697b7-14c2-4a84-a41d-2c085df17005.png)

### Pruebas
![image](https://user-images.githubusercontent.com/98135134/224522031-fe04da20-f5f4-4f79-8b1b-010769f7fb35.png)

![image](https://user-images.githubusercontent.com/98135134/224522038-98f689e7-e439-4ea7-8066-781f3951a91d.png)

![image](https://user-images.githubusercontent.com/98135134/224522050-4cbca0fa-cca4-4b89-92d7-5b6d9edd8bb4.png)


