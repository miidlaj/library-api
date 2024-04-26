package com.midlaj.olikassigment;

import com.midlaj.olikassigment.model.Author;
import com.midlaj.olikassigment.model.Book;
import com.midlaj.olikassigment.model.Rental;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootApplication
public class OlikAssigmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlikAssigmentApplication.class, args);
	}


}
