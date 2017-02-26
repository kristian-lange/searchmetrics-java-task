package com.example;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository class using Spring Data for MongoDB (Documentation
 * http://docs.spring.io/spring-data/data-mongo/docs/current/reference/html/) The parent class MongoRepository provides
 * all CRUD methods already.
 * 
 * @author Kristian Lange (2017)
 */
public interface ExchangeRateRepository extends MongoRepository<ExchangeRateModel, String> {

	/**
	 * Finds the last entry (according to the creation date) in the database
	 * 
	 * @return an ExchangeRateModel
	 */
	public ExchangeRateModel findTopByOrderByCreatedDateDesc();

	/**
	 * Finds all database entries that were created between the given dates and returns them as a Java 8 Stream.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return a stream of ExchangeRateModels
	 */
	public Stream<ExchangeRateModel> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
