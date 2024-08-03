package com.shopping.portal.security;

import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.shopping.portal.model.EndUser;
import com.shopping.portal.service.EndUserDetailService;
import com.shopping.portal.util.AuthEntryPointJWT;
import com.shopping.portal.util.AuthTokenFilter;

@Component
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

	private final AuthEntryPointJWT authEntryPointJWT;
	private final EndUserDetailService endUserService;
	private final AuthTokenFilter authTokenFilter;

	@Autowired
	public SecurityConfiguration(AuthEntryPointJWT authEntryPointJWT, EndUserDetailService endUserService,
			AuthTokenFilter authTokenFilter) {
		this.authEntryPointJWT = authEntryPointJWT;
		this.endUserService = endUserService;
		this.authTokenFilter = authTokenFilter;
	}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(registry -> {
                registry.requestMatchers("/v1/authenticate", "/v1/register/user").permitAll();
                registry.requestMatchers("/v1/admin").hasRole("ADMIN");
                registry.requestMatchers("/v1/decrypt/**").hasRole("ADMIN");
                registry.requestMatchers("/v1/user").hasRole("USER");
                registry.anyRequest().authenticated();
            })
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJWT))
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

	@Bean
	public UserDetailsService userDetailsService() {
		return endUserService;
	}

	@Bean
	public AuthTokenFilter authenticationJWTFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(endUserService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(authenticationProvider());
	}

	@Bean
	public static PasswordEncoder BCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new AESPasswordEncoder();
	}
	
	@Bean
	public static PasswordEncoder Argon2PasswordEncoder() {
		int saltLength = 16;
		int hashLength = 32;
		int parallelism = 1;
		int memory = 131072;
		int iterations = 5;
		return new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations);
	}

	public static boolean checkPassword(String rawPassword, String encodedPassword) {
		return passwordEncoder().matches(rawPassword, encodedPassword);
	}

//  @Bean
//  public DefaultOAuth2UserService oAuth2UserService() {
//      return new DefaultOAuth2UserService();
//  }

}
