# This is a Case study for Qred

### The project is build with 
-   Spring Boot
-   JWT tokens for authentication and authorization
-   In-memory H2 database


##Requirements
- Java 11 ( build and tested with OpenJDK 11 )
- Maven ( build and tested with 3.5.4 )

##Build the application and run the application

Navigate to the root of the application (where `pom.xml` is) and execute

```shell script
 mvn clean install
``` 
This will all execute all unit and integration tests
or if you navigate again to the root of the project you can start the app with

```shell script
mvn spring-boot:run
```
### h2 console
H2 console available at `/h2-console`
Database available at `jdbc:h2:mem:this-will-be-generated-at-runtime`
- user:`sa`
- pass:` ` (is empty) 


### hard coded users
there are 2 hardcoded users,That is the regular user
- username = "user";
- password = "password";

And The Agent

- username = "agent";
- password = "password";

### What the application supports
For the user
- Apply for loan.
- Get the latest application, including the offer details.
- Sign the application
- Get all signed contracts

For the Agent

- Get all applications that are pending
- Create a offer for a pending application

## Usage with cURL

In order to authenticate with valid user and pass POST on /authenticate
example:
```shell script
curl --location 'localhost:8080/authenticate' \
--header 'Content-Type: application/json' \
--data '{
  "username": "user",
  "password": "password"
}'

```
you will get back jwt token 
```shell script
`{
"jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNzA4MzkzNjczLCJpYXQiOjE3MDgzOTAwNzN9.Xpyee9OXIMjhD9FwoQ-vVAUxPVawLSvtSHiefJfc8Jo"
}`
```
that you will need for the endpoints for the user actions as a Bearer token

#### As a user you can apply for a Credit like
```shell script
curl --location 'http://localhost:8080/applications/apply' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNzA4MzkzNjczLCJpYXQiOjE3MDgzOTAwNzN9.Xpyee9OXIMjhD9FwoQ-vVAUxPVawLSvtSHiefJfc8Jo' \
--header 'Content-Type: application/json' \
--data-raw '{
  "amountApplied": 223110,
  "email": "test@test.com",
  "phoneNumber": "+46701234567",
  "organizationNumber": "5590089800"
}'
```
#### As a Agent you can view all  Credit Applications with status Pending
```shell script
curl --location 'http://localhost:8080/offers' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZ2VudCIsImV4cCI6MTcwODM5MTkxMiwiaWF0IjoxNzA4Mzg4MzEyfQ.wqb7Y1WSlyX8-5IiOR2usoJJYuARIzsQY9anmvBg780' \
--header 'Content-Type: application/json'
```

#### As a Agent you can make a offer for the given Credit Aplcation
```shell script
curl --location --request POST 'http://localhost:8080/offer/1' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZ2VudCIsImV4cCI6MTcwODM5MTkxMiwiaWF0IjoxNzA4Mzg4MzEyfQ.wqb7Y1WSlyX8-5IiOR2usoJJYuARIzsQY9anmvBg780' \
--header 'Content-Type: application/json'
}'
```

#### As a User you can See the offer for your Credit Application
```shell script
curl --location 'http://localhost:8080/applications/getOffers' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNzA4MzkxODM4LCJpYXQiOjE3MDgzODgyMzh9.xzBVKLvvzaDnrsibBwK0k24sr1yx2mV6yxUcaI_4v_w'
```

#### As a User you can Accept the offer for your Credit Application
```shell script
curl --location --request POST 'http://localhost:8080/applications/offer/sign/2' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNzA4MzkxODM4LCJpYXQiOjE3MDgzODgyMzh9.xzBVKLvvzaDnrsibBwK0k24sr1yx2mV6yxUcaI_4v_w' \
--header 'Content-Type: application/json'
```


#### As a User you can View All Signed Contracts
```shell script
curl --location 'http://localhost:8080/applications/getContracts' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNzA4MzkxODM4LCJpYXQiOjE3MDgzODgyMzh9.xzBVKLvvzaDnrsibBwK0k24sr1yx2mV6yxUcaI_4v_w' \
--header 'Content-Type: application/json'
```

#### As a User you can View All Credit Applications
```shell script
curl --location 'http://localhost:8080/applications' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNzA4MzkxODM4LCJpYXQiOjE3MDgzODgyMzh9.xzBVKLvvzaDnrsibBwK0k24sr1yx2mV6yxUcaI_4v_w' \
--header 'Content-Type: application/json'
```



## Known Issues

- jwt secret is hardcoded into the code
- logging should be better , monitoring should be added
- more unit and integration test should be added


![byebye](https://media.giphy.com/media/vFKqnCdLPNOKc/giphy.gif)

