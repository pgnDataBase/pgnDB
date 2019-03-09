Application deployment
===
##### Requirements: 
* `Java JDK`
* `docker`
* `docker-compose`

##### Configuration (example config): 
config.json
```json
{
  "resourcesPath": "src/main/resources",
  "database": {
    "driver": "org.postgresql.Driver",
    "url": "jdbc:postgresql://172.18.0.2:5432/maindb",
    "username": "admin",
    "password": "admin",
    "maximumPoolSize": 2,
    "connectionTimeout": 30000
  },
  "security": {
    "encryptionSecret": "super-super-secret",
    "jwtSecret": "dghdvtg23fgJNJJUedffcc..",
    "jwtExpirationMinutes": 240
  }
}
```

Windows (Docker Toolbox)
---
#### 1. Configure port forwarding for VM
1. Open Oracle VM VirtualBox Manager  
2. Right-click on proper VM instance
3. Settings
4. Network
5.     Card 1:   
        NAT network  
        Ports forwarding  
                 NAZWA        | PROTOKÓŁ | IP HOSTA  | PORT HOSTA | IP GOŚCIA | PORT GOŚCIA
            ---------------------------------------------------------------------------------
            * pgndb-backend  |    TCP   |           |    9095    |           |    9095
            * pgndb-database |    TCP   |           |    5432    |           |    5432
            * pgndb-frontend |    TCP   |           |    4200    |           |    4200
            * ssh            |    TCP   | 127.0.0.1 |   50678    |           |      22
       Card 2: 
        (host-only) network
#### 2. Run VM
1. Start VM for docker
    ```bash
    docker-machine start default
    ```

2. Configuring shell might be needed (example for machine name: **"default"**)
    ```
    @FOR /f "tokens=*" %i IN ('docker-machine env default') DO @%i
    ```  

#### 3. Start application from repository folder
```bash
docker-compose up --build -d
```

#### 4. Turn off application
```bash
docker-compose down
```

Linux
---
#### 1. Start application from repository folder
```bash
docker-compose up --build -d
```

#### 2. Turn off application

```bash
docker-compose down
```

Section for developers
===
**Configuration of config.json**

**Windows**:  
```bash
"url":"jdbc:postgresql://localhost:5432/maindb"
```

**Linux**:  
```bash
"url": "jdbc:postgresql://172.18.0.2:5432/maindb"
```  

---
**Starting backend from Intellij IDEA requires following database start/stop:**
#### 1. Create network
```bash
docker network create --subnet=172.18.0.0/16 backend
```

#### 2. Build image (run in catalog with database Dockerfile)
```bash
docker build -t pgndb-database:1.0.0 .
```

#### 3. Start database container
```bash
docker run --rm -d --net=backend --ip 172.18.0.2 -p 5432:5432 --name pgndb-database pgndb-database:1.0.0
```

#### 4. Start program in Intellij IDEA
On **Windows** turning off port forwarding for backend port might be needed.

---
**Starting frontend from Intellij IDEA requires following steps:**
#### 1. Install dependencies
```bash
npm install
```

#### 2. Run frontend
```bash
ng serve
```

---
##### Frontend container manual build and "cheat" deployment example
```bash
cd deployment
docker build -t pgndb-frontend:1.0.0 .
docker run --rm --net=backend --ip 172.18.0.4 -p 4200:4200 --name pgndb-frontend pgndb-frontend:1.0.0

```

---
**Accessing backend/database container shell:**
```bash
docker exec -it pgndb-backend bash
docker exec -it pgndb-database bash
docker exec -it pgndb-frontend bash
```

---
**Accessing real time logging for backend/database:**
```bash
docker logs -f pgndb-backend
docker logs -f pgndb-database
docker logs -f pgndb-frontend
```

---
**Cleanup:**
#### 1. Remove database container
```bash
docker rm -f pgndb-database
```

#### 2. Remove network
```bash
docker network rm backend
```

#### 3. Remove volume (if was used)
```bash
docker volume rm pgndb_pgndb-data
```


Application release process
===
### Windows
#### 1. Create and change branch for new release
```bash
git push origin master:release/1.0.0
git checkout release/1.0.0
```


#### 2. Build backend shadow jar file
```bash
gradlew bootJar
```

#### 3. Prepare frontend build
```bash
cd webclient
npm install
ng build --prod --output-path=../deployment/dist
mkdir ..\deployment\dist\src
xcopy src\chessboardjs ..\deployment\dist\src\chessboardjs\ /e
cd ..
```

### Linux
#### 1. Create and change branch for new release
```bash
git push origin master:release/1.0.0
git checkout release/1.0.0
```


#### 2. Build backend jar file
```bash
./gradlew bootJar
```

#### 3. Prepare frontend build
```bash
cd webclient
npm install
ng build --prod --output-path=../deployment/dist
mkdir -p ../deployment/dist/src
cp -R src/chessboardjs ../deployment/dist/src
cd ..
```
