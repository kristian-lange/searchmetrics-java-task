package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.ExchangeRateModel;
import com.example.ExchangeRateRepository;
import com.example.ExchangeRatesStreamer;

/**
 * Tests for class ExchangeRatesStreamer
 * 
 * @author Kristian Lange (2017)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class ExchangeRatesStreamerTest {

	@Autowired
	private ExchangeRatesStreamer exchangeRatesStreamer;

	@Autowired
	private ExchangeRateRepository repository;

	/**
	 * Tests ExchangeRatesStreamer.getExchangeRatesBetween(): Should put the serialized ExchangeRateModels into the
	 * OutputStream
	 */
	@Test
	public void testGetExchangeRatesBetween() throws IOException {
		// Store one ExchangeRateModel
		ExchangeRateModel exchangeRateModel = new ExchangeRateModel();
		exchangeRateModel.setEurToUsd("1.1234");
		exchangeRateModel.setCreatedDate(LocalDateTime.now());
		repository.save(exchangeRateModel);

		// Now retrieve it again
		LocalDateTime startDate = LocalDateTime.now().minusHours(1l);
		LocalDateTime endDate = LocalDateTime.now().plusHours(1l);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		exchangeRatesStreamer.getExchangeRatesBetween(startDate, endDate, outputStream);

		// Check that the eurToUsd field in the JSON string is correct
		String response = new String(outputStream.toByteArray(), "UTF-8");
		assertThat(response).isNotEmpty();
		assertThat(response).contains("\"eurToUsd\":\"1.1234\"");
	}

	/**
	 * Tests ExchangeRatesStreamer.getExchangeRatesBetween(): If there aren't any entries in the database for the given
	 * dates the OutputStream should be empty
	 */
	@Test
	public void testGetExchangeRatesBetweenEmpty() {
		LocalDateTime startDate = LocalDateTime.now();
		LocalDateTime endDate = LocalDateTime.now().plusHours(1l);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		exchangeRatesStreamer.getExchangeRatesBetween(startDate, endDate, outputStream);
		assertThat(outputStream.toByteArray()).isEmpty();
	}

}
