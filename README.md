<img src="./docs/assets/what2bake-baner2.png">

Do you ever find yourself with a pantry full of ingredients, but no idea what to bake? Look no further than What2Bake! Our app allows users to search for recipes and ingredients using advanced filters such as the ingredients they have on hand, recipe categories, and tags.

With the "What2Bake" app, you'll never have to worry about wasting ingredients again! Our advanced filters allow users to search for recipes based on the ingredients they already have on hand, as well as recipe categories and tags. Plus, you can keep track of your favorite recipes and discover new ones recommended by our community. Available on both web browsers and mobile devices, "What2Bake" is the perfect tool for anyone who loves to bake!

If you want to see the source code of the mobile client, check out our repository [MineSwek/What2Bake-MobileApp](https://github.com/MineSwek/What2Bake-MobileApp)

If you want to see the source code of the web client, check out our repository [KacperK461/what2bake-web](https://github.com/KacperK461/what2bake-web)

[//]: # (### Code )

[//]: # (- [Api]&#40;https://github.com/poneciak57/WhatToBake-api&#41;)

[//]: # (- [Mobile Client]&#40;https://github.com/MineSwek/What2Bake-MobileApp&#41;)

[//]: # (- Web Client &#40;Coming soon&#41;)

[//]: # ()
[//]: # (### Deploys)

[//]: # (- [Api]&#40;http://132.226.204.66:81/swagger-doc/swagger-ui.html&#41;)

[//]: # (- Mobile Client &#40;Coming soon&#41;)

[//]: # (- Web Client &#40;Coming soon&#41;)


# :books: Contents

- [Tech stack][tstack]
- [How to run](#how-to-run-localy)
- [Docs][docs]
- [Auth][auth]



---
## :hammer_and_wrench: Technologies & tools

<div>
    <img width="55" src="./docs/assets/icons/spring-original.svg">
    <img width="55" src="./docs/assets/icons/spring_webflux_logo.png">
    <img height="55" src="./docs/assets/icons/spring_security.png">
    <img width="55" src="./docs/assets/icons/neo4j.svg">
    <img width="55" src="./docs/assets/icons/pb.png">
    <img height="55" src="./docs/assets/icons/nginx.png">
    <img height="55" src="./docs/assets/icons/swagger.png">
</div>

---
## How to run localy

To run the app simply run this command:
```
docker-compose up --build -d
```
> it is possible that pocketbase will not start, 
> thats because it does not have an official docker image and 
> in dockerfile i download it and unzip, 
> so if its not working just change the arch in `pocketbase.dockerfile` 
> or indirectly in `docker-compose.yml`. 
> I found that issue while hosting on linux vps 

after that u should get 4 containers running and accessible as follows
- pocketbase: `http://localhost:9090`
- neo4j: `bolt://localhost:7687/neo4j`
- what2bake-api: `http:localhost:8080`
- nginx (we use it only in production)
>if any of these ports is used just override the properties in `docker-compose.yml` file or change them directly in `src/main/resaurces/application.properties`

### Pocketbase admin dashboard can be accessed with these credentials:
```
email = admin@gmail.com
password = adminpassword
url = http://localhost:9090
```
u can find schema of database in `./data/pb_schema.json`

### Neo4j database can be accessed with these credentials:
```
username = neo4j
password = password
url = bolt://localhost:7687
```
u can also initialize the database with example data to tests, u can find the query [here](./src/main/resources/init.cypher) 

---
## :clipboard: Docs
The api is up and running, docs for it can be found [here](http://api.what2bake.com).
Its simple swagger documentation and will be updated in a future.

---
## :lock: Auth
We use Pocketbase as user provider because it has built in authentication as well as oauth2 support. Moreover, it has library for flutter. 


Docs are under development 🚧

[tstack]: #hammer_and_wrench-technologies--tools
[docs]: #clipboard-docs
[auth]: #lock-auth
