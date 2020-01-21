package com.exercise.service.adapter.out;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.exercise.service.dto.GitHubOrganisationDTO;
import com.exercise.service.dto.GitHubUserOrganisationsDTO;

@Configuration
public class GitHubService {

	private static final Logger log = LoggerFactory.getLogger(GitHubService.class);

	private RestTemplate restTemplate;

	private String templateURL;

	public GitHubService(RestTemplate restTemplate, String templateURL) {
		this.restTemplate = restTemplate;
		this.templateURL = templateURL;
	}

	/**
	 * Returns if the user is valid and the organizations it is member of.
	 * 
	 * @param user
	 * @return GitHubUserOrganisationsDTO
	 */
	@Async
	public CompletableFuture<GitHubUserOrganisationsDTO> getUserOrgs(String user) {
		List<String> orgs = new ArrayList<>();
		boolean present = false;
		try {
			log.debug("getUserOrgs for dev: {}", user);
			ResponseEntity<GitHubOrganisationDTO[]> response = restTemplate.getForEntity(templateURL,
					GitHubOrganisationDTO[].class, user);
			GitHubOrganisationDTO[] orgList = response.getBody();
			for (int i = 0; i < orgList.length; i++) {
				orgs.add(orgList[i].getLogin());
			}
			present = true;
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
				throw e;
			}
		}

		return CompletableFuture.completedFuture(new GitHubUserOrganisationsDTO(orgs, present));
	}

}
