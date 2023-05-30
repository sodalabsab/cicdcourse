package se.sodalabs.hub.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import se.sodalabs.hub.domain.Mood;
import se.sodalabs.hub.domain.Participant;
import se.sodalabs.hub.repository.ConnectedParticipantsRepository;
import se.sodalabs.hub.views.dashboard.ParticipantListDataProvider;

@RestController
public class RegistrationController {

  @Autowired ApplicationEventPublisher applicationEventPublisher;

  @Autowired ParticipantListDataProvider participantListDataProvider;

  private final ConnectedParticipantsRepository connectedParticipantsRepository;

  public RegistrationController(ConnectedParticipantsRepository connectedParticipantsRepository) {
    this.connectedParticipantsRepository = connectedParticipantsRepository;
  }

  @PostMapping("/register")
  ResponseEntity<String> registerNewParticipant(@RequestBody Participant connectedParticipant) {
    Participant foundParticipant =
        connectedParticipantsRepository.findById(connectedParticipant.getId()).orElse(null);
    if (foundParticipant == null) {
      connectedParticipant.setLastHttpResponse(HttpStatus.CREATED.value());
      connectedParticipantsRepository.save(connectedParticipant);
      participantListDataProvider.refreshAll();
      return new ResponseEntity<>(connectedParticipant.toString(), HttpStatus.CREATED);
    } else {
      connectedParticipant.setLastHttpResponse(HttpStatus.CONFLICT.value());
      connectedParticipantsRepository.save(connectedParticipant);
      participantListDataProvider.refreshAll();
      return new ResponseEntity<>(
          "Participant "
              + connectedParticipant.getName()
              + " ("
              + connectedParticipant.getId()
              + ") is already registered.",
          HttpStatus.CONFLICT);
    }
  }

  @PutMapping("/heartbeat")
  ResponseEntity<String> heartbeat(@RequestHeader("participantId") String participantId) {
    Participant foundParticipant =
        connectedParticipantsRepository.findById(participantId).orElse(null);
    if (foundParticipant != null) {
      foundParticipant.setLastHttpResponse(HttpStatus.OK.value());
      connectedParticipantsRepository.save(foundParticipant);
      participantListDataProvider.refreshAll();
      return new ResponseEntity<>(foundParticipant.toString(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          "Participant with id " + participantId + " was not found, could not register heartbeat.",
          HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/participant/{participantId}")
  ResponseEntity<String> getConnectedParticipant(@PathVariable String participantId) {
    Participant foundParticipant =
        connectedParticipantsRepository
            .findById(participantId)
            .orElseThrow(() -> new ParticipantNotFoundException(participantId));
    if (foundParticipant != null) {
      return new ResponseEntity<>(foundParticipant.toString(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          "Participant with id " + participantId + " was not found.", HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/mood/{mood}")
  ResponseEntity<String> setMood(
      @RequestHeader("participantId") String participantId, @PathVariable String mood) {
    Participant foundParticipant =
        connectedParticipantsRepository.findById(participantId).orElse(null);
    if (foundParticipant != null) {
      if (Mood.isValidMood(mood)) {
        foundParticipant.setCurrentMood(mood);
        foundParticipant.setLastHttpResponse(HttpStatus.OK.value());
        connectedParticipantsRepository.save(foundParticipant);
        participantListDataProvider.refreshAll();
        return new ResponseEntity<>(foundParticipant.toString(), HttpStatus.OK);
      } else {
        foundParticipant.setLastHttpResponse(HttpStatus.BAD_REQUEST.value());
        connectedParticipantsRepository.save(foundParticipant);
        participantListDataProvider.refreshAll();
        return new ResponseEntity<>("Invalid mood: " + mood, HttpStatus.BAD_REQUEST);
      }
    } else {
      return new ResponseEntity<>(
          "Participant with id " + participantId + " was not found, can not set new mood.",
          HttpStatus.NOT_FOUND);
    }
  }
}
