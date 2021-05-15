![image-20210514211235915](G:\note\image\image-20210514211235915.png)

#### 案例

1 springBoot引入依赖

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.0.5</version>
</dependency>
//连接数据库的驱动
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

2 yml设置数据源

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=root
#打印SQl语句
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl

```

3 启动类

```java
@SpringBootApplication
@MapperScan("com.atguigu.mpdemo1010.mapper")
public class Mpdemo1010Application {
    public static void main(String[] args) {
        SpringApplication.run(Mpdemo1010Application.class, args);
    }

}
```

4 bean

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
//    @TableId(type = IdType.AUTO) 自动涨
//    @TableId(type = IdType.ID_WORKER)  Long 类型雪花
//    @TableId(type = IdType.ID_WORKER_STR) String
//    @TableId(type = IdType.UUID);  生成随机
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```

5 mapper

```java
@Component
public interface UserMapper extends BaseMapper<User> {
}
```

6 测试结果

```
userMapper.selectList(null).forEach(System.out::println);
```

#### 注解

######  @TableName 

数据库对应表名

```java
@TableName(value = "user")
```

###### @TableId 

指定数据库主键

```properties
@TableId(value = "id",type =IdType.AUTO,exist = true) 
value指定数据库主键 
exist 数据库是否存在该字段 封装vo的时候用
```

| 值                 | 描述                                 |
| :----------------- | ------------------------------------ |
| IdType.AUTO        | 数据库自增                           |
| IdType.NONE        | Mp set主键 雪花算法                  |
| IdType.INPUT       | 手动输入                             |
| IdType.ASSIGN_ID   | MP 分配ID，Long Integer String       |
| IdType.ASSIGN_UUID | MP 分配 UUID String 主键必须是String |

3 自动更新时间

1 属性添加注解

```java
@TableField(fill = FieldFill.INSERT)
private Date createTime;

@TableField(fill = FieldFill.UPDATE)
private Date updateTime;
```

2 实现MetaObjectHandler接口

```java
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }

```

```
    @Test
    void save() {
        User user = new User();
        user.setAge(20);
        user.setEmail("email");
        user.setName("小红");
        int insert = userMapper.insert(user);
        System.out.println(insert);
    }
```

###### @version 乐观锁 

**主要适用场景：**当要更新一条记录的时候，希望这条记录没有被别人更新，也就是说实现线程安全的数据更新

**乐观锁实现方式：**

- 取出记录时，获取当前version 
- 更新时，带上这个version 
- 执行更新时， set  version = newVersion where version = oldVersion 
- 如果version不对，就更新失败![image-20210108211427933](G:\note\image\image-20210108211427933.png)
- ![image-20210108211443987](G:\note\image\image-20210108211443987.png)

1 数据库添加version字段

```
ALTER TABLE `user` ADD COLUMN `version` INT
```

2 实体类添加字段

```
@Version2@TableField(fill = FieldFill.INSERT)private Integer version
```

3 利用自动填充设置默认值

```java
@Override2public void insertFill(MetaObject metaObject) {
    his.setFieldValByName("version", 1, metaObject);
}
```

4 配置类添加bean

```java
@EnableTransactionManagement
@Configuration
@MapperScan("com.atguigu.mybatis_plus.mapper")
public class MybatisPlusConfig {
    /**    * 乐观锁插件    */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {       return new OptimisticLockerInterceptor()  
    }
}

新版插件
     // 注册乐观锁插件与分页插件
 
    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        //乐观锁插件
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        
        return mybatisPlusInterceptor;
    }
```

5 测试乐观锁

```java
@Test
public void testOptimisticLocker() {
    User user = userMapper.selectById(1L); 
    //修改数据   
    user.setName("Helen Yao");   
    user.setEmail("helen@qq.com");    
    //执行更新    
    userMapper.updateById(user);
}
```

###### @EnumValue 

将数据库字段映射成实体类枚举类型成员变量

1 数据创建状态字段sex有几种值 枚举就写几种状态

假设sex 只有 0， 1

创建枚举

``` java
public enum  StatusEnum {
    
    MAN(0,"男"),
    WOMAN(1,"女");
    
    @EnumValue   //添加注释
    private Integer code;
    private String value;

    StatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}

```

实体类添加变量

```
private StatusEnum sex;   //sex  和数据库字段对应
```

配置文件添加扫描枚举包

```properties
mybatis-plus.type-enums-package=com.xnxun.demo.enums
```



查询数据

```
User user1 = userMapper.selectById(1392284633368948741L);
        System.out.println(user1);
        
User(id=1392284633368948741, name=小华3, age=20, email=email, sex=MAN, createTime=Wed May 12 09:50:39 CST 2021, updateTime=Wed May 12 10:23:01 CST 2021, version=3)        
```

###### @TableLogic 逻辑删除

1 数据库添加字段 deleted

2 实体类添加字段

```
@TableLogic
private int deleted;
```

3 配置类添加 0 1状态

```
逻辑删除
mybatis-plus.global-config.db-config.logic-delete-value=1
mybatis-plus.global-config.db-config.logic-not-delete-value=0
```

4 测试代码

```
int i = userMapper.deleteById(1);   //只改deleted状态
System.out.println(i);
```

#### 查询

```
@Resource
private UserMapper userMapper;
```

###### 增



###### 删

```
QueryWrapper<User> wrapper = new QueryWrapper<>();
int delete = userMapper.delete(wrapper);
```

###### 改



###### 查

1 查询全部

```
List<User> users = userMapper.selectList(null);
```

2 根据条件查询

ge >=

gt >

le <=

lt <

isNull

isNotNull

eq =

ne !=

between

notBetween

```java
QueryWrapper<User> wrapper = new QueryWrapper<>();
wrapper.eq("name","小红");  //eq 等于
```

3 多条件查询

userMapper.selectByMap(map);

userMapper.selectMap();  //查询的结果封装到map中

```java
//多个id查询
List<User> users = userMapper.selectBatchIds(Arrays.asList(1L, 2L, 3L));

//多条件查询
HashMap<String, Object> map = new HashMap<>();
map.put("name","Helen");
map.put("age",18);

List<User> users1 = userMapper.selectByMap(map);
```

4 allEq 多条件相等

```java
@Test
public void testSelectList() {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    Map<String, Object> map = new HashMap<>();
    map.put("id", 2);
    map.put("name", "Jack");
    map.put("age", 20);
    queryWrapper.allEq(map);
    List<User> users = userMapper.selectList(queryWrapper);
    users.forEach(System.out::println);
}

SELECT id,name,age,email,create_time,update_time,deleted,version FROM user WHERE deleted=0 AND name = ? AND id = ? AND age = ? 
```

4 模糊查询

![image-20210108225039235](G:\note\image\image-20210108225039235.png)

![image-20210108225128396](G:\note\image\image-20210108225128396.png)

![image-20210108225058687](G:\note\image\image-20210108225058687.png)

![image-20210108225207558](G:\note\image\image-20210108225207558.png)





###### 分页查询

2 查询

新版不用配置插件

```
 @Test
    public void testPages(){
        Page<Object> objectPage = new Page<>(1, 3);
        objectPage.getCurrent(); //当前叶
        objectPage.getRecords(); //当前页数据
        objectPage.getSize(); //当前页数量
        objectPage.getTotal(); //数据总条数
        objectPage.getPages(); //总页数
        objectPage.hasNext(); //是否又下一页
        objectPage.hasPrevious(); //是否有上一页
    }
```

4 测试

```
int i = userMapper.deleteById(1L);
```

![image-20210108220427836](G:\note\image\image-20210108220427836.png)

###### 自定义SQL(多表关联查询)

1 封装实体类VO 根据需要

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVo {
    private Integer id;
    private String productName;  //和数据库字段不一样
    private String userName;  //和数据库字段不一样
    private String userAge;   //和数据库字段不一样
}
```

2  编写sql

跟字段取别名和我们的vo的变量名一样

```
select p.id,p.name productName,u.name username,u.age userAge from product p,user u where p.user_id = u.id and u.id=#{id}
```

3 Dao创建方法

```java
@Select("select p.id,p.name productName,u.name username,u.age userAge from product p,user u where p.user_id = u.id and u.id=#{id}")

List<ProductVo> productList(Integer id);
```

4 测试

```java
List<ProductVo> productVos = userMapper.productList(4);
        System.out.println(productVos);
```



#### 性能分析插件

注入bean

```java
 @Bean
@Profile({"dev","test"})// 设置 dev test 环境开启
public PerformanceInterceptor performanceInterceptor() {
    PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
    performanceInterceptor.setMaxTime(200);//ms，超过此处设置的ms则sql不执行
    performanceInterceptor.setFormat(true);
    return performanceInterceptor;
}

```

指定运行环境

```
spring.profiles.active=dev
```

测试执行sql







![image-20210108225146947](G:\note\image\image-20210108225146947.png)



![image-20210108225223258](G:\note\image\image-20210108225223258.png)

![image-20210108225234635](G:\note\image\image-20210108225234635.png)

![image-20210108225250029](G:\note\image\image-20210108225250029.png)



#### 代码生成器

1 引入依赖

```xml
  <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.3.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity</artifactId>
            <version>1.7</version>
        </dependency>
```

2 启动类内

```java
@SpringBootApplication
@MapperScan("com.xnxun.demo.mapper")
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        //创建generator对象
        AutoGenerator autoGenerator =new AutoGenerator();
        //数据源
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setUrl("jdbc:mysql://192.168.111.156:3306/mybatis_plus?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
         dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("root");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        autoGenerator.setDataSource(dataSourceConfig);
           //全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir")+"/src/main/java");
        globalConfig.setOpen(false);
        globalConfig.setAuthor("southwind"); //作者信息
        globalConfig.setServiceName("%sService");  //去掉接口名称前的I
        autoGenerator.setGlobalConfig(globalConfig);
        //包信息
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.xnxun.demo");
        packageConfig.setModuleName("generator"); //路径下生成generator包
        packageConfig.setController("controller"); //控制器的文件夹
        packageConfig.setService("service");
        packageConfig.setServiceImpl("service.impl");
        packageConfig.setMapper("mapper");
        packageConfig.setEntity("entity");
        autoGenerator.setPackageInfo(packageConfig);
        //配置策略
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setEntityLombokModel(true);
        //strategyConfig.setInclude("user")  生成数据库内的部分表
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.execute();
    }

}
```



#### cloud oss

1 引入依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alicloud-oss</artifactId>
</dependency>
```

2 配置密码

![image-20210119124457352](G:\note\image\image-20210119124457352.png)

3 自动引入client

```
@Resource
	OSSClient ossClient;
```

4 上传代码

```
InputStream inputStream = new FileInputStream("路径") 文件流
ossClient.putObject(bucketName, objectName,inputStream)

// 关闭OSSClient。
ossClient.shutdown();  
```

![image-20210119125005112](G:\note\image\image-20210119125005112.png)

1 后台返回oss签名

```java
package com.atguigu.gulimall.thirdparty.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController

public class OssController {
    @Resource
    OSSClient ossClient;
    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;
    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;


    @RequestMapping("/oss/ploicy")
    public Map<String, String> policy(){
//        String bucket = "gulimall-xnxum"; // 请填写您的 bucketname 。
        String host = "https://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
//        String callbackUrl = "http://88.88.88.88:8888";
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = format+"/"; // 用户上传文件时指定的前缀。
        Map<String, String> respMap = null;
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));
        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return respMap;
    }
}

```

```json
请求地址  http://localhost:30000/oss/ploicy


{
    "accessid": "LTAI4G2dNNMouWxhX1Nj943s",
    "policy": "eyJleHBpcmF0aW9uIjoiMjAyMS0wMS0xOVQwNTozODoyOS45ODFaIiwiY29uZGl0aW9ucyI6W1siY29udGVudC1sZW5ndGgtcmFuZ2UiLDAsMTA0ODU3NjAwMF0sWyJzdGFydHMtd2l0aCIsIiRrZXkiLCIyMDIxLTAxLTE5LyJdXX0=",
    "signature": "DPYW6TUp4ZmCS0LDniLcjOAg5Ko=",
    "dir": "2021-01-19/",
    "host": "https://gulimall-xnxum.oss-cn-chengdu.aliyuncs.com",
    "expire": "1611034709"
}
```

如果设置阿里云上传之后跨域

![image-20210119140757307](G:\note\image\image-20210119140757307.png)

```

```

后端校验

1 对象名称添加注解

```java
@NotBlank(message = "品牌名不能为空")
private Integer showStatus;
@Pattern(regexp = "/^[a-zA-Z]$/",message = "必须是字母") 自定义校验规则
    private String firstLetter;
@URL(message = "logo地址不合法")
private String logo;
```

2 controller中开启校验

@Valid注解

```java
@RequestMapping("/save")
public R save(@Valid @RequestBody BrandEntity brand, BindingResult result){
    if(result.hasErrors()){
        //错误结果分装分map集合
        HashMap<String, String> map = new HashMap<>();
        //获取校验错误的结果
        result.getFieldErrors().forEach((item)->{
            //FielError获取错误提示
            String defaultMessage = item.getDefaultMessage();
            //获取错误的属性名字
            String field = item.getField();
            map.put(field,defaultMessage);
        });
        //返回错误结果
        return R.error(400,"数据不合法").put("data",map);
    }else{
        brandService.save(brand);
    }
    return R.ok();
}

```

统一异常处理

1 规定异常状态码和信息

```java
package com.atguigu.common.exception;

public enum BizCodeEnume {
    UNKNOW_EXCEPION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"参数格式校验失败");

    private int code;
    private String msg;

    BizCodeEnume(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

```

2 封装捕获异常类

```java
package com.atguigu.gulimall.product.exception;

import com.atguigu.common.exception.BizCodeEnume;
import com.atguigu.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

//处理异常的controller位置
@Slf4j //日志
@RestControllerAdvice(basePackages ="com.atguigu.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {
    //value位处理异常的类型
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e){
        log.error("数据校验出现问题{},异常类型:{}",e.getMessage(),e.getClass());
        HashMap<String, String> errorMap = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().forEach((item)->{
            errorMap.put(item.getField(),item.getDefaultMessage());
        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data",errorMap);
    }

    //处理其他未知崔错误
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        return R.error(BizCodeEnume.UNKNOW_EXCEPION.getCode(),BizCodeEnume.UNKNOW_EXCEPION.getMsg());
    }

}
```

分组校验

1 新建用于分组的空接口

```
//新增
package com.atguigu.common.valid;

public interface AddGroup {
}

//修改
package com.atguigu.common.valid;

public interface UpdateGroup {
}
```

2 属性类指定所属分组

```java
	/**
	 * 品牌id 分组校验
	 */
	@Null(message = "新增不能指定id",groups = {AddGroup.class})
	@NotNull(message = "修改缺少Id",groups = {UpdateGroup.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能为空",groups = {AddGroup.class,UpdateGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@URL(message = "logo地址不合法")
	private String logo;
```

3 controller根据所有分组校验

1 没有指定分组的校验不生效，只有指定分组的校验才会生效

```java
   @RequestMapping("/save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand){
        brandService.save(brand);
        return R.ok();
    }
```

![image-20210119174556841](G:\note\image\image-20210119174556841.png)

接口文档

```
https://easydoc.xyz/s/78237135/
```

条件查询

```java
 /**param
     * {
         page: 1,//当前页码
         limit: 10,//每页记录数
         sidx: 'id',//排序字段
         order: 'asc/desc',//排序方式
         key: '华为'//检索关键字
         }
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        if (catelogId == 0){
          IPage<AttrGroupEntity> page =this.page(new Query<AttrGroupEntity>().getPage(params),new QueryWrapper<AttrGroupEntity>());
          return new PageUtils(page);
        }else{
            String key = (String) params.get("key");
            //select * from pms_attr_group where catelog_id=? and (attr_group_id = key or attr_group_name like %key%)
             //如果id不为0 wrapper封装id条件
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId);
            //如果还有关键字 继续封装
            if(!StringUtils.isEmpty(key)){
                wrapper.and((obj)->{
                    obj.eq("attr_group_id",key).or().like("attr_group_name",key);
                });
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),wrapper);
            return new PageUtils(page);
        }
    }
```

属性 为空就不返回

```java
@JsonInclude(JsonInclude.Include.NON_EMPTY) //如果为空数组 就不返回这个属性
@TableField(exist = false)
private List<CategoryEntity> children;
```

