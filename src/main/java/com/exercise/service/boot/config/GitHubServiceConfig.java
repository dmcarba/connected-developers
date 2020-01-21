package com.exercise.service.boot.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.exercise.service.adapter.out.GitHubService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class GitHubServiceConfig {
	
	@Value("${github.endpoint:https://api.github.com/users/{user}/orgs}")
	private String gitHubURL;

	private ObjectMapper jacksonObjectMapper() {
		return new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
		jsonMessageConverter.setObjectMapper(jacksonObjectMapper());
		messageConverters.add(jsonMessageConverter);
		restTemplate.setMessageConverters(messageConverters);

		return restTemplate;
	}

	@Bean
	public GitHubService gitHubService(@Autowired RestTemplate restTemplate) {
		return new GitHubService(restTemplate, gitHubURL);
	}

}
