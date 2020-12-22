#### SpringBoot主要特性

1、 SpringBoot Starter：他将常用的依赖分组进行了整合，将其合并到一个依赖中，这样就可以一次性添加到项目的Maven或Gradle构建中；

 

2、 使编码变得简单，SpringBoot采用 JavaConfig的方式对Spring进行配置，并且提供了大量的注解，极大的提高了工作效率。

 

3、 自动配置：SpringBoot的自动配置特性利用了Spring对条件化配置的支持，合理地推测应用所需的bean并自动化配置他们；

 

4、 使部署变得简单，SpringBoot内置了三种Servlet容器，Tomcat，Jetty,undertow.我们只需要一个Java的运行环境就可以跑SpringBoot的项目了，SpringBoot的项目可以打成一个jar包。

 

现在流行微服务与分布式系统，springboot就是一个非常好的微服务开发框架，你可以使用它快速的搭建起一个系统。同时，你也可以使用spring cloud（Spring Cloud是一个基于Spring Boot实现的云应用开发工具）来搭建一个分布式的架构。

#### 环境准备

apache-maven-3.3.9\conf\setting

1 配置阿里云镜像

```xml
<mirror>  
    <id>nexus-aliyun</id>  
    <mirrorOf>central</mirrorOf>    
    <name>Nexus aliyun</name>  
    <url>http://maven.aliyun.com/nexus/content/groups/public</url>  
</mirror>
```

2 profiles标签添加

```xml
<profile>
    <id>jdk-1.8</id>
    <activation>
        <activeByDefault>true</activeByDefault>
        <jdk>1.8</jdk>
    </activation>
    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
    </properties>
</profile>
```

3 IDEA设置JDK 

4 Mavn仓库

#### 代码实现

1 创建一个maven父工程tx_sboot

![image-20201222230445335](G:\note\image\image-20201222230445335.png)

pom.xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    本项目的坐标
    <groupId>cn.tx.springboot</groupId>
    <artifactId>tx_sboot</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>

     使用启动器
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.0.RELEASE</version>
    </parent>

       注入依赖
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
</project>
```

在父工程下创建springboot-first

![image-20201222230635991](G:\note\image\image-20201222230635991.png)

1 创建测试类

```java
@RestController
public class TestController {
    @RequestMapping("hello")
    public String hello(){
        return "hello";
    }
}
```

2 创建springboot启动类

```java
@SpringBootApplication
public class FirstSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstSpringApplication.class, args);
    }
}
```

3 启动并且测试：http://localhost:8080/hello

#### 打包测试

父工程tx_sboot中加入构建依赖

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

执行命令

```
mvn -f springboot-first clean package
```

mvn -f springboot-first clean package

打完jar包后，我们切入到对应的jar包里面执行

#### 默认扫描器basepackage

springboot的主启动类所在的package就是扫描器的basepackage

**只有主启动类的平级和子包才能被扫描到**

![image-20201222231052328](G:\note\image\image-20201222231052328.png)

#### 源码解析

@AutoConfigurationPackage

自动配置包负责basepackge的注册

​	@AutoConfigurationPackage内部使用@Import来做bean的定义的注册

![image-20201222231306341](G:\note\image\image-20201222231306341.png)

让我们进入AutoConfigurationPackages.Registrar，通过register的调用来注册basepackage的bean定义的

![image-20201222231318571](G:\note\image\image-20201222231318571.png)

进入到PackageImports，获得basepackge设置给packageNames

![image-20201222231809760](G:\note\image\image-20201222231809760.png)

回到上一层进入到register，创建bean的定义并且把packageNames设置给bean定义

然后把bean定义的做注册。

![image-20201222231819433](G:\note\image\image-20201222231819433.png)

#### 热部署

1 加入依赖

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

2 IDEA设置

原理 

第三方包被 base classloader 加载

我们写的代码被  restart classloader加载

![image-20201222232150934](G:\note\image\image-20201222232150934.png)

ctrl + shift + alt + / ,选择Registry,勾上 Compiler autoMake allow when app running

 ![image-20201222232217714](C:\Users\gg\AppData\Roaming\Typora\typora-user-images\image-20201222232217714.png)

热部署的排除

默认情况下，/META-INF/maven，/META-INF/resources，/resources，/static，/templates，/public这些文件夹下的文件修改不会使应用重启，但是会重新加载（devtools内嵌了一个LiveReload server，当资源发生改变时，浏览器刷新）。

也可以自定义排除

```
spring.devtools.restart.exclude=static/**,public/**
```

#### boot配置文件

springboot 启动会扫描以下位置的application.properties或者application.yml文件作为Spring boot的默认配置文件

–file:./config/

–file:./ 项目的跟路径，如果当前的项目有父工程，配置文件要放在父工程 的根路径

–classpath:/config/

–classpath:/

优先级由高到底，高优先级的配置会覆盖低优先级的配置；

SpringBoot会从这四个位置全部加载主配置文件；互补配置；

![image-20201222232409214](G:\note\image\image-20201222232409214.png)

修改配置文件名称

如果我们的配置文件名字不叫application.properties或者application.yml，可以通过以下参数来指定配置文件的名字，myproject是配置文件名

```
java -jar myproject.jar --spring.config.name=myproject
```

#### yaml配置

1 创建config/application.yml

```yml
student:
    name:   小红
    age:    18
    birth:  2020/5/20
    list:   [100,xiaoming,小红]
    flag:   true
    Dnum:   88.88
    set:    [set1,set2,set3]
    map:    {k1: val1,k2: val2}
```

2 解析文件

```java
创建对应对象
@Component
@ConfigurationProperties(prefix = "student")
public class Student {
    private String name;
    private int age;
    private String birth;
    private Boolean flag;
    private List<Object> list;
    private Set<Object> set;
    private Double Dnum;
    private Map<String,String> map;

2 
@RestController
public class TestController {
    
    @Autowired
    private Student student;
    
    @RequestMapping("hello")
    public String hello(){
        System.out.println(student);
        return "你好d89";
    }

}
```

当前的实体类能在配置文件中有对应的提示，我们需要引入如下的依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

加完依赖后通过Ctrl+F9来使之生效

![image-20201222233951767](G:\note\image\image-20201222233951767.png)

在属性绑定的方式里，我们是通过set方法来完成的，我们可以借助Lombok来给我们带来方便。

父工程中引入Lombok的依赖

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.16.20</version>
</dependency>
```

