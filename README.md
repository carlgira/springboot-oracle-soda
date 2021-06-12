# springboot-oracle-soda
This example shows how to use soda library using Oracle relational database as nosql repository.

Oracle database creates under the hood a special table to store and index the collections of objects.

The next project is a simple work in progress to use the soda library with spring boot. 

For no, it does implement the database connection using some configuration beans but, its possible (and better) to implement this using a custom Spring Repository.

## Requirements
- JDK 11
- Oracle Database

## Configuration

1. Create a user in the database (it's a normal user but its need the special GRANT of **SODA_APP**) 
```roomsql
CREATE USER sodauser IDENTIFIED BY sodauser;

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

2. Enter as sodauser and create the collection.

```roomsql
DECLARE
  l_collection  SODA_COLLECTION_T;
  l_metadata    VARCHAR2(32767);
BEGIN
  l_metadata := '{
	"keyColumn": {
		"name": "ID",
		"sqlType": "VARCHAR2",
		"maxLength": 255,
		"assignmentMethod": "UUID"
	},
	"contentColumn": {
		"name": "JSON_DOCUMENT",
		"sqlType": "BLOB",
	},
	"versionColumn": {
		"name": "VERSION",
		"type": "String",
		"method": "SHA256"
	},
	"lastModifiedColumn": {
		"name": "LAST_MODIFIED"
	},
	"creationTimeColumn": {
		"name": "CREATED_ON"
	},
	"readOnly": false
    }';
    
  l_collection := DBMS_SODA.create_collection('books', l_metadata);

  IF l_collection IS NOT NULL THEN
    DBMS_OUTPUT.put_line('Collection ID : ' || l_collection.get_name());
  ELSE
    DBMS_OUTPUT.put_line('Collection does not exist.');  
  END IF;
END;
/

-- DECLARE
--  l_status  NUMBER := 0;
-- BEGIN
--  l_status := DBMS_SODA.drop_collection('books');
--  DBMS_OUTPUT.put_line('status    : ' || l_status);
-- END;
-- /


```


3. Change the database connection properties of the file "application.properties"

## Build
```commandline
./gradlew build
```

## Run
```commandline
java -Doracle.net.tns_admin=wallet-atp -jar build/libs/springboot-oracle-soda-1.0.0.jar
```

## Test

### Add
curl -H "Content-type: application/json" -X PUT http://localhost:8080/books -d '{"id" : "123456", "name":"super2"}'

### Find all
curl -H "Content-type: application/json" http://localhost:8080/books

## Update
curl -H "Content-type: application/json" -X POST http://localhost:8080/books -d '{"id" : "123456", "name":"super3"}'


## TODO
- Some javadocs
- Add more operations on the class OracleOperations using java generics.
- Create custom spring repository
- Unit tests (maybe is possible using "Oracle NoSQL Cloud Simulator")
