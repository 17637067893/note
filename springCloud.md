#### Spring Cloud

Spring Cloud 是一套完整的微服务解决方案，基于 Spring Boot 框架，准确的说，它不是一个框架，而是一个大的容器，它将市面上较好的微服务框架集成进来，从而简化了开发者的代码量。

1. 新建父工程

   ![image-20201230134511089](G:\note\image\image-20201230134511089.png)

   

2. 修改编码

   ![image-20201230134808073](G:\note\image\image-20201230134808073.png)

3. 注解生效

   ![image-20201230134915304](G:\note\image\image-20201230134915304.png)

4. 指定java版本

   ![image-20201230135041138](G:\note\image\image-20201230135041138.png)

5. 文件过滤

   ![image-20201230135124080](G:\note\image\image-20201230135124080.png)

6. maven中的dependencyManagent

    Maven中的dependencyManagent管理项目依赖版本的一个标签，一般在父项目中只会声明依赖的版本并不会引入依赖 ，子项目需要自己引入依赖，如果给你版本号就用自己的。如果没有指定，就去找父项目的！

   微服务 将单一的应用程序拆分成多个小型服务



#### 新建父工程

###### 1 指定项目编码

![image-20210107140901944](G:\note\image\image-20210107140901944.png)

2 注解生效激活

Lombok 插件可以帮忙简化代码的书写 , 此插件需要开启 [annotation processors] 功能

![image-20210107141057691](G:\note\image\image-20210107141057691.png)

3 设置项目maven

![image-20210107141933787](G:\note\image\image-20210107141933787.png)

1. 添加pom  规定项目中的依赖版本号

   ```xml
       <!-- 子模块继承之后，提供作用：锁定版本+子modlue不用写groupId和version  -->
   <!--    指定了依赖的版本并没有引入依赖-->
       <dependencyManagement>
           <dependencies>
               <!--spring boot 2.2.2-->
               <dependency>
                   <groupId>org.springframework.boot</groupId>
                   <artifactId>spring-boot-dependencies</artifactId>
                   <version>2.2.2.RELEASE</version>
                   <type>pom</type>
                   <scope>import</scope>
               </dependency>
               <!--spring cloud Hoxton.SR1-->
               <dependency>
                   <groupId>org.springframework.cloud</groupId>
                   <artifactId>spring-cloud-dependencies</artifactId>
                   <version>Hoxton.SR1</version>
                   <type>pom</type>
                   <scope>import</scope>
               </dependency>
               <!--spring cloud alibaba 2.1.0.RELEASE-->
               <dependency>
                   <groupId>com.alibaba.cloud</groupId>
                   <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                   <version>2.1.0.RELEASE</version>
                   <type>pom</type>
                   <scope>import</scope>
               </dependency>
   
               <dependency>
                   <groupId>mysql</groupId>
                   <artifactId>mysql-connector-java</artifactId>
                   <version>${mysql.version}</version>
               </dependency>
               <dependency>
                   <groupId>com.alibaba</groupId>
                   <artifactId>druid</artifactId>
                   <version>${druid.version}</version>
               </dependency>
               <dependency>
                   <groupId>org.mybatis.spring.boot</groupId>
                   <artifactId>mybatis-spring-boot-starter</artifactId>
                   <version>${mybatis.spring.boot.version}</version>
               </dependency>
               <dependency>
                   <groupId>junit</groupId>
                   <artifactId>junit</artifactId>
                   <version>${junit.version}</version>
               </dependency>
               <dependency>
                   <groupId>log4j</groupId>
                   <artifactId>log4j</artifactId>
                   <version>${log4j.version}</version>
               </dependency>
               <dependency>
                   <groupId>org.projectlombok</groupId>
                   <artifactId>lombok</artifactId>
                   <version>${lombok.version}</version>
                   <optional>true</optional>
               </dependency>
           </dependencies>
       </dependencyManagement>
   
       <build>
           <plugins>
               <plugin>
                   <groupId>org.springframework.boot</groupId>
                   <artifactId>spring-boot-maven-plugin</artifactId>
                   <version>2.2.4.RELEASE</version>
                   <configuration>
                       <fork>true</fork>
                       <addResources>true</addResources>
                   </configuration>
               </plugin>
           </plugins>
       </build>
   ```

   

![image-20210106111708829](G:\note\image\image-20210106111708829.png)

nacos启动后可以使用查看日志信息

```
cat /root/nacos/logs/nacos.log
```

错误一

```
java.net.UnknownHostException: jmenv.tbsite.net

很遗憾，是启动模式没设置导致的。 //默认为集群启动
sh startup.sh -m standalone
```

#### cloud-provider-payment8001

###### 1 pom

```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
    </dependencies>
```

###### 2 yml

```yml
server:
  port: 8001 #端口
spring:
  application:
    name: cloud-payment-service #模块名称
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource #连接池
    driver-class-name: com.mysql.jdbc.Driver  #驱动
    url: jdbc:mysql://localhost:3306/db2019?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: root01
mybatis:
  mapper-locations: classpath:mapper/*.xml 
#  扫描包下的对象类最为bean注入ioc容器
  type-aliases-package: com.atguigu.springcloud.entities
```

![image-20210107155913086](G:\note\image\image-20210107155913086.png)

![image-20210107155308537](G:\note\image\image-20210107155308537.png)

###### 3 entities

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
    private Long id;
    private String serial;
}
```

CommonResult

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {
    private Integer code;
    private String message;
    private T data;
    public CommonResult(Integer code,String message){
        this(code,message,null);
    }
}
```



###### 4 xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.atguigu.springcloud.dao.PaymentDao">
<!--    主要是在主键是自增的情况下，添加成功后可以直接使用主键值，其中keyProperty的值是对象的属性值不是数据库表中的字段名-->
    <insert id="create" parameterType="Payment" useGeneratedKeys="true" keyProperty="id">
        insert into payment(serial) values (#{serial})
    </insert>
    <resultMap id="BaseResultMap" type="com.atguigu.springcloud.entities.Payment">
        <id column="id" property="id" jdbcType="BIGINT"></id>
        <result column="serial" property="serial" jdbcType="VARCHAR"></result>
    </resultMap>
    <select id="getPaymentById" parameterType="Long" resultMap="BaseResultMap">
        select * from payment where id = #{id}
    </select>
</mapper>

```

###### 5 Dao

```java
@Mapper 自动个给接口生成一个实现类注入到IOC容器中
public interface PaymentDao {
    public int create(Payment payment);
    Payment getPaymentById(Long id);
}
```

###### 6 service

```java
public interface PaymentService {
    public int create(Payment payment);

    Payment getPaymentById(@Param("id") Long id);
}
```

 service实现

```java
@Service
public class PaymentServiceImpl implements PaymentService {
    // @Autowired按类型装配依赖对象，默认情况下它要求依赖对象必须存在
    @Resource  //默认为byName 也可以为byType
    private PaymentDao paymentDao;

    @Override
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }
}
```

###### 7 Controller

```java
@RestController
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/payment/create")
    public CommonResult create(Payment payment){
        int result = paymentService.create(payment);
        if(result>0){
            return new CommonResult(200,"数据插入成功",result);
        }else {
            return new CommonResult(4444,"数据插入失败",null);
        }
    }

    @GetMapping(value = "/payment/get")
    public CommonResult getPaymentById(@RequestParam("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        if(payment!=null){
            return new CommonResult(200,"查询成功",payment);
        }else{
            return new CommonResult(444,"查询失败",null);
        }
    }
}
```

#### RestTemplate

一种 HTTP 请求工具

###### 1 注入IOC

```java
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```

###### 2 使用

```java
@RestController
public class OrderController {
    public static final String URL="http://localhost:8001";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(URL + "/payment/create",payment,CommonResult.class);
    }
    @GetMapping("/consumer/payment/get")
    public CommonResult<Payment> getPayment(@RequestParam("id") Long id){
        return restTemplate.getForObject(URL +"/payment/get?id="+id,CommonResult.class);
    }
}
```

#### 抽取公共代码

1 新建cloud-api-commons

2 写pom

```xml
<dependencies>
        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
           <optional>true</optional>
        </dependency>

        <!-- https://mvnrepository.com/artifact/cn.hutool/hutool-all -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.1.0</version>
        </dependency>
    </dependencies>
```

3 编写公共代码

![image-20210107170309996](G:\note\image\image-20210107170309996.png)

4 maven命令  clean install

5 引入公共模块

```xml
<dependency>
    <groupId>com.atguigu.springcloud</groupId>
    <artifactId>cloud-api-commons</artifactId>
    <version>${project.version}</version>
</dependency>
```

#### eureka

spring cloud中的一个负责服务注册与发现的组件

服务端 pom依赖为spring-cloud-starter-netflix-eureka-server 主程序@EnableEurekaServer

客户端pom依赖为spring-cloud-starter-netflix-eureka-client 主程序@EnableEurekaClient

###### 服务端集群

host内先配域名

1 新建模块

cloud-eureka-server7001

2 添加依赖

```xml
<dependencies>
        <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-eureka-server -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>

        <dependency>
            <groupId>com.atguigu.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web  -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </dependency>

    </dependencies>
```

3 yml配置

```yaml
server:
  port: 7001
eureka:
  instance:
    hostname: localhost  #eureka服务端的实例名字
  client:
    register-with-eureka: false  #表识不向注册中心注册自己
    fetch-registry: false   #表示自己就是注册中心，职责是维护服务实例，并不需要去检索服务
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka    #设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
```

4 启动类

```java
@EnableEurekaServer  表示为服务端
@SpringBootApplication
public class EurekaMain7001 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaMain7001.class,args);
    }
}
```

5 访问页面

![image-20210107184639439](G:\note\image\image-20210107184639439.png)

新建模块

cloud-eureka-server7002

其他同理只需要修改

```
修改端口
1 server.port: 7002
注册到7001服务端
2 defaultZone: http://eureka7001.com:7001/eureka
```

###### 客户端集群

1 新建模块

cloud-provider-payment8001

2 添加依赖

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <artifactId>demo1</artifactId>
            <groupId>org.example</groupId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.example</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>
<!--        eureka的客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
    </dependencies>
```

3 yml配置

```yaml
server:
  port: 8001
spring:
  application:
    name: cloud-payment-service
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/db2019?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: root01
mybatis:
  mapper-locations: classpath:mapper/*.xml
#  扫描包下的对象类最为bean注入ioc容器
  type-aliases-package: com.atguigu.springcloud.entities
eureka:
  instance:
#    修改名称
#    DESKTOP-V1VQHCJ:cloud-payment-service:8001 -> payment8001
    instance-id: payment8001
#    显示ip
    prefer-ip-address: true
    #Eureka服务端 向客户端发送的心跳的时间间隔 单位为秒默认30秒
    lease-renewal-interval-in-seconds: 1
    #Eureka服务端接受最后一次心跳等待的时间上限，单位为秒(默认为90秒)
    lease-expiration-duration-in-seconds: 2
  client:
    register-with-eureka: true
    fetchRegistry: true
    service-url:
    #注册到全部客户端
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka

  server:
#    关闭自我保护
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 2000
```

4 主启动类

```java
@SpringBootApplication
@EnableEurekaClient
public class Payment8001 {
    public static void main(String[] args) {
        SpringApplication.run(Payment8001.class,args);
    }
}
```

5 controller

```java
    @Value("${server.port}")
    private String serverPort;

    @PostMapping(value = "/payment/create")
    public CommonResult create){
        System.out.println(serverPort);
        return new CommonResult(200,"数据插入成功"+serverPort,null);
    }
```

再新建 cloud-provider-payment8001 更改端口即可

###### 消费者

1 新建cloud-consumer-order80

2 pom

```xml
<dependencies>
        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web  -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <artifactId>demo1</artifactId>
            <groupId>org.example</groupId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.example</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-netflix-eureka-client</artifactId>
        </dependency>
    </dependencies>
```

3 yml

```yaml
server:
  port: 80
spring:
  application:
    name: cloud-order-service

eureka:
  client:
    register-with-eureka: true
    fetchRegistry: true
    service-url:  #localhost注册上去
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
```

4 启动类

```java
@SpringBootApplication
@EnableEurekaClient
public class OrderMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class,args);
    }
}
```

5 controller

```java
@RestController
public class OrderController {
                               //服务集群的名称
    public static final String URL="http://CLOUD-PAYMENT-SERVICE";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(URL + "/payment/create",payment,CommonResult.class);
    }
    @GetMapping("/consumer/payment/get")
    public CommonResult<Payment> getPayment(@RequestParam("id") Long id){
        return restTemplate.getForObject(URL +"/payment/get?id="+id,CommonResult.class);
    }
}
```

6 自定义均衡负载

```java
@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```

测试

```
localhost/consumer/payment/create?serial=loade
可以轮询返回结果
```

自我保护机制

死马当活马医 如果客户端心跳不稳定 服务端不会马上移除该客户端 等一段时间后再移除

#### consul

类似eureka注册中心一个写好的软件

软件所在的文件 执行 consul agent -dev即可开启注册中

http:localhost:8500

![image-20210107194020859](G:\note\image\image-20210107194020859.png)

###### provider

cloud-providerconsul-payment8006

2 pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>cloud2020</artifactId>
        <groupId>com.atguigu.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>cloud-providerconsul-payment8006</artifactId>

    <dependencies>
        <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-consul-discovery -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        </dependency>

        <dependency>
            <groupId>com.atguigu.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>


        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>



    </dependencies>

</project>
 
```

3 yml

```
server:
  port: 8006


spring:
  application:
    name: consul-provider-payment
  cloud:
    consul: 注册中心
      host: localhost
      port: 8500
      discovery: 集群的名字
        service-name: ${spring.application.name}
 
```

4 启动类

```java
package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PaymentMain8006 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8006.class,args);
    }
```

5 controller

```java
package com.atguigu.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/payment/consul")
    public String paymentConsul(){
        return "springcloud with consul: "+serverPort+"\t"+ UUID.randomUUID().toString();
    }
}
```

6 更改端口号新建8007模块

###### consumer

1 新建cloud-consumerconsul-order80

2 pom

```xml
<dependencies>
        <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-consul-discovery -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        </dependency>

        <dependency>
            <groupId>com.atguigu.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>


        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>



    </dependencies>
```

3 yml

```yaml
server:
  port: 80


spring:
  application:
    name: consul-consumer-order
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name}
```

4 启动类

```java
package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderConsulMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderConsulMain80.class,args);
    }
}
```

5 负载均衡

```java
package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {

    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
```

6 controller

```java
package com.atguigu.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderConsulController {

    public static final String INVOME_URL = "http://consul-provider-payment";

    @Resource 
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/consul")
    public String payment (){
      String result = restTemplate.getForObject(INVOME_URL+"/payment/consul",String.class);
      return result;
    }


}
```

测试

访问

localhost/consumer/payment/consul可以获得不同的端口

#### Ribbon

是一套基于客户端 负载均衡和服务调用工具 ===RestTemplate+负载均衡

Nginx 客户端请求交给nginx 然后由nginx实现转发

Ribbon 本地负载均衡 在注册中心上获取注册信息缓存到JVM本地从而实现远程调用

![image-20210107205218472](G:\note\image\image-20210107205218472.png)

###### 应用

在启动类的上层目录

![image-20210107205331903](G:\note\image\image-20210107205331903.png)

定义负载均衡策略

```java
@Configuration
public class MySelfRule {

    @Bean
    public IRule myRule(){
//        return new RandomRule();//定义为随机
        return new RoundRobinRule();//轮询

    }

}
```

启动类指定集群

```java
@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name="CLOUD-PAYMENT-SERVICE",configuration = MySelfRule.class)
public class OrderMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class,args);
    }
}
```

使用RestTemplate调用服务

```java
@RestController
public class OrderController {
    public static final String URL="http://CLOUD-PAYMENT-SERVICE";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(URL + "/payment/create",payment,CommonResult.class);
    }
    @GetMapping("/consumer/payment/get")
    public CommonResult<Payment> getPayment(@RequestParam("id") Long id){
        return restTemplate.getForObject(URL +"/payment/get?id="+id,CommonResult.class);
    }
}
```

原理

![image-20210107205616191](G:\note\image\image-20210107205616191.png)

#### OpenFeign

feign其实不是做负载均衡的,负载均衡是ribbon的功能,feign只是集成了ribbon而已,但是负载均衡的功能还是feign内置的ribbon再做,而不是feign。
feign的作用的替代RestTemplate,性能比较低，但是可以使代码可读性很强。

ribbon的负载均衡策略，默认的常见有随机规则,轮询规则,权重规则
随机不用说，轮询也不用说，权重意思是,请求时间越久的server,其被分配给客户端使用的可能性就越低。
ribbon也可以自定义策略。具体方法包括：
方法1.实现IRule接口
方法2.集成AbstractLoadBalancerRule 、PredicateBasedRule。

```java
@Bean
  public IRule ribbonRule() {
    // 负载均衡规则，改为随机
    return new RandomRule();
  }
```

新建cloud-consumer-feign-order80

1 pom

```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>com.atguigu.springcloud</groupId>
            <artifactId>cloud-api-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

2 yml配置

```yml
server:
  port: 80
eureka:
  client:
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka, http://eureka7002.com:7002/eureka
```

3 主动类

```java
package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients  开起Feign
public class OrderFeignMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignMain80.class,args);
    }
}
```

4 接口并新增注解@FeignClient

```
package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE")
public interface PaymentFeignService {

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id);
}

```

5 controller

```java
package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentFeignService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class OrderFeignController {

    @Resource
    private PaymentFeignService paymentFeignService;

    @GetMapping(value = "/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
       return paymentFeignService.getPaymentById(id);
    }
}
```

#### Hystrix

![image-20210107233244466](G:\note\image\image-20210107233244466.png)

![image-20210107233258041](G:\note\image\image-20210107233258041.png)

###### 服务端

新建cloud-provider-hystrix-payment8001

1pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>cloud2020</artifactId>
        <groupId>com.atguigu.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>cloud-provider-hystrix-payment8001</artifactId>


    <dependencies>
        <!--新增hystrix-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>


        <dependency>
            <groupId>com.atguigu.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>


        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
 

```

2 yml

```yaml
server:
  port: 8001


eureka:
  client:
    register-with-eureka: true    #表识不向注册中心注册自己
    fetch-registry: true   #表示自己就是注册中心，职责是维护服务实例，并不需要去检索服务
    service-url:
      # defaultZone: http://eureka7002.com:7002/eureka/    #设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://eureka7001.com:7001/eureka/
#  server:
#    enable-self-preservation: false
spring:
  application:
    name: cloud-provider-hystrix-payment
#    eviction-interval-timer-in-ms: 2000
```

3 启动类

```java
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker //开启
public class PaymentHystrixMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentHystrixMain8001.class,args);
    }
}
```

4 service

```java
@Service
public class PaymentService {

    //成功
    public String paymentInfo_OK(Integer id){
        return "线程池："+Thread.currentThread().getName()+"   paymentInfo_OK,id：  "+id+"\t"+"哈哈哈"  ;
    }

    //失败
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
            @HystrixProperty(name = "excution.isolation.thread.timeoutMilliseconds",value = "3000")
    })
    public String paymentInfo_TimeOut(Integer id){
        int timeNumber = 3;
        try { TimeUnit.SECONDS.sleep(timeNumber); }catch (Exception e) {e.printStackTrace();}
        return "线程池："+Thread.currentThread().getName()+"   paymentInfo_TimeOut,id：  "+id+"\t"+"呜呜呜"+" 耗时(秒)"+timeNumber;
    }
    public String paymentInfo_TimeOutHandler(Integer id){
        return "降级默认执行方法";
    }
}
```

5 controller

```java
@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/hystrix/ok")
    public String paymentInfo_OK(@RequestParam("id") Integer id){
        String result = paymentService.paymentInfo_OK(id);
        log.info("*******result:"+result);
        return result;
    }
    @GetMapping("/payment/hystrix/timeout")
    public String paymentInfo_TimeOut(@RequestParam("id") Integer id){
        String result = paymentService.paymentInfo_TimeOut(id);
        log.info("*******result:"+result);
        return result;
    }
}
```

###### 客户端

新建 cloud-consumer-feign-hystrix-order80

1 pom

```
 <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <artifactId>demo1</artifactId>
            <groupId>org.example</groupId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.example</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.example</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>com.netflix.hystrix</groupId>
            <artifactId>hystrix-javanica</artifactId>
        </dependency>
    </dependencies>
```

2 yml

```yaml
server:
  port: 80
feign:
  hystrix:
    enabled: true #如果处理自身的容错就开启。开启方式与生产端不一样。
eureka:
  client:
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka, http://eureka7002.com:7002/eureka
```

3  启动类

```
@SpringBootApplication
@EnableFeignClients //开启Feign
@EnableHystrix
public class OrderFeignMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignMain80.class,args);
    }
}
```

4 service

```
@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE")
public interface PaymentFeignService {

    @GetMapping(value = "/payment/get")
    public CommonResult getPaymentById(@RequestParam("id") Long id);
}
```

5 controller

添加全局兜底方法

![image-20210107234807329](G:\note\image\image-20210107234807329.png)

```
package om.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import om.atguigu.springcloud.service.PaymentFeignService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")  //全局的
public class OrderFeignController {

    @Resource
    private PaymentFeignService paymentFeignService;

    @GetMapping("/consumer/payment/hystrix/timeout")
    @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1500")  //3秒钟以内就是正常的业务逻辑
    })
    public String paymentInfo_TimeOut(@RequestParam("id") Integer id){
        CommonResult paymentById = PaymentFeignService.getPaymentById(id);
        return result;
    }

    //兜底方法
    public String paymentTimeOutFallbackMethod(@RequestParam("id") Integer id){
        return "我是消费者80，对付支付系统繁忙请10秒钟后再试或者自己运行出错请检查自己,(┬＿┬)";
    }
}
```

熔断

![image-20210107234906917](G:\note\image\image-20210107234906917.png)

![image-20210107234932159](G:\note\image\image-20210107234932159.png)

```java
//服务熔断
@HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
    @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),  //是否开启断路器
    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),   //请求次数
    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),  //时间范围
    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"), //失败率达到多少后跳闸
})
public String paymentCircuitBreaker(@PathVariable("id") Integer id){
    if (id < 0){
        throw new RuntimeException("*****id 不能负数");
    }
    String serialNumber = IdUtil.simpleUUID();

    return Thread.currentThread().getName()+"\t"+"调用成功,流水号："+serialNumber;
}
public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id){
    return "id 不能负数，请稍候再试,(┬＿┬)/~~     id: " +id;
```

#### gateway

![image-20210108110320604](G:\note\image\image-20210108110320604.png)

![image-20210108112015730](G:\note\image\image-20210108112015730.png)

![image-20210108110434097](G:\note\image\image-20210108110434097.png)

![image-20210108110508581](G:\note\image\image-20210108110508581.png)

1 新建cloud-gateway-gateway9527

2 pom

```xml
<dependencies>
        <!--新增gateway-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <dependency>
            <artifactId>demo1</artifactId>
            <groupId>org.example</groupId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

    </dependencies>
```

2 yml

```yml
server:
  port: 9527
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true  #开启从注册中心动态创建路由的功能，利用微服务名进行路由
      routes:
        - id: payment_routh #路由的ID，没有固定规则但要求唯一，建议配合服务名
          #uri: http://localhost:8001   #匹配后提供服务的路由地址
          uri: lb://cloud-payment-service
          predicates:
            - Path=/payment/get/**   #断言,路径相匹配的进行路由

        - id: payment_routh2
          #uri: http://localhost:8001   #匹配后提供服务的路由地址
          uri: lb://cloud-payment-service
          predicates:
            - Path=/payment/lb/**   #断言,路径相匹配的进行路由


eureka:
  instance:
    hostname: cloud-gateway-service
  client:
    service-url:
      register-with-eureka: true
      fetch-registry: true
      defaultZone: http://eureka7001.com:7001/eureka
```

3 启动类

```java
@SpringBootApplication
@EnableEurekaClient
public class GateWayMain9527 {
    public static void main(String[] args) {
        SpringApplication.run( GateWayMain9527.class,args);
    }
}
```

4 测试请求      localhost:9527/payment/get?id=2            自带负载均衡

###### predicate

![image-20210108110910283](G:\note\image\image-20210108110910283.png)

```yaml
 predicates:
            - Path=/payment/lb/**   #断言,路径相匹配的进行路由
            - After=2020-03-08T10:59:34.102+08:00[Asia/Shanghai]
            - Before=2020-03-08T10:59:34.102+08:00[Asia/Shanghai]
            - Between=2020-03-08T10:59:34.102+08:00[Asia/Shanghai] ,  2020-03-08T10:59:34.102+08:00[Asia/Shanghai]
            - Cookie=chocolate,ch.p  #一个cookie.name  一个正则表达式
            - Header=X-Request-Id, \d+  # key,正则
            - Host=**.xnxum.com #域名匹配
            - Method=GET #请求方法
            - Query=username,\+d #参数过滤
```

###### Filter

自定义过全局滤器

通过实现 GlobalFilter,Ordered接口，

1 统一网关

2 日志纪录

```java
@Component
@Slf4j
public class MyLogGateWayFilter implements GlobalFilter,Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("*********come in MyLogGateWayFilter: "+new Date());
        String uname = exchange.getRequest().getQueryParams().getFirst("username");
        if(StringUtils.isEmpty(username)){
            log.info("*****用户名为Null 非法用户,(┬＿┬)");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);//给人家一个回应
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

```

#### 配置中心

多态服务修改配置不方便

1 新建配置中心   cloud-config-center-3344

2 pom

```
<dependencies>


        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>com.atguigu.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

3 yml

![image-20210108114720574](G:\note\image\image-20210108114720574.png)

```yaml
server:
  port: 3344
spring:
  application:
    name: cloud-config-center
  cloud:
    config:
      server:
        git:
          uri:  https://github.com/17637067893/springcloud-config.git
         # search-paths:
         #   - springcloud-config
      label: master
eureka:
  client:
    service-url:
      defaultZone:  http://localhost:7001/eureka
```

4 启动类

@EnableConfigServer

```java
package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer        
public class ConfigCenterMain3344 {
    public static void main(String[] args) {
            SpringApplication.run(ConfigCenterMain3344 .class,args);
        }
}

```

新建 cloud-config-client-3355

1 pom

```pom
 <dependencies>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <artifactId>demo1</artifactId>
            <groupId>org.example</groupId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

2 bootstrap.yml

```
server:
  port: 3355
spring:
  application:
    name: config-client
  cloud:
    config:
      label: master
      name: config
      profile: dev
      uri: http://localhost:3344
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
#暴露监控端口
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

3 启动类

```java
package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConfigClientMain3355 {
    public static void main(String[] args) {
            SpringApplication.run( ConfigClientMain3355.class,args);
        }
}
 
```

4 controller

```java
package com.atguigu.springcloud.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/configInfo")
    public String getConfigInfo(){
        return configInfo;
    }
}
```

5 测试

修改git配置文件 

配置中心 3344

普通微服务 3355 发送  curl -X POST "http://localhost:3355/actuator/refresh"生效

#### nacos

1nacos 注册和配置中心 === Eureka + Config + Bus

###### 注册中心

1 下载nacos

![image-20210108130252783](G:\note\image\image-20210108130252783.png)

打开localhost:8848/nacos

###### provider

新建 cloudalibaba-provider-payment9001

1 pom

父pom

```
<!--spring cloud alibaba 2.1.0.RELEASE-->
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-alibaba-dependencies</artifactId>
  <version>2.1.0.RELEASE</version>
  <type>pom</type>
  <scope>import</scope>
</dependency>
```

笨模块pom

```xml
<dependencies>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency><dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.62</version>
</dependency>
 </dependencies>
 
```

3 yml

```yaml
server:
  port: 9001

spring:
  application:
    name: nacos-payment-provider
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #配置Nacos地址

management:
  endpoints:
    web:
      exposure:
        include: '*'
```

4 启动类

```java
package com.atguigu.springcloud.alibaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PaymentMain9001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain9001.class,args);
    }
}
```

5 业务类

```java
package com.atguigu.springcloud.alibaba.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PaymentController
{
    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/payment/nacos/{id}")
    public String getPayment(@PathVariable("id") Integer id)
    {
        return "nacos registry, serverPort: "+ serverPort+"\t id"+id;
    }
}
```

6 更改端口号 新建9002 

###### consumer

1 新建 cloudalibaba-consumer-nacos-order83

2 pom

```
<dependencies>
    <!--SpringCloud ailibaba nacos -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
        <dependency>
        <groupId>com.atguigu.springcloud</groupId>
        <artifactId>cloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

```

3 yml

```yaml
server:
  port: 83


spring:
  application:
    name: nacos-order-consumer
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848


service-url:
  nacos-user-service: http://nacos-payment-provider
```

4 启动类

```java
package com.atguigu.springcloud.alibaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class OrderNacosMain83
{
    public static void main(String[] args)
    {
        SpringApplication.run(OrderNacosMain83.class,args);
    }
}
```

5 配置类

```java
package com.atguigu.springcloud.alibaba.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class ApplicationContextConfig
{
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }
}
 
```

6 controller

```java
package com.atguigu.springcloud.alibaba.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


@RestController
@Slf4j
public class OrderNacosController
{
    @Resource
    private RestTemplate restTemplate;

    @Value("${service-url.nacos-user-service}")
    private String serverURL;

    @GetMapping(value = "/consumer/payment/nacos/{id}")
    public String paymentInfo(@PathVariable("id") Long id)
    {
        return restTemplate.getForObject(serverURL+"/payment/nacos/"+id,String.class);
    }

}
```

![image-20210108131028156](G:\note\image\image-20210108131028156.png)

###### 配置中心

1 yml

application.yml

```
server:
  port: 3377

spring:
  application:
    name: nacos-config-client
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #服务注册中心地址
      config:
        server-addr: localhost:8848 #配置中心地址
        file-extension: yaml #指定yaml格式的配置
        group: DEV_GROUP #分组
        namespace: 4273c930-470d-477b-8f1d-c8970eed4a52
```

boostrap.yml

```
spring:
  profiles:
    active: dev
```

 2 新建对应配置

![image-20210108131347307](G:\note\image\image-20210108131347307.png)

3 获取对应配置

@RefreshScope

```java
@RestController
@RefreshScope
public class ConfigClientController
{
    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/info")
    public String getConfigInfo() {
        return configInfo;
    }
}
```

###### nacos和持久化配置

1 nacos默认使用自带的嵌入式数据库derby

2 执行  D:\soft\nacos-server-1.1.4\nacos\conf\nacos-mysql.sql文件

4 nacos\conf\application.properties更改数据库

 ![image-20210108131750999](C:\Users\gg\AppData\Roaming\Typora\typora-user-images\image-20210108131750999.png)

![image-20210108131921514](G:\note\image\image-20210108131921514.png)

#### sentinel

熔断降级限流监控库

![image-20210108150045269](G:\note\image\image-20210108150045269.png)

 下载 

sentinel-dashboard-1.7.0.jar文件  原型 java -jar启动 访问8080端口

1 新建cloudalibaba-sentinel-service8401

2 pom

```xml
<dependencies>
        <dependency>
            <groupId>com.atguigu.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
      
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>4.6.3</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

    </dependencies>

```

3 yml

```
server:
  port: 8401

spring:
  application:
    name: cloudalibaba-sentinel-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
    sentinel:
      transport:
        dashboard: localhost:8080
        port: 8719  #默认8719，假如被占用了会自动从8719开始依次+1扫描。直至找到未被占用的端口

management:
  endpoints:
    web:
      exposure:
        include: '*'
```

4 启动类

```java
package com.atguigu.springcloud.alibaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class MainApp8401
{
    public static void main(String[] args) {
        SpringApplication.run(MainApp8401.class, args);
    }
}
```

5 controller

fallback运行时异常

blockHandler 违反设置规则

```java
package com.atguigu.springcloud.alibaba.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class FlowLimitController
{
    @GetMapping("/testA")使用默认的兜底方法
    public String testA() {
        return "------testA";
    }
    
    @GetMapping("/testB")
    @SentinelResource(value = "/testB", fallback = "fallbackHandler")
    public String testB() {
        int i = 10 / 0;
        return "------testB";
    }
    public String fallbackHandler(String str) {
        return "异常";
    }
    
}    
```

设置全局方法

```java
package com.atguigu.springcloud.alibaba.myhandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.*;

public class CustomerBlockHandler {

    public static CommonResult handleException(BlockException exception) {
        return new CommonResult(2020, "自定义限流处理信息....CustomerBlockHandler");

    }
}
```

使用该方法

```java
@GetMapping("/rateLimit/customerBlockHandler")
@SentinelResource(value = "customerBlockHandler",
        blockHandlerClass = CustomerBlockHandler.class,
        blockHandler = "handlerException2")
public CommonResult customerBlockHandler()
{
    return new CommonResult(200,"按客戶自定义",new Payment(2020L,"serial003"));
```

sentinel整合ribbon

1添加依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>

```

2 启动类添加 @EnableFeignCilents

```java
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class OrderNacosMain84
{
    public static void main(String[] args) {
        SpringApplication.run(OrderNacosMain84.class, args);
    }
}
```

4 添加配置类

```java
@Configuration
public class ApplicationContextConfig
{
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }
}
```

5 使用

````
@RestController
@Slf4j
public class CircleBreakerController
{
    public static final String SERVICE_URL = "http://nacos-payment-provider";

    @Resource
    private RestTemplate restTemplate;

    @RequestMapping("/consumer/fallback/{id}")
    @SentinelResource(value = "fallback")

    public CommonResult<Payment> fallback(@PathVariable Long id)
    {
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL + "/paymentSQL/"+id,CommonResult.class,id);

        if (id == 4) {
            throw new IllegalArgumentException ("IllegalArgumentException,非法参数异常....");
        }else if (result.getData() == null) {
            throw new NullPointerException ("NullPointerException,该ID没有对应记录,空指针异常");
        }

        return result;
    }

````

整合Feign

1 添加依赖

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

2 yml

```
server:
  port: 84


spring:
  application:
    name: nacos-order-consumer
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
    sentinel:
      transport:
        dashboard: localhost:8080
        port: 8719

service-url:
  nacos-user-service: http://nacos-payment-provider

#对Feign的支持
feign:
  sentinel:
    enabled: true
```

3 注解接口

```
@FeignClient(value = "nacos-payment-provider",fallback = PaymentFallbackService.class)
public interface PaymentService
{
    @GetMapping(value = "/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id);
}
```

4 实现类

```
Component
public class PaymentFallbackService implements PaymentService
{
    @Override
    public CommonResult<Payment> paymentSQL(Long id)
    {
        return new CommonResult<>(44444,"服务降级返回,---PaymentFallbackService",new Payment(id,"errorSerial"));
    }
}
```

5 controller调用服务

```
@Resource
private PaymentService paymentService;

@GetMapping(value = "/consumer/paymentSQL/{id}")
public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id) {
    return paymentService.paymentSQL(id);
}
```

6 启动类添加

```
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class OrderNacosMain84
{
    public static void main(String[] args) {
        SpringApplication.run(OrderNacosMain84.class, args);
    }
}
```

###### 规则持久化

一旦重启服务配置的规则就会消失

1添加数据元依赖

```
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```

2  yml

```
ring:
  application:
    name: cloudalibaba-sentinel-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #Nacos服务注册中心地址
    sentinel:
      transport:
        dashboard: localhost:8080 #配置Sentinel dashboard地址
        port: 8719
      datasource:
        ds1:
          nacos:
            server-addr: localhost:8848
            dataId: cloudalibaba-sentinel-service
            groupId: DEFAULT_GROUP
            data-type: json
            rule-type: flow

management:
  endpoints:
    web:
      exposure:
        include: '*'

feign:
  sentinel:
    enabled: true # 激活Sentinel对Feign的支持
```

3 nacos配置规则

```
[
    {
         "resource": "/retaLimit/byUrl",
         "limitApp": "default",
         "grade":   1,
         "count":   1,
         "strategy": 0,
         "controlBehavior": 0,
         "clusterMode": false    
    }
]
```

![image-20210108151833501](G:\note\image\image-20210108151833501.png)

4 重启访问接口 就可以看到规则

#### 分布式事务seata

![image-20210108152316067](G:\note\image\image-20210108152316067.png)

![image-20210108152425942](G:\note\image\image-20210108152425942.png)

1 下载seata-server-0.9.0文件 解压配置

file.conf  

 server模块 配置分组  ![image-20210108152550996](G:\note\image\image-20210108152550996.png)

store配置数据库

![image-20210108152625159](G:\note\image\image-20210108152625159.png)

2 利用db_store.sql文件 创建记录表

3利用Feign访问服务

1 调用其他服务的接口

```java
@FeignClient(value = "seata-account-service")
public interface AccountService{
    @PostMapping(value = "/account/decrease")
    CommonResult decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
```

```java
@FeignClient(value = "seata-storage-service")
public interface StorageService {
    @PostMapping(value = "/storage/decrease")
    CommonResult decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);
}
```

4 业务实现

```java
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;
    @Resource
    private StorageService storageService;
    @Resource
    private AccountService accountService;

    // 创建订单->调用库存服务扣减库存->调用账户服务扣减账户余额->修改订单状态
    @Override
    //name 随便写 唯一Id
    @GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class)
    public void create(Order order) {
        System.out.println(order);
        log.info("---->开始新建订单");
        //新建订单
        orderDao.create(order);

        // 扣减库存
        log.info("---->调用订单微服务扣减库存start");
        storageService.decrease(order.getProductId(),order.getCount());
        log.info("---->调用订单微服务扣减库存end");

        //扣减账户
        log.info("---->调用账户,扣减Money开始");
        accountService.decrease(order.getUserId(),order.getMoney());
        log.info("---->调用账户,扣减Money结束");

        //修改订单状态,从0到1表示已经完成
        log.info("---->修改订单状态start");
        orderDao.update(order.getUserId(),0);
        log.info("---->修改订单状态end");

        log.info("----->下订单结束了");
    }
}
```

