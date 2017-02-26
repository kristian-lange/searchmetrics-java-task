# searchmetrics-java-task

## HTTP Endpoints

* `/latestRate`
* `/historyRates?startDate=2017-02-26T15:40:00&endDate=2017-02-26T22:19:29`
* `/historyRateStream?startDate=2017-02-26T15:40:00&endDate=2017-02-26T22:19:29`

## Run 

`mvn spring-boot:run`

## Test

`mvn test`

## Technology

It uses Spring Boot, Spring MVC, Spring Data, Maven, an embedded MongoDB, and Logback.

External configuration is done via the 'application.properties'.

It also activates scheduling that is used by class EuroDollarExchangeRateChecker to poll exchange rates from a public
Rest service.
 
This application uses Spring Data with an embedded MongoDB - but with Spring Data the database can be easily switched to a different one.
