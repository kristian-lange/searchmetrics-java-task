package com.searchmetrics;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * This service class handles the retrieving of the Euro to US Dollar exchange rate from an external resource. It does
 * so regularly and the results are stored in the database.
 * 
 * @author Kristian Lange (2017)
 */
@Service
public class EuroDollarExchangeRateChecker {

	private static final Logger LOGGER = LoggerFactory.getLogger(EuroDollarExchangeRateChecker.class);

	/**
	 * External Rest API endpoint that provides currency exchange rates
	 */
	private static final String FIXER_IO_ENDPOINT_EUR_TO_USD = "http://api.fixer.io/latest?symbols=USD,EUR";

	@Autowired
	private ExchangeRateRepository repository;

	/**
	 * This method calls an external public service to retrieve the currency exchange rate euro -> dollar. It does so
	 * regularly using Spring's scheduler. The period time is specified in the 'application.properties' in the property
	 * 'searchmetrics.check-exchange-rate-period'. It maps the rate to a ExchangeRateModel and adds the creation date.
	 * In the end it stores the model into the database.
	 */
	@Scheduled(fixedRateString = "${searchmetrics.check-exchange-rate-period}")
	public void check() {
		RestTemplate restTemplate = new RestTemplate();
		ExchangeRateModel exchangeRateModel = restTemplate.getForObject(FIXER_IO_ENDPOINT_EUR_TO_USD,
				ExchangeRateModel.class);
		exchangeRateModel.setCreatedDate(LocalDateTime.now());
		repository.save(exchangeRateModel);
		LOGGER.debug(exchangeRateModel.toString());
	}

}
