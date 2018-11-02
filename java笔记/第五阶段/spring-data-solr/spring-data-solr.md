# spring-data-solr

## 一、概念

- solr建立在lucene（全文检索）之上， **它是一个索引数据库（nosql）**
- solrJ，官方提供的操作solr的类库
- springData solr 封装了solrj

## 二、配置分析器（在solr仓库的schema.xml中配置）

```xml
 <fieldType name="text_ik" class="solr.TextField">
		<analyzer class="org.wltea.analyzer.lucene.IKAnalyzer"/>
	</fieldType>
```



## 三、配置域  （在solr仓库的schema.xml中配置）

### 1.普通域

```xml
<field name="item_keywords" type="text_ik" indexed="true" 
							通过什么分词  是否添加索引
       stored="false" multiValued="true"/>
	是否存储           是否存储多值

```

### 2.复制域

```xml
<!--查询时，匹配以下的所有域-->
<field name="item_keywords" type="text_ik" indexed="true" stored="false" multiValued="true"/>
														不需要存储
		<copyField source="item_title" dest="item_keywords"/>
		<copyField source="item_category" dest="item_keywords"/>
		<copyField source="item_seller" dest="item_keywords"/>
		<copyField source="item_brand" dest="item_keywords"/>
```

### 3.动态域

```xml
<!--当需要动态扩充域时，及当域名称不固定时使用-->
<dynamicField name="item_spec_*" type="string" indexed="true" stored="true" />	
```

## 四、环境搭建

### 1.依赖

```xml
   <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-solr</artifactId>
            <version>1.5.5.RELEASE</version>
        </dependency>
```

### 2.配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:solr="http://www.springframework.org/schema/data/solr"
       xsi:schemaLocation="http://www.springframework.org/schema/data/solr
  		http://www.springframework.org/schema/data/solr/spring-solr-1.0.xsd
		http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context.xsd">
    <!-- solr服务器地址 -->
    <solr:solr-server id="solrServer" url="http://127.0.0.1:8080/solr" />
    <!-- solr模板，使用solr模板可对索引库进行CRUD的操作 -->
    <bean id="solrTemplate" class="org.springframework.data.solr.core.SolrTemplate">
        <constructor-arg ref="solrServer" />
    </bean>
</beans>

```

### 3.实体类与域字段通过@Field注解映射

```java
   	@Field
    private Long id;

    @Field("item_title")
    private String title;

    @Field("item_price")
    private BigDecimal price;

	//动态域
	//创建规格动态域
    @Dynamic  //表明为动态域
    @Field("item_spec_*")
    private Map<String,String> specMap;

```

## 五、基础操作(增删改，需要commit提交)

### 1.增

```java
public static void main(String[] arg){
    Tbitem  tbitem = new Tbitem();
    tbitem.Id(1L);
    tbitem.setTitel("啦啦啦");
    
    //添加
    solrTemplate.saveBean(item);  //添加集合可用solrTemplate.saveBeans(list);
    //提交
    solrTemplate.commit();
}
```

### 2.查询

```java
public static void main(String[] arg){
    
    //根据主键查询
    TbItem tbItem = solrTemplate.getById(1L,TbItem.class);
	System.out.println(tbItem);
}
```

### 3.删除

```java
    /**
     * 根据主键删除
     */
    @Test
    public void delById() {
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }
```

### 4.分页查询 

```java
 @Test
    public void findByPage() {
        Query query = new SimpleQuery("*:*");

        //设置查询起始索引
        query.setOffset(10);
        //设置每页显示数
        query.setRows(15);

        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        for (TbItem tbItem : tbItems) {
            System.out.println(tbItem.getTitle() + "-----" + tbItem.getPrice());
        }
        System.out.println("总记录数：");
        long totalElements = tbItems.getTotalElements();
        System.out.println(totalElements);
        System.out.println("总页数：");
        int totalPages = tbItems.getTotalPages();
        System.out.println(totalPages);

    }

```

### 5.条件查询

```java
 @Test
    public void findByCondition() {
        Query query = new SimpleQuery("*:*");
        //添加查询条件
        Criteria criteria = new Criteria("item_title").contains("2");
        criteria = criteria.and("item_title").contains("小米");

        query.addCriteria(criteria);

        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);

        for (TbItem tbItem : tbItems) {
            System.out.println(tbItem.getTitle() + "-----" + tbItem.getPrice());
        }

        System.out.println("总记录数：");
        long totalElements = tbItems.getTotalElements();
        System.out.println(totalElements);
        System.out.println("总页数：");
        int totalPages = tbItems.getTotalPages();
        System.out.println(totalPages);

    }
```

### 6.删除全部

```java
    /**
     * 删除所有数据
     */
    @Test
    public void delAll() {
        Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
```

## 六、进阶操作

### 1.高亮显示

```java
   //获取高亮页对象
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //通过高亮页对象，获取高亮域入口(每条记录的高亮入口)
        List<HighlightEntry<TbItem>> highlighted = page.getHighlighted();
        for (HighlightEntry<TbItem> tbItemHighlightEntry : highlighted) {
            //如果addField了多个域，则会有多个高亮域
            List<HighlightEntry.Highlight> highlights = tbItemHighlightEntry.getHighlights();
//            for (HighlightEntry.Highlight highlight : highlights) {
            //获取高亮域中的值集合，因为存在高亮域为复制域，则值集合会有多个
//                List<String> snipplets = highlight.getSnipplets();
//                System.out.println(snipplets);
//            }

            TbItem tbItem = tbItemHighlightEntry.getEntity();

            if (highlights.size() > 0 && highlights.get(0).getSnipplets().size() > 0) {
                tbItem.setTitle(tbItemHighlightEntry.getHighlights().get(0).getSnipplets().get(0));
            }
        }

        map.put("rows", page.getContent());
```

### 2.分组查询

```java
 private List<String> getCategory(Map searchMap) {
        List<String> list = new ArrayList<>();

        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //配置分组
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");
        query.setGroupOptions(groupOptions);

        //获取分组页对象
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);

        GroupResult<TbItem> item_category = page.getGroupResult("item_category");

        Page<GroupEntry<TbItem>> groupEntries = item_category.getGroupEntries();

        List<GroupEntry<TbItem>> content = groupEntries.getContent();
        for (GroupEntry<TbItem> itemGroupEntry : content) {
            String groupValue = itemGroupEntry.getGroupValue();
            list.add(groupValue);
        }

        return list;
    }

```

