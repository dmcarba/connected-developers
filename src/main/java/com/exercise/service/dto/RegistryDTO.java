package com.exercise.service.dto;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "registry")
@CompoundIndexes({
    @CompoundIndex(name = "dev1_dev2", def = "{'dev1' : 1, 'dev2': 1}")
})
public class RegistryDTO {

    private String dev1;

    private String dev2;
    
    private boolean connected;
    
    private Date registered_at;
    
    private List<String> organisations;
    
    public RegistryDTO(String dev1, String dev2, boolean connected, Date registered_at, List<String> organisations) {
    	this.dev1 = dev1;
    	this.dev2 = dev2;
    	this.connected = connected;
    	this.registered_at = registered_at;
    	this.organisations = organisations;
    }

	public String getDev1() {
		return dev1;
	}

	public void setDev1(String dev1) {
		this.dev1 = dev1;
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

	public String getDev2() {
		return dev2;
	}

	public void setDev2(String dev2) {
		this.dev2 = dev2;
	}

	@Override
	public String toString() {
		return "Registry [dev1=" + dev1 + ", dev2=" + dev2 + ", connected=" + connected + ", registered_at="
				+ registered_at + ", organisations=" + organisations + "]";
	}
 
}