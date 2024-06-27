## 拉取Nginx镜像
```
docker pull nginx:latest
```

## 创建Nginx容器
```
docker run --name nginx -p 80:80 -dit nginx
```

## 进入容器Copy配置文件
```
docker cp 容器id:/etc/nginx/nginx.conf /mnt/d/nginx/nginx.conf
```
> 对于文件，直接通过-v参数挂载到宿主机默认会创建为文件夹，所以文件要存在，因此我们此处使用cp命令将配置文件复制到宿主机中。

## 删除容器
```
docker rm -f 容器id
```

## 修改配置文件
在http节点下添加如下配置：

```
 server { 
    listen       80;
    server_name  localhost;
    location / {
        root   /usr/share/nginx/html;
        index  index.html;
    }

    location /api {
       proxy_pass http://ip:port;
    }
```

## 启动新容器
```
docker run --name nginx -p 80:80 -dit -v /d/docker/nginx/nginx.conf:/etc/nginx/nginx.conf -v /d/docker/nginx/dist:/usr/share/nginx/html nginx
```

## 浏览器访问
http://localhost/ 会打开Vue的index.html

http://localhost/api/xxx 则会转发到后端接口