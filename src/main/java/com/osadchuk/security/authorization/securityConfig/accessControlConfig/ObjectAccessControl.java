/*
 * Copyright (c) 2020 Roman Osadchuk. ALL RIGHTS RESERVED.
 */

package com.osadchuk.security.authorization.securityConfig.accessControlConfig;

public interface ObjectAccessControl {

	/**
	 * @param objectId - id of the object
	 * @return object's accessLevel
	 */
	Integer getObjectsAccessLevel(int objectId);


	/**
	 * Saves object's access level
	 * @param objectId - id of the object
	 * @param objectsAccessLevel - object's access level
	 */
	void saveObjectsAccessLevel(int objectId, int objectsAccessLevel);

	/**
	 * Checks if users can read object
	 *
	 * @param objectId         - id of the object
	 * @param usersAccessLevel - users's access level
	 * @return usersAccessLevel <= object's accessLevel
	 */
	default boolean canRead(int objectId, int usersAccessLevel) {
		Integer objectsAccessLevel = getObjectsAccessLevel(objectId);
		return objectsAccessLevel != null && usersAccessLevel <= objectsAccessLevel;
	}

	/**
	 * Checks if users can modify object
	 *
	 * @param objectId         - id of the object
	 * @param usersAccessLevel - users's access level
	 * @return usersAccessLevel >= object's accessLevel
	 */
	default boolean canWrite(int objectId, int usersAccessLevel) {
		Integer objectsAccessLevel = getObjectsAccessLevel(objectId);
		return objectsAccessLevel != null && usersAccessLevel >= objectsAccessLevel;
	}

}
