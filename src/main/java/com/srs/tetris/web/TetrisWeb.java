package com.srs.tetris.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@SpringBootApplication
public class TetrisWeb {
	public static void main(String[] args) {
		SpringApplication.run(TetrisWeb.class, args);
	}

	@Configuration
	public static class WebConfiguration {
		@Bean
		public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
			MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			jsonConverter.setObjectMapper(objectMapper);
			return jsonConverter;
		}
	}
}
