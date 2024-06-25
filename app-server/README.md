## 背景
Docker被墙，客户无法访问Docker网站，所以只能离线部署Docker。

## 打包
### 本地生成image
DockerFile
```
FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build
```
docker build -t myapp . 
```

>也可以本地打包成jar,然后上传jar和DockerFile到服务器，在服务器上执行docker build -t myapp .

### 本地下载image
```
 docker pull mysql:8.4.0
```

### 本地另存image
```
 docker save -o mysql.tar mysql:8.4.0
```

## 部署
提前准备好所有软件安装包和项目镜像文件。

### 操作系统
假设客户已经有了一台Linux服务器
```Ubuntu Server 24.04 LTS 64位```

### 远程访问
客户已经开通了SSH访问权限，并且已经生成了密钥对。
我们在本地使用SSH Client连接服务器：
```
ssh ubuntu@SERVERIP -i C:\Users\USERNAME\.ssh\SERVER.pem
```

### 安装ftp
服务器上按照教程安装FTP Server:
https://phoenixnap.com/kb/install-ftp-server-on-ubuntu-vsftpd


本地开发机器上安装FTP Client(FileZilla)，可以提前下载好。
下载地址：https://filezilla-project.org/download.php?type=client


### 离线安装Docker
1. 离线包下载：https://download.docker.com/linux/static/stable
```  
docker-26.1.4.tgz 
```

2. 使用FileZilla上传到服务器

3. 解压，设置为服务，开机启动
参考：https://blog.csdn.net/weixin_42571882/article/details/134015815


### 上传image
使用FileZilla上传到服务器

### 导入image
```
docker load -i myoffice.tar
```
```
docker load -i mysql.tar
```

### 查看image
```
docker image ls
```
如果要删除image，执行
```
docker image remove 镜像id
```

### 设置容器网络
```
docker network create my-docker-net
```

### 启动myoffice容器
```
docker run -p 80:80 --name myoffice --restart=always -d --network my-docker-net myoffice:latest
```

### 启动mysql容器
```
docker run -p 3306:3306 \
    -v /usr/local/docker/mysql/mysql-files:/var/lib/mysql-files \
    -v /usr/local/docker/mysql/conf:/etc/mysql \
    -v /usr/local/docker/mysql/logs:/var/log/mysql \
    -v /usr/local/docker/mysql/data:/var/lib/mysql \
    -e MYSQL_ROOT_PASSWORD=123123 \
    --name mysql \
    --restart=always \
    -e TZ=Asia/Shanghai \
    --network my-docker-net \
    -d mysql:8.4.0
```

启动mysql 容器后，进入交互模式
docker exec -it mysql /bin/bash

设置运行root账户远程访问：
mysql -uroot -p123123 
use mysql；
select host,user from user;
update user set host = ‘%’ where user = ‘root’;
flush privileges;­


启动过程如果遇到错误,可以通过以下命令查看日志：
```
docker logs 容器id
```