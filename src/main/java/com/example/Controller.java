package com.example;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * Controller class with endpoints to get Euro to US Dollar currency exchange rates that were stored by this service.
 * 
 * @author Kristian Lange (2017)
 */
@RestController
public class Controller {

	@Autowired
	private ExchangeRateRepository repository;

	@Autowired
	private ExchangeRatesStreamer exchangeRatesStreamer;

	/**
	 * @return Returns the latest exchange rate by creation date
	 */
	@RequestMapping("/latestRate")
	public String getLatestRate() {
		ExchangeRateModel last = repository.findTopByOrderByCreatedDateDesc();
		return last.getEurToUsd();
	}

	/**
	 * Gets all exchange rates between the startDate and the endDate in JSON.
	 * 
	 * Both endpoints '/historyRates' and '/historyRateStream' do the nearly same thing. This one simply produces a string
	 * response. It first gets all data from the database and subsequently turns them into a JSON string
	 * 
	 * Both parameters, startDate and endDate, are in the ISO DateTime Format yyyy-MM-dd'T'HH:mm:ss.SSSZ, e.g.
	 * "2000-10-31T01:30:00.000-05:00" although the milliseconds and timezone can be omitted and a valid request could
	 * be /historyRates?startDate=2017-02-26T15:40:00&endDate=2017-02-26T20:19:00
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Returns a JSON string with the rates and their creation dates
	 */
	@RequestMapping(value = "/historyRates", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
	public List<ExchangeRateModel> getHistoryRates(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		Stream<ExchangeRateModel> stream = repository.findByCreatedDateBetween(startDate, endDate);
		return stream.collect(Collectors.toList());
	}

	/**
	 * Gets all exchange rates between the startDate and the endDate in JSON.
	 * 
	 * Both endpoints '/historyRates' and '/historyRateStream' do the nearly same thing. The difference with this one is that
	 * it produces a chunked HTTP response that is like a stream. The HTTP response starts being transfered before all
	 * data is retrieved from the database. Use this endpoint if you expect the returned data to be rather big.
	 * 
	 * Both parameters, startDate and endDate, are in the ISO DateTime Format yyyy-MM-dd'T'HH:mm:ss.SSSZ, e.g.
	 * "2000-10-31T01:30:00.000-05:00" although the milliseconds and timezone can be omitted and a valid request could
	 * be /historyRateStream?startDate=2017-02-26T15:40:00&endDate=2017-02-26T20:19:00
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Returns a StreamingResponseBody containing the ExchangeRateModels as JSON strings one after another
	 */
	@RequestMapping(value = "/historyRateStream", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
	public StreamingResponseBody getHistoryRateStream(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		return new StreamingResponseBody() {
			@Override
			public void writeTo(OutputStream outputStream) {
				exchangeRatesStreamer.getExchangeRatesBetween(startDate, endDate, outputStream);
			}
		};
	}

}
