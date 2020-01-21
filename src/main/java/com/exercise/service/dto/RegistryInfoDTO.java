package com.exercise.service.dto;

import java.util.Date;
import java.util.List;


public class RegistryInfoDTO {

    
    private boolean connected;
    
    private Date registered_at;
    
    private List<String> organisations;
    
    public RegistryInfoDTO(boolean connected, Date registered_at, List<String> organisations) {
    	this.connected = connected;
    	this.registered_at = registered_at;
    	this.organisations = organisations;
    }

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public Date getRegistered_at() {
		return registered_at;
	}

	public void setRegistered_at(Date registered_at) {
		this.registered_at = registered_at;
	}

	public List<String> getOrganisations() {
		return organisations;
	}

	public void setOrganisations(List<String> organisations) {
		this.organisations = organisations;
	}
 
}