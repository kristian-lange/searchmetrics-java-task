package com.example;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * JSON model and database entity class of exchange rates. So far it contains only EUR to USD.
 * 
 * @author Kristian Lange (2017)
 */
@Document
public class ExchangeRateModel {

	/**
	 * Identifier in the database
	 */
	@Id
	@JsonIgnore
	private String id;

	/**
	 * Date when this model was created
	 */
	@CreatedDate
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime createdDate;

	/**
	 * Exchange rate from Euro to US Dollar
	 */
	private String eurToUsd;

	/**
	 * This method is used during JSON unmarshaling to get the exchange rate with the key 'USD' from the JSON object
	 * 'rates'.
	 * 
	 * @param rates
	 */
	@JsonProperty("rates")
	private void retrieveEurToUsd(Map<String, String> rates) {
		eurToUsd = rates.get("USD");
	}

	public String getEurToUsd() {
		return eurToUsd;
	}
	
	public void setEurToUsd(String eurToUsd) {
		this.eurToUsd = eurToUsd;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "ExchangeRateModel [createdDate=" + createdDate + ", eurToUsd=" + eurToUsd + "]";
	}

}
