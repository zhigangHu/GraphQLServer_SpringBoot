# GraphQLServer_SpringBoot
The GraphQL server project developed by SpringBoot
#  1.A First Look at GraphQL
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
