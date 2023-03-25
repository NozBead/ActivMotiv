package eu.euromov.activmotiv.config;

import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.PlatformTransactionManager;

import eu.euromov.activmotiv.authentication.ParticipantService;
import jakarta.persistence.EntityManagerFactory;

@SpringBootApplication
@EnableWebSecurity
@ComponentScan(basePackages = "eu.euromov.activmotiv")
@EnableJpaRepositories(basePackages = "eu.euromov.activmotiv.repository")
public class ActivMotivConfig {

	@Bean
	AbstractEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setPersistenceUnitName("ActivMotiv_database");
		return emf;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager manager = new JpaTransactionManager(emf);
		manager.setPersistenceUnitName("ActivMotiv_database");
		return manager;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new ParticipantService();
	}

	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService service) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(service);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}
	
	@Bean
	public SecretKey secretKey() {
		byte[] secret = new byte[256];
		Random rnd = new Random();
		rnd.nextBytes(secret);
		return new SecretKeySpec(secret, "HmacSHA256");
	}

	@Bean
	public JwtDecoder jwtDecoder(SecretKey secret) {
		return NimbusJwtDecoder.withSecretKey(secret)
				.macAlgorithm(MacAlgorithm.HS256)
				.build();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
			throws Exception {
		http.securityMatcher("/unlock")  
			.authorizeHttpRequests()
			.requestMatchers("/unlock").authenticated()
			.and().oauth2ResourceServer().jwt().and()
			.and().csrf().disable();
		return http.build();
	}

	@Bean
	public SecurityFilterChain loginSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
				.requestMatchers("/participant/login").authenticated()
				.anyRequest().permitAll()
			.and().httpBasic()
			.and().csrf().disable();
		return http.build();
	}
}
