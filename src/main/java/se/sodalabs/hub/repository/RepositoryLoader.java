package se.sodalabs.hub.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import se.sodalabs.hub.domain.Participant;

public class RepositoryLoader {

  private static final Logger log = LoggerFactory.getLogger(RepositoryLoader.class);

  CommandLineRunner initRepository(ConnectedParticipantsRepository repository) {
    return args -> {
      log.info("Preloading " + repository.save(new Participant("identifier", "John Doe")));
    };
  }
}
