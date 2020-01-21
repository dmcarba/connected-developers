package com.exercise.service.dto;

import java.util.List;

public class RealTimeServiceResponseDTO {
	
	private boolean connected;
	private List<String> organizations;
	
	public RealTimeServiceResponseDTO(boolean connected, List<String> organizations) {
		this.setConnected(connected);
		this.setOrganizations(organizations);
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public List<String> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<String> organizations) {
		this.organizations = organizations;
	}

}
