package com.thombs.ChessWeb.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@ComponentScan({"com.thombs.ChessWeb"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	//@Autowired
	//DataSource dataSource;
	
	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetails;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
	    	.authorizeRequests()
	    	.antMatchers("/profile", "/playChess", "/chessReplays").authenticated()
	        .antMatchers("/**").permitAll()
	        .and()
	    .formLogin()
	        .loginPage("/").loginProcessingUrl("/login").failureUrl("/404")
	        .permitAll()
	        .and()
	    .logout()
	        .permitAll();
    	
    	http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
    	//http.headers().frameOptions().sameOrigin();
    	http.csrf().disable();
    }
    
    
    
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
    @Bean(name="authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //auth.inMemoryAuthentication().withUser("test").password("password").roles("USER");
    	auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder());
    	auth.authenticationProvider(authenticationProvider());
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetails);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
