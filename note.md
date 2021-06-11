## 3月7号

解决跨域

1 jsonp

![image-20210526004014237](G:\note\image\image-20210526004014237.png) 

2 后端设置请求头

![image-20210526004040161](G:\note\image\image-20210526004040161.png)

3 proxy代理

```js
 const { createProxyMiddleware } = require('http-proxy-middleware');  //注意写法，这是1.0以后的版本，最好按抄

module.exports = function (app) {
     app.use(createProxyMiddleware('/api',
         {
            target: 'localhost:9089',
             pathRewrite: {
                '^/api': '/',
             },
             changeOrigin: true,
             secure: false, // 是否验证证书
             ws: true, // 启用websocket
        }
    ));
 };

```

4 Nginx代理

![image-20210526004149852](G:\note\image\image-20210526004149852.png)

5 gateway网关

6 添加@@CrossOrigin

张娜物联网卡 8986112024008358926

张娜 妇女节的后一天

切换root命令   su

宝塔主机

```
http://39.105.146.1:8888/77d8962c
username:1ukoxjyo
password: 002a667f
```

包月主机 

```
47.111.176.113
用户名root 密码 Root1234
```

宝塔面板

```
http://47.111.176.113:8888/b0d77ad7
username: cvtd7m86
password: c51c0ce5
```

Jinkens配置
```
JenKins
http://39.105.146.1:8080/
name root 
password qq123
```

Tomcat 服务器

```
http://192.168.183.141/manager/html
name tomcat
password tomcat
```

gitlab仓库

```
地址   192.168.183.132
root
root123
```

Jenkins

```
http://192.168.183.133:8888/
admin
admin
```

SonarQube

```
URL 192.168.183.136:9000
name admin
password admin
token  Secret key : 6434ac9810c0a8c5ad3db5faca0fcd48b34a45a2
```

数据库 

```
name  root
password qq123456
```

Harbor

```
192.168.183.135:85
name admin
password Harbor12345
```

gitLab仓库

```
user 17637067893
password qq851088072
```

部署服务器

```
url 192.168.183.130:85
name admin
password Harbor12345
```

QQ

```
851088072
whq19950201
```

樱花内网穿透

https://www.natfrp.com/user/

```1
851088072
qq851088072
```

docker hub

```
17637067893
qq851088072
851088072@qq.com
```

github

```
17637067893
qq851088072
```

#### 查看端口占用

```
netstat -aon|findstr 8080  //查看PID
```

![image-20210521225900767](G:\note\image\image-20210521225900767.png)

```
tasklist|findstr 8080  根据PID  查看进程
```

![image-20210521230038157](G:\note\image\image-20210521230038157.png)

```
杀掉进程使用taskkill -PID 进程号 -F

/PID processid 指定要终止的进程的 PID。

/F 指定强制终止进程。
```

```
netstat -aon|finstr 8888  8150  10002 10003
taskkill -PID 
```

163邮箱

```
whq17637067893
qq851088072
```

#### js长列表优化

固定高度分析

1 首先进行外侧容器的scroll事件

2 计算可是区域的高度和滚动的距离

3 计算显示可视区域数据

