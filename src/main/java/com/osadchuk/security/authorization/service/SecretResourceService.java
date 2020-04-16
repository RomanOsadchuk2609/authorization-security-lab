/*
 * Copyright (c) 2020 Roman Osadchuk. ALL RIGHTS RESERVED.
 */

package com.osadchuk.security.authorization.service;

import com.osadchuk.security.authorization.model.ApiResponse;
import com.osadchuk.security.authorization.model.SecretResource;
import com.osadchuk.security.authorization.securityConfig.accessControlConfig.SecretResourceAccessControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class SecretResourceService {

	private final AtomicInteger idCounter = new AtomicInteger(1);

	private final UserDetailsServiceImpl userDetailsService;

	private final SecretResourceAccessControl accessControl;

	private final AuditLogService auditLogService;

	private List<SecretResource> secretResourceList = new ArrayList<>();

	@Autowired
	public SecretResourceService(UserDetailsServiceImpl userDetailsService,
	                             SecretResourceAccessControl accessControl,
	                             AuditLogService auditLogService) {
		this.userDetailsService = userDetailsService;
		this.accessControl = accessControl;
		this.auditLogService = auditLogService;
	}

	public List<Integer> getResourcesIds() {
		return secretResourceList.stream()
				.map(SecretResource::getId)
				.collect(Collectors.toList());
	}

	public ApiResponse createResource(String username, SecretResource secretResource) {
		ApiResponse apiResponse = new ApiResponse();
		try {
			int usersAccessLevel = userDetailsService.getUsersAccessLevelByUsername(username);
			secretResource.setId(idCounter.getAndIncrement());
			secretResourceList.add(secretResource);
			accessControl.saveObjectsAccessLevel(secretResource.getId(), usersAccessLevel);
			apiResponse.setData(secretResource);
			auditLogService.addRecord(username, "Created object: " + secretResource);
		} catch (UsernameNotFoundException ex) {
			apiResponse.setError(ex.getMessage());
		}
		return apiResponse;
	}

	public ApiResponse readResource(String username, int secretResourceId) {
		ApiResponse apiResponse = new ApiResponse();
		try {
			SecretResource secretResource = findById(secretResourceId);
			if (secretResource != null) {
				int usersAccessLevel = userDetailsService.getUsersAccessLevelByUsername(username);
				if (accessControl.canRead(secretResourceId, usersAccessLevel)) {
					apiResponse.setData(secretResource);
					auditLogService.addRecord(username, "Read object: " + secretResource);
				} else {
					apiResponse.setError("User " + username + " can't read SecretResource with id " + secretResourceId);
					auditLogService.addRecord(username, "User " + username + " can't read SecretResource with id " + secretResourceId);
				}
			} else {
				apiResponse.setError("SecretResource with id " + secretResourceId + " not found");
				auditLogService.addRecord(username, ("SecretResource with id " + secretResourceId + " not found"));
			}
		} catch (UsernameNotFoundException ex) {
			apiResponse.setError(ex.getMessage());
		}
		return apiResponse;
	}

	public ApiResponse updateResource(String username, SecretResource newSecretResource) {
		ApiResponse apiResponse = new ApiResponse();
		try {
			int secretResourceId = newSecretResource.getId();
			SecretResource oldSecretResource = findById(secretResourceId);
			if (oldSecretResource != null) {
				int usersAccessLevel = userDetailsService.getUsersAccessLevelByUsername(username);
				if (accessControl.canWrite(secretResourceId, usersAccessLevel)) {
					secretResourceList.remove(oldSecretResource);
					secretResourceList.add(newSecretResource);
					apiResponse.setData(newSecretResource);
					auditLogService.addRecord(username, "Updated object: " + oldSecretResource);
				} else {
					apiResponse.setError("User " + username + " can't write into SecretResource with id " + secretResourceId);
					auditLogService.addRecord(username, "User " + username + " can't write into SecretResource with id " + secretResourceId);
				}
			} else {
				apiResponse.setError("SecretResource with id " + secretResourceId + " not found");
				auditLogService.addRecord(username, ("SecretResource with id " + secretResourceId + " not found"));
			}
		} catch (UsernameNotFoundException ex) {
			apiResponse.setError(ex.getMessage());
		}
		return apiResponse;
	}

	private SecretResource findById(int id) {
		return secretResourceList.stream()
				.filter(secretResource -> id == secretResource.getId())
				.findFirst()
				.orElse(null);
	}
}
