package de.adesso.jenkinshue;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.adesso.jenkinshue.repository.BridgeRepository;
import de.adesso.jenkinshue.repository.JobRepository;
import de.adesso.jenkinshue.repository.LampRepository;
import de.adesso.jenkinshue.repository.TeamRepository;
import de.adesso.jenkinshue.repository.UserRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ActiveProfiles("h2,secret")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
//@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCase {

	@Autowired
	private BridgeRepository bridgeRepository;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private LampRepository lampRepository;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private UserRepository userRepository;


	@Before
	public void clean() {
		bridgeRepository.deleteAll();
		userRepository.deleteAll();
		jobRepository.deleteAll();
		lampRepository.deleteAll();
		teamRepository.deleteAll();
		

		log.info("DB cleanded!");
	}

}
