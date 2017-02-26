# searchmetrics-java-task

## HTTP Endpoints

### `/latestRate`

Returns the latest exchange rate by creation date

### `/historyRates?startDate=2017-02-26T15:40:00&endDate=2017-02-26T22:19:29`

Gets all exchange rates between the startDate and the endDate in JSON.

Both endpoints '/historyRates' and '/historyRateStream' do the nearly same thing. This one simply produces a string response. It first gets all data from the database and subsequently turns them into a JSON string.
  
Both parameters, startDate and endDate, are in the ISO DateTime Format yyyy-MM-dd'T'HH:mm:ss.SSSZ, e.g. "2000-10-31T01:30:00.000-05:00" although the milliseconds and timezone can be omitted and a valid request could be /historyRates?startDate=2017-02-26T15:40:00&endDate=2017-02-26T20:19:00
  
### `/historyRateStream?startDate=2017-02-26T15:40:00&endDate=2017-02-26T22:19:29`

Gets all exchange rates between the startDate and the endDate in JSON.
	 
Both endpoints '/historyRates' and '/historyRateStream' do the nearly same thing. The difference with this one is that it produces a chunked HTTP response that is like a stream. The HTTP response starts being transfered before all data is retrieved from the database. Use this endpoint if you expect the returned data to be rather big.

Both parameters, startDate and endDate, are in the ISO DateTime Format yyyy-MM-dd'T'HH:mm:ss.SSSZ, e.g. "2000-10-31T01:30:00.000-05:00" although the milliseconds and timezone can be omitted and a valid request could be /historyRateStream?startDate=2017-02-26T15:40:00&endDate=2017-02-26T20:19:00

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
