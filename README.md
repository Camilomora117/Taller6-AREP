# TALLER 6 -AREP

### Autor: Yesid Camilo Mora Barbosa

## Desafio

Sebe construir una aplicación con la arquitectura propuesta y desplegarla en AWS usando EC2, ELB y autoescalado.

1. El servicio MongoDB es una instancia de MongoDB corriendo en una máquina virtual de EC2

2. LogService es un servicio REST que recibe una cadena, la almacena en la base de datos y responde en un objeto JSON con las 10 ultimas cadenas almacenadas en la base de datos y la fecha en que fueron almacenadas.

3. La aplicación web APP-LB-RoundRobin está compuesta por un cliente web y al menos un servicio REST. El cliente web tiene un campo y un botón y cada vez que el usuario envía un mensaje, este se lo envía al servicio REST y actualiza la pantalla con la información que este le regresa en formato JSON. El servicio REST recibe la cadena e implementa un algoritmo de balanceo de cargas de Round Robin, delegando el procesamiento del mensaje y el retorno de la respuesta a cada una de las tres instancias del servicio LogService.

## Arquitectura

![image](https://user-images.githubusercontent.com/98135134/224865848-e436a1af-aa7b-4292-9499-107052cd90fc.png)

## Solución

Para implementar la arquitectura lo primero que debemos hacer es crear las 5 instancias de maquinas virtuales. 
Cada una será un servidor.

Recomandación: Utilize el mismo grupo de seguridad para todas las instancias, con el fin de facilitar la configuración de los puertos mas adelante.

![image](https://user-images.githubusercontent.com/98135134/224521866-4d9279e9-17c7-47d9-9c2d-edb16867f8ce.png)


### 1. Conectar a local

Debemos instalar java en las instancias de logService y el servidor de Round robin.
Para esto debemos conectarlos a la consola de las instancias, para esto usamos la coneccion ssh que nos ofrece aws.

![image](https://user-images.githubusercontent.com/98135134/224522078-4b31bca0-c20d-4de7-95f8-27b565c00190.png)

Abrimos una terminal de linux y ejecutamos el comando, teniendo en cuenta que en el mismo directorio debe estar la clave .pm de la instancia.

![image](https://user-images.githubusercontent.com/98135134/224522087-40551feb-8157-418d-a443-ebf189d204f1.png)


### 2. Instalamos Java (versión 17)

Para instalar java 17 utilizamos el siguiente comando en la terminal de la instancia.

Recuerde que debe instalar java para las instancias de logService y la instancia de RoundRobin

```
sudo yum install java-17-amazon-corretto-devel
```

Luego de instalar java verificamos que se instalo conrrectamente:

```
java -version
```
### 3. Mongo

Para empezar implementaremos el servidor de mongo db.

Para esto abrimos una consola de la instancia de mongo localmente y por medio de vi creamos el siguiente archivo:

```
vi /etc/yum.repos.d/mongodb-org-6.0.repo
```

le agregamos la siguiente información:

```
[mongodb-org-6.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/amazon/2/mongodb-org/6.0/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-6.0.asc
```

Instalamos mongo en la instancia con el siguiente comando:

```
sudo yum install -y mongodb-org
```

Ahora debemos decirle a mongo que acepte cualquier ip para consultar la basen de datos.

Entramos al archivo mongod.conf con el siguiente comando:

```
vi /etc/mongod.conf
```

Y cambios la seccion bindIP.

![image](https://user-images.githubusercontent.com/98135134/224869960-4fd4b318-8d97-4f47-bcd0-7e7ad0c48a8d.png)

Por ultimo ejecutamos el servicio mongo con el siguiente comando:

```
sudo systemctl start mongod
```

Es importante recordar los siguientes comandos:

```
sudo systemctl status mongod  \\Conocer estado mongo
sudo systemctl stop mongod    \\Detener mongo
```

Verificamos su estado:

![image](https://user-images.githubusercontent.com/98135134/224521878-4e38ff17-b203-43b6-9e0a-b2829f4b4092.png)

En el codigo debemos cambiar la url de la conexion a mongo con la ip publica de la instancia de mongo seguido del puerto donde corre mongo (27017)

![image](https://user-images.githubusercontent.com/98135134/225461837-bbd6ed39-e8dd-4043-a8e3-902cd1cf8107.png)


### 4. Implementación Round Robin

Antes de utilizar las terminales de las instancias debemos modificar el codigo del servidor RoundRobin.

Para esto el round robin decidira sobre las 3 ip publicas de las intancias de los logServices

![image](https://user-images.githubusercontent.com/98135134/224867781-4e229b8d-8b6d-405c-b017-3b73209ba4de.png)

El url completo para conectarnos a uns intancia de logService es:

```
http://{ip-logService}:4567/service

4567 es el puerto donde corren los logService, y /service el path de los métodos.
```

Una vez tenemos el codigo debemos subirlo a las instancias de LogService y RoundRobin.

Para esto debemos usar el protocolo sftp seguido del mismo comando que usamos para conectarnos a la consola localmente.
Para subirlo comprimimos la carpeta en un archivo .zip y utulizamos put.

![image](https://user-images.githubusercontent.com/98135134/224522115-fd331fe3-d139-4e1f-b0e1-91ee475e6e54.png)

Nos salimos con el comando "exit" y entramos nuevamente a la consola con ssh

Para terminar descomprimimos la carpeta target.zip con el comando unzip:

```
unzip target.zip
```


### 5. Abrir los Puertos

Ahora debemos abrir los puertos de las instancias.

Para esto editamos las reglas de entrada del grupo de seguridad y agregamos los puertos.

![image](https://user-images.githubusercontent.com/98135134/224521994-78b04b60-bfeb-4f94-9c5a-6d4829e36830.png)


### 6. Ejecutar

Ejecutamos la carpeta target con el siguiente comando:

```
//Para las instancias de logService
java -cp "target/classes:target/dependency/*" org.example.LogServiceApp
```

![image](https://user-images.githubusercontent.com/98135134/224521972-c58697b7-14c2-4a84-a41d-2c085df17005.png)

```
//Para la instancia de Round Robin
java -cp "target/classes:target/dependency/*" org.example.RoundRobin
```

![image](https://user-images.githubusercontent.com/98135134/224521946-0913afa7-237d-40da-a999-99533b901d08.png)

Para terminar abrimos el siguiente link en el navegador

```
//Estructura
http://{dns-publico-instancia-roundRobin}:{puerto-roundRobin}/

//Usado
http://ec2-54-226-142-6.compute-1.amazonaws.com:4566/
```

![image](https://user-images.githubusercontent.com/98135134/224522018-f2b75d38-2ad3-48a4-bd54-24f3881863db.png)


### 7. Pruebas

Pagina mostrada

![image](https://user-images.githubusercontent.com/98135134/224522031-fe04da20-f5f4-4f79-8b1b-010769f7fb35.png)

Utilizamos el método post

![image](https://user-images.githubusercontent.com/98135134/224522038-98f689e7-e439-4ea7-8066-781f3951a91d.png)

Utilizamos el método get

![image](https://user-images.githubusercontent.com/98135134/224522050-4cbca0fa-cca4-4b89-92d7-5b6d9edd8bb4.png)
