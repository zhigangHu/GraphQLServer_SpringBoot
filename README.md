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
![在这里插入图片描述](https://graphql.cn/learn/schema/#object-types-and-fields)

# 4. GraphQL Server Implementation - Based on SpringBoot+GraphQL
Before implementing a GraphQL Server, it is recommended to go through how a specific GraphQL query statement is executed, and since there is so much theory, please refer specifically to.
https://graphql.org/learn/execution/
Project examples and some code references in this section are taken from：
https://www.graphql-java.com/documentation/v14/

## 4.1 SpringBoot简介
SpringBoot should be based on the Java language is currently one of the most important framework , but also the Java open source framework for a landmark product . Combined with Java's object-oriented language features and Spring's AOP software engineering ideas, it should be said that SpringBoot is the development of large-scale, complex front-end and back-end separation of the software of choice. Especially the Java-derived Scala combined with Spark, Hadoop and other big data platforms, to create a more capable "big back-end" provides the possibility.
Here we will combine a concrete example to see how to develop a GraphQL Server of our own using SpringBoot.

## 4.2 Development platform and development tools

JavaDevelopment Tools：IntellJ IDEA。
Database：MySQL
Database Management Tools：MySQL Workbench

## 4.3 Concrete development steps

### 4.3.1 MySQL Database creation

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
After executing the above code, you can create a database named graphql in MySQL, which contains two tables "author" and "book".
Once created, the structure of the schema is as follows.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220043858616.png#pic_center)

### 4.3.2 Creating SpringBoot projects in IntellJ

In IntellJ, select File=>new=>project, in the new project dialog box, select "Spring Initia", in the "Project SDK" It is recommended to select "1.8", because I have had many compatibility problems with SDK 11 in the actual development process.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220044954442.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)

Click “Next”
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220045334203.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)

In this step, press the default option, Java version is recommended to select "8", click "Next"

In this step we need to select the dependencies we need to use in the project:
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220050423462.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)
In this project, the following dependencies are used:
1. Developer Tools
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220050803828.png#pic_center)

2. Web
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220051002155.png#pic_center)

3. Template Engine
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220051120700.png#pic_center)
4. SQL.
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020122005130915.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)
Here we are going to add both Spring Data JPA and MySQL Driver for creating the data middle tier and MySQL driver respectively

5. Finally, name the project and choose a storage location
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220051651839.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)

### 4.3.3 Adding GraphQL plugin to IntellJ
File=>setting，Select plugins in the settings dialog to make sure the JS GraphQL plugin is added
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220052346109.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)

### 4.3.4 Adding GraphQL dependencies
At present, the Package with better support for GraphQL in SpringBoot is graphql-java-kickstart, which integrates quite a lot of useful tools, much better than the native GraphQL-Java package, and integrates the GraphQL Playground debugging tool is integrated in it, so it is very convenient to test GraphQL statements. For more details, please refer to：https://github.com/graphql-java-kickstart/graphql-spring-boot
After adding the above required dependencies, the complete pom.xml file will look like this：

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
### 4.3.5 Create project directory
The directory structure of this project is as follows.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220054212179.png#pic_center)

 - config directory: store config configuration file entity directory: store entity entity class file, entity class and MySQL database table
  
 - model directory: the data model file repo directory: the repository directory, which stores the data model operation files
   
 - resolver directory: this is the most important directory in GraphQL server project, the files inside are responsible for converting front-end GraphQL operations into real, SpringBoot-based database operations.

Also, create a graphql directory in the project's resources directory to hold GraphQL-related .graphql files.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220065455285.png#pic_center)

### 4.4.4 application.yml project configuration file

When a SpringBoot project is successfully created, the system will automatically create an application.properties file for us, which is mainly used for the environment parameters variables and related values used by the system at runtime, here we use our own application.yml file, compared to the .properties file, the . yml file is clearer and more concise, its content is as follows.

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
The more important configuration elements in the file are.

 - datasource: configures the database connection parameters. 
 - hikari: configure database connection pooling, using data connection pooling can greatly optimize the speed of access to the database connection domain, see: https://www.baeldung.com/spring-boot-hikari
 -graphql 
   graphql: the basic configuration of graphql.
   
 - graphql.playground: basic configuration of the playground debugging tool

### 4.4.5 Files in the entity directory

The files in the entity directory are data entity files, and the files in this directory correspond to the tables in the database.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220072137920.png#pic_center)
In the database we created "author" and "book" tables, so we created Author.java and Book.java files to correspond to them. And there are "created_time", "updated_time" and "Id" fields in "author" and "book" tables respectively, so to avoid redundancy, we created BaseEntity.java file to correspond to these fields.
The content codes of each document are as follows.
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
### 4.4.6 Files in the "model" directory

The data model files in this directory should be associated with the data operations defined in the schema in GraphQL, for example, I defined a mod with type input and name BookInput in the schema (please refer to the previous section for the content of the Type definition)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220074114137.png#pic_center)

Therefore, in this directory, we need to write a BookInput.java file corresponding to it, with the following content：

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
### 4.4.7 Files in the repo directory
The files in the repo directory are all Interface files, which all inherit from the JpaRepository interface. The data access and operation interfaces are defined in these interface files. The specific files are as follows.
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
### 4.4.8 Files in the resolver directory
The files in this directory are responsible for the concrete implementation of the database operations. Unlike the previous SpringBoot files, this directory is also responsible for parsing the GraphQL statements passed from the front-end into concrete data manipulation code, so there are the following files in this directory.
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
This file is responsible for updating the data and therefore defines the save, delete and update operations respectively

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
This file is responsible for implementing queries to the data

### 4.4.9 Files in the resources/graphql directory

The files in this directory mainly complete the type definition of the schema in GraphQL. For the system to successfully find this definition file, the location of this configuration file needs to be defined in application.yml as described earlier.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220083224533.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)
The directory contains the following two main files.
1.schema.graphql
This file is the schema defined using GraphQL's type definition language, and is the core of the entire GraphQL functionality implementation, which is as follows.

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
The importance of this file has been described in the previous sections and it is responsible for defining the formatting style of the language implemented in GraphQL's query language, which is as follows.

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
The next section will explain how to use GraphQL Playground to debug the server.

# 5. Debug by Graph Playground
Once the above code is completed, you can start the SpringBoot server for testing.
Tomcat server has been integrated in the SpringBoot framework, and after the system is successfully started, you can type in the browser address bar：http://localhost:8080/graphiql
Start the debugging tool, the interface is shown in the following figure.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220153939921.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)
The debugging tool has three windows.
The leftmost one allows you to enter GraphQL statements, the middle one shows the query or update data results, and the rightmost one allows you to preview the query and mutation statement styles defined in root.graphql.
We can first use GraphQL to add data to the database by entering the mutation code in the leftmost statement window.

```javascript
mutation{
  newAuthor(firstName:"andy",lastName:"liu"){
    id,
    firstName,
    lastName
  }
}
```
Click the Run button in the upper left corner of the test window at
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220154731169.png#pic_center)
After a successful run, the result of the run is displayed in the middle window.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220154841676.png#pic_center)
Continue adding data to the Book table.

```javascript
mutation{
  newBook(title:"China 100 years",
  isbn:"123456789",
  pageCount:100,
  authorId:1){
    title,
    isbn,
    pageCount,
    author{
      id,
      firstName,
      lastName
    }
  }
}
```
Results.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220155131837.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)
Also, the following records have been added to the Author and Book tables of the MySQL database.
author table：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220155456992.png#pic_center)
book table：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220155627649.png#pic_center)
Here's another query statement to test：

```javascript
{
  findAllBooks{
    id,
    isbn,
    pageCount,
    author{
      id,
      firstName,
      lastName
    }
  }
}
```
Test results.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220160939233.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbXFpYW8x,size_16,color_FFFFFF,t_70#pic_center)
At this point, the development of the GraphQL server based on SpringBoot is complete
