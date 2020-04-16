/*
 * Copyright (c) 2020 Roman Osadchuk. ALL RIGHTS RESERVED.
 */

package com.osadchuk.security.authorization.securityConfig;

import com.osadchuk.security.authorization.service.AuditLogService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderImpl implements PasswordEncoder {

	private final AuditLogService auditLogService;

	private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@Autowired
	public PasswordEncoderImpl(AuditLogService auditLogService) {
		this.auditLogService = auditLogService;
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return bCryptPasswordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		boolean matches = bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
		if (matches) {
			auditLogService.addRecord(Strings.EMPTY, "Password matches");
		} else {
			auditLogService.addRecord(Strings.EMPTY, "Password does not match");
		}
		return matches;
	}
}
