package se.sodalabs.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sodalabs.hub.domain.Participant;

public interface ConnectedParticipantsRepository extends JpaRepository<Participant, String> {

}
