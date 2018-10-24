AngularJs基础操作

## 1.常用指令

### **ng-app:**开启表达式，指定模块

```html
<body ng-app="模块名"></body>
```
###  **ng-model:**参数绑定

  ```html
  <intput ng-model="name"></body>
  ```

### **ng-init：**初始化值

  ```html
  <body ng-init="name='zhangsan'"></body>
  ```

### **ng-controller：**绑定控制器

  ```html
  <body ng-controller="控制器名"></body>
  ```

### **ng-repeat：**用于遍历

  ```html
  <!--基本类型引用-->
  <tr ng-repeat="x in list">
      <td>{{x}}</td>
  </tr>
  <!--对象类型引用-->
  <tr ng-repeat="x in list">
      <td>{{x.属性名}}</td>
  </tr>
  ```

### ng-if:  用于判断表达式

```html
<!--如果表达式成立则显示-->
<span ng-if="grand != 3">
    <button type="button" class="btn bg-olive btn-xs" ng-click="changeGrand(grand+1);selectByGrand(entity)">查询下级</button>
</span>
```

### ng-options: 用于下拉列表

```html
<select class="form-control" ng-model="entity.goods.category1Id" 
        ng-options="item.id as item.name for item in itemCat1List">
    		<!--下拉列表value值  文本内容      元素       元素集合 -->
```



## 2.基础操作

###  **创建模块：**

  ```html
  <script>
      //参数1：模块名称   参数2：引用其它模块
      var app = angular.module("mymodule",[]);
  </script>

  <!--引用模块-->
  <body ng-app="mymodule">
  </body>
  ```




###  **创建服务：**

```html
<script>
	//构建服务
	// 参数1：服务名称  参数2：服务方法
	app.service("brandService", function($http) {
		//需要使用this调用，创建各类方法
		this.findBrandList = function() {
			return $http.get("../brand/findByPage.do?pageNum=" + pageNum
					+ "&pageSize=" + pageSize);
		}
    }
                
                
     //控制层调用服务                                          //引用服务
     app.controller("brandController", function($scope, $http, brandService) {

		$scope.findBrandList = function(pageNum, pageSize) {
            //调用服务中的方法
			brandService.findBrandList().success(function(pageResult) {
				$scope.brandList = pageResult.rows;
				//修改总记录数
				$scope.paginationConf.totalItems = pageResult.total;
			})
		}
    }
</script>
```



### **创建控制器**：

  ```html
  <script>
      //参数1：模块名称   参数2：引用其它模块
      var app = angular.module("mymodule",[]);
      //创建控制器
      app.controller("mycontroller",function($scope){
          /*
          	$scope: 控制器与页面数据交互的桥梁
          */
          //定义方法变量：
          $scope.add=function(){
              .....
          }
          //定义变量
          $scope.num='sdfa';
          
      })
  </script>

  <!--引用模块  引入控制器-->
  <body ng-app="mymodule" ng-controller="mycontroller">
  </body>
  ```

###  **发送ajax异步请求**

```html
<script>
	var app = angular.module("mymodule", []);

	app.controller("mycontroller", function($scope, $http) {
		$scope.x = 100;

		//$http 为控制器内置对象，用于向后端发送ajax请求
		//定义一个方法用于发送异步请求
		$scope.findList = function() {
			//通过$http内置对象发送异步请求
			/*
				get（请求地址）
				success(function(响应数据){
					
				})
			 */
			$http.get("data.json").success(function(response) {
				//封装相应数据
				$scope.list = response;
			})
		}

	})
</script>

<!--初始化引用方法-->
<body ng-init="findList()">
    
</body>
```

### **获得源对象，操作复选框：**

  ```html
  <script>
  //定义一个集合用于存储被选中的复选框id
  		$scope.searchIds = [];

  		//获取选中复选框的id
  		$scope.getSearchIds = function($event, id) {
  			//$event 为标签源对象，通过target方法可以获得当前源对象所在的标签对象
  			if ($event.target.checked) { //如果被选中
  				$scope.searchIds.push(id); //通过push方法，将id添加进集合
  			} else { //如果没有被选中
  				//找到对应id值在集合中的索引
  				var index = $scope.searchIds.indexOf(id);
  				//通过索引在集合中删除该id,参数1：起始索引， 参数2：删除个数
  				$scope.searchIds.splice(index, 1);
  			}

  		}
  </script>
  <!--调用方法-->
  <input type="checkbox" og-click="getSearchIds($event, id)">
  ```

### **获取ng-repeat下集合的索引：**

```html
<tr ng-repeat="entity in list">
    <td>
        //通过$index获得当前行索引
		<button type="button" class="btn btn-default" title="删除" ng-click="method($index)">
            <i class="fa fa-trash-o"></i> 删除
        </button>
    </td>
</tr>
```



### **分页操作**

```html
1.第一步导入js和css：
    <!-- 分页组件开始 -->
    <script src="../plugins/angularjs/pagination.js"></script>
    <link rel="stylesheet" href="../plugins/angularjs/pagination.css">
    <!-- 分页组件结束 -->

2.在自己的模块中引入分页模块
	<script>
		var app = angular.module("pyg", [ 'pagination' ]);
    </script>

3.第二步在需要添加页码的地方添加分页组件：
	<tm-pagination conf="paginationConf"></tm-pagination>

4.配置分页属性
	<script>
        	//分页配置
		$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 10,
			perPageOptions : [ 5, 10, 15, 20, 25 ],
			//当页码发生变化时，执行该方法,加载页面时也会触发该方法
			onChange : function() {
				$scope.reloadList();//重新加载
			}

		}
	</script>
```

### **控制器的继承**

```javascript
//1.创建要主控制器    
app.controller("baseController",function($scope){}
//2.在子控制器中绑定主控制器
app.controller("brandController", function($scope, $http, $controller,brandService) {
	// 控制器伪继承
	//参数1：要继承的控制器，  参数2：绑定两个控制器中间的$scope
	$controller("baseController", {$scope : $scope});
}
//3.也需要在html页面引入主控制器
<script src="../js/controller/brandController.js"></script>
<script src="../js/controller/baseController.js"></script>
```

### select2下拉框的使用

```javascript
（1）修改 type_template.html  引入JS

   <link rel="stylesheet" href="../plugins/select2/select2.css" />
    <link rel="stylesheet" href="../plugins/select2/select2-bootstrap.css" />
    <script src="../plugins/select2/select2.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="../js/angular-select2.js">  </script>


（2）修改typeTemplateController.js  ，定义品牌列表数据

$scope.brandList={data:[{id:1,text:'联想'},{id:2,text:'华为'},{id:3,text:'小米'}]};

（3）在type_template.html 用select2组件实现多选下拉框
<input select2  select2-model="entity.brandIds" config="brandList" multiple placeholder="选择品牌（可多选）" class="form-control" type="text"/>
    	select2  表示使用select2下拉框
   		 multiple 表示可多选
		Config  用于配置数据来源
		select2-model  用于指定用户选择后提交的变量
	
```

### 变量监听

```javascript
//当指定变量值发生改变时，执行方法
				//指定变量             执行的方法：参数1 改变后的数据 ， 参数2 改变前的数据         
$scope.$watch('entity.goods.category1Id', function(newValue, oldValue) {          
    	//根据选择的值，查询二级分类
    	itemCatService.findByParentId(newValue).success(
    		function(response){
    			$scope.itemCat2List=response; 	    			
    		}
    	);    	
}); 

```



## 3.补充

### js集合方法：

#### 1.向集合添加内容 ：  push（内容）

#### 2.获取集合中的元素索引： indexOf（元素）

#### 3.删除集合中指定索引的元素： splice（索引，个数）

#### 4.字符串转换为int：parseInt（数据）

#### 5.字符串转换为json对象：JSON.parse(数据)







