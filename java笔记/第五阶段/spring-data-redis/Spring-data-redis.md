# Spring-data-redis

### jedis：对redis操作的客户端

### Spring-data-redis： 对jedis的封装

## 一、spring-data-redis针对jedis提供了如下功能：

        ####  1.连接池自动管理，提供了一个高度封装的“RedisTemplate”类
    
        ####  2.针对jedis客户端中大量api进行了归类封装,将同一类型操作封装为operation接口

​         ValueOperations：简单K-V操作

​         SetOperations：set类型数据操作

​         ZSetOperations：zset类型数据操作

​         HashOperations：针对map类型的数据操作

​         ListOperations：针对list类型的数据操作

## 二、准备与基本操作

### 1.引入相关jar包

```xml
<!-- 缓存 -->
<dependency> 
		  <groupId>redis.clients</groupId> 
		  <artifactId>jedis</artifactId> 
		  <version>2.8.1</version> 
</dependency> 
<dependency> 
		  <groupId>org.springframework.data</groupId> 
		  <artifactId>spring-data-redis</artifactId> 
		  <version>1.7.2.RELEASE</version> 
</dependency>

```

### 2.创建properties文件夹，建立redis-config.properties

```properties
redis.host=127.0.0.1 
redis.port=6379 
redis.pass= 
redis.database=0 
redis.maxIdle=300 
redis.maxWait=3000 
redis.testOnBorrow=true 


maxIdle ：最大空闲数
maxWaitMillis:连接时的最大等待毫秒数
testOnBorrow：在提取一个jedis实例时，是否提前进行验证操作；如果为true，则得到的jedis实例均是可用的；


```

### 3.建立applicationContext-redis.xml

```xml
<?xml version="1.0" encoding="UTF-8"?> 
<beans xmlns="http://www.springframework.org/schema/beans" 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p" 
  xmlns:context="http://www.springframework.org/schema/context" 
  xmlns:mvc="http://www.springframework.org/schema/mvc" 
  xmlns:cache="http://www.springframework.org/schema/cache"
  xsi:schemaLocation="http://www.springframework.org/schema/beans   
            http://www.springframework.org/schema/beans/spring-beans.xsd   
            http://www.springframework.org/schema/context   
            http://www.springframework.org/schema/context/spring-context.xsd   
            http://www.springframework.org/schema/mvc   
            http://www.springframework.org/schema/mvc/spring-mvc.xsd 
            http://www.springframework.org/schema/cache  
            http://www.springframework.org/schema/cache/spring-cache.xsd">  
  
   <context:property-placeholder location="classpath*:properties/*.properties" />   
  
   <!-- redis 相关配置 ，配置数据源信息-->
   <bean id="poolConfig" class="redis.clients.jedis.JedisPoolConfig">  
     <property name="maxIdle" value="${redis.maxIdle}" />   
     <property name="maxWaitMillis" value="${redis.maxWait}" />  
     <property name="testOnBorrow" value="${redis.testOnBorrow}" />  
   </bean>  

    <!--配置redis连接池信息-->
   <bean id="JedisConnectionFactory" class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory" 
       p:host-name="${redis.host}" p:port="${redis.port}" p:password="${redis.pass}" p:pool-config-ref="poolConfig"/>  

    <!--基于连接池创建操作redis的模板-->
   <bean id="redisTemplate" class="org.springframework.data.redis.core.RedisTemplate">  
    	<property name="connectionFactory" ref="JedisConnectionFactory" />  
   </bean>  
      
</beans>  
```

### 4.0 注入RedisTemplate对象

```java
	@Autowired
	private RedisTemplate redisTemplate;	
```

### 4.1 值类型操作

```java
增：redisTemplate.boundValueOps("name").set("itcast");
查：String str = (String) redisTemplate.boundValueOps("name").get();
删全：redisTemplate.delete("name");;
```

### 4.2 Set类型操作 

```java
增：redisTemplate.boundSetOps("nameset").add("曹操");		        redisTemplate.boundSetOps("nameset").add("刘备");	
查：Set members = redisTemplate.boundSetOps("nameset").members();
删：redisTemplate.boundSetOps("nameset").remove("孙权");
删全：redisTemplate.delete("nameset");;
```

### 4.3 List类型操作

```java
增：	redisTemplate.boundListOps("namelist1").rightPush("刘备");  //右压栈
		redisTemplate.boundListOps("namelist1").leftPush("关羽");  //坐压栈
查：List list = redisTemplate.boundListOps("namelist1").range(0, 10); //（起始索引，显示个数）
	String s = (String) redisTemplate.boundListOps("namelist1").index(1); // 根据索引查询
删：redisTemplate.boundListOps("namelist1").remove(1, "关羽");  //（移除个数，要移除的元素）
删全：redisTemplate.delete("namelist");;
```

### 4.4 Hash类型操作

```java
增：	redisTemplate.boundHashOps("namehash").put("a", "唐僧");
		redisTemplate.boundHashOps("namehash").put("b", "悟空");
查：Set s = redisTemplate.boundHashOps("namehash").keys();  //查询出所有的key
	List values = redisTemplate.boundHashOps("namehash").values();  //查询出所有的值
	Object object = redisTemplate.boundHashOps("namehash").get("b");  //根据key找值
删：redisTemplate.boundHashOps("namehash").delete("c"); //根据key删除
删全：redisTemplate.delete("namehash");;
```

