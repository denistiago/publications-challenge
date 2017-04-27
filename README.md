# publications-challenge

## before you run

You need to set up mongo and rabbitmq dependencies provided as docker containers:
```shell
docker-compose up
```

## tests
```shell
mvn test
```

## running
```
mvn spring-boot:run
```

### documents rest endpoints

```shell
curl -XPOST -H "Content-Type: application/json" http://localhost:8080/documents -d '{"title": "The Dark Code", "author": "Bruce Wayne"}'
```

```shell
curl -XGET http://localhost:8080/documents/{DOCUMENT_ID}
```

```shell
curl -XGET http://localhost:8080/documents?page=0&size=10
```


### watermark rest endpoints

```shell
curl -XPOST -H "Content-Type: application/json" http://localhost:8080/documents/{DOCUMENT_ID}/watermark -d '{"content":"book", "title":"The Dark Code","author":"Bruce Wayne","topic":"Science"}'
```

```shell
curl http://localhost:8080/watermarks/{WATERMARK_ID}
```