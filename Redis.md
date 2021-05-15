jav存储的数据类型 

string 字符串

  list 有序集合可重复 

set 无需不可重复 

zset(sort set有序集合)  有序不可重复

hash map集合 

#### 安装

安装redis   yum install redis

启动 systemctl start redis

设置开机启动 systemctl enable redis  端口6379

**修改配置**

打开`/etc/redis.conf`文件

1 允许远程连接

```
bind 127.0.0.1 改为： #bind 127.0.0.1
```

2 启用密码  

找到`# requirepass foobared`一行，删除前面的`#`注释，然后将`foobared`改为你自己的密码。

3 登录客户端 

```
redis-cli -p 6379 -a redis //密码
```

4 操作

``` 
keys * 获取当前数据库所有键
set name "xiaoming“
get name
type a     type键名可以查看数据类型
del a      删除键
expire a 10  为键设置过期时间 单位为妙
ttl a  查看还有多少秒过期
dbsize 查看当前库的数量
Flushdb 清空当前库
Flushal 清空全部库
```

判断name是否有值

![image-20210116222056030](G:\note\image\image-20210116222056030.png)

有16个数据库   默认使用0个数据库

使用命令select 1 选择第二数据库



select 询问每个请求是否准备就绪 最多监视1024个

poll 监视每个请求 不限制数量、

epoll 为每个请求添加是否准备就绪的表示

单线程+多路IO复用技术

字符串

```
set name xiao  
get name 
append name ming // xiaoming 
strlen name //8 获取长度
setnx name wang //当 name值不时 才会设置值

set num 10
incr num  // 11
decr num // 9

incrby  num 2  // 12
decrby num 2 // 8

mget a b c //获取多个值
msetnx a k1 b k2 //设置多个值
setex <key> <过期时间> <value> //设置键的同时 设置过期时间
```

List

```
lpush <key> value1 value2 value3  //左侧增加
rpush key value1 value2 value3   // 右侧增加
lpop 删除左侧第一个
rpop 删除右侧第一个

lrange <key> start stop  lrang testList 0 -1 //查看全部
lindex key index  lindex testList 0 获取索引值
llen 获取长度
linsert key before|after value newValue //
linsert testList before a 6666 //在list中的a的前边添加 666
lrem <key> <n> <value> 从左侧删除n个(从左到→删除)
lrem testList 2 a 从左侧删除两个a
lrem testList -2 a 从右侧删除两个a
lrem testList 0 a 删除全部的a
```

Set

```
sadd testSet1 a b c d //添加值
smembers testSet1 获取全部值
sismemer testSet1 a  判断a是否是testSet1的值
scard testSet1 返回元素个数
srem key value1 value2 删除多个元素
spop key 随机删除一个值
srandmember key n 随机取出n个值
sinter key1 key2 返回两个集合的交集元素
sunion key1 key2 返回两个集合的并集元素
sdiff key1 key2 返回两个集合的差集元素
```

#### springboot整合redis

1 引入依赖

```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
<!--        springboot2.x redis需要的-->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
            <version>2.6.0</version>
        </dependency>
```

2 添加配置类

```java
package com.example.demo3.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setConnectionFactory(factory);
        //key序列化方式
        template.setKeySerializer(redisSerializer);
        //value序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //value hashmap序列化
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 配置序列化（解决乱码的问题）,过期时间600秒
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600)) //数据过期时间
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }
}

```

###### 使用缓存注解

**@Cacheable**

把方法的返回值存入redis中，如果有值 直接从缓存中读取，如果没有执行方法，一般用在查询方法上

| value      | 必填  ，缓存的命名空间 |
| ---------- | ---------------------- |
| cacheNames | 等于value              |
| key        | 缓存的key              |



**@CachePut**

每次执行方法都会将结果存入redis中，适用于新增方法参数同上



**@CacheEvict**

清空指定缓存，一般用在更新或删除方法上

| 参数             | 作用                              |
| ---------------- | --------------------------------- |
| value            | 必填,指定命名空间                 |
| cacheNames       | 等于 value                        |
| key              | 缓存的key值                       |
| allEntries       | 是否清空所有换粗默认为false       |
| beforeInvocation | 是否在执行方法前清空，默认为false |

测试  list添加    **‘list’**

```java
   @Cacheable(value = "test",key = "'list'")  
    public List<User> selectList() {
        List<User> users = this.baseMapper.selectList(null);
        return users;
    }
```

![image-20210514115621607](G:\note\image\image-20210514115621607.png)

