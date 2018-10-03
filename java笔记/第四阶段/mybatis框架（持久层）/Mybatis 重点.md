# Mybatis 重点

## 1、概述及地位

​	ORM 框架 

​	角色：Dao层



## 2、基于配置文件开发

### 	1、开发环境搭建

​		a.jar包

​		b.核心配置文件

​		c.sql 映射文件

​		c.测试类

​		备注：可参考官方文档（别死记硬背）

### 	2、核心配置文件

​		了解，整合 spring 后，可以是一个空文件

### 	3、sql 映射文件

​		精通，常用标签、属性		

​		标签：mappers、select、insert、update、delete、ResultMap、sql、include、cache

​			select 的子节点：selectKey

​			resultMap 的子节点：

```xml-dtd
<resultMap id="">
	<id column="" property=""/>
	<result column="" perperty=""/>
	......
</resultMap>
```

​			sql 的子节点：if、where、foreach、trim、set、choose、when、otherwise

​		属性：namespace、id、resultType、resultMap、parameterType

​		**备注：可以参看逆向工程生成的文件**

### 	4、输入映射 （参数封装）parameterType

​		简单参数		参数名随意，一般与形参名称一致

​		pojo			参数名必须与 pojo 的属性一致

​		包装 pojo		参数名必须与 包装 pojo 的属性一致（可以使用 . 的形式访问其属性）

​		@param注解	多个参数前，使用@param("name") 修饰形参，在 xml 中使用 #{name} 映射绑定形参

### 	5、输出映射（结果封装）

#### 		resultType：底层是 resultMap

​			简单类型：int、double、String 等，直接返回 类型全类名 或者 别名

​				譬如：int、java.lang.String 等

​			pojo：可以是全类名，也可以是别名。推荐使用全类名：包名.类名

​				譬如：com.itheima.domain.User

​			List 集合：是集合中元素的具体类型，格式同 pojo

​			map：java.util.map 或者 map

#### 		resultMap

​			查询结果集与映射pojo属性不一致时，可以使用resultMap指定结果集列名与pojo的属性名进行手动映射

​			后期还可以映射关联关系

​				一对一：association

​				一对多：collection

### 	6、动态sql

​		if、where、foreach、sql片段

```xml
<select id="findUserListByCondition" resultType="com.itheima.domain.User">
	select * from user
    <where>
    	<if test="username != null and username != ''">
        	and username like '%${usename}%'
            <!-- and username like concat('%',#{username},'%') -->
        </if>
        <if ....></if>
    </where>
</select>

<!--批量插入-->
<!--int insertManyUser(@Param("users") List<User> users);-->
<insert id="insertManyUser">
    INSERT INTO `user` (<include refid="base_column"/> )
    VALUES
    <foreach collection="users" item="u" open="" close="" separator=",">
        <!--'jack','1999-12-12','女','USA'-->
        (#{u.userName},#{u.userBirthday},#{u.userSex},#{u.userAddress})
    </foreach>
</insert>

<sql id="base_column">
	username, birthday, sex, address
</sql>

<!--参考官网-->
<set></set>
<trim></trim>
```



### 	7、关联关系

​		根据具体业务需要分析表与表之间的关系（实体与实体之间的关系）

​		使用 resultMap 进行映射。

​			一对一(多对一)：association

​				javaType：指定具体关联的属性类型

```xml
<resultMap id="">
    <!--映射主键-->
	<id column="" property=""></id>
    <result column="" property="" ></result>
    result.....
    <association property="JavaBean中关联属性的名称" javaType="property属性对应的全类名">
        <id column="" property=""></id>
        <result column="" property="" ></result>
        result.....	
    </association>
</resultMap>

```

​			一对多：collection

​				ofType：指定具体关联的集合中元素的类型

```xml
<resultMap id="">
    <!--映射主键-->
	<id column="" property=""></id>
    <result column="" property="" ></result>
    result.....
    <collection property="JavaBean中关联集合属性的名称" ofType="property属性对应的全类名">
        <id column="" property=""></id>
        <result column="" property="" ></result>
        result.....	
    </collection>
</resultMap>
```

​			多对多映射：collection

​				需要借助 中间表

​					userid      roleid       联合主键

​					1		1

​					2		1

​					1		2

```xml
<resultMap id="">
    <!--映射主键-->
	<id column="roleid" property="roleId"></id>
    <result column="" property="" ></result>
    result.....
    <collection property="JavaBean中关联集合属性的名称" ofType="property属性对应的全类名">
        <id column="userid" property="userId"></id>
        <result column="" property="" ></result>
        result.....	
    </collection>
</resultMap>
```



### 	8、延迟加载（懒加载）

​		在关联查询的基础上才可能出现延迟加载（发送两条或两条以上的SQL）

​		多对一（一对一）或者一对多	   都可以使用懒加载

```xml

<!-- 定义User的resultMap-->
    <resultMap id="userAccountMap" type="user">
        <id property="id" column="id"></id>
        <result property="username" column="username"></result>
        <result property="address" column="address"></result>
        <result property="sex" column="sex"></result>
        <result property="birthday" column="birthday"></result>
        <!-- select : 要执行的关联查询的SQL语句所在的 StatementId
 			column : SQL语句要执行的参数所对应的结果集列名
		-->
        <collection property="accounts" ofType="account" select="com.itheima.dao.IAccountDao.findAccountByUid" column="id"></collection>
    </resultMap>
<多对一  类似>
```



### 	9、缓存

​		一级缓存

​			SqlSession级别，默认开启，无法关闭。

```
失效的情况：
	不是同一个 SqlSession
	两次相同查询操作中间执行了以下操作
		增删改
		执行 commit
		执行 clearCache
```

​		二级缓存

​			SqlSessionFatory级别（也可以说是 namesapce级别），是否开启，查看 mybatis 的具体版本。

​			可以在 mybatis 的全局配置文件中指定是否开启

​			1. <setting name="cacheEnabled" value="true"/>

​			2. 在需要使用缓存的Sql映射文件中配置 <cache/>

##### 		注意

​		多个一级缓存共享一个二级缓存

​		只有当一级缓存关闭时，数据才会被保存到二级缓存中

​		一级缓存的介质是内存，二级缓存的介质可以是内存或者磁盘

​		用户在进行查询时，会先从二级缓存中查找，若二级缓存中无数据，则从一级缓存中查找，若一级缓存中无记		录，则从数据库查找，返回结果后，把结果放入一级缓存，当一级缓存关闭时，把结果放入二级缓存。



##### 一级缓存跟二级缓存的区别（面试题目）

​	SqlSession级别，默认开启，无法关闭。

​	SqlSessionFatory级别（也可以说是 namesapce级别），可以在 mybatis 的全局配置文件中指定是否开启

​	多个一级缓存共享一个二级缓存

​	只有当一级缓存关闭时，数据才会被保存到二级缓存中



## 3、基于注解开发

- @Insert:实现新增
- @Update:实现更新
- @Delete:实现删除
- @Select:实现查询
- @Result:实现结果集封装
- @Results:可以与@Result 一起使用，封装多个结果集       （等同于 xml 中的 ResultMap）
- @ResultMap:实现引用@Results 定义的封装
- @One:实现一对一结果集封装
- @Many:实现一对多结果集封装
- @SelectProvider: 实现动态 SQL 映射
- @CacheNamespace:实现注解二级缓存的使用



## 4、逆向工程（Mybatis官方提供的）

​	概念：就是由 数据库表 生成 pojo，mapper接口，mapper.xml  （只能生成单表CRUD）

​	需要一个项目，该项目只能在 eclipse 里中运行。

​	这个项目是普通的Java项目。

​	我们要做的：

​		1、改数据库的连接信息

​		2、改包名

​		3、指定要逆向生成的表名与类名

​			tb_user (表名)   ----    TbUser (默认的类名)    ----   User (自定义类名)

​		4、右键 run



​		