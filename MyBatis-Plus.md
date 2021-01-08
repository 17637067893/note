#### 搭建项目

1 springBoot引入依赖

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.0.5</version>
</dependency>
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

#### 自动填充

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
        this.setFieldValByName("createTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
```

#### 乐观锁

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
@Version2@TableField(fill = FieldFill.INSERT)3private Integer version
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

BaseMapper

```java
//多个id查询
List<User> users = userMapper.selectBatchIds(Arrays.asList(1L, 2L, 3L));

//多条件查询
HashMap<String, Object> map = new HashMap<>();
map.put("name","Helen");
map.put("age",18);
List<User> users1 = userMapper.selectByMap(map);
```

#### 分页查询

1 添加bean

```JAVA
//分页插件
@Bean
public PaginationInterceptor paginationInterceptor(){
return new PaginationInterceptor();
}
```

2 查询

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

#### 删除

物理删除  

```java
//普通删除
int i = userMapper.deleteById(1L);
//批量删除
int i1 = userMapper.deleteBatchIds(Arrays.asList(1, 2, 3));
```

逻辑删除

1 数据库添加deleted字段   

deleted 1 为删除

deleted 0 为没有删除

```
ALTER TABLE `user` ADD COLUMN `deleted` boolean
```

2 实体类添加deleted字段

```java
@TableLogic
@TableField(fill = FieldFill.INSERT)
private Integer deleted;
```

3 添加bean

```
 //逻辑删除
 @Bean
 public ISqlInjector sqlInjector() {
 return new LogicSqlInjector();
 }
```

4 测试

```
int i = userMapper.deleteById(1L);
```

![image-20210108220427836](G:\note\image\image-20210108220427836.png)

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

#### 复杂条件插叙

1

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
@Test
public void testDelete() {   
QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .isNull("name")
        .ge("age", 12)
        .isNotNull("email")
        .between("age", 20, 30);
    int result = userMapper.delete(queryWrapper);
    System.out.println("delete return count = " + result);

}
```

allEq 多条件相等

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

![image-20210108225039235](G:\note\image\image-20210108225039235.png)

![image-20210108225058687](G:\note\image\image-20210108225058687.png)

![image-20210108225128396](G:\note\image\image-20210108225128396.png)

![image-20210108225146947](G:\note\image\image-20210108225146947.png)

![image-20210108225207558](G:\note\image\image-20210108225207558.png)

![image-20210108225223258](G:\note\image\image-20210108225223258.png)

![image-20210108225234635](G:\note\image\image-20210108225234635.png)

![image-20210108225250029](G:\note\image\image-20210108225250029.png)