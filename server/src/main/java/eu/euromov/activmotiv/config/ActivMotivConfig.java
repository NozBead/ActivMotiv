package eu.euromov.activmotiv.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	public static PasswordEncoder passwordEncoder() {
	    return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService service) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(service);
		return provider;
	}
	
	@Bean
	public SecurityFilterChain loginSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
				.requestMatchers("/unlock").authenticated()
				.requestMatchers("/participant/login").authenticated()
				.anyRequest().permitAll()
			.and().httpBasic()
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			.and().csrf().disable();
		return http.build();
	}
}
