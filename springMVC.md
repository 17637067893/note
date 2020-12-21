#### 第一个程序

1 导入jar包

```
jar 包： •
commons-logging-1.1.3.jar –
spring-aop-4.0.0.RELEASE.jar –
spring-beans-4.0.0.RELEASE.jar –
spring-context-4.0.0.RELEASE.jar –
spring-core-4.0.0.RELEASE.jar –
spring-expression-4.0.0.RELEASE.jar –
spring-web-4.0.0.RELEASE.jar –
spring-webmvc-4.0.0.RELEASE.jar 
```

2 配置文件

<img src="G:\note\image\image-20201221090945593.png" alt="image-20201221090945593" style="zoom:50%;" />

web.xml

```xml
    <servlet>
        <servlet-name>hello</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--        <init-param>-->
        <!--            <param-name>contextConfigLocation</param-name>-->
        <!--            <param-value>classpath:application-servlet.xml</param-value>-->
        <!--        </init-param>-->
    </servlet>
    <servlet-mapping>
        <servlet-name>hello</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
```

hello-servlet.xml

```xml
<bean id="viewresolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/pages/"></property>
    <property name="suffix" value=".jsp"></property>
</bean>
    <context:component-scan base-package="dem1"></context:component-scan>
```

3 创建测试类

```java
@Controller
public class TestDemo {
    请求路径
    @RequestMapping(value="/hello")
    public String hello(){
        System.out.println("hello");
        return "success";
    }
}
```

param

```java
@Controller
public class TestDemo {
param控制请求参数
@RequestMapping(value = "/hello",method = RequestMethod.POST,params = {"username"})
        public String hello(){
        System.out.println("hello");
        return "success";
    }
}
```

@RequestMapping位置



```
1 加在类上 对类中所有方法都有效
2 加在方法上 只对当前方法有效
```

支持Antd

```
支持Antd请求
    @RequestMapping(value="/*/hello")  lcalhost:/任意字符/hello
    @RequestMapping("/ant??/hello")   localhost:/antaa/hello
    @RequestMapping("/**/hello")      localhost:/ant/btn/hello
```

占位符传参

```java
http://localhost:8080/test1/100/夏明

@RequestMapping(value = "test1/{id}/{name}")
public String test1(@PathVariable("id")Integer id,@PathVariable("name")String name){
    System.out.println(id+name);
    return "success";
}
```

REST请求

通过 _method的值 判断 put delete

1 get

```java
  <form action="test1/100" method="get">
    <input type="submit" value="测试">
  </form>
      @RequestMapping(value = "test1/{id}",method = RequestMethod.GET)
      public String test1(@PathVariable("id")Integer id){
      System.out.println("get"+id);
      return "success";
  }
  
```

2 post

```java
<form action="test1/100" method="post">
    <input type="submit" value="测试">
  </form>

@RequestMapping(value = "test1/{id}",method = RequestMethod.POST)
public String test2(@PathVariable("id")Integer id){
    System.out.println("post"+id);
    return "success";
}
```

3 put

```java
@RequestMapping(value = "test1/{id}", method=RequestMethod.PUT)
public String test3(@PathVariable("id")Integer id){
    System.out.println("PUT"+id);
    return "success";
}
    
<form action="test1/100" method="post">
    <input type="hidden" name="_method" value="PUT">
    <input type="submit" value="测试">
  </form>
```

4 delete

```java
@RequestMapping(value = "test1/{id}", method=RequestMethod.DELETE)
public String test3(@PathVariable("id")Integer id){
    System.out.println("PUT"+id);
    return "success";
}
    
<form action="test1/100" method="post">
    <input type="hidden" name="_method" value="DELETE">
    <input type="submit" value="测试">
  </form>
```

获取请求参数

1 @RequestParam

```java
@RequestMapping(value="test1",method = RequestMethod.POST)
public String test4(@RequestParam(value = "name",required = false,defaultValue = "小红")String name){
    System.out.println("name"+name);
    return "success";
}
```

2 使用对象

```java
equestMapping(value="test1",method = RequestMethod.POST)
    public String test4(User user){
    System.out.println(user);
    return "success";
}
```

域对象

```java
@RequestMapping(value="test1",method = RequestMethod.POST)
public String test4(){
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("username","小明");
    modelAndView.addObject("age","100");
    return "success";
}
@RequestMapping(value="test1",method = RequestMethod.POST)
public String test5(Map<String,String> map){
    map.put("username","小明");
    return "success";
}
@RequestMapping(value="test1",method = RequestMethod.POST)
public String test6(Model mdel){
    mdel.addAttribute("username","小明");
    return "success";
}
```

处理JSON

1. 加入 jar 包

2. ![image-20201221203007940](G:\note\image\image-20201221203007940.png)

   2 在方法上添加 @ResponseBody 注解

```java
@RequestMapping(value = "hello",method =RequestMethod.GET)
@ResponseBody
public String hello(){
    System.out.println("hello");
    return "success";
}
```





