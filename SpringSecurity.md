#### 入门案例

1 创建springboot项目

加入springsecurity依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

2 添加登录页面

![image-20210511073821629](G:\note\image\image-20210511073821629.png)

```html
<form action="/login">
    用户名: <input type="text" name="username">
    密码: <input type="password" name="password">
    <input type="submit" value="登录">
</form>
```

3 启动项目访问localhost:8080/login

![image-20210511074137847](G:\note\image\image-20210511074137847.png)

默认用户名user

密码 每次启动项目都不同

![image-20210511074207278](G:\note\image\image-20210511074207278.png)

###### 1 自定义账户密码登录

在配置文件自定义密码

```properties
spring.security.user.name=admin
spring.security.user.password=admin
```



1 添加配置类

```java
@Configuration
public class securityConfig {
    @Bean
    public PasswordEncoder getpw(){
        return new BCryptPasswordEncoder();
    }
}
```

2 创建类实现UserDetailsService接口内部添加账户密码 

```java
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!"admin".equals(username)){  //账户 根据数据库匹配
            throw  new UsernameNotFoundException("用户名不存在");
        }
        //比较密码
        String password = passwordEncoder.encode("123");  //密码 根据数据库匹配
        User user = new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normal")); //权限
        return user;
    }
}
```

3 再次访问页面 是我们定义的账户密码才能进入系统

###### 2 自定义登录页面

填写配置类

```java
@Configuration                       //集成类
public class securityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder getpw(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                //自定义登录页面 此时所有的页面也可以访问下面认证后才可以
                .loginPage("/login.html")
                //自定义登登录信息提交的路径
                .loginProcessingUrl("/login")
                //登录成功,必须是POST方式  自定义一个post方法跳转页面
                .successForwardUrl("/tomain")
                //登录失败
                .failureForwardUrl("/toError");
        //授权
        http.authorizeRequests()
                //放心的路径
                 .antMatchers("/","/test/hello/","/user/login").permitAll()
                //放行登录页面
                .antMatchers("/login.html").permitAll()
                //放行登录失败页面
                .antMatchers("/error.html").permitAll()
                //所有请求都必须被认证(登录)
                .anyRequest().authenticated();
        //403页面
        http.exceptionHandling().accessDeniedPage("/403page.html");

        //关闭csrf防护
        http.csrf().disable();
    }
}
```

###### 3 自定义成功跳转逻辑

默认是根据路径跳转必须是post请求

```java
//自定登录成功跳转逻辑
public class MySucessHandler implements AuthenticationSuccessHandler {
    private String url;
    public MySucessHandler(String url){
        this.url = url;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        User user = (User)authentication.getPrincipal();
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getAuthorities());
        httpServletResponse.sendRedirect(url);
    }
}
```

![image-20210511100853743](G:\note\image\image-20210511100853743.png)

###### 4 自动以失败处理

```java
public class MyfailuerHandler implements AuthenticationFailureHandler {
  private    String url;
  public void MyfailuerHandler(String url){
      this.url = url;
  }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.sendRedirect(url);
    }
}
```

![image-20210511101813102](G:\note\image\image-20210511101813102.png)

#### 授权

###### 1 资源放行

![image-20210511103219495](G:\note\image\image-20210511103219495.png)

###### 2 基于权限

![image-20210511110252693](G:\note\image\image-20210511110252693.png)

###### 3 基于角色

分配角色

![image-20210511110339213](G:\note\image\image-20210511110339213.png)

控制权限

![image-20210511110313724](G:\note\image\image-20210511110313724.png)

4 基于ip地址

![image-20210511110713629](G:\note\image\image-20210511110713629.png)

###### 5 自定义403页面

//创建类继承AccessDeniedHandler

```java
@Component
public class My403Page implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        //相应状态
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN); //403
        httpServletResponse.setHeader("Content-Type","application/json;chartset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write("{\"status\":\"error\",\"msg\":\"权限不足,请联系\"}");
        writer.flush();
        writer.close();
    }
}
```

配置类中添加

```java
 @Autowired
    private My403Page my403Page;
    
    
 http.exceptionHandling().accessDeniedHandler(my403Page);
```

6 自定义access方法

![image-20210511114221770](G:\note\image\image-20210511114221770.png)

创建类

```java
@Service
public class MyServiceImplImpl implements MyServiceImpl {
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        //获取主体
        Object obj = authentication.getPrincipal();
        //判断主机是否属于UserDetails
        if(obj instanceof UserDetails){
            //获取权限
            UserDetails userDetails = (UserDetails)obj;
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            //判断请求的URI是否在权限里
            return authorities.contains(new SimpleGrantedAuthority(request.getRequestURI()));
        }
        return false;
    }
}
```

###### 6 开启注解权限

启动类添加

```java
@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true)  //默认为false
public class SpringsecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringsecurityApplication.class, args);
    }

}
```

在方法上添加指定权限

```java
    @Secured("ROLE_abc")  //角色 
    @RequestMapping("/tomain")
    public String login(){
        return "redirect:main.html";
    }
```

7 @PreAuthorize() @PostAuthorize()

@PreAuthorize()  //方法或类在执行前 判断权限
@PostAuthorize()  //在执行后判断权限

使用前先开启配置类的

```
@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true) //开启prePostEnabled
public class SpringsecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringsecurityApplication.class, args);
    }

}
```

方法上使用

![image-20210511115641839](G:\note\image\image-20210511115641839.png)

7 remember mo

1 引入依赖

```xml
  <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.18</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.1</version>
        </dependency>
```

2 配置数据源

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://192.168.111.156:3306/book?characterEncoding=utf8&characterSetResults=utf8&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false
spring.datasource.username=root
spring.datasource.password=root
```

3 配置类加入bean

```java
@Autowired
    private com.xnxun.springsecurity.service.UserDetailServiceImpl userDetailServiceImpl;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;
```



```java
@Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        jdbcTokenRepository.setDataSource(dataSource);
        //是否自动建表  第一次开启  第二次 注释掉
        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }
```

4 引入

```java
       http.rememberMe()
                //设置数据源
                .tokenRepository(persistentTokenRepository)
                // 超时时间
                .tokenValiditySeconds(60)
                //登录逻辑
                .userDetailsService(userDetailServiceImpl);
```

7 退出登录

页面

```
<a href="/login.html">退出</a>  //内部自动实现退出
```

配置类中

```java
  //退出登录
        http.logout()
                .logoutUrl("/logout")
                //退出登录后跳转的页面
                .logoutSuccessUrl("/login.html");
```

8 CSRF 跨站请求伪造

springsecurity会跟请求返回一个_scrf.token值得，客户端每次请求都会带上token识别身份保证安全

#### Oauth2协议

第三方认证技术方案解决认证协议的通用标准，因为要实现跨系统认证，各系统之间要遵循一定接口协议。

#### 项目实战

