# AngularJs基础操作

## 1.常用指令

- **ng-app:**开启表达式，指定模块

  ```html
  <body ng-app="模块名"></body>
  ```

- **ng-model:**参数绑定

  ```html
  <intput ng-model="name"></body>
  ```

- **ng-init：**初始化值

  ```html
  <body ng-init="name='zhangsan'"></body>
  ```

- **ng-controller：**绑定控制器

  ```html
  <body ng-controller="控制器名"></body>
  ```

- **ng-repeat：**用于遍历

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

  ​

## 2.基础操作

- **创建模块：**

  ```html
  <script>
      //参数1：模块名称   参数2：引用其它模块
      var app = angular.module("mymodule",[]);
  </script>

  <!--引用模块-->
  <body ng-app="mymodule">
  </body>
  ```


 

- **创建控制器**：

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

-  **发送ajax异步请求**

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

