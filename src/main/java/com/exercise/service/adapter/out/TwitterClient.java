package com.exercise.service.adapter.out;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterClient {

	private static final Logger log = LoggerFactory.getLogger(TwitterClient.class);

	private Twitter client;

	private static final int MAX_BATCH_SIZE = 200;

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
	 * Return true if user 2 is a follower of user1
	 * 
	 * @param user1
	 * @param user2
	 * @return A Future<Boolean> indicating if user2 is a follower of user1. The
	 *         task may need to be cancelled so a Future is used (CompletableFuture is not cancellable)
	 * @throws TwitterException
	 */
	@Async
	public Future<Boolean> isFollower(String user1, String user2) throws TwitterException {

		log.debug("check if user {} follows user {} ", user2, user1);
		PagableResponseList<User> followersList = client.getFollowersList(user1, -1, MAX_BATCH_SIZE);

		while (followersList.size() > 0) {
			if (Thread.currentThread().isInterrupted()) {
				return new AsyncResult<>(false);
			}
			for (int i = 0; i < followersList.size(); i++) {
				if (user2.equals(followersList.get(i).getScreenName())) {
					return new AsyncResult<>(true);
				}
			}
			followersList = client.getFollowersList(user1, followersList.getNextCursor(), MAX_BATCH_SIZE);
		}
		return new AsyncResult<>(false);
	}

}
