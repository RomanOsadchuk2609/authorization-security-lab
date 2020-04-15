/*
 * Copyright (c) 2020 Roman Osadchuk. ALL RIGHTS RESERVED.
 */

package com.osadchuk.security.authorization.securityConfig.accessControlConfig;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SecretResourceAccessControl implements ObjectAccessControl {

	private Map<Integer, Integer> secretResourceAccessLevelMap = new HashMap<>();

	@Override
	public Integer getObjectsAccessLevel(int objectId) {
		return secretResourceAccessLevelMap.get(objectId);
	}

	@Override
	public void saveObjectsAccessLevel(int objectId, int objectsAccessLevel) {
		secretResourceAccessLevelMap.put(objectId, objectsAccessLevel);
	}
}
