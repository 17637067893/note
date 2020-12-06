1 持续集成 CI

```
频繁的将代码合并到主干上，目的可以让产品快速迭代同时保持代码的质量，核心措施是，集成到主干之前必须通过自动化测试，测试不通过，就不集成
```
安装Jenkins
```
Jenkins依赖JDK所有先安装JDK1.8
1 yum install java-1.8.0-openjdk* -y
2 有两种方法
   第一 利用官网命令安装Jenkins  配置文件在 etc/sysconfig/jenkins
  第二种 下载 war包上传到服务器
   rpm -ivh jenkins-2.190.3-1.1.noarch.rpm
3 启动jenkins
   systemctl start jenkins
4 打开浏览器
    ip地址:8080
 ip地址:8080/restart //后边根restart重启jenkins
```
管理员用户名 admin  密码 admin

安装harbor 请求页面502
链接 [(4条消息) centos harbor 安装与踩坑_asihacker的博客-CSDN博客](https://blog.csdn.net/weixin_42887206/article/details/109478278)

```
查看容器 unhealty需要关闭防火墙 

我们检查一下防火墙
systemctl status firewalld.service
发现运行中
我们把防火墙关闭试试
systemctl stop firewalld.service #关闭防火墙
systemctl disable firewalld.service #设置开机不自动启动防火墙
然后cd harbor的目录
service docker restart #关闭防火墙之后docker需要重启
docker-compose stop #然后关闭
docker-compose up -d #再启动一下看看
```