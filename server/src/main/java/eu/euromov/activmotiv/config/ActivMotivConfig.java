package eu.euromov.activmotiv.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@SpringBootApplication
@ComponentScan(basePackages = "eu.euromov.activmotiv")
@EnableJpaRepositories(basePackages="eu.euromov.activmotiv.repository")
public class ActivMotivConfig {
	
	@Bean(name="entityManagerFactory")
	LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf  = new LocalContainerEntityManagerFactoryBean();
		emf.setPersistenceUnitName("ActivMotiv_database");
		return emf;
	}
	
	@Bean(name="transactionManager")
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager manager = new JpaTransactionManager(emf);
		manager.setPersistenceUnitName("ActivMotiv_database");
		return manager;
	}
}
