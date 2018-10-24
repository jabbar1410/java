# fastDFS文件上传

## 结合springmvc使用步骤：

### 1.导入依赖

```xml
<!-- 文件上传组件 -->
        <dependency>
            <groupId>org.csource.fastdfs</groupId>
            <artifactId>fastdfs</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
        </dependency>	
```

### 2.添加配置文件fdfs_client.conf ，设置其中的服务器地址

### 3.入门demo

```java
  // 1、加载配置文件，配置文件中的内容就是 tracker 服务的地址。
		ClientGlobal.init("D:/maven_work/fastDFS-demo/src/fdfs_client.conf");
		// 2、创建一个 TrackerClient 对象。直接 new 一个。
		TrackerClient trackerClient = new TrackerClient();
		// 3、使用 TrackerClient 对象创建连接，获得一个 TrackerServer 对象。
		TrackerServer trackerServer = trackerClient.getConnection();
		// 4、创建一个 StorageServer 的引用，值为 null
		StorageServer storageServer = null;
		// 5、创建一个 StorageClient 对象，需要两个参数 TrackerServer 对象、StorageServer 的引用
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		// 6、使用 StorageClient 对象上传图片。
		//扩展名不带“.”
		String[] strings = storageClient.upload_file("D:/pic/benchi.jpg", "jpg",
				null);
		// 7、返回数组。包含组名和图片的路径。
		for (String string : strings) {
			System.out.println(string);
		}

//运行结果：
 		group1
		M00/00/00/wKgZhVkMP4KAZEy-AAA-tCf93Fo973.jpg
		
//浏览上传的东西
在浏览器输入：
http://192.168.25.133/group1/M00/00/00/wKgZhVkMP4KAZEy-AAA-tCf93Fo973.jpg

```

### 4.配置多媒体解析器，用于控制层封装文件数据

```xml
<!-- 配置多媒体解析器 -->
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
		<property name="defaultEncoding" value="UTF-8"></property>
		<!-- 设定文件上传的最大值5MB，5*1024*1024 -->
		<property name="maxUploadSize" value="5242880"></property>
</bean>

```



### 5.结合前端上传文件

#### 后台控制层

```java
package com.pinyougou.shop.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import entity.Result;
import util.FastDFSClient;
/**
 * 文件上传Controller
 * @author Administrator
 *
 */
@RestController
public class UploadController {
	
	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;//文件服务器地址，读取properties文件

	@RequestMapping("/upload")
	public Result upload( MultipartFile file){				
		//1、取文件的扩展名
		String originalFilename = file.getOriginalFilename();
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		try {
//2、创建一个 FastDFS 的客户端
			FastDFSClient fastDFSClient  
= new FastDFSClient("classpath:config/fdfs_client.conf");
			//3、执行上传处理
			String path = fastDFSClient.uploadFile(file.getBytes(), extName);
			//4、拼接返回的 url 和 ip 地址，拼装成完整的 url
			String url = FILE_SERVER_URL + path;			
			return new Result(true,url);			
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "上传失败");
		}		
	}	
}

```

#### 前台angularjs服务层

```javascript
//文件上传服务层
app.service("uploadService",function($http){
	this.uploadFile=function(){
		var formData=new FormData();
	    formData.append("file",file.files[0]);   
		return $http({
            method:'POST',
            url:"../upload.do",
            data: formData,
            headers: {'Content-Type':undefined},
            transformRequest: angular.identity
        });		
	}	
});

//anjularjs对于post和get请求默认的Content-Type header 是application/json。通过设置‘Content-Type’: undefined，这样浏览器会帮我们把Content-Type 设置为 multipart/form-data.

//通过设置 transformRequest: angular.identity ，anjularjs transformRequest function 将序列化我们的formdata object.


```

