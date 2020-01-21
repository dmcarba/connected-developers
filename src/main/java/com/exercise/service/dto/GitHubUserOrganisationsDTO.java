package com.exercise.service.dto;

import java.util.List;

public class GitHubUserOrganisationsDTO {

	public GitHubUserOrganisationsDTO(List<String> orgs, boolean present) {
		this.orgs = orgs;
		this.present = present;
	}

	private List<String> orgs;

	private boolean present;

	public List<String> getOrgs() {
		return orgs;
	}

	public boolean isPresent() {
		return present;
	}

}
