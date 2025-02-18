package se.sodalabs.hub.repository.service;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.sodalabs.hub.repository.RegisteredParticipant;

class ParticipantServiceTest {
  private ParticipantService participantService;

  @BeforeEach
  void setUp() {
    participantService = new ParticipantService();
  }

  @Test
  void testAddParticipant() {
    RegisteredParticipant participant = new RegisteredParticipant();
    participant.setId("participant-id-1");
    participant.setName("participant-name");
    participantService.addParticipant(participant);
    List<RegisteredParticipant> participants = participantService.getAllParticipants();
    Assertions.assertTrue(participants.contains(participant));
  }

  @Test
  void testRemoveParticipant() {
    RegisteredParticipant participant = new RegisteredParticipant();
    participant.setId("participant-id-1");
    participant.setName("participant-name");
    participantService.addParticipant(participant);
    participantService.removeParticipant(participant);
    List<RegisteredParticipant> participants = participantService.getAllParticipants();
    Assertions.assertFalse(participants.contains(participant));
  }

  @Test
  void testGetById() {
    RegisteredParticipant participant = new RegisteredParticipant();
    String id = "participant-id-1";
    participant.setId(id);
    participant.setName("participant-name");
    participantService.addParticipant(participant);
    RegisteredParticipant retrievedParticipant = participantService.getById(id);
    Assertions.assertEquals(participant, retrievedParticipant);
  }

  @Test
  void testUpdateParticipant() {
    RegisteredParticipant participant = new RegisteredParticipant();
    String id = "participant-id-1";
    participant.setId(id);
    participant.setName("participant-name");
    participantService.addParticipant(participant);

    RegisteredParticipant updatedParticipant = participant;
    String expectedName = "participant-other-name";
    updatedParticipant.setName(expectedName);
    boolean updated = participantService.updateParticipant(updatedParticipant);
    RegisteredParticipant retrievedParticipant = participantService.getById(id);

    Assertions.assertTrue(updated);
    Assertions.assertEquals(expectedName, retrievedParticipant.getName());
  }

  @Test
  void testAddAndRemoveParticipantChangeListener() {
    ParticipantChangeListener listener = () -> System.out.println("Participant changed");

    participantService.addParticipantChangeListener(listener);
    Assertions.assertTrue(participantService.getListeners().contains(listener));

    participantService.removeParticipantChangeListener(listener);
    Assertions.assertFalse(participantService.getListeners().contains(listener));
  }
}
