# DynatraceInternTask

This task was implemented using Spring

## To start server:

#### 1) Using IntelliJ Idea
* go to src/main/java/org/LamberM/App.java class
* run 'App'

#### 2) Using command line
* cd to project root (where the pom.xml file is located)
* run mvn org.springframework.boot:spring-boot-maven-plugin:run

Server will start at http://localhost:8080/
## Available endpoints:
### /api/exchange/rates/average/{currencyCode}/{date}

This endpoint will provide average exchange rate given currency code and date.

* field {currencyCode} - three-letter currency code ([ISO 4217](https://pl.wikipedia.org/wiki/ISO_4217))
  * Will return 400 Bad Request given PLN
* field {date} - date w format YYYY-MM-DD ([ISO 8601](https://pl.wikipedia.org/wiki/ISO_8601))
  * Can't be later than today

### /api/exchange/rates/majorDifferenceBuyAsk/{currencyCode}/{topCount}

This endpoint will provide the max and min average value.

* field {currencyCode} - three-letter currency code ([ISO 4217](https://pl.wikipedia.org/wiki/ISO_4217))
  * Will return 400 Bad Request given PLN
* field {topCount} - integer specifying the maximum number of returned data series.
  * ranged 1-255

### /api/exchange/rates/minMaxAverageValue/{currencyCode}/{topCount}

This endpoint will provide the major difference between the buy and ask rate.

* field {currencyCode} - three-letter currency code ([ISO 4217](https://pl.wikipedia.org/wiki/ISO_4217))
  * Will return 400 Bad Request given PLN
* field {topCount} - integer specifying the maximum number of returned data series.
  * ranged 1-255

## Response codes

#### 200 OK - when operation is successful

#### 400 Bad Request - when validation error occurs

#### 404 Not Found - when no data in NBP API

## Examples
1) USD average exchange rate in 2023-04-11

URL:
GET http://localhost:8080/api/exchange/rates/average/USD/2023-04-11


response code:

200 OK

response body:

{"average":4.2917}

2) USD average exchange rate in 2023-04-10

URL:
GET http://localhost:8080/api/exchange/rates/average/USD/2023-04-10

response code:

404 Not Found

response body:
{"httpCode":404,"message":"404 Not Found - Brak danych: \"404 NotFound - Not Found - Brak danych\""}

3) USD min max average value in last 10

URL:
GET http://localhost:8080/api/exchange/rates/minMaxAverageValue/USD/10


response code:

200 OK

response body:
{"min":4.128,"max":4.2916}

4) USD min max average value in last 256

URL:
GET http://localhost:8080/api/exchange/rates/minMaxAverageValue/usd/256


response code:

400 Bad Request

response body:

{"httpCode":400,"message":"Top count can't be more than 255 and less or equal 0"}

5) USD min max average value in last 0

URL:
GET http://localhost:8080/api/exchange/rates/minMaxAverageValue/usd/0

response code:

400 Bad Request

response body:

{"httpCode":400,"message":"Top count can't be more than 255 and less or equal 0"}

6) USD major difference buy ask rate in last 10

URL:
GET http://localhost:8080/api/exchange/rates/majorDifferenceBuyAsk/usd/10

response code:

200 OK

response body:

{"average":0.08499999999999996}

7) USD major difference buy ask rate in last 256 

URL:
GET http://localhost:8080/api/exchange/rates/majorDifferenceBuyAsk/usd/256


response code:

400 Bad Request

response body:

{"httpCode":400,"message":"Top count can't be more than 255 and less or equal 0"}

8) USD major difference buy ask rate in last 0

URL:
GET http://localhost:8080/api/exchange/rates/majorDifferenceBuyAsk/usd/0


response code:

400 Bad Request

response body:

{"httpCode":400,"message":"Top count can't be more than 255 and less or equal 0"}

9) PLN major difference buy ask rate in last 10

URL:
GET http://localhost:8080/api/exchange/rates/majorDifferenceBuyAsk/usd/0

response code:

400 Bad Request

response body:

{"httpCode":400,"message":"PLN is a comparable currency"}