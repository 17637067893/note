#### 注解

1. @ComponentScan 组件扫描

   ```
   @ComponentScan主要就是定义扫描的路径从中找出标识了需要装配的类自动装配到spring的bean容器中
   ```

2. @MapperScan

   ```
   指定要变成实现类的接口所在的包，然后包下面的所有接口在编译之后都会生成相应的实现类
   ```

3. @Value

   注入Spring boot application.properties配置的属性的值

   ```
      @Value(value = “#{message}”)
      private String message;
   ```

4. @Bean

   ```
   用@Bean标注方法等价于XML中配置的bean。
   ```

5. @ResponseBody

   ```java
   表示该方法的返回结果直接写入HTTP response body中，一般在异步获取数据时使用，用于构建RESTful的api。在使用@RequestMapping后，返回值通常解析为跳转路径，加上@esponsebody后返回结果不会被解析为跳转路径，而是直接写入HTTP response body中。比如异步获取json数据，加上@Responsebody后，会直接返回json数据。该注解一般会配合
   @RequestMapping(“/test”)
     @ResponseBody
     public String test(){
        return”ok”;
    }
   ```

6. @RestController

   ```
   @RestController注解是@Controller和@ResponseBody的合集,表示这是个控制器bean,并且是将函数的返回值直 接填入HTTP响应体中,是REST风格的控制器。
   ```

7. @Configuration

   ```
   @Configuration 等同于spring的XML配置文件；使用Java代码可以检查类型安全
   ```

8. @SpringBootApplication

   申明让spring boot自动给程序进行必要的配置，这个配置等同于：@Configuration ，@EnableAutoConfiguration 和 @ComponentScan 三个配置。

   ```java
   package com.example.myproject;
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   
   @SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan 
   public class Application {
       public static void main(String[] args) {
           SpringApplication.run(Application.class, args);
       }
   }
   ```

9. @EnableAutoConfiguration

   pringBoot自动配置（auto-configuration）：尝试根据你添加的jar依赖自动配置你的Spring应用。例如，如果你的classpath下存在HSQLDB，并且你没有手动配置任何数据库连接beans，那么我们将自动配置一个内存型（in-memory）数据库”。你可以将@EnableAutoConfiguration或者@SpringBootApplication注解添加到一个@Configuration类上来选择自动配置。如果发现应用了你不想要的特定自动配置类，你可以使用@EnableAutoConfiguration注解的排除属性来禁用它们。

   ```
   
   ```

10. 

11. 

12. 

    

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

#### 注解

basepackage

springboot的主启动类所在的package就是扫描器的basepackage

**只有主启动类的平级和子包才能被扫描到**

![image-20201222231052328](G:\note\image\image-20201222231052328.png)

###### 1 @Configuration

```java
@Configuration(proxyBeanMethods = false) //Lite模式每次启动不会检查容器中有没有当前bean 重新创建新的
@Configuration(proxyBeanMethods = true) // Full模式 每次启动会检查容器中有没有当前bean 没有重新创建新的  有就不创建

配置类组件之间无依赖关系用Lite模式加速容器启动过程，减少判断
配置类组件之间有依赖关系，方法会被调用得到之前单实例组件，  • 用Full模式
```

###### 2 @Import

```
@Import({User.class, DBHelper.class})
 *      给容器中自动创建出这两个类型的组件、默认组件的名字就是全类名
 *
```

###### 3 @Conditional

条件注解

![image-20210513113313149](G:\note\image\image-20210513113313149.png)

###### 4 @ImportResource

原生配置文件引入

```
@ImportResource("classpath:beans.xml")
public class MyConfig {}

======================测试=================
        boolean haha = run.containsBean("haha");
        boolean hehe = run.containsBean("hehe");
        System.out.println("haha："+haha);//true
        System.out.println("hehe："+hehe);//true
```

###### 5 @ConfigurationProperties

把配置文件的数值和 对象的属性绑定

```
@ConfigurationProperties(prefix = "mycar")
```

###### 6 @EnableConfigurationProperties

@EnableConfigurationProperties =  @Component + @ConfigurationProperties

```java
@EnableConfigurationProperties(Car.class)
//1、开启Car配置绑定功能
//2、把这个Car这个组件自动注册到容器中
```

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

通过构造函数解析

1 bean

```java
@ConstructorBinding
如果某个bean没有使用@Component注入 可以@ConfigurationProperties(prefix = "student")启用该bean
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

    public Student(String name, int age, String birth, Boolean flag, List<Object> list, Set<Object> set, Double dnum, Map<String, String> map) {
        this.name = name;
        this.age = age;
        this.birth = birth;
        this.flag = flag;
        this.list = list;
        this.set = set;
        Dnum = dnum;
        this.map = map;
    }
    
}
```

2 解析文件

```java
@RestController

@EnableConfigurationProperties(Student.class)

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

springBoot的自动配置类原理

1 找到自动的配置类的文件

![image-20201223151732267](G:\note\image\image-20201223151732267.png)

2 找打需要配置

![image-20201223151759017](G:\note\image\image-20201223151759017.png)

3 

![image-20201223151919928](G:\note\image\image-20201223151919928.png)

4 

![image-20201223151955616](G:\note\image\image-20201223151955616.png)

松散绑定

![image-20201223162117175](G:\note\image\image-20201223162117175.png)

![image-20201223162130490](G:\note\image\image-20201223162130490.png)

绑定校验

![image-20201223163456154](G:\note\image\image-20201223163456154.png)

2配置文件 

![image-20201223163524500](G:\note\image\image-20201223163524500.png)

3 运行时会自动检查

![image-20201223163536691](G:\note\image\image-20201223163536691.png)

4 内部类 校验

![image-20201223164115849](G:\note\image\image-20201223164115849.png)

5 使用@Value绑定

![image-20201223185545653](G:\note\image\image-20201223185545653.png)

![image-20201223185533204](G:\note\image\image-20201223185533204.png)

application.yml分区

```yml
//表示启用的区域
spring:
  profiles:
    active: test2
---
student:
  userName: test1
  age:  18
//当前区域的名字
spring:
  profiles: test1
---
student:
  userName: test2
  age:  18
spring:
  profiles: test2
---
student:
  userName: test3
  age:  18
spring:
  profiles: test3
```

也可以在IDEA中覆盖

![image-20201223192005679](G:\note\image\image-20201223192005679.png)

配置文件拆分

![image-20201223193311566](G:\note\image\image-20201223193311566.png)

```yml
student:
  userName: test2
  age:  18
  host: ${student.userName}:8080
```

启用时选择

![image-20201223193331671](G:\note\image\image-20201223193331671.png)

SPI

java SPI机制的思想。我们系统里抽象的各个模块，往往有很多不同的实现方案。面向的对象的设计里，我们一般推荐模块之间基于接口编程，模块之间不对实现类进行硬编码。一旦代码里涉及具体的实现类，就违反了可拔插的原则，如果需要替换一种实现，就需要修改代码。为了实现在模块装配的时候能不在程序里动态指明，这就需要一种服务发现机制。

java SPI就是提供这样的一个机制：为某个接口寻找服务实现的机制。有点类似IOC的思想，就是将装配的控制权移到程序之外，在模块化设计中这个机制尤其重要。

![image-20201223211225462](G:\note\image\image-20201223211225462.png)

1 java_spi下创建 service-common模块用于提供接口

![image-20201223211326598](G:\note\image\image-20201223211326598.png)



```
package cn.tx.service;

public interface PayService {
    void pay();
}
```

2 java_spi下创建ali-pay模块用于实现PayService接口

![image-20201223211438775](G:\note\image\image-20201223211438775.png)

pom.xml引用service-common模块

```
 <dependencies>
        <dependency>
            <groupId>tx.sprintboot</groupId>
            <artifactId>service-common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

创建AliPayImpl实现接口

```
public class AliPayImpl implements  PayService{
    @Override
    public void pay() {
        System.out.println("支付宝 支付");
    }
}
```

![image-20201223211948325](G:\note\image\image-20201223211948325.png)

2 创建wx-pay也实现service-common的接口

![image-20201223211652311](G:\note\image\image-20201223211652311.png)

pom配置依赖

```
<dependencies>
        <dependency>
            <groupId>tx.sprintboot</groupId>
            <artifactId>service-common</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>
```

![image-20201223211731294](G:\note\image\image-20201223211731294.png)

实现接口

```java
public class WxPayImpl implements PayService{
    @Override
    public void pay() {
        System.out.println("微信支付");
    }
}
```

创建配置文件

![image-20201223212041936](G:\note\image\image-20201223212041936.png)

3 创建测试模块

配置依赖

```xml
<dependency>
    接口必须
    <groupId>tx.sprintboot</groupId>
    <artifactId>service-common</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
需要哪个模块添加哪个依赖
<dependency>
    <groupId>tx.sprintboot</groupId>
    <artifactId>ali-pay</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>tx.sprintboot</groupId>
    <artifactId>wx-pay</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
    </dependencies>
```



![image-20201223212120955](G:\note\image\image-20201223212120955.png)

配置druid数据源

1 通过配置文件查看属性和前缀

![image-20201224201948416](G:\note\image\image-20201224201948416.png)

![image-20201224202005916](G:\note\image\image-20201224202005916.png)

找到配置文件对应的类可以看到前缀为 spring.datasource

![image-20201224202020679](G:\note\image\image-20201224202020679.png)

2 引入依赖

```xml
<dependency>
   <groupId>com.alibaba</groupId>
   <artifactId>druid</artifactId>
   <version>1.0.9</version>
</dependency>
<dependency>
   <groupId>log4j</groupId>
   <artifactId>log4j</artifactId>
   <version>1.2.15</version>
</dependency>
```

```yml
spring:
  datasource:
    username: root
    password: root
    url: jdbc:mysql://localhost:3306/boot_demo
    driver-class-name: com.mysql.jdbc.Driver
    修改数据源
    type: com.alibaba.druid.pool.DruidDataSource
    以下属性 配置类中可以创建注册类
    initialSize: 5
    minIdle: 5
    maxActive: 20
    maxWait: 60000
    timeBetweenEvictionRunsMillis: 60000
    minEvictableIdleTimeMillis: 300000
    validationQuery: SELECT 1 FROM DUAL
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    poolPreparedStatements: true
    filters: stat,wall,log4j
    maxPoolPreparedStatementPerConnectionSize: 20
    useGlobalDataSourceStat: true
    connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
```

创建数据源注册类

```java
@Configuration
public class DruidConfig {
    @ConfigurationProperties(prefix ="spring.datasource")
    @Bean
    public DataSource dataSource(){
        return new DruidDataSource();
    }
}
```

JdbdTemplate配置

由于内部已经自动关联了DataSource数据源 可以直接使用

```java
@RequestMapping("query")
public List<Map<String, Object>> query(){
    System.out.println("查询");
    List<Map<String, Object>> maps = jdbcTemplate.queryForList("select  * from user");
    return maps;
}
```

#### 请求参数

###### 1 @RequestParam

```java
 //获取指定参数
    @GetMapping("/test1")
    public String test1(@RequestParam(value = "username",required = false,defaultValue = "小华") String name){
        return name;
    }
```

###### 2 @PathVariable

```java
 @GetMapping("/test2/{id}/{username}")
    public String test2(@PathVariable("id") String id,@PathVariable("username") String username){
        System.out.println(id);
        System.out.println(username);
        return username;
    }

全部参数集中到map中
@GetMapping("/test2/{id}/{username}")
    public Map<String,String> test2(@PathVariable Map<String,String> map){
        System.out.println(map);
        return map;
    }
```

###### 3 @RequestHeader

```java
 @GetMapping("/test3")            @RequestHeader("host") String host //获取单个属性
    public Map<String,String> test3(@RequestHeader Map<String,String> map){

        System.out.println(map);
        return map;
    }

```

###### 4 @CookieValue

```java
 @GetMapping("/test2")
    public String test2(@CookieValue("pgv_pvi") String pgv_pvi){
        System.out.println(pgv_pvi);
        return pgv_pvi;
    }
```

5 POST请求

```java
@PostMapping("/hello")
    public String hello(@RequestParam("name") String name,
                        @RequestParam("age") Integer age) {
        return "name：" + name + "\nage：" + age;
    }
```



```java
 @PostMapping("/test3")          //  @RequestHeader("host") String host //获取单个属性
    public String test3(@RequestParam Map<String,Object> content){
        System.out.println(content.get("name"));
        System.out.println(content.get("password"));
        return "test3";
    }
```



#### Mybaties

1 引入依赖

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.1</version>
</dependency>
    <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.0.9</version>
</dependency>
```

2 创建bean

```java
@Data
public class User {
    private int id;
    private String name;
    private String d_id;
}
```

3 创建mapper

Mapper上可以不加@Mapper注解，通过@MapperScan扫描器来扫描mapper包下

```java
@SpringBootApplication
@MapperScan("cn.tx.sboot.mapper")
public class FirstSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstSpringApplication.class, args);
    }
}
```



```java
@Mapper
public interface UserMapper {
    @Select("select * from user")
    public List<User> getUser();

    @Select("select * from user where user.id = #{id}")
    public User getUserById(int id);

    @Insert("insert into user (id,name,d_id) values(#{id},#{name},#{d_id})")
    public void insert(User user);

    @Delete("delete from user where id=#{id}")
    void delete(int id);
}
```

4测试接口

```java
@RestController
public class TestUser {
    @Autowired
    private UserMapper userMapper;
    @RequestMapping("getUser")
    public List<User> test1(){
        return userMapper.getUser();
    }

    @RequestMapping("getUserById")
    public User getUserById(int id){
        return userMapper.getUserById(id);
    }

    @RequestMapping("insert")
    public String insert(User u){
        userMapper.insert(u);
        return "success";
    }
    @RequestMapping("delete")
    public String deleteUser(int id){
        userMapper.delete(id);
        return "success";
    }
}
```

Mybaties 配置文件

1 配置

```xml
mybatis:
  configuration:
    map-underscore-to-camel-case: true        把数据user_name 对应为 userName
  mapper-locations: classpath:mapper/*.xml   sql文件所在的文件
  type-aliases-package: demo1.bean   bean下的所有对象累 都以小写字母 别名
```

2 创建Dao

```java
@Mapper
public interface UserMapper {
    public List<User> getUser();

    public User getUserById(int id);

    public void insert(User user);

    void delete(int id);
}
```

3 创建sql文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="demo1.mapper.UserMapper">
   <select id="getUser" resultType="user">
       select * from user
   </select>
    <select id="getUserById" resultType="user">
        select * from user where user.id = #{id}
    </select>
    <insert id="insert" parameterType="user">
        insert into user (id,name,d_id) values(#{id},#{name},#{dId})
    </insert>
    <delete id="delete" parameterType="int">
        delete from user where id=#{id}
    </delete>
</mapper>
```

3 测试

```java
@RestController
public class TestUser {
    @Autowired
    private UserMapper userMapper;
    @RequestMapping("getUser")
    public List<User> test1(){
        return userMapper.getUser();
    }

    @RequestMapping("getUserById")
    public User getUserById(int id){
        return userMapper.getUserById(id);
    }

    @RequestMapping("insert")
    public String insert(User u){
        userMapper.insert(u);
        return "success";
    }
    @RequestMapping("delete")
    public String deleteUser(int id){
        userMapper.delete(id);
        return "success";
    }
}


```

#### 整合redis



#### Spring MVC自动配置

##### 拦截器

1 配置类拦截器

```java
@Configuration
@EnableWebMvc
//开启了自定义WebMVC
public class WebConfig implements WebMvcConfigurer {
    //配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置拦截器的路径                                请求的路径                 排除的路径
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**").excludePathPatterns("/hello");
    }
}

```

2 自动定义拦截

```java
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getMethod());
        System.out.println("前置拦截");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        System.out.println("前置之后");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("请求即将完成");
    }
}
```

3 接口测试

```java
@RestController
public class ReqDemo {
    @RequestMapping("webmvc")
    public String test1(){
        return "web mvc666";
    }
    @RequestMapping("hello")
    public String hello(){
        return "hello mvc666";
    }
}
```

##### 资源处理

1 配置类定义

```java
@Configuration
@EnableWebMvc
//开启了自定义WebMVC自定义配置 不用springboot内置的
public class WebConfig implements WebMvcConfigurer {
    //资源请求
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //  请求的地址                             资源的位置
        registry.addResourceHandler("/resource/**").addResourceLocations("classpath:/resource/");
    }
}

```

2 

![image-20201226144543570](G:\note\image\image-20201226144543570.png)

3 请求测试

![image-20201226144627991](G:\note\image\image-20201226144627991.png)

#### 消息转换器

1 引入依赖

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.31</version>
</dependency>
```

2 配置类中配置

```java
//    消息转换器
@Override
public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    //1.需要定义一个convert转换消息的对象;
    FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
    //2:添加fastJson的配置信息;
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
    //3处理中文乱码问题
    List<MediaType> fastMediaTypes = new ArrayList<>();
    fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
    //4.在convert中添加配置信息.
    fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
    fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
    converters.add(fastJsonHttpMessageConverter);
}
```

3 请求测试

```java
  @RequestMapping("fastjson")
public User get(){
    User user = new User("小明",100,null);
    return user;
}
```

#### Thymeleaf

1 引入依赖

```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

springBoot启动的时候会自动配置
org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration

从ThymeleafAutoConfiguration的源代码中我们可以得知ThymeleafProperties
中配置了Thymeleaf的规则
public class ThymeleafProperties {
    private static final Charset DEFAULT_ENCODING;
    public static final String DEFAULT_PREFIX = "classpath:/templates/";
    public static final String DEFAULT_SUFFIX = ".html";
    private boolean checkTemplate = true;
    private boolean checkTemplateLocation = true;
    private String prefix = "classpath:/templates/";
    private String suffix = ".html";
    private String mode = "HTML";
    private Charset encoding;
    private boolean cache;
```

2  创建HTMl

 在html中引入thymeleaf的命名空间

<html lang="en" xmlns:th="http://www.thymeleaf.org">

```html
<!DOCTYPE HTML>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
 <h1 th:text="${hello}"></h1>
</body>
</html>
```

3 添加Controller

```java
@Controller
public class DemoController {
    @GetMapping("hello")
    public String test1(Model model){
        model.addAttribute("hello","<h1>你好</h1>");
        return "hello";
    }
}

访问页面即可
```

##### 国际化

1 创建不同语言对应的配置文件

![image-20201227061647680](G:\note\image\image-20201227061647680.png)

2 添加配置  默认为 /messages文件

![image-20201227061707904](G:\note\image\image-20201227061707904.png)

3 页面获取

```xml
切换不同国家 显示不同内容
<!DOCTYPE HTML>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<!-- #引入属性文件-->
<div th:text="#{username}"></div>
</body>
</html>
```

##### 获取实体类对象属性

1 创建对象

```java
package demo1.controller.model;

public class User {
    public String name;
    private int age;
    private Rote rote;

    public User(String name, int age, Rote rote) {
        this.name = name;
        this.age = age;
        this.rote = rote;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Rote getRote() {
        return rote;
    }

    public void setRote(Rote rote) {
        this.rote = rote;
    }

    public static class Rote {
        private String routename;

        public Rote(String routename) {
            this.routename = routename;

        }

        public String getRoutename() {
            return routename;
        }

        public void setRoutename(String routename) {
            this.routename = routename;
        }
    }
}

```

2 

```java
 @GetMapping("test2")
    public String test2(Model model){
        User.Rote rote = new User.Rote("学生");
        User user = new User("小明",19,rote);
        model.addAttribute("user",user);
        return "hello";
    }
```

3 

```html
<div th:text="${'姓名'+user.name}"></div>
<div th:text="${'年龄'+user.age}"></div>
<div th:text="${'角色'+user.rote.routename}"></div>
```



![image-20210513233015237](G:\note\image\image-20210513233015237.png)