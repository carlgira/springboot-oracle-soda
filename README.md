# springboot-oracle-soda
This example shows how to use soda library using Oracle relational database as nosql repository.

Oracle database creates under the hood a special table to store and index the collections of objects.

The next project is a simple work in progress to use the soda library with spring boot. 

For now, it does implement the database connection using some configuration beans but, its possible (and better) to implement this using a custom Spring Repository.

## Requirements
- JDK 11
- Oracle Database

## Configuration

1. Create a user in the database (it's a normal user but its need the special GRANT of **SODA_APP**) 
```roomsql
CREATE USER sodauser IDENTIFIED BY sodauser;
GRANT connect, resource TO sodauser IDENTIFIED BY sodauser;

GRANT create session TO sodauser;
GRANT create table TO sodauser;
GRANT create view TO sodauser;
GRANT create any trigger TO sodauser;
GRANT create any procedure TO sodauser;
GRANT create sequence TO sodauser;
GRANT create synonym TO sodauser;
grant SODA_APP to sodauser;
GRANT UNLIMITED TABLESPACE TO sodauser;
```

2. Change the database connection properties of the file "application.properties"

## Build
```commandline
./gradlew build
```

## Run
```commandline
java -jar build/libs/springboot-oracle-soda-1.0.0.jar
```

## Test

### Add
curl -H "Content-type: application/json" -X PUT http://localhost:8080/books -d '{"id" : "123456", "name":"super2"}'

### Find all
curl -H "Content-type: application/json" http://localhost:8080/books

### Update
curl -H "Content-type: application/json" -X POST http://localhost:8080/books -d '{"id" : "123456", "name":"super3"}'


## TODO
- Some javadocs
- Add more operations on the class OracleOperations using java generics.
- Create custom spring repository
- Unit tests (maybe is possible using "Oracle NoSQL Cloud Simulator")
