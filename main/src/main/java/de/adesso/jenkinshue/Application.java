package de.adesso.jenkinshue;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author wennier
 *
 */
@Log4j2
@SpringBootApplication
public class Application {

	@Value("${trust-store:#{null}}")
	private String trustStore;

	@Value("${trust-store-password:#{null}}")
	private String trustStorePassword;

	@PostConstruct
	public void init() {
		if(trustStore != null && trustStorePassword != null) {
			log.debug(new File(trustStore).getAbsolutePath());
			System.setProperty("javax.net.ssl.trustStore", trustStore);
			System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
			log.info("Keystore set");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
