/*
 * Copyright (c) 2020 Roman Osadchuk. ALL RIGHTS RESERVED.
 */

package com.osadchuk.security.authorization.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuditLogEntry {

	private int id;

	private String username;

	private LocalDateTime date;

	private String message;

	@Override
	public String toString() {
		return "|" + id + "\t|" + username + "\t|" + date + "|" + message + "|\n";
	}
}
