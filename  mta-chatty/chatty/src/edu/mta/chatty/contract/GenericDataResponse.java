package edu.mta.chatty.contract;

import java.util.List;

import edu.mta.chatty.domain.City;
import edu.mta.chatty.domain.Country;

public interface GenericDataResponse {
	List<Country> getCountries();
	List<City> getCities();
}
