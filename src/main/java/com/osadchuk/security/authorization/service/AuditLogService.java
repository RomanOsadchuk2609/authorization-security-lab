/*
 * Copyright (c) 2020 Roman Osadchuk. ALL RIGHTS RESERVED.
 */

package com.osadchuk.security.authorization.service;

import com.osadchuk.security.authorization.model.AuditLogEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuditLogService {

	private static final String AUDIT_LOG_ENTRIES_DELIMITER =
			"------------------------------------------------------------------------------------------------------------------------\n";

	private static final long SAVING_AUDIT_LOG_DURATION_MILLIS = 60000L;

	private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);

	private final List<AuditLogEntry> auditLogList = new ArrayList<>();

	@Value("classpath:static/auditLog.txt")
	private Resource auditLogFile;

	public void addRecord(String username, String message) {
		auditLogList.add(new AuditLogEntry(
				ID_COUNTER.getAndIncrement(),
				username,
				LocalDateTime.now(),
				message));
	}

	@Scheduled(fixedDelay = SAVING_AUDIT_LOG_DURATION_MILLIS)
	@PreDestroy
	public void saveAuditLogIntoFile() {
		log.debug("Writing audit log into file {}.", auditLogFile.getFilename());
		String auditLog = auditLogList.stream()
				.map(AuditLogEntry::toString)
				.collect(Collectors.joining(AUDIT_LOG_ENTRIES_DELIMITER));
		try {
			Files.write(Paths.get(auditLogFile.getURI()), auditLog.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			log.error("Could not write audit log messages into file: {}.", auditLogFile.getFilename(), e);
		}
	}
}
