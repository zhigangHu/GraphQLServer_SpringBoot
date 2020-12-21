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
