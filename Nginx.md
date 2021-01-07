![image-20201228131852590](G:\note\image\image-20201228131852590.png)

#### 正向代理

![image-20201228131924605](G:\note\image\image-20201228131924605.png)

比如我们国内访问谷歌，直接访问访问不到，我们可以通过一个正向代理服务器，请求发到代理服，代理服务器能够访问谷歌，这样由代理去谷歌取到返回数据，再返回给我们，这样我们就能访问谷歌了

**正向代理的用途：**

　　（1）访问原来无法访问的资源，如google

​        （2） 可以做缓存，加速访问资源

　　（3）对客户端访问授权，上网进行认证

　　（4）代理可以记录用户访问记录（上网行为管理），对外隐藏用户信息

## 反向代理

反向代理（Reverse Proxy）实际运行方式是指以代理服务器来接受internet上的连接请求，然后将请求转发给内部网络上的服务器，并将从服务器上得到的结果返回给internet上请求连接的客户端，此时代理服务器对外就表现为一个服务器

![image-20201228132148558](G:\note\image\image-20201228132148558.png)

![image-20201228132220610](G:\note\image\image-20201228132220610.png)

#### 负载均衡

**意思是将负载（工作任务，访问请求）进行平衡、分摊到多个操作单元（服务器，组件）上进行执行。是解决高性能，单点故障（高可用），扩展性（水平伸缩）的终极解决方案**

![image-20201228133229711](G:\note\image\image-20201228133229711.png)

#### 动静分离

动静分离是将网站静态资源（HTML，JavaScript，CSS，img等文件）与后台应用分开部署，提高用户访问静态代码的速度，降低对后台应用访问。

![image-20201228133905129](G:\note\image\image-20201228133905129.png)

#### 安装nginx

1 安装前准备 

 yum install yum-utils

2 添加源

到 cd /etc/yum.repos.d/ 目录下

新建 vim nginx.repo 文件

输入以下信息

```
[nginx-stable]
name=nginx stable repo
baseurl=http://nginx.org/packages/centos/$releasever/$basearch/
gpgcheck=1
enabled=1
gpgkey=https://nginx.org/keys/nginx_signing.key

[nginx-mainline]
name=nginx mainline repo
baseurl=http://nginx.org/packages/mainline/centos/$releasever/$basearch/
gpgcheck=1
enabled=0
gpgkey=https://nginx.org/keys/nginx_signing.key
```

##### 安装nginx

通过yum search nginx看看是否已经添加源成功。如果成功则执行下列命令安装nginx。

```
yum install nginx
```

安装完后，rpm -qa | grep nginx 查看

启动nginx：systemctl start nginx

加入开机启动：systemctl enable nginx

重启 nginx   systemctl restart nginx.service

查看nginx的状态：systemctl status nginx

##### 配置文件

网站文件存放默认位置（Welcome to nginx 页面）
/usr/share/nginx/html



网站默认站点配置
/etc/nginx/conf.d/default.conf



自定义 nginx 站点配置文件存放目录
/etc/nginx/conf.d/



nginx 全局配置文件
/etc/nginx/nginx.conf

```
进入 /etc/nginx目录下，打开 nginx.conf 文件最下面有一句话 include /etc/nginx/conf.d/*.conf; 表明 conf.d 下的 所有以 .conf 结尾的文件都属于 nginx的配置文件
进入 conf.d 下，只有一个 default.conf 默认配置文件，cp default.conf test.conf 复制一份 default.conf 并改名为 test.conf

vim test.conf 打开 test.conf  (只复制前几行)
server {
listen       80;
server_name  localhost;

#charset koi8-r;
#access_log  /var/log/nginx/host.access.log  main;

location / {
root   /usr/share/nginx/html;
index  index.html index.htm;
}

# ....... 省略中间的代码     

}
```

```conf
全局块  //配置整体运
user nginx;
worker_processes auto;   //处理并的数量
error_log /var/log/nginx/error.log;
pid /run/nginx.pid;

# Load dynamic modules. See /usr/share/doc/nginx/README.dynamic.
include /usr/share/nginx/modules/*.conf;

 
//events块 配置nginx 与网络的链接
events {
worker_connections 1024;  //最大链接数
}

//http配置频繁的部分 代理缓存 日志
//http块也包括http全局块 server块
http {
log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
'$status $body_bytes_sent "$http_referer" '
'"$http_user_agent" "$http_x_forwarded_for"';


access_log  /var/log/nginx/access.log  main;

sendfile            on;
tcp_nopush          on;
tcp_nodelay         on;
keepalive_timeout   65;
types_hash_max_size 2048;

include             /etc/nginx/mime.types;
default_type        application/octet-stream;

# Load modular configuration files from the /etc/nginx/conf.d directory.
# See http://nginx.org/en/docs/ngx_core_module.html#include
# for more information.
include /etc/nginx/conf.d/*.conf;

server {
listen       80 default_server;
listen       [::]:80 default_server;
server_name  _;
root         /usr/share/nginx/html;

# Load configuration files for the default server block.
include /etc/nginx/default.d/*.conf;

location / {
}

error_page 404 /404.html;
location = /40x.html {
}

error_page 500 502 503 504 /50x.html;
location = /50x.html {
}
}

# Settings for a TLS enabled server.
#
#    server {
#        listen       443 ssl http2 default_server;
#        listen       [::]:443 ssl http2 default_server;
#        server_name  _;
#        root         /usr/share/nginx/html;
#
#        ssl_certificate "/etc/pki/nginx/server.crt";
#        ssl_certificate_key "/etc/pki/nginx/private/server.key";
#        ssl_session_cache shared:SSL:1m;
#        ssl_session_timeout  10m;
#        ssl_ciphers PROFILE=SYSTEM;
#        ssl_prefer_server_ciphers on;
#
#        # Load configuration files for the default server block.
#        include /etc/nginx/default.d/*.conf;
#
#        location / {
#        }
#
#        error_page 404 /404.html;
#            location = /40x.html {
#        }
#
#        error_page 500 502 503 504 /50x.html;
#            location = /50x.html {
#        }
#    }

}

```



#### 防火墙

关闭防火墙

```
systemctl stop firewalld
```

查看防火墙

```
systemctl status firewalld
```

禁止开机启动防火墙

```
systemctl disable firewalld
```



查看全部开放端口

```
firewall-cmd --list-all
```

 添加nginx端口

```
sud firewall-cmd --add-port=80/tcp --permanent
```

重启防火墙

```
firewall-cmd --reload
```

访问nginx

#### 反向代理配置

![image-20201228160448005](G:\note\image\image-20201228160448005.png)

实现效果

浏览器访问www.123.com 访问到nginx服务器 然后转发到tomcat服务器

1 本地host添加域名

![image-20201228161449320](G:\note\image\image-20201228161449320.png)



![image-20201228161549316](G:\note\image\image-20201228161549316.png)

 2 nginx配置转发

![image-20201229003943452](G:\note\image\image-20201229003943452.png)

2 根据路径转发

![image-20201229010123658](G:\note\image\image-20201229010123658.png)

#### 配置负载均衡

将请求分发到不同的服务器上

![image-20201229011538680](G:\note\image\image-20201229011538680.png)

1  轮询 默认方式  服务器轮流访问

2 weight 权重

3 ip_hash 如果客户端访问一天服务 以后都访问这个服务器 解决session共享问题

```
#动态服务器组
    upstream dynamic_zuoyu {
        ip_hash;    #保证每个访客固定访问一个后端服务器
        server localhost:8080   weight=2;  #tomcat 7.0
        server localhost:8081;  #tomcat 8.0
        server localhost:8082;  #tomcat 8.5
        server localhost:8083   max_fails=3 fail_timeout=20s;  #tomcat 9.0
    }
```



4 fair第三方  根据相应时间

```
 #动态服务器组
    upstream dynamic_zuoyu {
        server localhost:8080;  #tomcat 7.0
        server localhost:8081;  #tomcat 8.0
        server localhost:8082;  #tomcat 8.5
        server localhost:8083;  #tomcat 9.0
        fair;    #实现响应时间短的优先分配
    }
```

#### 动静分离

![image-20201229012646272](G:\note\image\image-20201229012646272.png)

```
   gzip  on;
    #增加upstream模块
    upstream tomcat_server {
      server 192.168.0.25:8080;
      }
    upstream static_server {
      server 192.168.0.102;
     }
    #修改默认server的中配置
    server {
        listen       80;
        server_name  localhost;
        location / {
        root html;
        index index.html;
}
            #匹配到http://ip/*.jpg或者*.png或者*.css时，交由静态资源服务器102处理
        location ~ .*\.(jpg|png|css) {
          proxy_pass http://static_server;
          proxy_set_header X-Real-IP $remote_addr;
}
           #匹配到http://ip/*.jsp时，交由后台tomcat处理动态资源
       location ~ .*\.jsp$ {
         proxy_pass http://tomcat_server;
         proxy_set_header X-Real-IP $remote_addr;
}
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
}
```

#### 高可用

1 准备

![image-20201229014550997](G:\note\image\image-20201229014550997.png)

```
1 需要两台nginx服务器
2 需要keepalived
3 虚拟ip
```

3 两台服务器 装keepalived

```
yum install keepalived -y
```

![image-20201229022031486](G:\note\image\image-20201229022031486.png)

修改配置文件

```
! Configuration File for keepalived

global_defs {
router_id bhz005 ##标识节点的字符串，通常为hostname
}
## keepalived会定时执行脚本并且对脚本的执行结果进行分析，动态调整vrrp_instance的优先级。这里的权重weight 是与下面的优先级priority有关，如果执行了一次检查脚本成功，则权重会-20，也就是由100 - 20 变成了80，Master 的优先级为80 就低于了Backup的优先级90，那么会进行自动的主备切换。
如果脚本执行结果为0并且weight配置的值大于0，则优先级会相应增加。
如果脚本执行结果不为0 并且weight配置的值小于0，则优先级会相应减少。
vrrp_scriptchk_nginx {
    script "/etc/keepalived/nginx_check.sh" ##执行脚本位置
    interval 2 ##检测时间间隔
    weight -20 ## 如果条件成立则权重减20（-20）
}
## 定义虚拟路由 VI_1为自定义标识。
vrrp_instance VI_1 {
state MASTER   ## 主节点为MASTER，备份节点为BACKUP
## 绑定虚拟IP的网络接口（网卡），与本机IP地址所在的网络接口相同（我这里是eth6）
interface eth6  
virtual_router_id 172  ## 虚拟路由ID号
mcast_src_ip 192.168.1.172  ## 本机ip地址
priority 100  ##优先级配置（0-254的值）
Nopreempt  ## 
advert_int 1 ## 组播信息发送间隔，俩个节点必须配置一致，默认1s
authentication {  
auth_type PASS
auth_passbhz ## 真实生产环境下对密码进行匹配
    }

track_script {
chk_nginx
    }

virtual_ipaddress {
        192.168.1.170 ## 虚拟ip(vip)，可以指定多个
    }
}
```

添加检查脚本

```
#!/bin/bash
A=`ps -C nginx–no-header |wc -l`
if [ $A -eq 0 ];then
    /usr/local/nginx/sbin/nginx
sleep 2
if [ `ps -C nginx --no-header |wc -l` -eq 0 ];then
killallkeepalived
fi
fi
```

Keepalived常用命令

```
servicekeepalived start
servicekeepalived stop
```

#### Nginx原理解析

![image-20201229031527797](G:\note\image\image-20201229031527797.png)

![image-20201229031134515](G:\note\image\image-20201229031134515.png)

一个master和多个woker好处

 1 可以使用nginx -s reload 热部署

  2 每个worker是独立的进程，如果其中一个worker出问题 其他的worker继续执行

  3 worker数量和cup的线程数相同

  4  链接数worker_connection

第一个 发送请求占用了 woker的几个练技术

静态资源两个 2 

还要去请求tomcat 4个

worker m 个  每个worker的最大连接数1024支持的最大并发数 

m*1024/2  静态资源

m*1024/4  请求数据库