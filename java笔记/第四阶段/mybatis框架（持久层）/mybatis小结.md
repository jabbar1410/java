# mybatis小结
## 配置文件开发的环境配置
### 1.pom文件配置
```xml
 <dependencies>    
   <!-- mybatis依赖-->
   <dependency>     
     <groupId>org.mybatis</groupId>     
     <artifactId>mybatis</artifactId>     
     <version>3.4.5</version>    
   </dependency> 
    <!-- 单元测试依赖-->
   <dependency>     
     <groupId>junit</groupId>     
     <artifactId>junit</artifactId>     
     <version>4.10</version>     
     <scope>test</scope>    
   </dependency>  
   <!-- mysql数据库依赖-->
   <dependency>     
     <groupId>mysql</groupId>     
     <artifactId>mysql-connector-java</artifactId>    
     <version>5.1.6</version>     
     <scope>runtime</scope>    
   </dependency>   
   <!-- 日志依赖-->
   <dependency>     
     <groupId>log4j</groupId>     
     <artifactId>log4j</artifactId>     
     <version>1.2.12</version>    
   </dependency>   
</dependencies> 
```
### 2.核心配置文件（SqlMapConfig）
```xml
<?xml version="1.0" encoding="UTF-8"?> 
<!DOCTYPE configuration     PUBLIC "-//mybatis.org//DTD Config 3.0//EN"     "http://mybatis.org/dtd/mybatis-3-config.dtd"> 
<configuration> 
 <!-- 配置 mybatis 的环境 -->  
  <environments default="mysql">   
    <!-- 配置 mysql 的环境 -->   
    <environment id="mysql">    
      <!-- 配置事务的类型 -->    
      <transactionManager type="JDBC"></transactionManager>    
      <!-- 配置连接数据库的信息：用的是数据源(连接池) -->    
      <dataSource type="POOLED">     
        <property name="driver" value="com.mysql.jdbc.Driver"/>     
        <property name="url" value="jdbc:mysql://localhost:3306/ee50"/>
        <property name="username" value="root"/>    
        <property name="password" value="1234"/>    
      </dataSource>   </environment>  </environments>   
 <!-- 告知 mybatis 映射配置的位置 -->  <mappers> 
  <mapper resource="com/itheima/dao/IUserDao.xml"/>  </mappers> </configuration> 
```
### 3.映射资源配置（IUserDao.xml）
```xml
<?xml version="1.0" encoding="UTF-8"?> 
<!DOCTYPE mapper     PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"     "http://mybatis.org/dtd/mybatis-3-mapper.dtd"> 
<mapper namespace="com.itheima.dao.IUserDao">   
  <!-- 配置查询所有操作 -->  
  <select id="findAll" resultType="com.itheima.domain.User">   
  select * from user  
  </select> 
```
## 简单增删改查的基本配置
### 1.增加用户（selectKey用法）
**方式1**：
- 增加用户时，可以通过select last_insert_id()查询出当前添加用户的id值，并通过 `<selectKey>` 标签将id信息封装会user对象
- 参数含义：
  - keyProperty:   实体类id名
  - keyColumn:    数据库字段名
  - resultType:     返回值类型
  - order:            执行时机（mysql中：after，oracle中：before）
```xml
   <insert id="saveUser" parameterType="com.alibaba.pojo.User" >
        <!-- 新增用户 id 的返回值 -->
        <selectKey keyProperty="id" keyColumn="id" resultType="int" order="AFTER">
            SELECT last_insert_id();
        </selectKey>
        insert into user(username,birthday,sex,address) VALUES (#{username},#{birthday},#{sex},#{address});
    </insert>
```
**方式2**：
```xml
<insert id="saveuser" parameterType="user" useGeneratedKeys="true"    KeyProperty="userId">
  <!--此用法获取方式，只适用于拥有主键自动增长的数据库-->
```
**通过uuid实现主键自增**：
```xml

```



### 2.删除用户

```xml
<delete id="deleteUser" parameterType="int">
        delete FROM user where id = #{id};
    </delete>
```
### 3.修改用户
```xml
 <!--更新一条数据-->
    <update id="updateUser" parameterType="com.alibaba.pojo.User">
        UPDATE user set username = #{username},birthday = #{birthday},sex = #{sex},address = #{address} where id=#{id};
    </update>
```
### 4.查询用户
```xml
  <select id="findById" parameterType="Integer" resultType="com.alibaba.pojo.User">
        SELECT * from USER where id = #{uid};
    </select>
```
### 5.模糊查询
```xml
    <!--模糊查询2-->
    <select id="findUser2" parameterType="String" resultType="com.alibaba.pojo.User">
        select * from USER where username LIKE '%${value}%';
    </select>
```
### 6.聚合函数查询
```xml
    <!--查询使用聚合函数-->
    <select id="countUser" resultType="int">
        select count(*) from USER ;
    </select>
```

## 映射配置文件的特殊配置标签
### 1.`<resultMap>`
**定义**： 标签可以建立查询的列名和实体类的属性名称不一致时建立对应关系。从而实现封装
**标签**：
   - `<id>`：用于指定主键字段
   - `<result>：`用于指定非主键字段

**属性**：
  - column：用于指定数据库列名 
  - property：用于指定实体类属性名称
```xml
<!-- 建立 User 实体和数据库表的对应关系 type 属性：指定实体类的全限定类名  id 属性：给定一个唯一标识，是给查询 select 标签引用用的。 --> 
<resultMap type="com.itheima.domain.User" id="userMap">  
  <id column="id" property="userId"/>  
  <result column="username" property="userName"/>  
  <result column="sex" property="userSex"/>  
  <result column="address" property="userAddress"/>  
  <result column="birthday" property="userBirthday"/> 
</resultMap> 
```
**使用**：在select标签中定义resultMap属性
```xml
<!-- 配置查询所有操作 --> 
<select id="findAll" resultMap="userMap">  
  select * from user 
</select>
```
### 输出映射（结果封装）

resultType：底层是 resultMap

			简单类型：int、double、String 等，直接返回 类型全类名 或者 别名
	
				譬如：int、java.lang.String 等
	
			pojo：可以是全类名，也可以是别名。推荐使用全类名：包名.类名
	
				譬如：com.itheima.domain.User
	
			List 集合：是集合中元素的具体类型，格式同 pojo
	
			map：java.util.map 或者 map

resultMap

			查询结果集与映射pojo属性不一致时，可以使用resultMap指定结果集列名与pojo的属性名进行手动映射
	
			后期还可以映射关联关系
	
				一对一：association
	
				一对多：collection
					属性：
						- select：属性指定的内容：查询用户的唯一标识（查询账户时，需要关联查询用户的sql所在的statementId）
						- column：属性指定的内容：用户id查询时，所需要的参数的值 select属性所需要的参数






## SqlMapConfig.xml 中配置的内容和顺序

### sql 映射文件

		精通，常用标签、属性		
	
		标签：mappers、select、insert、update、delete、ResultMap、sql、include、cache
	
			select 的子节点：selectKey
	
			resultMap 的子节点：
	
	<resultMap id="">
		<id column="" property=""/>
		<result column="" perperty=""/>
		......
	</resultMap>
	
			sql 的子节点：if、where、foreach、trim、set、choose、when、otherwise
	
		属性：namespace、id、resultType、resultMap、parameterType

```
-properties（属性）  
    --property 
-settings（全局配置参数）  
    --setting 
-typeAliases（类型别名）  
    --typeAliase  
    --package 
-typeHandlers（类型处理器） 
-objectFactory（对象工厂） 
-plugins（插件） 
-environments（环境集合属性对象）  
    --environment（环境子属性对象）   
        ---transactionManager（事务管理）  
        ---dataSource（数据源） 
-mappers（映射器）  
    --mapper  
    --package
```
### 1.`<properties>`
**方式一**：
```xml
<properties> 
  <property name="jdbc.driver" value="com.mysql.jdbc.Driver"/>  
  <property name="jdbc.url" value="jdbc:mysql://localhost:3306/eesy"/> 
  <property name="jdbc.username" value="root"/>  
  <property name="jdbc.password" value="1234"/>  
</properties> 
```
**方式二**：引入外部配置文件（推荐使用）
```xml
<!-- 配置连接数据库的信息    
resource 属性：用于指定 properties 配置文件的位置，要求配置文件必须在类路径下     resource="jdbcConfig.properties" 
  url 属性：    URL： Uniform Resource Locator 统一资源定位符     

http://localhost:8080/mystroe/CategoryServlet  URL 
    协议  主机   端口     URI   
URI：Uniform Resource Identifier 统一资源标识符     /mystroe/CategoryServlet 
    它是可以在 web 应用中唯一定位一个资源的路径 --> 
<properties url= 
file:///D:/IdeaProjects/day02_eesy_01mybatisCRUD/src/main/resources/jdbcConfig.prop erties"> 
</properties> 
```
**使用**：
```xml
 <dataSource type="POOLED">   
   <property name="driver" value="${jdbc.driver}"/>   
   <property name="url" value="${jdbc.url}"/>  
   <property name="username" value="${jdbc.username}"/>   
   <property name="password" value="${jdbc.password}"/>  
</dataSource>
```
### 2.`<typeAliases>`(类型别名)
**属性**：
     -  type:要定义的实体类全限定类名
     -  alias:别名
```xml
在 SqlMapConfig.xml 中配置： 
<typeAliases>  
  <!-- 单个别名定义 -->  
  <typeAlias alias="user" type="com.itheima.domain.User"/>  
  <!-- 批量别名定义，扫描整个包下的类，别名为类名（首字母大写或小写都可以） -->  
  <package name="com.itheima.domain"/>  
  <package name=" 其它包 "/> 
</typeAliases> 
```
### 2.1`<package>`：用在typeAliases标签下
**作用**：指定包路径下的所有类可以通过类名代替全限定类名

### 2.2. `<package>` ：用在mappers标签下（常用）
```xml
注册指定包下的所有 mapper 接口 如：
<package name="cn.itcast.mybatis.mapper"/> 
【注意】：
此种方法要求 mapper 接口名称和 mapper 映射文件名称相同，
且放在同一个目录中
```
### 3.`<mappers>`映射器

1. `<mapper resource=" " /> `

```
使用相对于类路径的资源 如：<mapper resource="com/itheima/dao/IUserDao.xml" /> 
```
2. `<mapper class=" " /> `

```
使用 mapper 接口类路径 如：<mapper class="com.itheima.dao.UserDao"/> 注意：此种方法要求 mapper 接口名称和 mapper 映射文件名称相同，且放在同一个目录中。
```

3. `<package name=""/> `
```
注册指定包下的所有 mapper 接口 如：<package name="cn.itcast.mybatis.mapper"/> 注意：此种方法要求 mapper 接口名称和 mapper 映射文件名称相同，且放在同一个目录中。
```
## 基于注解开发

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

## 逆向工程（Mybatis官方提供的）

	概念：就是由 数据库表 生成 pojo，mapper接口，mapper.xml  （只能生成单表CRUD）
	
	需要一个项目，该项目只能在 eclipse 里中运行。
	
	这个项目是普通的Java项目。
	
	我们要做的：
	
		1、改数据库的连接信息
	
		2、改包名
	
		3、指定要逆向生成的表名与类名
	
			tb_user (表名)   ----    TbUser (默认的类名)    ----   User (自定义类名)
	
		4、右键 run

### 延迟加载（懒加载）

**基于子类的动态代理**

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



### 缓存

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

##### 注意

​		多个一级缓存共享一个二级缓存

​		只有当一级缓存关闭时，数据才会被保存到二级缓存中

​		一级缓存的介质是内存，二级缓存的介质可以是内存或者磁盘

​		用户在进行查询时，会先从二级缓存中查找，若二级缓存中无数据，则从一级缓存中查找，若一级缓存中无记		录，则从数据库查找，返回结果后，把结果放入一级缓存，当一级缓存关闭时，把结果放入二级缓存。













## 知识点补充
**提示**：parameterType属性可以不写，通过映射配置文件可以获取到，最好写上容易阅读判断。

### 1.动态sql
#### 1.1如果参数类型不是pojo类型，可通过在参数类型前添加`@Param（“value”）`注解，给参数取别名，并可在映射xml文件中通过别名对参数进行数值判断

#### 示例：
**dao**：
```java
  List<User> findByCondition(@Param("sex") String sex);
/*
    若有多个非pojo参数，可以如下定义：
    List<User> findUser(@Param("username") String username, @Param("password") String password);
*/
```
**xml**：
```xml
  <!--通过单个参数条件查询用户
        注意：select标签中不能写parameterType属性
-->
    <select id="findByCondition" resultType="com.alibaba.pojo.User">
        SELECT * from USER
        <where>
            <if test="sex != null and sex.length() > 0">
                and sex = #{sex};
            </if>
        </where>
    </select>

```
#### 1.2批量插入多条记录

#### 示例：
**dao**：
```java

    /**
     * 批量插入多条记录
     * @param users
     * @return
     */
    int saveManyUser(@Param("users") List<User> users);
```
**xml**：
```xml
    <!--批量插入多条记录-->
    <insert id="saveManyUser">
        insert into user VALUES
        <if test="users != null and users.size() > 0">
            <foreach collection="users" open="" close="" item="user" separator=",">
                (#{user.id},#{user.username},#{user.birthday},#{user.sex},#{user.address})
            </foreach>
        </if>
    </insert>
```

#### 1.3`<trim>`标签代替`<where>`标签：

**dao**：
```java
   /**
     * 利用trim代理where，进行动态sql查询
     * @param user
     * @return
     */
    List<User> findByCondition02(User user);
```
**xml**：
属性：
    - prefix：前缀（语句以什么开头）
    - suffix：后缀（语句以什么结尾）
    - suffixOverrides：当语句后面没有衔接是去掉最后面的指定内容
    - prefixOverrides：当有衔接语句时去掉最前面的指定内容
```xml
    <!--利用trim代理where，进行动态sql查询-->
    <select id="findByCondition02" resultType="com.alibaba.pojo.User">
        select * from user
        <trim prefix="where" suffixOverrides="and">
            <if test="username != null and username != ''">
                username like #{username} AND
            </if>
            <if test="sex != null">
                sex=#{sex} AND
            </if>
        </trim>
    </select>
```

#### 1.4通过set标签，对字段动态更新
**`<set>`特点**：当语句后面没有衔接语句时，可以过滤更新语句多出来的（逗号）

**dao**：
```java
    /**
     * 通过set标签，对字段动态更新
     * @param user
     * @return
     */
    int updateByCondition(User user);
```
**xml**：
属性：
    - prefix：前缀（语句以什么开头）
    - suffix：后缀（语句以什么结尾）
    - suffixOverrides：当语句后面没有衔接是去掉最后面的指定内容
    - prefixOverrides：当有衔接语句时去掉最前面的指定内容
```xml

    <!--通过set标签，对字段动态更新-->
    <update id="updateByCondition">
        update USER
        <set>
            <if test="username != null">username=#{username},</if>
            <if test="sex != null">sex=#{sex},</if>
            <if test="address != null">address=#{address},</if>
        </set>
        <where>
            <if test="id != 0">
                AND id=#{id}
            </if>
        </where>
    </update>
```
### 2.sql – 可被其他语句引用的可重用语句块。
**特点**：可以引用sql语句中的任意

```xml
    <sql id="fields">
        id,username,birthday,sex,address
    </sql>

  <!--引用sql语句 -->
    <select id="findAll" resultType="com.alibaba.pojo.User">
        SELECT <include refid="fields"></include> FROM USER
    </select>
```














## 关键词分析

#### #{} 和 ${} 的区别：

##### #{}：
**特点**：占位符（相当于jdbc中的？），防止sql注入问题，使用PerparedStatement实现设置参数，支持java类型和jdbc类型转换
**用法**：
  - 传入参数是实体类型，值要和实体类中的属性名一致
  - 传入参数是基本数据类型和String值可以随便写

**示例**：
  - select * from user where username = #{username}
        - 封装sql语句：select * from user where username = ? ; 
##### ${}：
**特点**：原样拼接sql，不支持解决sql注入，不支持类型转换（）
**用法**：
  - 传入参数是实体类型，值要和实体类中的属性名一致
  - 传入参数是基本数据类型和String值必须是value

**示例**：
  - select * from user where username = '%${value}%'
        - 封装sql语句：select * from user where username = '%xxx%' ; 

**${}特殊运用场所**：
    - select * from user order by 'uid'








**ORM**:对象关系映射（实体类与表的映射关系）

**SSM**:SpringMVC+Spring+MyBatis  应用于电商项目等等；

**SSH**：Struts+Spring+Hibernate  应用于员工企业版等等，多用于企业内部员工使用

**OGNL**: 对象图导航语言它是通过对象的取值方法来获取数据。在写法上把get给省略了。
比如：我们获取用户的名称
- 类中的写法：user.getUsername();
- OGNL表达式写法：user.username

mybatis中为什么能直接写username,而不用user.呢：
因为在parameterType中已经提供了属性所属的类，所以此时不需要写对象名


**构建者模式**：把对象创建细节隐藏让使用者直接调用方法即可拿到对象

**工厂模式**：解耦合（降低类之间的依赖关系）

**动态代理模式**：不修改源码的基础上对已有方法增强

**mapper标签中的关键字class**：
设置配置文件映射时，class既可以指定xml文件的位置，也可以指定接口位置 （前提：配置文件与接口，目录结构必须相同）




**一级缓存跟二级缓存的区别（面试题目）**：

	SqlSession级别，默认开启，无法关闭。
	
	SqlSessionFatory级别（也可以说是 namesapce级别），可以在 mybatis 的全局配置文件中指定是否开启
	
	多个一级缓存共享一个二级缓存
	
	只有当一级缓存关闭时，数据才会被保存到二级缓存中
