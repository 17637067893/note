![image-20201218133539044](G:\note\image\image-20201218133539044.png)

#### 第一个实例

1 创建数据库表对应的对象类

2 导入jar包

![image-20201218130636317](G:\note\image\image-20201218130636317.png)

3 创建配置文件

![image-20201218130945615](G:\note\image\image-20201218130945615.png)

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
 PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
 "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<environments default="development">
		<environment id="development">
			<transactionManager type="JDBC" />
			<dataSource type="POOLED">
				<property name="driver" value="com.mysql.jdbc.Driver" />
				<property name="url" value="jdbc:mysql://localhost:3306/mybatis" />
				<property name="username" value="root" />
				<property name="password" value="123456" />
			</dataSource>
		</environment>
	</environments>
	<!-- 将我们写好的sql映射文件（EmployeeMapper.xml）一定要注册到全局配置文件（mybatis-config.xml）中 -->
	<mappers>
		<mapper resource="EmployeeMapper.xml" />
	</mappers>
</configuration>
```

4 配置sql文件集中sql

![image-20201218133812605](G:\note\image\image-20201218133812605.png)

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
 PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
 "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="demo1">
<!-- 
namespace:名称空间;指定为接口的全类名
id：唯一标识
resultType：返回值类型
#{id}：从传递过来的参数中取出id值

public Employee getEmpById(Integer id);
 -->
	<select id="getUser" resultType="demo1.User">
		select * from user where id = #{id}
	</select>
</mapper>
```

5 创建test类

```java
@Test
    public void test1() throws IOException {
        //从配置文件获取SqlSessionFactory去执行sql
        String resource = "conf/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = build.openSession();
        try {
            //通知sql配置文件获取对应sql          命名空间.sql
            User user = sqlSession.selectOne("demo1.getUser", 4);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭
            sqlSession.close();
        }
    }
```

使用接口改进sql 规范插叙的输出类型和输入的参数类型

 1 创建接口

```java
package demo1.dao;
import demo1.User;
public interface Demo1 {
    //一个方法对应sql配置文件的一个sql
    public User getUser(Integer id);
}
```

2 更改sql配置文件里的命名空间为接口全类型

![image-20201218142056642](G:\note\image\image-20201218142056642.png)

3 测试结果

```java
@Test
public void test2() throws IOException {
    String resource = "conf/mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);
    SqlSession sqlSession = build.openSession();
    
    
    //获取接口的实现类对象
    Demo1 mapper = sqlSession.getMapper(Demo1.class);
    //获取返回值  生成User的代理对象
    User user = mapper.getUser(4);
    System.out.println(user);
    //关闭连接
    sqlSession.close();
}
```

IDEA引入dtd

![image-20201218143150954](G:\note\image\image-20201218143150954.png)

增加操作

1 接口添加方法

![image-20201218162925039](G:\note\image\image-20201218162925039.png)

2 sql文件编写sql

```xml
使用insert标签       使用对应的标签update delete
<insert id="addUser">
		insert into user (name,password,address) values(#{name},#{password},#{address})
	</insert>
```



3 测试

```java
@Test
    public void test2() throws IOException {
        String resource = "conf/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //获取到的SqlSessionFactory 不会自动提交数据
        SqlSessionFactory  build = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = build.openSession();


        //获取接口的实现类对象
        Demo1 mapper = sqlSession.getMapper(Demo1.class);
        //获取返回值  生成User的代理对象
        mapper.addUser(new User(null,"小猪","7889","上海",12345689.00));
        //关闭连接
        sqlSession.close();
    }
```

获取自增主键

![image-20201218163855158](G:\note\image\image-20201218163855158.png)

![image-20201218163909825](G:\note\image\image-20201218163909825.png)

传递一个参数时

Dao

![image-20201218165824474](G:\note\image\image-20201218165824474.png)

![image-20201218165940138](G:\note\image\image-20201218165940138.png)

传递多个参数时

![image-20201218170240802](G:\note\image\image-20201218170240802.png)

![image-20201218170253374](G:\note\image\image-20201218170253374.png)



![image-20201218170439818](G:\note\image\image-20201218170439818.png)

如果多个参数是我们业务逻辑的数据模型直接使用pojo传参

如果不是我们的业务模型也不常用使用map

```
 //一个方法对应sql配置文件的一个sql
    public User getUserByIdAndName2(Map<String,String> map);

<select id="getUserByIdAndName2" resultType="demo1.User">
    select * from user where id = #{id} and name = #{name}
</select>


Map<String,String> map = new HashMap<String,String >();
    map.put("id","1");
    map.put("name","小明");
User user =  mapper.getUserByIdAndName2(map);
```

获取User对象列表

```java
public List<User> getList();

                        为集合里的元素类型
<select id="getList" resultType="demo1.User">
  select * from user where id>10
</select>

String resource = "conf/mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);
SqlSession sqlSession = build.openSession();

Demo1 mapper = sqlSession.getMapper(Demo1.class);
List<User> list = mapper.getList();
System.out.println(list);
//关闭连接
sqlSession.close();
```

#### 获取单个map对象

```java
1 Dao
public Map<String,String> getMap();

2 sql
<select id="getMap" resultType="map">
		select * from user where id=4
	</select>
	
3 测试类
@Test
    public void test2() throws IOException {
        String resource = "conf/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = build.openSession();

        Demo1 mapper = sqlSession.getMapper(Demo1.class);
        Map<String, String> map = mapper.getMap();
        System.out.println(map);
        //关闭连接
        sqlSession.close();
    }
```

#### 获取mapList

```java
     获取mapList
    指定数据的ID值为key  也可以其他字段
    @MapKey("id")
    public Map<Integer,User> getMapList();
    
    <select id="getMapList" resultType="map">
		select * from user where id>4
	</select>
    
    测试类
     @Test
    public void test2() throws IOException {
        String resource = "conf/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = build.openSession();

        Demo1 mapper = sqlSession.getMapper(Demo1.class);
        Map<Integer, User> mapList = mapper.getMapList();
        System.out.println(mapList);
        //关闭连接
        sqlSession.close();
    }
```



