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

#### resultMap

##### 普通map

第一  自定义map

1 Dao接口

```
// 获取单个自定义map
public Map<String,Object> getCustomMap();
```

 2 sql配置

```java
<resultMap type="map" id="MyMap">

    <!--		id自定义主键key-->
    <!--		column	数据库中的列名，或者是列的别名。
    <!		property 指定对应的对象类中的属性-->
    <id column="id" property="id"></id>
    <result column="name" property="name"></result>
    <result column="password" property="password"></result>
    <result column="address" property="address"></result>
    <result column="phone" property="phonessss"></result>
    
    </resultMap>

    <select id="getCustomMap" resultMap="MyMap">
    select id,name,password,address,phone from user where id = 4
    </select>
```

3 测试

```java
@Test
    public void test3() throws IOException {
      String source = "conf/mybatis-config.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(source);
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();

        Demo1 mapper = sqlSession.getMapper(Demo1.class);

        Map<String,Object> customMap= mapper.getCustomMap();
        System.out.println(customMap);
        sqlSession.close();

    }
```

##### 第二 实体类对象属性是有其他对象

1 Dao

```java
public User getUserAndDept(int id);
```

2 sql配置

1. 

```xml
<resultMap type="demo1.bean.User" id="userAndDept">
		<id column="id" property="id"></id>
		<result column="name" property="name"></result>
		<result column="did" property="did"></result>
		                               给对象内属性赋值
		<result column="udid" property="dept.id"></result>
		<result column="deptname" property="dept.deptName"></result>
</resultMap>
	
	<select id="getUserAndDept" resultMap="userAndDept">
		select u.id id,d.id did,u.name name,u.d_id udid, d.dept_name deptname from user u,dept d where u.d_id = d.id and u.id = #{id}
	</select>
```

2. ```xml
   <resultMap type="demo1.bean.User" id="userAndDept">
   		<id column="id" property="id"></id>
   		<result column="name" property="name"></result>
   		<result column="did" property="did"></result>
   		<association property="dept"javaType="demo1.bean.Dept">
   			<id column="udid" property="id"></id>
   			<result column="deptname" property="deptName"></result>
   		</association>
   	</resultMap>
   ```

   

3 测试

```java
 @Test
    public void test4() throws IOException {
        String source = "conf/mybatis-config.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(source);
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        Demo1 mapper = sqlSession.getMapper(Demo1.class);

        User user = mapper.getUserAndDept(4);
        System.out.println(user);
        sqlSession.close();
    }
```

##### 分布式map查询

![image-20201220154936676](G:\note\image\image-20201220154936676.png)

<img src="G:\note\image\image-20201219142206645.png" alt="image-20201219142206645" style="zoom: 50%;" />

1 根据id查出User

2 根据User对象的关联d_id 去查 Dept的对象



实现

1 根据id差dept

```xml
 1 接口
 Dept getDept(int id);

2 sql配置
     <resultMap type="demo1.bean.Dept" id="deptMap">
            <id property="id" column="id"></id>
            <result column="dept_name" property="deptName"></result>
        </resultMap>
        <select id="getDept" resultMap="deptMap">
            select id,dept_name from dept where id=#{id}
        </select>      
```

2 根据id查 User

```xml
1 接口 
public User getUserAndDept(int id);

2 sql配置
      //配置resultMap 实现分步
    <resultMap type="demo1.bean.User" id="userAndDept">
        <id column="id" property="id"></id>
        <result column="name" property="name"></result>
        <result column="d_id" property="did"></result>
       //property 为User的属性        
       // select 是定位查询dapt的sql的位置  命名空间.id  
      //  column 根据User那个字段去查Dept 
        <association property="dept" select="demo1.dao.Dept.getDept" column="d_id">
        </association>
    </resultMap>

	<select id="getUserAndDept" resultMap="userAndDept">
		select id,name,d_id from user where id = #{id}
	</select>
```

association- - 分段 查询& & 延迟加载

开启延迟加载和属性按需加载

![image-20201219144627096](G:\note\image\image-20201219144627096.png)

#### 实体类对象中含有List集合

#### 一 多表联查

查询部门时 还查询关联的员工

1 Dao接口

```
List<Dept> getDepAndUserList(int id);
```

2 sql 配置

```xml
<!--    查询部门时 连同员工一起查出来-->
    <resultMap type="demo1.bean.Dept" id="deptAndUserList">
        <id column="did" property="id"></id>
        <result column="dept_name" property="deptName"></result>
<!--        collection 关联集合类型的数据-->
<!--        ofType关联 集合的数据类型-->
        <collection property="emps" ofType="demo1.bean.User">
            <result column="id" property="id"></result>
            <result column="name" property="name"></result>
            <result column="d_id" property="did"></result>
        </collection>
    </resultMap>

    <select id="getDepAndUserList" resultMap="deptAndUserList">
        SELECT u.id,name,d_id,d.id did,dept_name  from dept d left join user u on d.id=u.d_id where d.id = #{id};
    </select>
```

3 测试

```java
@Test
    public void test4() throws IOException {
        String source = "conf/mybatis-config.xml";
        InputStream resourceAsStream = Resources.getResourceAsStream(source);
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        Dept mapper = sqlSession.getMapper(Dept.class);
        List<Dept> dept = mapper.getDepAndUserList(2);
        System.out.println(dept);
        sqlSession.close();
    }
```



##### 分布式查询有List集合

![image-20201219155200283](G:\note\image\image-20201219155200283.png)

1 查出集合

```
1  List<User> getUserList(int id);

2 配Sql
<select id="getUserList" resultType="demo1.bean.User">
		select * from user where d_id = #{id};
	</select>
```

2 查对象

```xml
1 List<Dept> getDepAndUserList(int id);

2 
<!--    查询部门时 连同员工一起查出来-->
    <resultMap type="demo1.bean.Dept" id="deptAndUserList">
        <id column="id" property="id"></id>
        <result column="dept_name" property="deptName"></result>
<!--        collection 关联集合类型的数据-->
<!--        ofType关联 集合的数据类型-->
        <collection property="emps" select="demo1.dao.Demo1.getUserList" column="id">
        </collection>
    </resultMap>

    <select id="getDepAndUserList" resultMap="deptAndUserList">
        SELECT * from dept where id = #{id};
    </select>
```

扩展

```xml
<resultMap type="demo1.bean.Dept" id="deptAndUserList">
        <id column="id" property="id"></id>
        <result column="dept_name" property="deptName"></result>                              
 如果下个方法需要接收多个参数可以传一个map集合key下个方法的形参 value 需要传入的字段名
        <collection property="emps" select="demo1.dao.Demo1.getUserList" column="{id=id}">
        </collection>
    </resultMap>
```

#### discriminator 鉴别器

```xml
	<resultMap type="demo1.bean.User" id="userAndDept">
		<id column="id" property="id"></id>
		<result column="name" property="name"></result>
		<result column="d_id" property="did"></result>
<!--	  javaType d_id的java数据类型	根据字段判 d_id 断结果-->
		<discriminator javaType="string" column="d_id">
<!--			如果为1 查出部门-->
			<case value="1" resultType="demo1.bean.User">
					<association property="dept" select="demo1.dao.Dept.getDept" column="d_id">
				    </association>
			</case>
<!--			如果为2 不查部门-->
			<case value="2" resultType="demo1.bean.User">
			</case>
		</discriminator>
	</resultMap>
	<select id="getUserAndDept" resultMap="userAndDept">
		select id,name,d_id from user where id = #{id}
	</select>
```

#### 动态sql

1 if 判断

```xml
<select id="getUserList" resultType="demo1.bean.User">
    select * from user where
    如果id不存在 后的and会直接拼到where后面
    <if test="id!=null and id!=0">id = #{id}</if>
    <if test="name!=null and name!=''">and name = #{name}</if>
</select>


<if test='id!=null and id lt 4'>select id from user where id &lt; #{id}</if>
 <if test='id!=null and id >=  4'>select * from user where id > #{id}</if>
```



2 where 解决1 的问题

```xml
<select id="getUserList" resultType="demo1.bean.User">
		select * from user
    <where>
        <if test="id!=null and id!=0">id = #{id}</if>
        <if test="name!=null and name!=''">and name = #{name}</if>
    </where>
</select>
```

3 trim

```xml
select * from user
-- prefix 给这段拼接好的sql添加前缀
-- prefixOverrides 去掉前缀
-- suffix 添加后缀
-- suffixOverrides 去掉后缀
<trim prefix="where" prefixOverrides="and" suffix="" suffixOverrides="">
    <if test="id!=null and id!=0">id = #{id}</if>
    <if test="name!=null and name!=''">and name = #{name}</if>
</trim>
```

4 choose	

![image-20201219233907567](G:\note\image\image-20201219233907567.png)

5 trim (where, set) 

![image-20201219233928528](G:\note\image\image-20201219233928528.png)

![image-20201219234042326](G:\note\image\image-20201219234042326.png)

trim

![image-20201219234138639](G:\note\image\image-20201219234138639.png)

6 foreach

![image-20201219234334818](G:\note\image\image-20201219234334818.png)

7 bind

bind 元素可以从 OGNL 表达式中创建一个变量并 将其绑定到上下文。比如

![image-20201219234423182](G:\note\image\image-20201219234423182.png)

8 _ databaseId”变量

![image-20201219234618016](G:\note\image\image-20201219234618016.png)

![image-20201219234626770](G:\note\image\image-20201219234626770.png)

9 sql 标签抽取重用的sql标签

```

```

#### 缓存机制

 MyBatis系统中默认定义了两级缓存。
• 一级 缓存和 二级缓存。
– 1、默认情况下，只有一级缓存（SqlSession级别的缓存，
也称为本地缓存）开启。就是一次事务内会缓存，如果中间有增删改，提交操作会关闭事务，也会清除缓存
– 2、二级缓存需要手动开启和配置，他是基于namespace级
别的缓存。
– 3、为了提高扩展性。MyBatis定义了缓存接口Cache。我们
可以通过实现Cache接口来自定义二级缓存

一级 缓存

一级缓存(local cache), 即本地缓存, 作用域默认为sqlSession。当 Session flush close 后, 该Session 中的所有 Cache 将被清空。
• 本地缓存不能被关闭, 但可以调用 clearCache()来清空本地缓存, 或者改变缓存的作用域.
• 在mybatis3.1之后, 可以配置本地缓存的作用域.在 mybatis.xml 中配置

一级缓存演示& &失效情况

 同一次会话期间只要查询过的数据都会保存在当
前SqlSession的一个Map中
• key:hashCode+查询的SqlId+编写的sql查询语句+参数
• 一级缓存失效的四种情况
– 1、不同的SqlSession对应不同的一级缓存
– 2、同一个SqlSession但是查询条件不同
– 3、同一个SqlSession两次查询期间执行了任何一次增删改操作
– 4、同一个SqlSession两次查询期间手动清空了缓存

#### 二级缓存

 二级缓存(second level cache)，全局作用域缓存

• 二级缓存默认不开启，需要手动配置

• MyBatis提供二级缓存的接口以及实现，缓存实现要求POJO实现Serializable接口

• **二级缓存在 SqlSession 关闭或提交 之后才会 生效**

 使用步骤

 1、全局配置文件中开启二级缓存
• <setting name= "cacheEnabled" value="true"/>

 2、需要使用二级缓存的映射文件处使用cache配置缓存
• <cache />

```
 eviction=“ FIFO” ：缓存回收策略：
    • LRU – 最近最少使用的：移除最长时间不被使用的对象。
    • FIFO – 先进先出：按对象进入缓存的顺序来移除它们。
    • SOFT – 软引用：移除基于垃圾回收器状态和软引用规则的对象。
    • WEAK – 弱引用：更积极地移除基于垃圾收集器状态和弱引用规则的对象。
    • 默认的是 LRU。
• flushInterval ：刷新间隔，单位毫秒
    • 默认情况是不设置，也就是没有刷新间隔，缓存仅仅调用语句时刷新
• ssize ：引用数目，正整数
    • 代表缓存最多可以存储多少个对象，太大容易导致内存溢出
• readOnly ：只读，true/false
    • true：只读缓存；会给所有调用者返回缓存对象的相同实例。因此这些对象
    不能被修改。这提供了很重要的性能优势。
    • false：读写缓存；会返回缓存对象的拷贝（通过序列化）。这会慢一些，
    但是安全，因此默认是 false。
```

– 3、注意：POJO需要实现Serializable接口setting缓存有关设置

1、全局setting的cacheEnable：– 配置二级缓存的开关。一级缓存一直是打开的。

• 2、select标签的useCache属性：– 配置这个select是否使用二级缓存。一级缓存一直是使用的

 3、sql标签的flushCache属性：增删改默认flushCache=true。sql执行以后，会同时清空一级和二级缓存。查询默认flushCache=false。
• 4、sqlSession.clearCache()： 只是用来清除一级缓存。
• 5、当在某一个作用域 (一级缓存Session/二级缓存Namespaces) 进行了 C/U/D 操作后，默认该作用域下 所有 有 select 中的缓存将被 clear。

![image-20201220004457244](G:\note\image\image-20201220004457244.png)

#### 第三方缓存

EhCache 是一个纯Java的进程内缓存框架，具有快速、精
干等特点，是Hibernate中默认的CacheProvider。
• MyBatis定义了Cache接口方便我们进行自定义扩展。

步骤：
– 1、导入ehcache包，以及整合包，日志包
ehcache-core-2.6.8.jar、mybatis-ehcache-1.0.3.jar
slf4j-api-1.6.1.jar、slf4j-log4j12-1.6.2.jar

![image-20201220010701513](G:\note\image\image-20201220010701513.png)– 2、编写ehcache.xml配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:noNamespaceSchemaLocation="ehcache.xsd">
 <!-- 磁盘保存路径 -->
 <diskStore path="D:\44\ehcache" />
 
 <defaultCache 
   maxElementsInMemory="1" 
   maxElementsOnDisk="10000000"
   eternal="false" 
   overflowToDisk="true" 
   timeToIdleSeconds="120"
   timeToLiveSeconds="120" 
   diskExpiryThreadIntervalSeconds="120"
   memoryStoreEvictionPolicy="LRU">
 </defaultCache>
</ehcache>
 
<!-- 
属性说明：
l diskStore：指定数据在磁盘中的存储位置。
l defaultCache：当借助CacheManager.add("demoCache")创建Cache时，EhCache便会采用<defalutCache/>指定的的管理策略
 
以下属性是必须的：
l maxElementsInMemory - 在内存中缓存的element的最大数目 
l maxElementsOnDisk - 在磁盘上缓存的element的最大数目，若是0表示无穷大
l eternal - 设定缓存的elements是否永远不过期。如果为true，则缓存的数据始终有效，如果为false那么还要根据timeToIdleSeconds，timeToLiveSeconds判断
l overflowToDisk - 设定当内存缓存溢出的时候是否将过期的element缓存到磁盘上
 
以下属性是可选的：
l timeToIdleSeconds - 当缓存在EhCache中的数据前后两次访问的时间超过timeToIdleSeconds的属性取值时，这些数据便会删除，默认值是0,也就是可闲置时间无穷大
l timeToLiveSeconds - 缓存element的有效生命期，默认是0.,也就是element存活时间无穷大
 diskSpoolBufferSizeMB 这个参数设置DiskStore(磁盘缓存)的缓存区大小.默认是30MB.每个Cache都应该有自己的一个缓冲区.
l diskPersistent - 在VM重启的时候是否启用磁盘保存EhCache中的数据，默认是false。
l diskExpiryThreadIntervalSeconds - 磁盘缓存的清理线程运行间隔，默认是120秒。每个120s，相应的线程会进行一次EhCache中数据的清理工作
l memoryStoreEvictionPolicy - 当内存缓存达到最大，有新的element加入的时候， 移除缓存中element的策略。默认是LRU（最近最少使用），可选的有LFU（最不常使用）和FIFO（先进先出）
 -->
```



– 3、配置cache标签
– <cache type= "org.mybatis.caches.ehcache.EhcacheCache"></cache>

![image-20201220010807038](G:\note\image\image-20201220010807038.png)