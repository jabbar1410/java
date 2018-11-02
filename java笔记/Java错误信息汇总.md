### A
```java
1.org.springframework.security.access.AccessDeniedException: Access is denied
	解决方法：可能实在spring-security配置文件中，配置了<!-- 配置加密的方式
            <security:password-encoder ref="passwordEncoder"/>-->，将其注释掉可以解决
```

### R

```java
java.lang.RuntimeException: org.springframework.data.solr.UncategorizedSolrException: missing content stream; nested exception is org.apache.solr.client.solrj.impl.HttpSolrServer$RemoteSolrException: missing content stream
	原因：我在项目中出现此问题，就是因为没有将对象加入List中。如果你也提示这个异常，请仔细检查代码，是不是提交数据到solr时，忘记加入包装了。
```



### I

```java
1.java.lang.IllegalAccessException: class reflect2.Demo cannot access a member of class        		reflect2.Student with modifiers "private"
    解决方法：通过setAccessible（true）暴力反射
2.java.lang.IllegalArgumentException: wrong number of arguments
	解决方法：反射中调用getConstructor（Class）创建有参构造时需要传递参数类型的字节码对象
```

### M

```java
1.MySQLSyntaxErrorException: Table 'user' already exists
```

### N

```java
1.java.lang.NoSuchMethodException: reflect2.Student.<init>(java.lang.String, java.lang.Integer)
    解决方法：获取参数类型的字节码对象时，要使用包装类类型
    
2.java.lang.NoSuchMethodException: cn.itcast.travel.web.servlet.UserServlet.add(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletRequest)
    问题原因：在优化servlet时，通过反射获取方法时，方法权限不是public，
    解决方法：
    	1.通过getDeclaredMethod()获取方法，并通过暴力反射method.setAccessible(true)执行方法
    	2.将方法权限修改为public  【推荐使用】		
```

### J

```
1.java.lang.IllegalStateException: ContainerBase.addChild: start: org.apache.catalina.LifecycleException: Failed to start component 
	解决方法：selvet中的路径格式写错了。
```



### WEB

```
web错误500：       解决方法：1.项目中WEB-INF包要放在web包下
						 2.web项目中，web包下的lib包名必须是lib，改成别的名字会读取不到
```

