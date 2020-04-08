/*
 * Copyright (c) 2020 Roman Osadchuk. ALL RIGHTS RESERVED.
 */

package com.osadchuk.security.authorization.service;

import com.google.gson.Gson;
import com.osadchuk.security.authorization.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Value("classpath:static/users.json")
	private Resource resourceFile;
	
	private List<User> userList = Collections.emptyList();
	
	@PostConstruct
	public void initUsers() {
		Gson gson = new Gson();
		try (FileReader fileReader = new FileReader(resourceFile.getFile())) {
			userList = Arrays.asList(gson.fromJson(fileReader, User[].class));
		} catch (IOException e) {
			log.error("Could not read static/users.json file.", e);
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		return userList.stream()
				.filter(user -> username.equals(user.getUsername()))
				.map(user -> new org.springframework.security.core.userdetails.User(
						user.getUsername(),
						user.getPassword(),
						Collections.singleton(new SimpleGrantedAuthority(user.getRole()))))
				.findFirst()
				.orElseThrow(() -> new UsernameNotFoundException("User with username \"" + username + "\" not found"));
	}
}
