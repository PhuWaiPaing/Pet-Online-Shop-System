package com.ci6225.springboot.shoppingcart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ci6225.springboot.shoppingcart.service.UserDetailsServiceImpl;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder;
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)throws Exception{
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable();
		
		http.authorizeRequests().antMatchers("/admin/orderList","/admin/order","/admin/account","/admin/product").access("hasRole('ROLE_EMPLOYEE')");
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
		http.authorizeRequests().and().formLogin().loginProcessingUrl("/spring_security_check")
													.loginPage("/admin/login")
													.defaultSuccessUrl("/admin/account")
													.failureUrl("/admin/login?error=true")
													.usernameParameter("userName")
													.passwordParameter("password")
													.and().logout().logoutUrl("/admin/logout").logoutSuccessUrl("/");
	}

}
