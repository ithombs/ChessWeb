package com.thombs.ChessWeb.DataAccess;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.thombs.ChessWeb.Models.ChessUser;
import com.thombs.ChessWeb.Models.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service("customUserDetailsService")
public class UserDetailsImpl implements UserDetailsService{
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsImpl.class);
	@Autowired
	private UserService userService;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		org.springframework.security.core.userdetails.User authedUser;
		User user = userService.getUser(username);
		
		if(user == null){
			logger.info("User not found!!!");
			throw new UsernameNotFoundException("***Username does not exist***");
		}
		logger.info("User found!!!");
		
		//authedUser = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getRoles(user));
		authedUser = new ChessUser(user, getRoles(user));
		return authedUser;
	}
	
	private List<SimpleGrantedAuthority> getRoles(User u){
		List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
		u.getRoles().forEach(role -> roles.add(new SimpleGrantedAuthority(role.toString())));
		
		return roles;
	}

}
