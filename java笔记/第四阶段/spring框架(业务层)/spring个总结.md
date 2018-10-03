# spring第一天
## 1. spring 是什么
    Spring是分层的 Java SE/EE应用 full-stack 轻量级开源框架，以 IoC（Inverse Of Control： 反转控制）和 AOP（Aspect Oriented Programming：面向切面编程）为内核，提供了展现层 Spring MVC 和持久层 Spring JDBC 以及业务层事务管理等众多的企业级应用技术，还能整合开源世界众多 著名的第三方框架和类库，逐渐成为使用最多的Java EE 企业应用开源框架

## 2. 控制反转（IOC）
    作用：降低类之间的耦合，它包含依赖注入（DI）和依赖查找（DL）
    
## 3. spring对bean的管理细节
        1.创建bean的三种方式
        2.bean对象的作用范围
        3.bean对象的生命周期
### 3.1  创建Bean的三种方式 
    第一种方式：使用默认构造函数创建。
            在spring的配置文件中使用bean标签，配以id和class属性之后，且没有其他属性和标签时。
            采用的就是默认构造函数创建bean对象，此时如果类中没有默认构造函数，则对象无法创建。

    <bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl"></bean>


    第二种方式： 使用普通工厂中的方法创建对象（使用某个类中的方法创建对象，并存入spring容器）
    <bean id="instanceFactory" class="com.itheima.factory.InstanceFactory"></bean>
    <bean id="accountService" factory-bean="instanceFactory" factory-method="getAccountService"></bean>
    

    第三种方式：使用工厂中的静态方法创建对象（使用某个类中的静态方法创建对象，并存入spring容器)
    <bean id="accountService" class="com.itheima.factory.StaticFactory" factory-method="getAccountService"></bean>
    
### 3.2 bean的作用范围调整
        bean标签的scope属性：
            作用：用于指定bean的作用范围
            取值： 常用的就是单例的和多例的
                singleton：单例的（默认值）
                prototype：多例的
                request：作用于web应用的请求范围
                session：作用于web应用的会话范围
                global-session：作用于集群环境的会话范围（全局会话范围），当不是集群环境时，它就是session

    <bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl" scope="prototype"></bean>

### 3.3 bean对象的生命周期
        单例对象
            出生：当容器创建时对象出生
            活着：只要容器还在，对象一直活着
            死亡：容器销毁，对象消亡
            【总结】：单例对象的生命周期和容器相同
        多例对象
            出生：当我们使用对象时spring框架为我们创建
            活着：对象只要是在使用过程中就一直活着。
            死亡：当对象长时间不用，且没有别的对象引用时，由Java的垃圾回收器回收
            bean标签的属性：
            init-method：指定类中的初始化方法名称。  
            destroy-method：指定类中销毁方法名称。

## 4. sping的依赖注入

### 4.1 依赖注入概念
    依赖注入：Dependency Injection。它是 spring 框架核心 ioc 的具体实现。 我们的程序在编写时，通过控制反转，把对象的创建交给了 spring，但是代码中不可能出现没有依赖的情况。 ioc 解耦只是降低他们的依赖关系，但不会消除。例如：我们的业务层仍会调用持久层的方法。 那这种业务层和持久层的依赖关系，在使用 spring 之后，就让 spring 来维护了。 简单的说，就是坐等框架把持久层对象传入业务层，而不用我们自己去获取。
         依赖注入：
            Dependency Injection
        IOC的作用：
            降低程序间的耦合（依赖关系）
        依赖关系的管理：
            以后都交给spring来维护
        在当前类需要用到其他类的对象，由spring为我们提供，我们只需要在配置文件中说明
        依赖关系的维护：
            就称之为依赖注入。
         依赖注入：
            能注入的数据：有三类
                基本类型和String
                其他bean类型（在配置文件中或者注解配置过的bean）
                复杂类型/集合类型
             注入的方式：有三种
                第一种：使用构造函数提供
                第二种：使用set方法提供
                第三种：使用注解提供

### 4.2 构造函数注入
        使用的标签:constructor-arg
        标签出现的位置：bean标签的内部
        标签中的属性
            type：用于指定要注入的数据的数据类型，该数据类型也是构造函数中某个或某些参数的类型
            index：用于指定要注入的数据给构造函数中指定索引位置的参数赋值。索引的位置是从0开始
            name：用于指定给构造函数中指定名称的参数赋值                                        常用的
            =============以上三个用于指定给构造函数中哪个参数赋值===============================
            value：用于提供基本类型和String类型的数据
            ref：用于指定其他的bean类型数据。它指的就是在spring的Ioc核心容器中出现过的bean对象

        优势：
            在获取bean对象时，注入数据是必须的操作，否则对象无法创建成功。
        弊端：
            改变了bean对象的实例化方式，使我们在创建对象时，如果用不到这些数据，也必须提供。
```xml
    <bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl">
        <constructor-arg name="name" value="泰斯特"></constructor-arg>
        <constructor-arg name="age" value="18"></constructor-arg>
        <constructor-arg name="birthday" ref="now"></constructor-arg>
    </bean>

    <!-- 配置一个日期对象 -->
    <bean id="now" class="java.util.Date"></bean>
```

### 4.3 set方法注入(常用的方式)
        涉及的标签：property
        出现的位置：bean标签的内部
        标签的属性
            name：用于指定注入时所调用的set方法名称
            value：用于提供基本类型和String类型的数据
            ref：用于指定其他的bean类型数据。它指的就是在spring的Ioc核心容器中出现过的bean对象
        优势：
            创建对象时没有明确的限制，可以直接使用默认构造函数
        弊端：
            如果有某个成员必须有值，则获取对象是有可能set方法没有执行。
```xml
    <bean id="accountService2" class="com.itheima.service.impl.AccountServiceImpl2">
        <property name="name" value="TEST" ></property>
        <property name="age" value="21"></property>
        <property name="birthday" ref="now"></property>
    </bean>
```
### 4.4 复杂类型的注入/集合类型的注入
        用于给List结构集合注入的标签：
            list array set
        用于个Map结构集合注入的标签:
            map  props
        结构相同，标签可以互换
```xml
    <bean id="accountService3" class="com.itheima.service.impl.AccountServiceImpl3">
        <property name="myStrs">
            <array>
                <value>AAA</value>
                <value>BBB</value>
                <value>CCC</value>
          </array>
        </property>

        <property name="myList">
            <list>
                <value>AAA</value>
                <value>BBB</value>
                <value>CCC</value>
            </list>
        </property>

        <property name="mySet">
            <set>
                <value>AAA</value>
                <value>BBB</value>
                <value>CCC</value>
            </set>
        </property>

        <property name="myMap">
            <map>
                <entry key="testA" value="aaa"></entry>
                <entry key="testB">
                    <value>BBB</value>
                </entry>
            </map>
      
        </property>

        <property name="myProps">
            <props>
                <prop key="testC">ccc</prop>
                <prop key="testD">ddd</prop>
            </props>
        </property>
    </bean>
```
**补充：map集合中注入应用类型**
```xml
    方式一：
    <bean id="date" class="java.util.Date"></bean>
  <bean>
   <property name="myMap">
            <map>
                <entry key="birthday" value-ref="date"></entry>
            </map>
        </property>
  </bean>
    
  方式二：
  <bean>
   <property name="myMap">
            <map>
               <entry key="birthday">
                    <bean class="java.util.Date"></bean>
                </entry>
            </map>
        </property>
  </bean>
```

## 5.测试

### 获取ApplicationContext 的ioc容器（单例对象适用）：
```java
ApplicationContext ac = new ClassPathXmlApplicationContext（”bean.xml“）；
```

### 获取BeanFactory 的ioc容器（多例对象适用）：
```java
        Resource rs = new ClassPathResource("bean.xml");
        BeanFactory factory = new XmlBeanFactory(rs);
```

### 获取ioc容器后，根据配置文件中的id获取对象的两种方式：
```java
- 方式一：
    - ac.getBean(Sting id);
- 方式二：
    - ac.getBean(String id,Class<T> tclass);
```

### 单例与多例的创建时机：
- 单例：读取配置文件时
- 多例：调用方法获取对象时







# spring第二天

## 1.使用注解配置IOC的xml配置
```xml
<?xml version="1.0" encoding="UTF-8"?> <beans xmlns="http://www.springframework.org/schema/beans"  xmlns:context="http://www.springframework.org/schema/context" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"     xsi:schemaLocation="http://www.springframework.org/schema/beans         http://www.springframework.org/schema/beans/spring-beans.xsd 
        http://www.springframework.org/schema/context         http://www.springframework.org/schema/context/spring-context.xsd"> 
     
 <!-- 告知 spring 创建容器时要扫描的包 -->  
  <context:component-scan base-package="com.itheima"></context:component-scan>
  <!-- 扫描多个包 -->
    <context:component-scan base-package="com.itheima,com.aaa"></context:component-scan>
  <!-- 引入外部配置文件 -->
  <context:property-placeholder location="jdbcConfig.properties"/>
  <!-- 引入类路径下所有的properties文件>
  <context:property-placeholder location="classpass*:*.properties*"/>
```

## 2. 基于注解的 IOC 配置
### 2.1 注解的四大类型
    - 用于创建对象的注解，相当于<bean>标签
    - 用于注入数据的，相当于<property>标签
    - 用于改变作用范围的，相当于scope属性	
    - 和生命周期相关的，相当于init-method  destroy-method属性

#### 2.1.1 用于创建对象的注解
**@Component**：（用于普通bean对象）
    作用：  把资源让 spring 来管理。相当于在 xml 中配置一个 bean。 
    属性：  value：指定 bean 的 id。如果不指定 value 属性，默认 bean 的 id 是当前类的类名。首字母小写
**@Controller**：一般用于表现层的注解。
**@Service**：一般用于业务层的注解
**@Repository**：一般用于持久层的注解
#### 2.1.2用于注入数据的 
**@Autowired**：(低层是反射注入，会打破封装性)

      作用：  自动按照类型注入。当使用注解注入属性时，set方法可以省略。它只能注入其他 bean 类型。当有多个 类型匹配时，使用要注入的对象变量名称作为 bean 的 id，在 spring 容器查找，找到了也可以注入成功。找不到 就报错
      
**@Qualifier**：
  
    作用：  在自动按照类型注入的基础之上，再按照 Bean 的 id 注入。它在给字段注入时不能独立使用，必须和 @Autowire 一起使用；但是给方法参数注入时，可以独立使用。 属性：  value：指定 bean 的 id

**@Resource**：
    
    作用：  直接按照 Bean 的 id 注入。它也只能注入其他 bean 类型。 属性：  name：指定 bean 的 id。

**@Value**：@Value("${jdbc.driver}")

    作用：  注入基本数据类型和 String 类型数据的 属性：  value：用于指定值
    
#### 2.1.3用于改变作用范围的

**@Scope**：@Scope("prototype")

    作用：  指定 bean 的作用范围。 属性：  value：指定范围的值。      取值：singleton  prototype request session globalsession

#### 2.1.4和生命周期相关的：(了解)
**@PostConstruct**： 
   
    作用：  用于指定初始化方法。 
**@PreDestroy** 
    
    作用：  用于指定销毁方法
    
### 2.2spring中的新注解
 **@Configuration**：
      作用：指定当前类是一个配置类
      细节：当配置类作为AnnotationConfigApplicationContext对象创建的参数时，该注解可以不写。
 **@ComponentScan**：@ComponentScan("com.alibaba")
       作用：用于通过注解指定spring在创建容器时要扫描的包
       属性：
           value：它和basePackages的作用是一样的，都是用于指定创建容器时要扫描的包。
                  我们使用此注解就等同于在xml中配置了:
                       <context:component-scan base-package="com.itheima"></context:component-scan>
 **@Bean**： @Bean(name="dataSource")
       作用：用于把当前方法的返回值作为bean对象存入spring的ioc容器中
       属性:
           name:用于指定bean的id。【当不写时，默认值是当前方法的名称】
       细节：
           当我们使用注解配置方法时，如果方法有参数，spring框架会去容器中查找有没有可用的bean对象。
           查找的方式和Autowired注解的作用是一样的
 **@Import**：@Import(SqlConfig.class)
       作用：用于导入其他的配置类
       属性：
           value：用于指定其他配置类的字节码。
                   当我们使用Import的注解之后，有Import注解的类就父配置类，而导入的都是子配置类
 **@PropertySource**：@PropertySource("classpath:jdbcConfig.properties")
       作用：用于指定properties文件的位置
       属性：
           value：指定文件的名称和路径。
                   关键字：classpath，表示类路径下
  
### 2.3.1 xml方式配置数据源
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
         http://www.springframework.org/schema/context/spring-context.xsd">

    <!--导入配置文件-->
    <context:property-placeholder location="jdbcConfig.properties"></context:property-placeholder>

    <!--配置accountservice-->
    <bean id="accountService" class="com.alibaba.service.impl.AccountServiceImpl">
        <!--注入accountdao对象-->
        <property name="ad" ref="accountDao"></property>
    </bean>

    <!--配置accountDao-->
    <bean id="accountDao" class="com.alibaba.dao.impl.AccountDaoImpl">
        <!--注入queryRunnser对象-->
        <property name="qr" ref="queryRunner"></property>
    </bean>

    <!--配置QueryRunner-->
    <bean id="queryRunner" class="org.apache.commons.dbutils.QueryRunner">
        <!--通过构造注入数据源对象-->
        <constructor-arg name="ds" ref="dataSource"></constructor-arg>
    </bean>


    <!-- 配置数据源 -->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${jdbc.driver}"></property>
        <property name="jdbcUrl" value="${jdbc.url}"></property>
        <property name="user" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
</beans>
```
### 2.3.2 全注解方式配置数据源
**主配置文件**
```java
/**
 * 注解配置ioc的主配置文件
 */

@Configuration
@ComponentScan("com.alibaba")
@Import(SqlConfig.class)
@PropertySource("classpath:jdbcConfig.properties")
public class DataSourceConfig {
}
```
**子配置文件**
```java
public class SqlConfig {

    @Value("${jdbc.driver}")
    private String driver;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;


    /**
     * 获取QueryRunner对象
     */
    @Bean(name="queryRunner")
    @Scope("prototype")
    public QueryRunner createQueryRunner(DataSource ds){
        return new QueryRunner(ds);
    }


    /**
     * 获取数据源对象
     */
    @Bean(name="dataSource")
    public DataSource getDataSource(){
        ComboPooledDataSource ds = new ComboPooledDataSource();
        try {
            ds.setDriverClass(driver);
            ds.setJdbcUrl(url);
            ds.setUser(username);
            ds.setPassword(password);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return ds;
    }
}
```
## 3.spring整合junnit
使用Junit单元测试：测试我们的配置
Spring整合junit的配置
       1、导入spring整合junit的jar(坐标)
       
         ```xml
         <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        
           <!--单元测试-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
         ```
       2、使用Junit提供的一个注解把原有的main方法替换了，替换成spring提供的(让其能获得spring提供 的容器)
              @Runwith
       3、告知spring的运行器，spring和ioc创建是基于xml还是注解的，并且说明位置
           @ContextConfiguration
                   locations：指定xml文件的位置，加上classpath关键字，表示在类路径下
                   classes：指定注解类所在地位置
 
   **【提示】 当我们使用spring 5.x版本的时候，要求junit的jar必须是4.12及以上**
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)   --指定了注解配置位置
@ContextConfiguration(locations="classpath:bean.xml")  --指定了xml的位置


# Spring第三天
## 1.动态代理
### 1.1 基于接口的动态代理（实现与被代理对象实现相同接口的实现类都是代理的对象）
**特点**：字节码随用随创建，随用随加载
**作用**：不修改源码的基础上对方法增强
**涉及的类**：Proxy
**提供者**：JDK官方
**如何创建代理对象**：
               使用Proxy类中的newProxyInstance方法
**创建代理对象的要求**：
               被代理类最少实现一个接口，如果没有则不能使用
**newProxyInstance方法的参数**：
- ClassLoader：类加载器
                   它是用于加载代理对象字节码的。和被代理对象使用相同的类加载器。固定写法。
- Class[]：字节码数组
                   它是用于让代理对象和被代理对象有相同方法。固定写法。
- InvocationHandler：用于提供增强的代码
                   它是让我们写如何代理。我们一般都是些一个该接口的实现类，通常情况下都是匿名内部类，但不是必须的。
                   此接口的实现类都是谁用谁写‘。
                            
**实现类参数**：        
- proxy：代理对象的引用。不一定每次都用得到      
- method：当前执行的方法对象       
- args：执行方法所需的参数      
- 返回值：        当前执行方法的返回值
### 1.2基于子类的动态代理
  - 要求：     被代理对象不能是最终类   *
  - 用到的类：   Enhancer  
  - 创建的方法：      create(Class, Callback) 
  - 方法的参数：   * 
      - Class：被代理对象的字节码  
      - Callback：如何代理
  -  参数： 前三个和基于接口的动态代理是一样的。     
      -  MethodProxy：当前执行方法的代理对象。     
      - 返回值：        当前执行方法的返回值
## 2. AOP
### 2.1概述：
    简单的说它就是把我们程序重复的代码抽取出来，在需要执行的时候，使用动态代理的技术，在不修改源码的基础上，对我们的已有方法进行增强
    
### 2.2 AOP 的作用及优势 
    作用：  在程序运行期间，不修改源码对已有方法进行增强。 优势：  减少重复代码     提高开发效率     维护方便

### 2.3 AOP 相关术语
   -  **Joinpoint(连接点)**:   所谓连接点是指那些被拦截到的点。在 spring 中,这些点指的是方法,因为 spring 只支持方法类型的 连接点。 
   -  **Pointcut(切入点)**:   所谓切入点是指我们要对哪些 Joinpoint 进行拦截的定义。（被增强的方法）
    - **Advice(通知/增强)**:   所谓通知是指拦截到 Joinpoint 之后所要做的事情就是通知。   通知的类型：前置通知,后置通知,异常通知,最终通知,环绕通知。（给切入点增加功能的代码）
    - **Introduction(引介)**:   引介是一种特殊的通知在不修改类代码的前提下, Introduction 可以在运行期为类动态地添加一些方 法或 Field。 
    -  **Targe(目标对象)**:   代理的目标对象。 
    -  **Weaving(织入)**:   是指把增强应用到目标对象来创建新的代理对象的过程。   spring 采用动态代理织入，而 AspectJ 采用编译期织入和类装载期织入。
    -   **Proxy（代理）**:   一个类被 AOP 织入增强后，就产生一个结果代理类。
    -    **Aspect(切面)**:   是切入点和通知（引介）的结合。

## 3.AOP配置
### 3.1基于XML的AOP配置
1. 把通知Bean也交给spring来管理
```xml
<!-- 配置通知 --> 
 <bean id="txManager" class="com.itheima.utils.TransactionManager">  <property name="dbAssit" ref="dbAssit"></property> </bean> 
```
2. 使用`<aop:config>`标签表明开始AOP的配置
3. 使用`<aop:aspect>`标签表明配置切面
      - id属性：是给切面提供一个唯一标识
      - ref属性：是指定通知类bean的Id。
4. 在`<aop:aspect>`标签的内部使用对应标签来配置通知的类型
               ``<aop:before>``：表示配置前置通知
    - method属性：用于指定Logger类中哪个方法是前置通知
    - pointcut属性：用于指定切入点表达式，该表达式的含义指的是对业务层中哪些方法增强，【需要在pom文件中导入aspectJweaver依赖，用于解析切点表达式内容】
    - order属性：指定多个切面的执行顺序
5. 可以配置使用 `<aop:pointcut>` 配置切入点表达式 

#### 3.1.1`<aop:aspect>`标签中的通知的标签类型
- 前置通知`<aop:before>`
- 后置通知`<aop:after-returning >`
- 异常通知`<aop:after-throwing>`
- 最终通知`<aop:after>`
- 环绕通知`<aop:around>`
   - 作用：   用于配置环绕通知  
   - 属性：   method：指定通知中方法的名称。   
      - pointct：定义切入点表达式   
      - pointcut-ref：指定切入点表达式的引用  

      说明：   它是 spring 框架为我们提供的一种可以在代码中手动控制增强代码什么时候执行的方式。  

      Spring框架为我们提供了一个接口：**ProceedingJoinPoint**。该接口有一个方法**proceed()**，此方法就相当于明确调用切入点方法。该接口可以作为环绕通知的方法参数，在程序执行时，spring框架会为我们提供该接口的实现类供我们使用。**getArgs()**:获取要执行目标方法参数
      spring中的环绕通知：
      它是spring框架为我们提供的一种可以在代码中手动控制增强方法何时执行的方式。

 **【注意】**：   通常情况下，环绕通知都是独立使用的，环绕通知必须要有返回值
 **xml配置实现环绕通知**：
 ```xml
 <!-- 配置事务管理器-->
    <bean id="transactionManager" class="com.itheima.utils.TransactionManager">
        <property name="cu" ref="connectionUtils"></property>
    </bean>
    <!--使用aop配置实现事务管理-->
    <aop:config>
        <!--定义切入点表达式-->
        <aop:pointcut id="pr1" expression="execution(* com.itheima.service.impl.*.*(..))"></aop:pointcut>
        <!--配置切面-->
        <aop:aspect id="transationByAOP" ref="transactionManager">
            <!--配置环绕通知-->
            <aop:around method="aroundConf" pointcut-ref="pr1"></aop:around>
        </aop:aspect>
    </aop:config>
```
```java
  /**
     * 环绕通知
     *
     * @param pjp ProceedingJoinPoint为spring定义的接口，可用于执行目标方法
     * @return 必须要有返回值
     */
    public Object aroundConf(ProceedingJoinPoint pjp) {
        Object proceed = null;
        //前置通知
        beginTransaction();
        //获取目标方法参数
        Object[] args = pjp.getArgs();
        //执行目标方法
        try {
            proceed = pjp.proceed(args);
            //后置通知
            commit();

        } catch (Throwable throwable) {
            //异常通知
            rollback();
            throwable.printStackTrace();
        } finally {
            //最终通知
            release();
        }
        return proceed;
    }
```
**注解配置实现环绕通知**：
```java
@Component
@Aspect
public class TransactionManager {

    @Pointcut("execution(* com.itheima.service.impl.*.*(..))")
    public void pr1(){};




    //获取数据库连接工具类对象
    @Autowired
    private ConnectionUtils cu;

    /**
     * 开启事务
     */
    //@Before("pr1()")
    public void beginTransaction(){
        try {
            cu.getThreadConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    //@AfterReturning("pr1()")
    public void commit(){
        try {
            cu.getThreadConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 回滚
     */
    //@AfterThrowing("pr1()")
    public void rollback(){
        try {
            cu.getThreadConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * 归还连接
     */
//    @After("pr1()")
    public void release(){
        try {
            cu.getThreadConnection().close();
            cu.removeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 环绕通知
     *
     * @param pjp ProceedingJoinPoint为spring定义的接口，可用于执行目标方法
     * @return 必须要有返回值
     */
    @Around("pr1()")
    public Object aroundConf(ProceedingJoinPoint pjp) {
        Object proceed = null;
        //前置通知
        beginTransaction();
        //获取目标方法参数
        Object[] args = pjp.getArgs();
        //执行目标方法
        try {
            proceed = pjp.proceed(args);
            //后置通知
            commit();

        } catch (Throwable throwable) {
            //异常通知
            rollback();
            throwable.printStackTrace();
        } finally {
            //最终通知
            release();
        }
        return proceed;
    }
```

#### 3.1.2切入点表达式的写法
**关键字**：execution(表达式)
**表达式**：
                    访问修饰符  返回值  包名.包名.包名...类名.方法名(参数列表)
   **标准的表达式写法**：
                    public void com.itheima.service.impl.AccountServiceImpl.saveAccount()
                访问修饰符可以省略
                    void com.itheima.service.impl.AccountServiceImpl.saveAccount()
                返回值可以使用通配符，表示任意返回值
                    ``* com.itheima.service.impl.AccountServiceImpl.saveAccount()``
                包名可以使用通配符，表示任意包。但是有几级包，就需要写几个*.
                   `` * *.*.*.*.AccountServiceImpl.saveAccount())``
                包名可以使用..表示当前包及其子包
                   `` * *..AccountServiceImpl.saveAccount()``
                类名和方法名都可以使用*来实现通配
                   `` * *..*.*()``
**参数列表**：
                    可以直接写数据类型：
                        基本类型直接写名称           int
                        引用类型写包名.类名的方式   java.lang.String
                    可以使用通配符表示任意类型，但是必须有参数
                    可以使用..表示有无参数均可，有参数可以是任意类型
                全通配写法：
                   `` * *..*.*(..)``
**实际开发中切入点表达式的通常写法**：
                    切到业务层实现类下的所有方法
                        ``* com.itheima.service.impl.*.*(..)``
#### 3.1.3 配置切入点表达式
**作用范围**：
- 作用在`<aop:config>`标签下：每个切面都能引用该表达式
    - 【注意】：在此标签下运用时，必须写在最上面
- 作用在`<aop:aspect>`标签下：当前切面可以引用该表达式


 **作用**：   用于配置切入点表达式。就是指定对哪些类的哪些方法进行增强。
 **属性**：   
 - expression：用于定义切入点表达式。  
 -  id：用于给切入点表达式提供一个唯一标识 
```xml
 <aop:pointcut expression="execution(  public void com.itheima.service.impl.AccountServiceImpl.transfer(    java.lang.String, java.lang.String, java.lang.Float) )" id="pt1"/>
```
#### 3.1.4 xml配置示例
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">
  
    <!-- 配置事务管理器-->
    <bean id="transactionManager" class="com.itheima.utils.TransactionManager">
        <property name="cu" ref="connectionUtils"></property>
    </bean>


    <!--使用aop配置实现事务管理-->
    <aop:config>
        <!--定义切入点表达式-->
        <aop:pointcut id="pr1" expression="execution(* com.itheima.service.impl.*.*(..))"></aop:pointcut>
        <!--配置切面-->
        <aop:aspect id="transationByAOP" ref="transactionManager">
            <!-- 前置通知-->
            <aop:before method="beginTransaction" pointcut-ref="pr1"></aop:before>
          <!-- 后置通知-->
            <aop:after-returning method="commit" pointcut-ref="pr1"></aop:after-returning>
          <!-- 异常通知-->
            <aop:after-throwing method="rollback" pointcut-ref="pr1"></aop:after-throwing>
          <!-- 最终通知-->
            <aop:after method="release" pointcut-ref="pr1"></aop:after>
        </aop:aspect>
    </aop:config>
```

### 3.2基于注解的AOP配置
#### 3.2.1 新增注解
**`@Aspect`**：表明当前类是一个切面类
**`@Before`**：
- 作用：   把当前方法看成是前置通知。  
- 属性：   value：用于指定切入点表达式，还可以指定切入点表达式的引用。 

**`@AfterReturning`**：
- 作用：   把当前方法看成是后置通知。

**`@AfterThrowing`**：
- 作用：   把当前方法看成是异常通知。

**`@After`**：
- 作用：   把当前方法看成是最终通知。

**`@Around`**：
- 作用：   把当前方法看成是环绕通知。

**`@Pointcut`**：
- 作用：  指定切入点表达式 
- 属性： value：指定表达式的内容 
- 示例：
``@Pointcut("execution(* com.itheima.service.impl.*.*(..))")`

**``@EnableAspectJAutoProxy``**:不使用xml，让spring能解析aop注解

##### 基于全注解的配置前提：
1. pom文件中导入aspectJweaver依赖
2. 在主配置文件上加上@EnableAspectJAutoProxy注解（让spring解析aop注解）

## 使用全注解配置的注意事项：
1. 注解配置对事物进行控制时有bug，即除前置通知外，其他通知顺序会乱序。这种情况可以使用环绕通知解决
2. 引入切点表达式时，需要写方法名加括号
**主配置文件**
```java
/**
 * 使用全注解配置控制事务
 */

@Configuration  // 通知spring类为配置类
@ComponentScan("com.itheima")  //提示spring要扫描的包
@PropertySource("classpath:jdbcConfig.properties")  //引入外部配置文件
@Import(DataSourceConfig.class)  //导入子配置文件
@EnableAspectJAutoProxy  //让spring可以使用标签配置
public class AnnotationConfig {
}
```
**子配置文件**
```java
public class DataSourceConfig {

    @Value("${jdbc.driver}")
    private String driver;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;


    //配置queryRunner
    @Bean
    public QueryRunner getQueryRunner() {
        return new QueryRunner();
    }


    //配置数据源
    @Bean
    public DataSource getDataSource() {
        ComboPooledDataSource cds = new ComboPooledDataSource();
        try {
            cds.setDriverClass(driver);
            cds.setJdbcUrl(url);
            cds.setUser(username);
            cds.setPassword(password);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return cds;
    }

    //配置ThreadLocal
    @Bean
    public ThreadLocal getThreadLocal(){
        return new ThreadLocal();
    }
}
```
**切面类**
```java
@Component
@Aspect
public class TransactionManager {

    @Pointcut("execution(* com.itheima.service.impl.*.*(..))")
    public void pr1(){};

    //获取数据库连接工具类对象
    @Autowired
    private ConnectionUtils cu;

    /**
     * 开启事务
     */
    //@Before("pr1()")
    public void beginTransaction(){
        try {
            cu.getThreadConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    //@AfterReturning("pr1()")
    public void commit(){
        try {
            cu.getThreadConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 回滚
     */
    //@AfterThrowing("pr1()")
    public void rollback(){
        try {
            cu.getThreadConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * 归还连接
     */
//    @After("pr1()")
    public void release(){
        try {
            cu.getThreadConnection().close();
            cu.removeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 环绕通知
     *
     * @param pjp ProceedingJoinPoint为spring定义的接口，可用于执行目标方法
     * @return 必须要有返回值
     */
    @Around("pr1()")
    public Object aroundConf(ProceedingJoinPoint pjp) {
        Object proceed = null;
        //前置通知
        beginTransaction();
        //获取目标方法参数
        Object[] args = pjp.getArgs();
        //执行目标方法
        try {
            proceed = pjp.proceed(args);
            //后置通知
            commit();

        } catch (Throwable throwable) {
            //异常通知
            rollback();
            throwable.printStackTrace();
        } finally {
            //最终通知
            release();
        }
        return proceed;
    }

```


# spring第四天
## 1. spring中配置数据源 
### 1.1配置 C3P0 数据
```xml
导入c3p0-0.9.2.1.jar 到工程的 lib 目录。在 spring 的配置文件中配置： 
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource"> 
 <property name="driverClass" value="com.mysql.jdbc.Driver"></property>  <property name="jdbcUrl" value="jdbc:mysql:///spring_day02"></property> 
 <property name="user" value="root"></property>  
  <property name="password" value="1234"></property> 
</bean>
```
### 1.2 配置 DBCP 数据源 
```xml
导入commons-dbcp.jar和commons-pool.jar 到工程的 lib 目录。在 spring 的配置文件中配置： 
<!-- 配置数据源 --> 
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource">  <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>  <property name="url" value="jdbc:mysql:// /spring_day02"></property>  <property name="username" value="root"></property>  <property name="password" value="1234"></property> 
</bean>
```
### 1.3  配置 spring 内置数据源 
```xml
spring 框架也提供了一个内置数据源，我们也可以使用 spring 的内置数据源，它就在 spring-jdbc-5.0.2.REEASE.jar 包中： 
<bean id="dataSource" 
class="org.springframework.jdbc.datasource.DriverManagerDataSource">  <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>  <property name="url" value="jdbc:mysql:///spring_day02"></property>  <property name="username" value="root"></property>  <property name="password" value="1234"></property> 
</bean>
```
## 2.使用spring内置事务管理器配置事务
### 2.1 第一步：引入标签坐标
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans 
       xmlns="http://www.springframework.org/schema/beans"      
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"      
       xmlns:aop="http://www.springframework.org/schema/aop"     
       xmlns:tx="http://www.springframework.org/schema/tx"      
       xsi:schemaLocation="http://www.springframework.org/schema/beans         
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/tx         
                           http://www.springframework.org/schema/tx/spring-tx.xsd           
                           http://www.springframework.org/schema/aop
                           http://www.springframework.org/schema/aop/spring-aop.xsd"> 
 </beans>
```
### 2.2 第二步：配置事务管理器（用来管理事务）
```xml
<!-- 配置一个事务管理器 --> 
<bean id="transactionManager" 
class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
 <!-- 注入 DataSource -->  
  <property name="dataSource" ref="dataSource"></property>
</bean>
```
### 2.3 第三步：配置事务的通知引用事务管理器(用来配置事务管理器的属性)
```xml
<!-- 事务的配置 --> 
<tx:advice id="txAdvice" transaction-manager="transactionManager"> </tx:advice> 
```
### 2.4 第四步：配置事务的属性 
```xml
<!-- 事务的配置 --> 
<tx:advice id="txAdvice" transaction-manager="transactionManager"> 
<!--在 tx:advice 标签内部 配置事务的属性 --> 
  <tx:attributes> 
<!-- 指定方法名称：是业务核心方法   
read-only：是否是只读事务。默认 false，不只读。  
isolation：指定事务的隔离级别。默认值是使用数据库的默认隔离级别。   
propagation：指定事务的传播行为。  
  - REQUIRED：必须的
  - SUPPORTS：可选的
timeout：指定超时时间。默认值为：-1。永不超时。  
rollback-for：用于指定一个异常，当执行产生该异常时，事务回滚。产生其他异常，事务不回滚。 没有默认值，任何异常都回滚。  
no-rollback-for：用于指定一个异常，当产生该异常时，事务不回滚，产生其他异常时，事务回 滚。没有默认值，任何异常都回滚。  -->  
    <!--增删改-->
    <tx:method name="*" read-only="false" propagation="REQUIRED"/>  
    <!--查询-->
    <tx:method name="find*" read-only="true" propagation="SUPPORTS"/> 
  </tx:attributes>
</tx:advice> 
```
### 2.5 第五步：配置 AOP 切入点表达式 
```xml
<!-- 配置 aop --> 
<aop:config> 
 <!-- 配置切入点表达式 -->  
  <aop:pointcut expression="execution(* com.itheima.service.impl.*.*(..))" id="pt1"/> 
</aop:config>
```
### 2.6 第六步：配置切入点表达式和事务通知的对应关系（引用修改好的事务管理器通知，让spring控制通知调用的顺序） 
```xml
<!-- 配置 aop --> 
<aop:config> 
 <!-- 配置切入点表达式 -->  
  <aop:pointcut expression="execution(* com.itheima.service.impl.*.*(..))" id="pt1"/> 
<!-- 在 aop:config标签内部：建立事务的通知和切入点表达式的关系 -->
  <aop:advisor advice-ref="txAdvice" pointcut-ref="pt1"/>
</aop:config>
```
### 2.7 xml配置结合注解注意
- 开启spring对aop注解支持
```xml
<aop:aspectj-autoproxy></aop:aspectj-autoproxy>
```
- 开启 spring对注解事务的支持
```xml
<tx:annotation-driven transcation-manager="transactionManager"/>
```
### 2.8 使用全注解配置事务
#### 2.8.1 不使用 xml的配置方式
```java
@Configuration 
@EnableTransactionManagement  //通知spring启用注解控制事务
public class SpringTxConfiguration { 
 //里面配置数据源，配置 JdbcTemplate,配置事务管理器。在之前的步骤已经写过了。 } 
```
#### 2.8.2 配置service层启用事务
```java
@Transactional(readOnly = false,propagation = Propagation.REQUIRED)
public class AccountServiceImpl implements AccountService {


    private AccountDao ad;

    public void setAd(AccountDao ad) {
        this.ad = ad;
    }

    public void transfer(String sendPerson, String getPerson, Double money) {
        Account sendAccount = ad.findAcocountByName(sendPerson);
        Account getAccount = ad.findAcocountByName(getPerson);

        sendAccount.setMoney((float) (sendAccount.getMoney() - money));
        getAccount.setMoney((float) (getAccount.getMoney() + money));

        ad.updateAccountByName(sendAccount);

        int i = 1 / 0;

        ad.updateAccountByName(getAccount);
    }
}
```
#### 2.8.3 @Transational注解的用法

    该注解的属性和 xml 中的属性含义一致。该注解可以出现在接口上，类上和方法上。 出现接口上，表示该接口的所有实现类都有事务支持。 出现在类上，表示类中所有方法有事务支持 出现在方法上，表示方法有事务支持。 以上三个位置的优先级：方法>类>接口

