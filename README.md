# GraphQLServer_SpringBoot
The GraphQL server project developed by SpringBoot
#  1. A First Look at GraphQL
Some of the information in this section comes from：https://graphql.com
GraphQL was first introduced as a next-generation data interface standard by FaceBook, Inc. Development began in 2012, with the initial version released in 2015, and was detached from FaceBook as a separate, open source project in July 2018.

## 1.1 Why GraphQL?
In fact, the core of any software is the storage, query, processing and display of data. If the web-based software project is divided into front and backend, the main task of the backend is to store and process, while the main task of the frontend is to query and display data. But often the data structure stored in the background and the data structure needed in the foreground has a certain gap, for example, most of the background database is still using a relational structure (of course, there are also NoSQL such as MongoDB, graphical database Neo4J, etc., but the mainstream is still using relational databases such as MySQL, Oracle, etc., after all, these products have years of stability and efficiency in the (accumulated reputation), while the web side requires graphical, tree-like data structure. This requires the development of data interface APIs between the front and back office.
Before GraphQL, interface specifications like RESTful were mainly used. The core idea is to pass key parameters such as tables, fields, field values, etc. to the backend by enhancing the URI content. After receiving these parameters, the backend designs different Router and corresponding business processing logic according to the different contents of the URI, obtains the corresponding processing results, and then returns them to the front-end in a unified and standardized data format (such as JSON format).（For the RESTful interface specification, see：http://www.ruanyifeng.com/blog/2014/05/restful_api.html，https://restfulapi.net/）
However, as the data required by the front-end becomes more and more complex, and the URI in RESTful can pass more limited parameter information, this will lead to the situation that the data required by the front-end and the data provided by the back-end do not match. If the frontend passes an id parameter through the URI of Restful specification, it is likely that the backend will pass all the data related to the id to the frontend, and the frontend will filter which data is needed, which will bring a lot of data redundancy problems. For large websites, it is likely to bring problems such as increased database server load and slowdown, for which GraphQL was born.

## 1.2 GraphQL Features
GraphQL is essentially a data query API that doesn't require any specific database or storage engine to be bound. You just need to define a GraphQL API, and you don't need to pay attention to what database or storage engine is used in the backend.
Let's look at an example.

```javascript
{
  hero {
    name
    # Queries can have comments!
    friends {
      name
    }
  }
}
```
This is an example of a GraphQL query statement that, when sent to a GraphQL server for execution, returns one of the following results.

```javascript
{
  "data": {
    "hero": {
      "name": "R2-D2",
      "friends": [
        {
          "name": "Luke Skywalker"
        },
        {
          "name": "Han Solo"
        },
        {
          "name": "Leia Organa"
        }
      ]
    }
  }
}
```
It follows that the GraphQL query result structure is the structure of the definition of the query statement, the so-called“ask exactly what you want.”
# 2.GraphQL Basic Concepts
Parts of this section and case codes are referenced from：http://graphql.cn
## 2.1 Fields
One of the most important features of GraphQL is that the query and the result have almost the same structure, so that the front-end always gets the desired data and the back-end server knows exactly the fields requested by the front-end.
In GraphQL, a field refers to an object type (Object), and GraphQL's ability to traverse related objects and their fields allows the client to request a large amount of related data at once, rather than having to query it multiple times as in traditional REST architectures.
GraphQL query code：

```javascript
{
  hero {
    name
      friends {
      name
    }
  }
}
```
In this query statement, "hero" is an Object, which contains a name attribute and an embedded friends object, and the result of the query is the name attribute of all hero data objects and their associated friends data objects in the database.

```javascript
{
  "data": {
    "hero": {
      "name": "R2-D2",
      "friends": [
        {
          "name": "Luke Skywalker"
        },
        {
          "name": "Han Solo"
        },
        {
          "name": "Leia Organa"
        }
      ]
    }
  }
}
```
##  2.2 Parameters
In GraphQL queries, parameters can also be used.

```javascript
{
  human(id: "1000") {
    name
    height(unit: FOOT)
  }
}
```
Query the human data object with id="1000", the result contains the name and height attributes.

```javascript
{
  "data": {
    "human": {
      "name": "Luke Skywalker",
      "height": 5.6430448
    }
  }
}
```
## 2.3 Aliases
Anyone who has written SQL statements knows that sometimes it is necessary to use self-nested statements in SQL statements, and it is then necessary to use aliases to distinguish the same data objects. This feature is also provided in GraphQL.

```javascript
 empireHero: hero(episode: EMPIRE) {
    name
  }
  jediHero: hero(episode: JEDI) {
    name
  }
}
```
In this query statement, there are two "hero" data objects, but they are distinguished using the aliases "empireHero" and "jediHero", respectively.

```javascript
{
  "data": {
    "empireHero": {
      "name": "Luke Skywalker"
    },
    "jediHero": {
      "name": "R2-D2"
    }
  }
}
```
## 2.4 Operation name
In the previous code examples, the operation name of query queryName is omitted. In the GraphQL specification, if the operation name is omitted, the default is the query operation type.
Complete query code: 
contains operation type keyword: query, 
operation name: HeroNameAndFriends
```javascript
{
  "data": {
    "hero": {
      "name": "R2-D2",
      "friends": [
        {
          "name": "Luke Skywalker"
        },
        {
          "name": "Han Solo"
        },
        {
          "name": "Leia Organa"
        }
      ]
    }
  }
}
```
The operation types in GraphQL are: query (query), mutation (change), subscription, etc., which cannot be omitted except for query.
## 2.5 Variables
Variables can also be used in GraphQL statements. This makes it possible to wait for specific values to be entered interactively on the front end during a query:.

```javascript
query HeroNameAndFriends($episode: Episode) {
  hero(episode: $episode) {
    name
    friends {
      name
    }
  }
}
```
The variable is used in the format $variableName so that the front-end user can transfer specific values to the query statement using a JSON data file in the format variableName: value.

## 2.6 mutation
While the statements described in the previous section implement the query function, another important feature of GraphQL is the ability to update the data.
In GraphQL, the operation keyword to implement updates to the data is：mutation:

```javascript
mutation CreateReviewForEpisode($ep: Episode!, $review: ReviewInput!) {
  createReview(episode: $ep, review: $review) {
    stars
    commentary
  }
}
```
This code implements the insertion of a data record into the database, and it is important to note in this statement that the parameters of the data are in the $variableName format, meaning that the specific value is passed through a JSON file with variableName: value. the "! " indicates that it cannot be null.
# 3. Schema and Type
Schema is a very important concept in GraphQL, and a good understanding of this concept and a good use of it in your own GraphQL server is crucial for implementing relevant GraphQL queries and change statements.

## 3.1 Type System
Let's start with a GraphQL query statement.

```javascript
{
  hero {
    name
    appearsIn
  }
}
```
The execution order of this query statement is.
1. start with a special "root" object. This means that there needs to be a root object in the GraphQL schema.
2. select the hero field on the root; 
3. select the name and appearsIn attributes for the object returned by hero.
Although it is said that the results of GraphQL queries are so close to the query statements that we can even predict the results of the queries based on the query statements. But we still need to make a precise definition of the required data, and this definition is independent of the specific database. What fields can we select? What kind of objects will the server return? What fields are available under these objects? This is the reason for introducing schema.
Each GraphQL service defines a set of types that describe the data you might query from that service. Whenever a query arrives, the server validates and executes the query against the schema. So in developing your own GraphQL server, you need to define a Schema type file, no matter what language you write it in.

## 3.2 Type Language
GraphQL services can be written in any language, as we do not rely on any language-specific syntax syntax (e.g. JavaScript) to communicate with GraphQL schema, we define our own simple language called "GraphQL schema language" - which is similar to GraphQL query language and allows us to communicate with GraphQL schema without language differences.

## 3.3 Object Types and Fields
The most basic component in a GraphQL schema is the object type, which indicates what type of object you can get from the service, and what fields this object has. Using the GraphQL schema language, we can represent it like this.

```javascript
type Character {
  name: String!
  appearsIn: [Episode!]!
}
```

 - Character is a GraphQL object type, indicating that it is a type that has some fields. Most of the types in your schema will be object types.
   
 - name and appearsIn are fields on the Character type. This means that only the name and appearsIn fields can appear in any part of a GraphQL query that operates on the Character type. 
 - String is one of the built-in scalar types -- a scalar type is a type that resolves to a single scalar object and cannot be subselected in a query. We'll talk more about scalar types later. 
 - String! means that the field is non-null, and the GraphQL service guarantees that when you query this field, it will always return a value to you. In the type language, we use an exclamation point to indicate this feature.
 - [Episode!]! represents an array of Episodes. Since it is also non-empty, you will always get an array (zero or more elements) when you query the appearsIn field. And since Episode! is also non-empty, you can always expect each item in the array to be an Episode object.
 - 

Example of combining specific language.
1. JavaScript:In JavaScript, we use a schema.js file to define the Schema:

```javascript
type Post {
        id: Int
        text: String
        user:User
    }
```
2. Java: In GraphQL server developed using Java+Spring boot framework, we use schema.graphql file to define Schema

```javascript
type Author{
    id:ID!
    createdTime:String
    firstName:String
    lastName:String
    books:[Book]
}
```
This shows that regardless of the language used to develop GraphQL, the definition of Schema is written according to the specifications of the type language.

There is a lot more to the type language, so I won't go into it all here for space reasons. For more details, see.
(https://graphql.cn/learn/schema/#object-types-and-fields)

# 4. GraphQL Server实现——基于SpringBoot+GraphQL
在实现一个GraphQL Server之前，建议大家先去看看一个具体的GraphQL查询语句是如何执行的，由于理论的内容太多，请大家具体参阅：
https://graphql.org/learn/execution/
本章节项目案例及部分代码参考自：
https://www.graphql-java.com/documentation/v14/

## 4.1 SpringBoot简介
SpringBoot应该是目前基于Java语言的最重要的一个框架了，也是Java开源框架中的一个具有划时代意义的产品。结合Java面向对象的语言特性和Spring的AOP软件工程思想，应该说SpringBoot是开发大型、复杂的前后端分离的软件的不二选择。特别是由Java衍生出的Scala结合Spark、Hadoop等大数据平台，为打造能力更强的“大后端”提供了可能性。
下面我们就结合一个具体的例子，来看看如何使用SpringBoot来开发一个我们自己的GraphQL Server。

## 4.2 开发平台和开发工具
Java开发工具：IntellJ IDEA，一个能够让我这个用了十多年Eclipse的转投IntellJ自然尤其自身的优势。
数据库：MySQL
数据库管理工具：MySQL Workbench

## 4.3 具体开发步骤
### 4.3.1 MySQL数据库创建

```sql
CREATE DATABASE /*!32312 IF NOT EXISTS*/`graphql` /*!40100 DEFAULT CHARACTER SET utf8 */;
 
USE `graphql`;
 
/*Table structure for table `author` */
 
DROP TABLE IF EXISTS `author`;
 
CREATE TABLE `author` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Key word',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Updated time',
  `first_name` varchar(50) DEFAULT NULL COMMENT 'firstName',
  `last_name` varchar(50) DEFAULT NULL COMMENT 'lastName',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 
/*Table structure for table `book` */
 
DROP TABLE IF EXISTS `book`;
 
CREATE TABLE `book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Key word',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Updated time',
  `title` varchar(50) DEFAULT NULL COMMENT 'Title',
  `author_id` bigint(20) NOT NULL,
  `isbn` varchar(255) DEFAULT NULL,
  `page_count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 
/*Table structure for table `user` */
 
DROP TABLE IF EXISTS `user`;
```
执行完上述代码之后，在MySQL中能创建一个名为graphql的database。它包含两个表格“author”和“book”。
创建完成之后，schema的结构如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220043858616.png#pic_center)
### 4.3.2 在IntellJ中创建SpringBoot项目
在IntellJ中，选择File=>new=>project，在new project对话框中，选择“Spring Initia”，在“Project SDK”中建议选择“1.8”，因为本人在实际开发过程中SDK 11曾经出现过很多兼容性问题。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220044954442.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)
单击“Next”
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220045334203.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)
在这一步就按默认的选项，Java version建议选“8”，单击“Next”
在这一步我们需要选择项目当中需要用的的dependecies:
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220050423462.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)
在本项目中，需要用到以下dependencies:
1. Developer Tools![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220050803828.png#pic_center)

2. Web![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220051002155.png#pic_center)

3. Template Engine![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220051120700.png#pic_center)
4. SQL.![在这里插入图片描述](https://img-blog.csdnimg.cn/2020122005130915.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)在这里我们要同时添加 Spring Data JPA和MySQL Driver，分别用于创建数据中间层和MySQL的驱动

5. 最后为项目命名并选择存储位置![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220051651839.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)

### 4.3.3 在IntellJ中添加GraphQL plugin
File=>setting，在setting对话框中选择plugins，确认添加了JS GraphQL plugin
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220052346109.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)
### 4.3.4 添加GraphQL的dependencies
目前在SpringBoot中对GraphQL支持比较好的Package就是graphql-java-kickstart了，它集成了相当多的有用工具，比原生的GraphQL-Java package要好用很多，而且在其中就集成了GraphQL Playground调试工具，测试GraphQL语句非常方便。详细请参阅：https://github.com/graphql-java-kickstart/graphql-spring-boot
添加完上述所需dependencies后，完整的pom.xml文件内容如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.4.0-SNAPSHOT</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.example</groupId>
	<artifactId>springboot_graphql</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>springboot_graphql</name>
	<description>Demo project for Spring Boot</description>

	<properties>
		<java.version>8</java.version>
		<kotlin.version>0.3.70</kotlin.version>
	</properties>


	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<!-- https://mvnrepository.com/artifact/com.zaxxer/HikariCP -->
		<dependency>
			<groupId>com.zaxxer</groupId>
			<artifactId>HikariCP</artifactId>
			<version>3.4.5</version>
		</dependency>

		<!-- graphQL begin-->

		<dependency>
			<groupId>com.graphql-java-kickstart</groupId>
			<artifactId>graphql-spring-boot-starter</artifactId>
			<version>8.0.0</version>
		</dependency>

		<!-- to embed Altair tool -->
		<dependency>
			<groupId>com.graphql-java-kickstart</groupId>
			<artifactId>altair-spring-boot-starter</artifactId>
			<version>8.0.0</version>
			<scope>runtime</scope>
		</dependency>

		<!-- to embed GraphiQL tool -->
		<dependency>
			<groupId>com.graphql-java-kickstart</groupId>
			<artifactId>graphiql-spring-boot-starter</artifactId>
			<version>8.0.0</version>
			<scope>runtime</scope>
		</dependency>

		<!-- to embed Voyager tool -->
		<dependency>
			<groupId>com.graphql-java-kickstart</groupId>
			<artifactId>voyager-spring-boot-starter</artifactId>
			<version>8.0.0</version>
			<scope>runtime</scope>
		</dependency>

		<!-- testing facilities -->
		<dependency>
			<groupId>com.graphql-java-kickstart</groupId>
			<artifactId>graphql-spring-boot-starter-test</artifactId>
			<version>8.0.0</version>
			<scope>test</scope>
		</dependency>

		<!-- https://mvnrepository.com/artifact/com.graphql-java-kickstart/playground-spring-boot-starter -->
		<dependency>
			<groupId>com.graphql-java-kickstart</groupId>
			<artifactId>playground-spring-boot-starter</artifactId>
			<version>8.0.0</version>
			<scope>runtime</scope>
		</dependency>

		<dependency>
			<groupId>io.github.graphql-java</groupId>
			<artifactId>graphql-java-annotations</artifactId>
			<version>8.2</version>
		</dependency>


		<!-- graphQL end -->


		<!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.14</version>
			<scope>provided</scope>
		</dependency>

		<!-- https://mvnrepository.com/artifact/com.coxautodev/graphql-java-tools -->
		<dependency>
			<groupId>com.coxautodev</groupId>
			<artifactId>graphql-java-tools</artifactId>
			<version>2.1.2</version>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

	<repositories>
		<repository>
			<id>spring-milestones</id>
			<name>Spring Milestones</name>
			<url>https://repo.spring.io/milestone</url>
		</repository>
		<repository>
			<id>spring-snapshots</id>
			<name>Spring Snapshots</name>
			<url>https://repo.spring.io/snapshot</url>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
		</repository>

		<repository>
			<id>jcenter</id>
			<url>https://jcenter.bintray.com/</url>
		</repository>

	</repositories>
	<pluginRepositories>
		<pluginRepository>
			<id>spring-milestones</id>
			<name>Spring Milestones</name>
			<url>https://repo.spring.io/milestone</url>
		</pluginRepository>
		<pluginRepository>
			<id>spring-snapshots</id>
			<name>Spring Snapshots</name>
			<url>https://repo.spring.io/snapshot</url>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
		</pluginRepository>
	</pluginRepositories>

</project>

```
### 4.3.5 创建项目目录
本项目目录结构如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220054212179.png#pic_center)

 - config目录：存放config配置文件 entity目录：存放entity实体类文件，entity实体类与MySQL数据库中表
  
 - model目录：存放数据model文件 repo目录：repository目录，存放对数据模型操作文件
   
 - resolver目录：这是GraphQL

  

 - server项目中最为重要的目录，里面的文件负责将前端的GraphQL操作转换成真实的、基于SpringBoot的数据库操作。

同时，在项目的resources目录中创建graphql目录，用于存放与GraphQL相关的.graphql文件：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220065455285.png#pic_center)
### 4.4.4 application.yml项目配置文件
当成功创建一个SpringBoot项目时，系统会自动为我们创建一个application.properties文件，主要用于系统运行时用到的环境参数变量以及相关的值，在这里我们用自己定义的application.yml文件，相比较.properties文件，.yml文件的层次更加清晰也更简洁，其内容如下：

```xml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/graphql?serverTimezone=UTC
    username: root
    password: 1234
#    driver-class-name: com.mysql.jdbc.Driver
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      minimum-idle: 10
      maximum-pool-size: 20
      idle-timeout: 60000
      max-lifetime: 1800000
      connection-timeout: 30000
      data-source-properties:
        cachePreStmts: true
        preStmtCacheSize: 250


  main:
    allow-bean-definition-overriding: true
#    dbcp2:
#      driver-class-name: com.mysql.jdbc.Driver
#      max-wait-millis: 20
#      max-idle: 8
#      min-idle: 8
#      initial-size: 10
  jpa:
    hibernate:
      ddl-auto: update
      database: mysql


graphql:
  servlet:
    mapping: /graphql
    enabled: true
    corsEnabled: true
    cors:
      allowed-origins: http://some.domain.com
    # if you want to @ExceptionHandler annotation for custom GraphQLErrors
    exception-handlers-enabled: true
    contextSetting: PER_REQUEST_WITH_INSTRUMENTATION
  tools:
    schema-location-pattern: "**/*.graphql"
    # Enable or disable the introspection query. Disabling it puts your server in contravention of the GraphQL
    # specification and expectations of most clients, so use this option with caution
    introspection-enabled: true
  annotations:
    base-package: com.exaple.springboot_graphql # required
    always-prettify: true #true is the default value, no need to specify it
graphql.playground:
  mapping: /playground
  endpoint: /graphql
  subscriptionEndpoint: /subscriptions
  staticPath.base: my-playground-resources-folder
  enabled: true
  pageTitle: Playground
  cdn:
    enabled: false
    version: latest
  settings:
    editor.cursorShape: line
    editor.fontFamily: "'Source Code Pro', 'Consolas', 'Inconsolata', 'Droid Sans Mono', 'Monaco', monospace"
    editor.fontSize: 14
    editor.reuseHeaders: true
    editor.theme: dark
    general.betaUpdates: false
    prettier.printWidth: 80
    prettier.tabWidth: 2
    prettier.useTabs: false
    request.credentials: omit
    schema.polling.enable: true
    schema.polling.endpointFilter: "*localhost*"
    schema.polling.interval: 2000
    schema.disableComments: true
    tracing.hideTracingResponse: true
  headers:
    headerFor: AllTabs
  tabs:
    - name: Example Tab
      query: classpath:exampleQuery.graphql
      headers:
        SomeHeader: Some value
      variables: classpath:variables.json
      responses:
        - classpath:exampleResponse1.json
        - classpath:exampleResponse2.json
```
在文件中比较重要的配置内容有：

 - datasource: 配置数据库连接参数； 
 - hikari: 配置数据库连接池，使用数据连接池可以大大优化对数据库的连接域访问速度，具体请参阅：https://www.baeldung.com/spring-boot-hikari
 - 
   graphql:graphql的基本配置；
   
 - graphql.playground: playground调试工具的基本配置

### 4.4.5 entity目录中的文件
entity目录中的文件为数据实体文件，该目录中的文件与数据库中的表对应：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220072137920.png#pic_center)
在数据库中我们创建了author表和book表，因此我们就创建了Author.java和Book.java文件与之相对应。并且在author表和book表中分别都有created_time、updated_time以及Id字段，为避免冗余，我们创建了BaseEntity.java文件来对应这些字段。
各文件内容代码如下：
1. BaseEntity.java

```java
package com.exaple.springboot_graphql.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author comqiao
 * @create 2020-11-12 17:18
 */
@Data
@MappedSuperclass
public class BaseEntity implements Serializable {


    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint", nullable = false)
    protected Long id;

    /** Create Timestamp (unit:second) */
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdTime;

    /** Update Timestamp (unit:second) */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedTime;


    public BaseEntity() {
        createdTime = new Date();
        updatedTime = createdTime;
    }

    @PreUpdate
    private void doPreUpdate() {
        updatedTime = new Date();
    }

}
```

2. Author.java

```java
package com.exaple.springboot_graphql.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Author extends BaseEntity {

    @Column(columnDefinition = "varchar(50)")
    private String firstName;

    @Column(columnDefinition = "varchar(50)")
    private String lastName;
}
```

3. Book.java

```java
package com.exaple.springboot_graphql.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Book extends BaseEntity {
    @Column(columnDefinition = "varchar(50)")
    private String title;

    private String isbn;

    private int pageCount;

    private long authorId;
}

```
### 4.4.6 model目录中的文件
这个目录中的数据model文件要与GraphQL中schema中所定义的数据操作相关联，例如我在schema中定义了一个type为input，名称为BookInput的model（Type definition的内容请查阅前面章节）
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220074114137.png#pic_center)
因此，在这个目录中，我们需要编写一个BookInput.java文件与之相对应，内容如下：

```java
package com.exaple.springboot_graphql.model;


import lombok.Data;


@Data
public class BookInput {
    private String title;

    private String isbn;

    private int pageCount;

    private long authorId;
}
```
### 4.4.7 repo目录中的文件
repo目录中的文件都是Interface文件，它们均继承自JpaRepository接口。在这些接口文件中定义了数据访问及操作接口。具体文件内容如下：
1. AuthorRepo.java

```java
package com.exaple.springboot_graphql.repo;

import com.exaple.springboot_graphql.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author comqiao
 * @create 10-10-2020 13:35
 */
public interface AuthorRepo extends JpaRepository<Author,Long> {

    Author findAuthorById(Long id);
}
```

2. BookRepo.java

```java
package com.exaple.springboot_graphql.repo;

import com.exaple.springboot_graphql.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author comqiao
 * @create 2020-10-10 13:39
 */
public interface BookRepo extends JpaRepository<Book,Long> {
    List<Book> findByAuthorId(Long id);

    Book findBookById(Long id);
}
```
### 4.4.8 resolver目录中的文件
这个目录中的文件负责实现对数据库操作的具体实现，与以往的SpringBoot文件不同的是，这个目录中的问价还要负责将前端传递过来的GraphQL语句解析成具体的数据操作代码，因此该目录中有以下文件：
1. AuthorResolver.java 

```java
package com.exaple.springboot_graphql.resolver;

import com.exaple.springboot_graphql.entity.Author;
import com.exaple.springboot_graphql.entity.Book;
import graphql.kickstart.tools.GraphQLResolver;

import com.exaple.springboot_graphql.repo.BookRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author comqiao
 * @create 2020-10-12 12:03
 */
@Component
@AllArgsConstructor
public class AuthorResolver implements GraphQLResolver<Author> {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private BookRepo bookRepo;

    public String getCreatedTime(Author author) {
        return sdf.format(author.getCreatedTime());
    }

    public List<Book> getBooks(Author author) {
        return bookRepo.findByAuthorId(author.getId());
    }


}

```

2. BookResolver.java

```java
package com.exaple.springboot_graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import com.exaple.springboot_graphql.entity.Author;
import com.exaple.springboot_graphql.entity.Book;
import com.exaple.springboot_graphql.repo.AuthorRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author comqiao
 * @create 2020-10-12 12:10
 */
@Component
@AllArgsConstructor
public class BookResolver implements GraphQLResolver<Book> {
    private AuthorRepo authorRepo;

    public Author getAuthor(Book book) {
        return authorRepo.findAuthorById(book.getAuthorId());
    }

}
```

3. Mutation.java

```java
package com.exaple.springboot_graphql.resolver;

import graphql.kickstart.tools.GraphQLMutationResolver;
import com.exaple.springboot_graphql.entity.Author;
import com.exaple.springboot_graphql.entity.Book;
import com.exaple.springboot_graphql.model.BookInput;
import com.exaple.springboot_graphql.repo.AuthorRepo;
import com.exaple.springboot_graphql.repo.BookRepo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author comqiao
 * @create 2019-10-14 12:46
 */
@Component
@AllArgsConstructor
public class Mutation implements GraphQLMutationResolver {
    private AuthorRepo authorRepo;
    private BookRepo bookRepo;

    public Author newAuthor(String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        return authorRepo.save(author);
    }

    public Book newBook(String title, String isbn, int pageCount, Long authorId) {
        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPageCount(pageCount);
        book.setAuthorId(authorId);
        return bookRepo.save(book);
    }


    public Book saveBook(BookInput input) {
        Book book = new Book();
        book.setTitle(input.getTitle());
        book.setIsbn(input.getIsbn());
        book.setPageCount(input.getPageCount());
        book.setAuthorId(input.getAuthorId());
        return bookRepo.save(book);
    }

    public Boolean deleteBook(Long id) {
        bookRepo.deleteById(id);
        return true;
    }

    public Book updateBookPageCount(int pageCount,long id) {
        Book book = bookRepo.findBookById(id);
        book.setPageCount(pageCount);
        return bookRepo.save(book);
    }

}
```
该文件负责对数据进行更新操作，因此分别定义了save、delete和update操作

4. Query.java

```java
package com.exaple.springboot_graphql.resolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import com.exaple.springboot_graphql.entity.Author;
import com.exaple.springboot_graphql.entity.Book;
import com.exaple.springboot_graphql.repo.AuthorRepo;
import com.exaple.springboot_graphql.repo.BookRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author comqiao
 * @create 2020-10-17 12:40
 */
@Component
@AllArgsConstructor
public class Query implements GraphQLQueryResolver {

    private AuthorRepo authorRepo;

    private BookRepo bookRepo;

    public Author findAuthorById(Long id) {
        return authorRepo.findAuthorById(id);
    }

    public List<Author> findAllAuthors() {
        return authorRepo.findAll();
    }

    public Long countAuthors() {
        return authorRepo.count();
    }

    public List<Book> findAllBooks() {
        return bookRepo.findAll();
    }

    public Long countBooks() {
        return bookRepo.count();
    }
}

```
该文件负责实现对数据的查询

### 4.4.9 resources/graphql目录中的文件
该目录中的文件主要是完成GraphQL中的schema的类型定义，要让系统成功找到该定义文件，需要在前面所讲述的application.yml中定义该配置文件所在的位置：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220083224533.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)
该目录中主要有以下两个文件：
1.schema.graphql
该文件就是使用GraphQL的类型定义语言所定义的schema，是整个GraphQL功能实现的核心，其内容如下：

```javascript
type Author{
    id:ID!
    createdTime:String
    firstName:String
    lastName:String
    books:[Book]
}

input BookInput{
    title:String!
    isbn:String!
    pageCount:Int
    authorId:ID
}

type Book{
    id:ID!
    title:String
    isbn:String
    pageCount:Int
    author:Author
}
```

2.root.graphql
该文件的重要性在前面章节已经介绍过，它负责定义在GraphQL的查询语言中实现的语言的格式样式，其内容如下：

```javascript
type Query{
    findAuthorById(id:ID!):Author
    findAllAuthors:[Author]
    countAuthors:Int
    findAllBooks:[Book]!
    countBooks:Int!

}

type Mutation {
    newAuthor(firstName: String!,lastName: String!) : Author!

    newBook(title: String!,isbn: String!,pageCount: Int, authorId: ID!) : Book!
    saveBook(input: BookInput!) : Book!
    deleteBook(id: ID!) : Boolean
    updateBookPageCount(pageCount: Int!, id:ID!) : Book!
}
```
至此，基于SpringBoot的GraphQL Server代码编写全部完成，下一章节将讲解如何利用GraphQL Playground对server进行调试
