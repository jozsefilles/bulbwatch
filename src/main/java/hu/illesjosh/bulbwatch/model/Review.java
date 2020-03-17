package hu.illesjosh.bulbwatch.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {

	private String author;
	private LocalDateTime date;
	private String content;

}
