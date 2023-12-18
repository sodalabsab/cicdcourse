package se.sodalabs.hub.api;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.sodalabs.hub.domain.FeedbackDTO;
import se.sodalabs.hub.domain.ParticipantDTO;
import se.sodalabs.hub.repository.RegisteredParticipant;
import se.sodalabs.hub.repository.SubmittedFeedback;
import se.sodalabs.hub.repository.service.ParticipantService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ParticipantControllerV1Test {

  @Mock private ParticipantService participantService;

  @InjectMocks private ParticipantControllerV1 participantController;

  private static final Gson gson = new Gson();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRegisterNewParticipant() {
    ParticipantDTO participantDTO = new ParticipantDTO("demo-sodalabs:sha-1", "sodalabs");
    RegisteredParticipant expectedParticipant = new RegisteredParticipant();
    expectedParticipant.setName("sodalabs");
    expectedParticipant.setId("demo-sodalabs:sha-1");
    when(participantService.getById("demo-sodalabs:sha-1")).thenReturn(null);
    ResponseEntity<String> response = participantController.registerNewParticipant(participantDTO);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    RegisteredParticipant actualParticipant =
        gson.fromJson(response.getBody(), RegisteredParticipant.class);
    assertEquals(expectedParticipant.getName(), actualParticipant.getName());
    assertEquals(expectedParticipant.getId(), actualParticipant.getId());
  }

  @Test
  void testCollectFeedback() {
    final int happinessScore = 55;

    RegisteredParticipant expectedParticipant = new RegisteredParticipant();
    expectedParticipant.setName("sodalabs");
    expectedParticipant.setId("demo-sodalabs:sha-1");

    when(participantService.getById("demo-sodalabs:sha-1")).thenReturn(expectedParticipant);
    when(participantService.updateParticipant(any())).thenReturn(true);

    ResponseEntity<String> response =
        participantController.collectFeedback("demo-sodalabs:sha-1", happinessScore);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    SubmittedFeedback submittedFeedback =
        new Gson().fromJson(response.getBody(), SubmittedFeedback.class);
    assertEquals(happinessScore, submittedFeedback.getHappinessScore());
  }

  @Test
  void testGetConnectedParticipant() {
    RegisteredParticipant expectedParticipant = new RegisteredParticipant();
    expectedParticipant.setName("sodalabs");
    expectedParticipant.setId("demo-sodalabs:sha-1");
    when(participantService.getById("demo-sodalabs:sha-1")).thenReturn(expectedParticipant);
    ResponseEntity<String> response =
        participantController.getConnectedParticipant("demo-sodalabs:sha-1");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(gson.toJson(expectedParticipant), response.getBody());
  }

  @Test
  void testDeleteConnectedParticipant() {
    RegisteredParticipant expectedParticipant = new RegisteredParticipant();
    expectedParticipant.setName("sodalabs");
    expectedParticipant.setId("demo-sodalabs:sha-1");
    when(participantService.getById("demo-sodalabs:sha-1")).thenReturn(expectedParticipant);
    ResponseEntity<String> response =
        participantController.deleteConnectedParticipant("demo-sodalabs:sha-1");
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(participantService, times(1)).removeParticipant(expectedParticipant);
  }
}
