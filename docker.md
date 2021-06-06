#### docker 安装nacos

https://nacos.io/zh-cn/docs/quick-start-docker.html

```shell
docker run -d \
-e PREFER_HOST_MODE=ip \      
-e MODE=standalone \                启动模式
-e SPRING_DATASOURCE_PLATFORM=mysql \  数据库
-e MYSQL_SERVICE_HOST=192.168.0.21 \  数据库ip
-e MYSQL_SERVICE_PORT=3306 \            端口
-e MYSQL_SERVICE_USER=root \
-e MYSQL_SERVICE_PASSWORD=Root@123 \
-e MYSQL_SERVICE_DB_NAME=nacos \
-e TIME_ZONE='Asia/Shanghai' \
-v /home/nacos/logs:/home/nacos/logs \
-p 8848:8848 \
--name nacos \
--restart=always \
nacos/nacos-server
```

#### docker 安装seata

```shell
docker run --name seata-server  -d -p 8091:8091 --restart=always \
docker-e SEATA_IP=192.168.194.129  -v /mydata/seata/conf:/root/seata-config -v /mydata/seata/logs:/root/logs/seata  seataio/seata-server
```

