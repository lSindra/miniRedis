# miniRedis

### Run unit tests
mvn tests

### Package app
mvn package

### Run server
mvn spring-boot:run

## Commands over the network
* set= curl -d "value=cool-value" -X PUT localhost:8080/key
* get= curl localhost:8080/key
* del= curl -X DELETE localhost:8080/key
* dbsize= curl -X localhost:8080/dbsize
* incr= curl -X localhost:8080/incr/key
* zadd= curl -d "members=1 key1, 2 key2" -X PUT localhost:8080/zadd/key
* zcard= curl localhost:8080/zcard/key
* zrank= curl -d "member=key1" -X POST localhost:8080/zrank/key
* zrange=  curl -d first=1 -d last=2 -X POST localhost:8080/zrange/key
