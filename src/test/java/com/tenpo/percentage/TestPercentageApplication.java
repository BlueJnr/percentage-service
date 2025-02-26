package com.tenpo.percentage;

import org.springframework.boot.SpringApplication;

public class TestPercentageApplication {

	public static void main(String[] args) {
		SpringApplication.from(PercentageApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
