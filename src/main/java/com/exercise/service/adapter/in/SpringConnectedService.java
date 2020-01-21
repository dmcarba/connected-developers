package com.exercise.service.adapter.in;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.service.adapter.out.GitHubService;
import com.exercise.service.adapter.out.RegistryRepository;
import com.exercise.service.adapter.out.TwitterClient;
import com.exercise.service.dto.GitHubUserOrganisationsDTO;
import com.exercise.service.dto.RealTimeServiceResponseDTO;
import com.exercise.service.dto.RegistryDTO;
import com.exercise.service.dto.RegistryInfoDTO;
import com.exercise.service.exception.IdNotFoundException;

/**
 * For simplicity the domain business implementation is the endpoint
 * interface implementing class
 * 
 * @author davidcarballo
 *
 */
@RestController
public class SpringConnectedService implements ConnectedService {

	public static String DEVNOTGITHUB = " is not a valid user in Github";
	public static String DEVNOTTWITTER = " is not a valid user in Twitter";

	private static final Logger log = LoggerFactory.getLogger(SpringConnectedService.class);

	@Autowired
	private GitHubService gitHubService;

	@Autowired
	private TwitterClient twitterClient;

	@Autowired
	private RegistryRepository registryRepository;

	@Override
	public RealTimeServiceResponseDTO connectedRealTime(@PathVariable String user1, @PathVariable String user2)
			throws Exception {

		log.debug("Checking connections for {} and {}", user1, user2);

		List<String> errors = new ArrayList<String>();
		CompletableFuture<GitHubUserOrganisationsDTO> devOrgs1 = gitHubService.getUserOrgs(user1);
		CompletableFuture<GitHubUserOrganisationsDTO> devOrgs2 = gitHubService.getUserOrgs(user2);
		CompletableFuture<Boolean> dev1inTwitter = twitterClient.exists(user1);
		CompletableFuture<Boolean> dev2inTwitter = twitterClient.exists(user2);

		// According to requirements, we cannot fail fast, need to check if users exists
		// in both systems. Calls executed in parallel
		if (!devOrgs1.get().isPresent()) {
			errors.add(user1 + DEVNOTGITHUB);
		}
		if (!devOrgs2.get().isPresent()) {
			errors.add(user2 + DEVNOTGITHUB);
		}
		if (!dev1inTwitter.get()) {
			errors.add(user1 + DEVNOTTWITTER);
		}
		if (!dev2inTwitter.get()) {
			errors.add(user2 + DEVNOTTWITTER);
		}

		if (!errors.isEmpty()) {
			throw new IdNotFoundException(errors);
		}

		List<String> result = ListUtils.intersection(devOrgs1.get().getOrgs(), devOrgs2.get().getOrgs());

		boolean connected = !result.isEmpty();

		if (connected) {
			connected = twitterClient.mutualFriends(user1, user2);
		}

		// dev1 and dev2 values stored with deterministic ordering
		DeveloperPair pair = getOrderedPair(user1, user2);

		RegistryDTO registry = new RegistryDTO(pair.getFirst(), pair.getLast(), connected,
				new Date(System.currentTimeMillis()), result);
		registryRepository.save(registry);

		return new RealTimeServiceResponseDTO(connected, result);
	}

	@Override
	public List<RegistryInfoDTO> connectedRegistry(@PathVariable String user1, @PathVariable String user2) {
		DeveloperPair pair = getOrderedPair(user1, user2);
		// dev1 and dev2 values queried with deterministic ordering
		return registryRepository.findByDev1AndDev2(pair.getFirst(), pair.getLast());
	}

	private DeveloperPair getOrderedPair(String user1, String user2) {
		if (user1.compareTo(user2) < 0) {
			return new DeveloperPair(user1, user2);
		} else {
			return new DeveloperPair(user2, user1);
		}
	}

	private static class DeveloperPair {
		private String first;
		private String last;

		DeveloperPair(String first, String last) {
			this.first = first;
			this.last = last;
		}

		public String getFirst() {
			return first;
		}

		public String getLast() {
			return last;
		}

	}

}
