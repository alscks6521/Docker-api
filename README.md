### 작업했던 TDD-API 서비스를 Docker로.
#### TODO  
- deploy > mysql > [ Dockerfile, setup.sql ]
- deploy > springboot > [ Dockerfile ]
- docker-compose.yaml


#### 민감한 정보 처리  
```yaml
예: MYSQL_ROOT_PASSWORD=your_local_password //변수로만 작성할 것.

SPRING_DATASOURCE_PASSWORD: ${MYSQL_ROOT_PASSWORD}

MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: ${MYSQL_DATABASE}
```
