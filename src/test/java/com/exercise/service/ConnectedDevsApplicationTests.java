package com.exercise.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.exercise.service.adapter.in.ConnectedService;
import com.exercise.service.adapter.in.SpringConnectedService;
import com.exercise.service.adapter.out.GitHubService;
import com.exercise.service.adapter.out.TwitterClient;
import com.exercise.service.dto.GitHubUserOrganisationsDTO;
import com.exercise.service.dto.RealTimeServiceResponseDTO;
import com.exercise.service.dto.RegistryInfoDTO;
import com.exercise.service.exception.IdNotFoundException;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnectedDevsApplicationTests {

	private static final String ORG4 = "org4";

	private static final String ORG3 = "org3";

	private static final String ORG2 = "org2";

	private static final String ORG1 = "org1";

	private static final String USER20 = "user20";

	private static final String USER10 = "user10";

	private static final String USER2 = "user2";

	private static final String USER1 = "user1";

	@MockBean
	private GitHubService mockGitHubService;

	@Autowired
	private ConnectedService connectedService;

	@MockBean
	private TwitterClient mockTwitterClient;


	@Test
	public void testRealTimeNotConnectedUsers() throws Exception {
		CompletableFuture<GitHubUserOrganisationsDTO> response = CompletableFuture
				.completedFuture(new GitHubUserOrganisationsDTO(new ArrayList<>(), true));
		Mockito.when(mockGitHubService.getUserOrgs(USER1)).thenReturn(response);
		Mockito.when(mockGitHubService.getUserOrgs(USER2)).thenReturn(response);
		Mockito.when(mockTwitterClient.exists(USER1)).thenReturn(CompletableFuture.completedFuture(true));
		Mockito.when(mockTwitterClient.exists(USER2)).thenReturn(CompletableFuture.completedFuture(true));

		RealTimeServiceResponseDTO serviceResponse = connectedService.connectedRealTime(USER1, USER2);

		assertFalse(serviceResponse.isConnected());
		assertTrue(serviceResponse.getOrganizations().isEmpty());

		Mockito.when(mockGitHubService.getUserOrgs(USER1)).thenReturn(CompletableFuture
				.completedFuture(new GitHubUserOrganisationsDTO(new ArrayList<>(Arrays.asList(ORG1, ORG2)), true)));
		Mockito.when(mockGitHubService.getUserOrgs(USER2)).thenReturn(CompletableFuture
				.completedFuture(new GitHubUserOrganisationsDTO(new ArrayList<>(Arrays.asList(ORG3, ORG4)), true)));
		serviceResponse = connectedService.connectedRealTime(USER1, USER2);
		assertFalse(serviceResponse.isConnected());
		assertTrue(serviceResponse.getOrganizations().isEmpty());
	}

	@Test
	public void testRealTimeConnectedUsers() throws Exception {

		Mockito.when(mockTwitterClient.exists(USER1)).thenReturn(CompletableFuture.completedFuture(true));
		Mockito.when(mockTwitterClient.mutualFriends(USER1, USER2))
				.thenReturn(true);
		Mockito.when(mockTwitterClient.exists(USER2)).thenReturn(CompletableFuture.completedFuture(true));

		Mockito.when(mockGitHubService.getUserOrgs(USER1)).thenReturn(CompletableFuture
				.completedFuture(new GitHubUserOrganisationsDTO(new ArrayList<>(Arrays.asList(ORG1, ORG2)), true)));
		Mockito.when(mockGitHubService.getUserOrgs(USER2)).thenReturn(CompletableFuture
				.completedFuture(new GitHubUserOrganisationsDTO(new ArrayList<>(Arrays.asList(ORG2, ORG3)), true)));
		RealTimeServiceResponseDTO serviceResponse = connectedService.connectedRealTime(USER1, USER2);

		assertTrue(serviceResponse.isConnected());
		assertFalse(serviceResponse.getOrganizations().isEmpty());
		assertEquals(ORG2, serviceResponse.getOrganizations().get(0));
	}

	@Test
	public void testRegisteredConnectedUsers() throws Exception {
		List<RegistryInfoDTO> registries = connectedService.connectedRegistry(USER10, USER20);
		assertTrue(registries.isEmpty());
		Mockito.when(mockTwitterClient.exists(USER10)).thenReturn(CompletableFuture.completedFuture(true));
		Mockito.when(mockTwitterClient.mutualFriends(USER10, USER20))
				.thenReturn(true);
		Mockito.when(mockTwitterClient.exists(USER20)).thenReturn(CompletableFuture.completedFuture(true));;
		Mockito.when(mockGitHubService.getUserOrgs(USER10)).thenReturn(CompletableFuture
				.completedFuture(new GitHubUserOrganisationsDTO(new ArrayList<>(Arrays.asList(ORG1, ORG2)), true)));
		Mockito.when(mockGitHubService.getUserOrgs(USER20)).thenReturn(CompletableFuture
				.completedFuture(new GitHubUserOrganisationsDTO(new ArrayList<>(Arrays.asList(ORG2, ORG3)), true)));
		connectedService.connectedRealTime(USER10, USER20);
		registries = connectedService.connectedRegistry(USER10, USER20);
		RegistryInfoDTO registry = registries.get(0);
		assertEquals(ORG2, registry.getOrganisations().get(0));

		assertEquals(1, registry.getOrganisations().size());
	}

	@Test
	public void testRealTimeInvalidUsers() throws Exception {
		Mockito.when(mockTwitterClient.exists(USER1)).thenReturn(CompletableFuture.completedFuture(false));
		Mockito.when(mockTwitterClient.exists(USER2)).thenReturn(CompletableFuture.completedFuture(false));

		Mockito.when(mockGitHubService.getUserOrgs(USER1)).thenReturn(CompletableFuture
				.completedFuture(new GitHubUserOrganisationsDTO(new ArrayList<>(Arrays.asList()), false)));
		Mockito.when(mockGitHubService.getUserOrgs(USER2)).thenReturn(CompletableFuture
				.completedFuture(new GitHubUserOrganisationsDTO(new ArrayList<>(Arrays.asList()), false)));
		try {
			connectedService.connectedRealTime(USER1, USER2);
			fail("Exception not thrown");
		} catch (IdNotFoundException e) {
			assertTrue(e.getErrors().contains(USER1+SpringConnectedService.DEVNOTGITHUB)
					&& e.getErrors().contains(USER2+SpringConnectedService.DEVNOTGITHUB)
					&& e.getErrors().contains(USER1+SpringConnectedService.DEVNOTTWITTER)
					&& e.getErrors().contains(USER2+SpringConnectedService.DEVNOTTWITTER));
		}

	}

}
