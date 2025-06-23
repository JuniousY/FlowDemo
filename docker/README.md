打包（在项目根目录下运行）：
```sh
docker build -f docker/Dockerfile -t flow-demo .
```

运行：
```sh
docker run -d --name flow-demo -p 8080:8080 flow-demo:latest
```