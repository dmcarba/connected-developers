package com.exercise.service.adapter.out;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.exercise.service.dto.RegistryDTO;
import com.exercise.service.dto.RegistryInfoDTO;

/**
 * CRUD operations + query for getting the registered invocations
 * @author davidcarballo
 *
 */
public interface RegistryRepository extends MongoRepository<RegistryDTO, String> {
	/* Instead of using the more complex query: 
	@Query("{'$or':[{'$and':[{'dev1':?0}, {'dev2':?1}]},{'$and':[{'dev1':?1}, {'dev2':?0}]} ]}")
	we rely on the lexicographically ordering of dev1 and dev2 when stored*/
	
	List<RegistryInfoDTO> findByDev1AndDev2(String dev1, String dev2);
}
