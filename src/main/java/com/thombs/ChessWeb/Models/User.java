package com.thombs.ChessWeb.Models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userid")
	private int userID;
	
	@NotNull
	@Column(unique=true, name="username")
	private String username;
	
	@NotNull
	@Column(name="email")
	private String email;
	
	@NotNull
	@Column(name = "password")
	private String password;
	
	@Transient
	private String passwordConfirmation;

	@ElementCollection(targetClass = Role.class, fetch=FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "userid"))
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private List<Role>roles;
	
	
	public User(){
		
	}
	
	public User(int userid) { 
	    this.userID = userid;
	}
	
	public User(int userid, String username, String password, List<Role>roles){
		this.userID = userid;
		this.username = username;
		this.password = password;
		this.roles = roles;
	}
	
	public long getUserid() {
		return userID;
	}

	public void setUserid(int userid) {
		this.userID = userid;
	}

	public String getUsername() {
		return username;
	}
	
	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return this.email;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPasswordConfirmation(){
		return this.passwordConfirmation;
	}
	
	public void setPasswordConfirmation(String passwordConfirmation){
		this.passwordConfirmation = passwordConfirmation;
	}
	
	public List<Role>getRoles(){
		return this.roles;
	}
	
	public void setRoles(List<Role> roles){
		this.roles = roles;
	}
	
	public List<SimpleGrantedAuthority> getAuthRoles(){
		List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
		this.getRoles().forEach(role -> roles.add(new SimpleGrantedAuthority(role.toString())));
		
		return roles;
	}
	
	private String getAllRoles(){
		String roles = "";
		for(int i = 0; i < this.roles.size(); i++){
			roles += this.roles.get(i);
			if(i < this.roles.size() - 1){
				roles += ", ";
			}
		}
		return roles;
	}
	
	@Override
	public String toString(){
		String lineBreak = System.getProperty("line.separator");
		if(username == null || username.equals("")){
			return "anonymousUser";
		}
		
		return "UserID: " + this.userID + lineBreak +
				"Username: " + this.username + lineBreak +
				"Password: " + this.password + lineBreak + 
				this.roles!= null && this.roles.size() > 0?"Roles : " + this.getAllRoles():"Roles: none";
	}
}
