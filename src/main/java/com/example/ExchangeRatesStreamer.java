package com.example;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service class that handles streaming of database results into a OutputStream
 * 
 * @author Kristian Lange (2017)
 */
@Service
public class ExchangeRatesStreamer {

	@Autowired
	private ExchangeRateRepository repository;

	/**
	 * Jackson's JSON mapper - it must be configured not to automatically close an output stream in order to use it with
	 * Spring MVC StreamingResponseBody
	 */
	private final ObjectMapper jsonMapper = new ObjectMapper();
	{
		jsonMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
	}

	/**
	 * Gets all exchange rates between the startDate and the endDate and puts them into the given OutputStream. It
	 * queries the database, serializes the resulting ExchangeRateModels into JSON strings and writes them into the
	 * given OutputStream.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param outputStream
	 */
	public void getExchangeRatesBetween(LocalDateTime startDate, LocalDateTime endDate, OutputStream outputStream) {
		Stream<ExchangeRateModel> stream = repository.findByCreatedDateBetween(startDate, endDate);
		fillOutputStreamWithModelStream(stream, outputStream);
	}

	/**
	 * Fills the given OutputStream with JSON from the data of the given Java 8 stream
	 * 
	 * @param modelStream
	 *            Java 8 stream of ExchangeRateModels
	 * @param outputStream
	 */
	private void fillOutputStreamWithModelStream(Stream<ExchangeRateModel> modelStream, OutputStream outputStream) {
		modelStream.forEach((m) -> writeModelAsJsonToStream(m, outputStream));
	}

	/**
	 * Uses Jackson to turn a ExchangeRateModel into JSON and writes it into the the given OutputStream.
	 * 
	 * @param model
	 *            an ExchangeRateModel
	 * @param outputStream
	 */
	private void writeModelAsJsonToStream(ExchangeRateModel model, OutputStream outputStream) {
		try {
			jsonMapper.writeValue(outputStream, model);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
