package hu.illesjosh.bulbwatch.model;

import java.net.URL;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Product {

	@NonNull
	private URL url;
	private String name;
	private LocalDate lastReviewDate;

}
