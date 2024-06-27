## 拉取OnlyOffice镜像
```
docker pull onlyoffice:latest
```

## 创建容器
```
docker run -i -t -d -p 80:80 -e JWT_SECRET=my_jwt_secret onlyoffice/documentserver
```

## 社区版需要解决的问题
1. 连接数最多20个限制
2. 需要增加中文字体的选择，如：宋体，隶书
3. 需要增加中文字号的选择，如：小五，五号