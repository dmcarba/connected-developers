package com.exercise.service.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.exercise.service.adapter.out.TwitterClient;

import twitter4j.TwitterFactory;

@Configuration
public class TwitterClientConfig {
	
	@Value("${twitter.consumerKey}")
	private String consumerKey;
	
	@Value("${twitter.consumerSecret}")
	private String consumerSecret;

	@Bean
	public TwitterFactory twitterFactory() {
		return new TwitterFactory();
	}

	@Bean
	public TwitterClient twitterClient(@Autowired TwitterFactory twitterFactory) {
		return new TwitterClient(twitterFactory, consumerKey, consumerSecret);
	}

}
