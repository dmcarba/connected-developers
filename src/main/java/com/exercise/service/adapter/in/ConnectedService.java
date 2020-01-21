package com.exercise.service.adapter.in;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exercise.service.dto.RealTimeServiceResponseDTO;
import com.exercise.service.dto.RegistryInfoDTO;

/**
 * Api definition
 * @author davidcarballo
 *
 */
public interface ConnectedService {
	
	/**
	 * return if the developers are connected and what GitHub organizations they have in common
	 * @param dev1 Developer handle
	 * @param dev2 Developer handle
	 * @return RealTimeServiceResponseDTO indicating if connected and the common orgs
	 * @throws Exception
	 */
	@GetMapping("/connected/realtime/{user1}/{user2}")
	@ResponseBody
	public RealTimeServiceResponseDTO connectedRealTime(@PathVariable String user1, @PathVariable String user2) throws Exception;

	/**
	 * return all the related information from previous requests to the real-time endpoint
	 * @param dev1 Developer handle
	 * @param dev2 Developer handle
	 * @return List<RegistryDTO> with the records stored referencing both developers.
	 */
	@GetMapping("/connected/register/{user1}/{user2}")
	@ResponseBody
	public List<RegistryInfoDTO> connectedRegistry(@PathVariable String user1, @PathVariable String user2);
	
}
