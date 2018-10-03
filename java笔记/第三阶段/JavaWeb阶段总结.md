# JavaWeb阶段总结

[TOC]

## Tomcat

Tomcat的目录结构（重点掌握）
![tomcat](http://os4z8t7lb.bkt.clouddn.com/201710112316_298.png)

HTTP协议
GET和POST的区别:

* GET的提交的参数会显示到地址栏上,而POST不显示.
* GET往往是有大小限制的,而POST没有大小的限制.
* GET没有请求体,而POST有请求体.

补充：
HTTP协议未规定GET和POST的长度限制
GET的最大长度显示是因为浏览器和web服务器限制了URI的长度
不同的浏览器和WEB服务器，限制的最大长度不一样

HTTP请求响应的状态码

* 200 ：成功
* 302 ：重定向
* 304 ：查找本地缓存
* 404 ：资源不存在
* 500 ：服务器内部错误

## Servlet

### Servlet概念

Servlet就是一个运行在WEB服务器上的小的Java程序(当访问到指定的Servlet时有服务器找到指定Servlet并利用反射执行其Service方法),用来接收和响应从客户端发送过来的请求,通常使用HTTP协议; 从技术层面来说Servlet就是一套接口.

### Servlet生命周期

创建:用户第一次访问Servlet创建Servlet的实例, 只会被创建一次, 所以是单例
销毁:当项目从服务器中移除的时候，或者关闭服务器的时候
用户第一次访问Servlet的时候,服务器会创建一个Servlet的实例,那么Servlet中init方法就会执行.任何一次请求服务器都会创建一个新的线程访问Servlet中的service的方法.在service方法内部根据请求的方式的不同调用doXXX的方法.(get请求调用doGet,post请求调用doPost).当Servlet中服务器中移除掉,或者关闭服务器,Servlet的实例就会被销毁,那么destroy方法就会执行.

### Servlet配置

编写Servlet：
第一步：编写一个类实现Servelet接口
第二步：配置Servlet的访问路径（WEB-INF-->web.xml）
==Servlet的映射路径不能重复==

`url-pattern`配置方式共有三种:

1. 完全路径匹配 ：以 / 开始`<url-pattern>/ServletDemo1</url-pattern>`
1. 目录匹配 ：以 / 开始需要以 * 结束.`<url-pattern>/aaa/*</url-pattern>` 如 `http://127.0.0.1/day11/aaa/easfes`
1. 扩展名匹配 ：不能以 / 开始 以 * 开始的. `<url-pattern>*.action</url-pattern>` 如`http://127.0.0.1/day11/UserServlet.do`

==错误的写法 ：`/*.do`==

* 绝对路径:不需要找位置相对关系. 以`/`开始的.
  绝对路径中分为客户端路径和服务器端路径:
  * 客户端路径一定要加工程名.`/day09/ServletDemo6`
  * 服务器端路径不需要加工程名.`/ServletDemo6 （转发）`

```xml
<!-- 配置Servlet -->
<servlet>
  <!-- servlet-name和servlet-class顺序不能颠倒 -->
  <!-- Servlet的名称 -->
  <servlet-name>test1</servlet-name>
  <!-- Servlet的全路径 -->
  <servlet-class>com.itheima.a_servlet.ServletDemo1</servlet-class>
</servlet>
<!-- Servlet的映射 -->
<servlet-mapping>
  <!-- servlet-name和url-pattern顺序不能颠倒 -->
  <!-- Servlet的名称 -->
  <servlet-name>test1</servlet-name>
  <!-- Servlet的访问路径 -->
  <url-pattern>/ServletDemo1</url-pattern>
</servlet-mapping>
<!--访问的路径-->
<!--http://localhost:8080/工程名称/ServletDemo1-->
```

### Servlet执行流程

浏览器访问---> `<url-pattern>/ServletDemo1</url-pattern>`  --->匹配类路径`<servlet-class>xxx.ServletDemo1</servlet-class>` ----->服务器反射创建对应类的对象，调用方法执行 ------>服务器返回响应到浏览器

## ServletContext

* 用来获得全局初始化参数.
* 用来获得文件的MIME的类型.
* 作为域对象存取数据.(ServletContext是一个域对象)
  * 作用范围:整个web工程.
  * 创建:服务器启动的时候,tomcat服务器为每个web项目创建一个单独ServletContext对象.
  * 销毁:服务器关闭的时候,或者项目从服务器中移除的时候.
* 用来读取web项目下的文件
  默认在tomcat的项目根目录下（webapps----->项目）：
  `ServletContext.getResourceAsStream("/WEB-INF/classes/db.properties");`
  通过相对路径获得绝对路径
  `String realPath = context.getRealPath("/WEB-INF/classes/db.properties");`
  // 获得该文件的磁盘绝对路径.
  `InputStream is = new FileInputStream(realPath);`

## Request和Response

### Request接收参数

* `String getParameter(String name);` ---用于接收一个名称对应一个值的数据.
* `String[] getParameterValues(String name);` ---用于接收一个名称对应多个值的数据.
* `Map getParameterMap();` ---用于接收表单中的所有的数据,Map的key是表单提交的参数名称,Map的value是提交参数的值.
* `Enumeration getParameterNames();` ---用于获取表单中提交的所有的参数的名称.

### Request获取客户机信息

* `String getRequestURI()` --获取请求URI  /day10/xxxx
* `StringBuffer getRequestURL()` --获取请求的URL http://localhost:8080/day10/xxxx
* `String getRemoteAddr()` --获取请求者的IP地址
* `String getContextPath()` --获取项目名称  /day10

### Request实现转发

当使用了request域存储数据时还想做页面的跳转 就只能使用转发
`req.getRequestDispatcher("path").forward(request, response);`

### Response对象常用API

`resp.addCookie(cookie);` -- 添加Cookie
`resp.addHeader(name, value);` -- 设置响应头信息
`resp.setHeader(name, value);` -- 设置响应头信息
`resp.getWriter().print("");` -- 使用字符流向页面输出
`resp.sendRedirect(location);` -- 使用重定向做页面跳转

### 处理中文乱码

接收中文乱码
get方式  `new String(String.getBytes("ISO-8859-1"),"UTF-8");`    - （当使用tomcet7时需要)

post方式 `request.setCharacterEncoding(“UTF-8”)`    - (只有tomcat8 及以上可用)

输出中文乱码
字符流
`response.setContentType("text/html;charset=UTF-8");`
字节流
`response.setHeader("Content-Type", "text/html;charset=UTF-8");` -- 设置浏览器默认打开的时候采用的字符集编码
`response.getOutputStream().write("中文".getBytes("UTF-8"));`    -- 设置中文转成字节数组的时候取出的编码

## 转发和重定向

* 重定向的地址栏会发生变化,转发的地址栏不变
* 重定向两次请求两次响应,转发一次请求一次响应
* 重定向路径需要加工程名(及虚拟路径 ),转发的路径不需要加工程名
* 重定向可以跳转到任意网站,转发只能在服务器内部进行转发
* 重定向不能使用request域存储数据，转发可以使用request存储数据

## 域对象

域对象的主要作用是在各个页面或者Servlet之间进行数据的传递

### 域对象存取API

存入数据:`setAttribute(key,value)`
获取数据:`Object getAttribute(key)`
移除数据:`removeAttribute(key)`

### ServletContext域对象

作用范围:整个web工程.
创建:服务器启动的时候,tomcat服务器为每个web项目创建一个单独ServletContext对象.
销毁:服务器关闭的时候,或者项目从服务器中移除的时候.

### Request域对象

request的作用范围就是一次请求的范围(同一个请求)
创建:客户端向服务器发送了一次请求以后,服务器就会创建一个request的对象
销毁:当服务器对这次请求作出了响应之后

### Session域对象

session作用范围多次请求.(一次会话)
创建:服务器端第一次调用getSession()创建session.，以后调用getSession获得同一个
销毁:三种情况销毁session

1. session过期. 默认过期时间为30分钟.
    在Tomcat设置：所有的项目的session
    在某个项目中的web.xml设置：当前工程的所有session
    给某个session对象设置：对应的session对象
    `setMaxInactiveInterval(int interval)`
1. 非正常关闭服务器.如果正常关闭session序列化到硬盘.
1. 手动调用session.invalidate();

==浏览器关闭Session并未销毁,只是保存SessionID的Cookie销毁了(Cookie的默认是会话基本)==

## 会话技术

会话: 用户打开一个浏览器访问页面,访问网站的很多页面,访问完成后将浏览器关闭的过程称为是一次会话.
常见的会话技术:

* Cookie  :将数据保存到客户端浏览器.
* Session :将数据保存到服务器端.
* URL重写
* 隐藏表单域 hidden

### Cookie

#### Cookie常用API

* `void addCookie(Cookie cookie);` --向浏览器写回Cookie(HttpServletResponse)
* `Cookie[] getCookies();` -- 获得浏览器带过来的Cookie(HttpServletRequest)
* `Cookie(String name,String value);` -- 创建一个Cookie对象
* `getName();`  --获取Cookie的名称
* `getValue();`   --获取Cookie的值
* `setDomain(String domain);` -- 设置Cookie的有效域名.
* `setPath(String path);` -- 设置Cookie的有效路径.
* `setMaxAge(int maxAge);` -- 设置Cookie的有效时间.

#### Cookie路径问题

`http://localhost:8080/JavaEE10/Demo/CountServlet2` 这个路径下创建
`http://localhost:8080/JavaEE10/Demo/ServletDemo1`  可以获取
`http://localhost:8080/JavaEE10/Demo/AAA/ServletDemo2`  可以获取
`http://localhost:8080/JavaEE10/ServletDemo3`  不能获取

#### Cookie时间问题

如果不手动设置值它的默认值是-1,即为临时Cookie
如果想删除一个cookie的话就给它设置为0
如果给它设置为一个正数值,代表就是它的存活时间 `60 * 60 * 24 * 7`

#### Cookie的分类

* 会话级别的Cookie:默认的Cookie.关闭浏览器Cookie就会销毁.
* 持久级别的Cookie:可以设置Cookie的有效时间.那么关闭浏览器Cookie还会存在. 手动销毁持久性`Cookie.setMaxAge(0)`(【前提是有效路径必须一致】)

### Session

Session的创建
服务器端第一次调用`req.getSession()`创建session.以后调用`getSession()`获得的是同一个

Session的销毁

1. session过期. 默认过期时间为30分钟.
    在Tomcat设置：所有的项目的session
    在某个项目中的web.xml设置：当前工程的所有session
    给某个session对象设置：对应的session对象
    `setMaxInactiveInterval(int interval)`
1. 非正常关闭服务器.如果正常关闭session序列化到硬盘.
1. 手动调用session.invalidate();

### Cookie和Session的区别

* Cookie本身是有大小和个数的限制.大小一般不超过4KB, 个数的不超过20个,Session没有限制.
* Cookie的数据保存在客户端,Session数据保存在服务器端.
* Cookie只能存储String Session可以存储object
* Session是域对象 Cookie不是域对象
* Session是依赖于Cookie Cookie被禁止了，默认session也不能使用

## 过滤器

### 过滤器概念

过滤器是拦在请求和目标资源之间的一层网,我们可以在一次请求到达它的目标资源之前对这次请求进行拦截并进行处理
拦截到请求之后可以修改请求并决定是否要进行放行,也可以在请求返回时对期进行拦截并做处理

### 过滤器的写法

1. 编写java类实现Filter接口，并实现其doFilter方法。
1. 在 web.xml 文件中使用`<filter>`和`<filter-mapping>`元素对编写的filter类进行注册，并设置它所能拦截的资源。

* 编写一个类实现Filter接口:
  `doFilter(request,response,FilterChain)` 处理逻辑
* 配置过滤器:

  ```xml
  <!-- 配置过滤器 -->
  <filter>
     <filter-name>aaa</filter-name>
     <filter-class>com.itheima.filter.FilterDemo1</filter-class>
  </filter>
  <filter-mapping>
     <filter-name>aaa</filter-name>
     <!-- 指定这个过滤器要拦截的资源路径 -->
     <!-- 一个过滤器可以设置多个拦截路径 指定多个filter-mapping或者是指定多个url-pattern -->
     <url-pattern>/demo4/demo1.jsp</url-pattern>
  </filter-mapping>
  ```

### 过滤器的相关配置

`<url-pattern>`的配置：
完全路径匹配：以 / 开始  `/demo4/demo1.jsp`
目录匹配：以 / 开始 以 * 结束.  `/*`  `/demo1/*`
扩展名匹配：不能以 / 开始 以 * 开始.  `*.do`  `*.action`

`<servlet-name>`的配置:
根据Servlet的名称拦截Servlet

`<dispatcher>`的配置:

* REQUEST：当用户直接访问页面时，Web容器将会调用过滤器。如果目标资源是通过`RequestDispatcher`的`include()`或`forward()`方法访问时，那么该过滤器就不会被调用。(默认值)
* INCLUDE：如果目标资源是通过`RequestDispatcher的include()`方法访问时，那么该过滤器将被调用。除此之外，该过滤器不会被调用。只要是通过`<jsp:include page="xxx.jsp" />`，嵌入进来的页面，每嵌入的一个页面，都会走一次指定的过滤器
* FORWARD：如果目标资源是通过`RequestDispatcher`的`forward()`方法访问时，那么该过滤器将被调用，除此之外，该过滤器不会被调用。
* ERROR：如果目标资源是通过声明式异常处理机制调用时，那么该过滤器将被调用。除此之外，过滤器不会被调用。

## JSP&EL&JSTL

### JSP中的三个指令

`<%@page>`指令 用于设置当前JSP页面的属性及其导包
`<%@include>`指令 用于设置当前JSP页面包含其他JSP页面(静态包含)
`<%@taglib>`指令 用于给当前JSP页面导入标签库
静态包含和动态包含的区别`(<%@ include  file=""%>和<jsp:include>)`
静态包含：直接将源代码拷贝到目标文件中，一起翻译成`.java`编译运行
动态包含：每个文件单独翻译成`.java`单独编译，将编译后运行的结果进行包含.调用一个方法将结果包含

### JSP中有九大内置对象

* request
* response
* session
* application   ServletContext
* page  当前页面
* pageContext  可以获得其他8个内置对象
* config
* out    JspWriter
* exception

### JSP的四个域范围

* PageScope: 当前页面中有效.
    `pageContext PageContext`
* RequestScope: 一次请求范围.
    `request HttpServletRequest`
* SessionScope: 一次会话范围.
    `session HttpSession`
* ApplicationScope: 整个应用范围
    `application ServletContext`

### EL表达式获取域对象中的值

获取域对象 `${ applicationScope.name }`
访问数组元素 `${ arrs[下标] }`
访问集合 `${ list[下标] }` `${ map.key的值 }`
获取对象的属性 `${ user.id }`
获取对象的集合的数据 `${ userList[0].id }`

`.`和`[]`的区别
`[]`用于有下标的数据(数组,list集合) `.`用于有属性的数据(map,对象)
如果属性名中包含有特殊的字符.必须使用`[]`
如果EL表达式从四个域对象中没有取到值会返回`""`, 而不是`null`, 但属性名写错会报错，如 `${user.naaa}`
在书写表达式时，如果没有指定搜索范围，那么系统会依次调用`pageContext`、`request`、`session`、`application`的`getAttribute()`方法
这样不限定查找范围的代码不利于排错，所以这种取值的操作可以先定对象的查找范围,如：`${sessionScope.user.name}`
一旦指定了对象所在的范围，那么只会在范围内查找绑定对象，不会在找不到的时候再去其他区域中查找了

### EL表达式执行运算

1. EL执行算数运算 `${ n1 + n2 }`
1. EL执行比较运算 `${ n1 eq n2 }`
1. EL执行关系运算 `and  or    not`
1. EL执行三元运算 `${ n1 < n2 ? "正确":"错误" }`
1. 判断null `${ empty user }``${ not empty user }`
   空运算主要用于判断字符串，集合是否为空，是空或为null及找不到值时都会输出true

### EL表达式操作WEB开发的常用的对象

`pageScope,requestScope,sessionScope,applicationScope` - 获取JSP中域中的数据
`param,paramValues` - 接收参数.
`header,headerValues` - 获取请求头信息
`initParam` - 获取全局初始化参数
`cookie` - WEB开发中cookie
`pageContext` - WEB开发中的pageContext.

1. `${ param.id }===request.getPamameter(“id")`
1. 获取Cookie中的值`${ cookie.history.value }`
1. 调用API：`${ pageContext.request.remoteAddr }` `${ pageContext.request.contextPath }`

### JSTL标签

```jsp
 <c:if  test="${}">
   ...
 </c:if>
 <%-- c:if 没有else语句 --%>
```

forEach
加强`For: var="i"   items = “{被遍历的对象}"`
普通`for: var ="i"  begin="开始数据" end="结束数据"  step="步数" varstatus=""` 统计循环的个数
其中items属性为要遍历的集合，var属性为每次取出来的一个对象，varStatus指定当前迭代的状态

```jsp
<c:forEach items="${users}" var="u" varStatus="s">
    <tr>
    <td>${s.count}</td>
    <td>${u.name}</td>
    <td>${u.age}</td>
    </tr>
</c:forEach>
```

## MVC

MVC：
Model  模型层(Javabean)
View视图层(JSP,可分为物理视图和逻辑视图)
Controller 控制层(Servlet)
MVC是一种设计模式也是一种编程思想, 除了Java开发中其他平台也都会使用这种设计模式, 它不同于JavaEE的三层结构

## 反射常用API

```java
//获取Class对象的三种方法
// 第一种方式
Class clazz1 = Person.class;
// 第二种方式
Person person = new Person();
Class clazz2 = person.getClass();
// 第三种方式(最常用)
Class clazz3 = Class.forName("com.itheima.demo.Person");

// 获得类中的方法:包括私有方法
Method method = clazz.getDeclaredMethod("run");
// 获得方法执行权
method.setAccessible(true);
// 执行方法
method.invoke(clazz.newInstance());// p.run();

// 获得带有参数的方法:
Method method2 = clazz.getDeclaredMethod("sayHello", String.class);
String s = (String) method2.invoke(clazz.newInstance(), "凤姐"); // String s = p.sayHello("凤姐");
System.out.println(s);
```

## 事务

### 事务特性（ACID）

* 原子性：强调事务的不可分割.
* 一致性：强调的是事务的执行的前后，数据的完整性要保持一致.
* 隔离性：一个事务的执行不应该受到其他事务的干扰.
* 持久性：事务一旦结束(提交/回滚)数据就持久保持到了数据库.

### 事务隔离级别

* 脏读: 一个事务读到另一个事务还没有提交的数据.
* 不可重复读: 一个事务读到了另一个事务已经提交的update的数据,导致在当前的事务中多次查询结果不一致.
* 虚读/幻读: 一个事务读到另一个事务已经提交的insert的数据,导致在当前的事务中多次的查询结果不一致.

### 设置事务的隔离级别

* `read uncommitted`: 未提交读.脏读，不可重复读，虚读都可能发生.
* `read committed`: 已提交读.避免脏读.但是不可重复读和虚读有可能发生.(Oracle默认级别)
* `repeatable read`: 可重复读.避免脏读,不可重复读.但是虚读有可能发生.(MYSQL默认级别)
* `serializable`: 串行化的.避免脏读，不可重复读，虚读的发生.

### DBUtils管理事务

`conn = JDBCUtils.getConnection();` -- 获得连接
`conn.setAutoCommit(false);` -- 关闭MySQL的自动提交
`QueryRunner queryRunner = new QueryRunner();`
`queryRunner.update(conn, sql, money,to);` -- 使用上层传递过来的连接
`queryRunner.query.(conn…….)` -- 使用上层传递过来的连接
`DbUtils.commitAndCloseQuietly(conn);` -- 提交事务并关闭连接
`DbUtils.rollbackAndCloseQuietly(conn);` -- 回滚事务并关闭连接

## JSON

中括号代表数组, 大括号代表对象,只能出现的字符有`[] {} "" , :`

```json
{"id":1,"name":aaa}
[{"id":1,"name":aaa},{"id":2,"name":bbb}]
{"city":{"cid":1,"cname":"xxx"}}
{
    code: 101,
    msg: 成功,
    content: [
        { key:value,key:value...},
        { key:value,key:value...},
        { key:value,key:value...},
        { key:value,key:value...}
        ....
    ]
}
```

```java
// 将List集合转成JSON:
JsonConfig config = new JsonConfig();
// 在转换时不包括pid字段
config.setExcludes(new String[]{"pid"});
JSONArray jsonArray = JSONArray.fromObject(list,config);
//JSONObject.fromObject(object)
response.getWriter().println(jsonArray.toString());//{xxx:xxx}
```

```js
$.post("/day15/ServletDemo8",{"pid":pid},function(data){
    var $city = $("#city");
    $city.html("<option>-请选择-</option>");
    $(data).each(function(i,n){
        $city.append("<option value='"+n.cid+"'>"+n.cname+"</option>");
    });
},"json");
```

## Ajax

```js
// Data就是Servlet中response.getWriter().println(1);输出的内容
// $.post 和 $.get写法上一样,具体选择的时候可以根据参数的长度和参数安全判断,一般都会使用$.post
// $.ajax则和上面两个不一样,使用$.ajax可以自己手动设置请求方式(同步/异步),根据不同的返回做出不同的显示
$(function(){
    $("#username").blur(function(){
        // 获得文本框的值:
        var username = $(this).val();
        // 演示load方法:
        // $("#s1").load("/day15/ServletDemo3",{"username":username});
        // 演示get/post方法:
        $.post("/day15/ServletDemo3",{"username":username},function(data){
            if(data == 1){
                $("#s1").html("<font color='green'>用户名可以使用</font>");
            }else if(data == 2){
                $("#s1").html("<font color='red'>用户名已经存在</font>");
            }
        });
    });
});
```

## 分页

* 物理分页:一次只查10条记录,点击下一页,再去查询后10条.使用SQL语句进行控制的分页.
  * 缺点:经常需要和数据库交互.
  * 优点:数据量特别大,不会导致内存的溢出.
* 逻辑分页:一次性将所有数据全都查询出来,根据需要进行截取.List集合进行控制. subList();
  * 缺点:数据量如果特别大,容易导致内存溢出.
  * 优点:与数据库交互次数少.

MYSQL进行分页: 使用`limit`关键字.
`select * from xxx where .. Group by ... Having ... Order by ... limit a,b;` -- a:从哪开始  b:查询的记录数.

封装PageBean
计算总页数，开始记录数，每页记录数，当前页码，数据

## 动态代理

动态代理技术就是用来产生一个对象的代理对象的
代理对象存在的价值主要用于拦截对真实业务对象的访问
代理对象应该具有和目标对象(真实业务对象)相同的方法
现在要生成某一个对象的代理对象，这个代理对象通常也要编写一个类来生成,通过`Proxy`类提供的一个`newProxyInstance`方法用来创建一个对象的代理对象
`static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)`
`newProxyInstance`方法用来返回一个代理对象，这个方法总共有3个参数
`ClassLoader loader` 用来指明生成代理对象使用哪个类装载器
`Class<?>[] interfaces` 用来指明生成哪个对象的代理对象，通过接口指定
`InvocationHandler h` 用来指明产生的这个代理对象要做什么事情
所以我们只需要调用`newProxyInstance`方法就可以得到某一个对象的代理对象了
在java中规定，要想产生一个对象的代理对象，那么==这个对象必须要有一个接口==,所以只有实现了接口的类才可以创建他的代理对象
Proxy类负责创建代理对象时，如果指定了handler（处理器），那么不管用户调用代理对象的什么方法，该方法都是调用处理器的invoke方法
由于invoke方法被调用需要三个参数：代理对象、方法、方法的参数，因此不管代理对象哪个方法调用处理器的invoke方法，都必须把自己所在的对象、自己（调用invoke方法的方法）、方法的参数传递进来。
因此在动态代理技术里，由于不管用户调用代理对象的什么方法，都是调用开发人员编写的处理器的invoke方法（这相当于invoke方法拦截到了代理对象的方法调用）

动态代理和装饰者模式
相同点 : 都能增强一个类的方法
不同的 : 如果是装饰者我们得自己写一个装饰者类  实现被装饰者的接口
        如果是动态代理  我们直接使用Proxy对象  代理对象
其实动态代理解决了装饰者模式的最大缺点就是你不用把接口中所有的方法都重新调用一遍
不管是装饰者还是动态代理都会有一个新的类产生  装饰者类  Proxy对象  是在原来的被增强的类基础上进行增强

```java
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    final HttpServletRequest req = (HttpServletRequest) request;
    // 增强req:
    HttpServletRequest myReq = (HttpServletRequest) Proxy.newProxyInstance(req.getClass().getClassLoader(),
            req.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    // 判断执行的方法是否是getParameter:
                    if("getParameter".equals(method.getName())){
                        // 调用的是getParameter:需要增强这个方法.
                        // 判断请求方式是GET还是POST:
                        String type = req.getMethod();
                        if("get".equalsIgnoreCase(type)){
                            String value = (String) method.invoke(req, args);
                            value = new String(value.getBytes("ISO-8859-1"),"UTF-8");
                            return value;
                        }else if("post".equalsIgnoreCase(type)){
                            req.setCharacterEncoding("UTF-8");
                        }
                    }
                    return method.invoke(req, args);
                }
            });
    chain.doFilter(myReq, response);
}
```

10:D
20:A
25:A
26:D
43:CD
45:ABC