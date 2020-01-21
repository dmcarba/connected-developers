package com.exercise.service.adapter.out;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterClient {

	private static final Logger log = LoggerFactory.getLogger(TwitterClient.class);

	private Twitter client;

	public TwitterClient(TwitterFactory factory, String consumerKey, String consumerSecret) {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setApplicationOnlyAuthEnabled(true);
		client = new TwitterFactory(builder.build()).getInstance();
		client.setOAuthConsumer(consumerKey, consumerSecret);
		try {
			client.getOAuth2Token();
		} catch (TwitterException ex) {
			throw new IllegalStateException("Twitter app login has failed!", ex);
		}
	}

	/**
	 * Returns if the user is a valid twitter user
	 * 
	 * @param user
	 * @return true if this is a valid handle in Twitter
	 * @throws TwitterException
	 */
	@Async
	public CompletableFuture<Boolean> exists(String user) throws TwitterException {
		boolean result = false;
		log.debug("check if user {} exists", user);
		try {
			client.showUser(user);
			result = true;
		} catch (TwitterException e) {
			if (e.getStatusCode() != 404)
				throw e;
		}
		return CompletableFuture.completedFuture(result);
	}

	/**
	 * Return true if user 1 and 2 are mutual friends
	 * 
	 * @param user1
	 * @param user2
	 * @return A boolean indicating if users are mutual friends
	 * @throws TwitterException
	 */	
	public boolean mutualFriends(String user1, String user2) throws TwitterException {
		log.debug("check if user {} is friend with user {} ", user2, user1);
		Relationship rel = client.showFriendship(user1, user2);
		return rel.isSourceFollowingTarget() && rel.isTargetFollowingSource();
	}

}
