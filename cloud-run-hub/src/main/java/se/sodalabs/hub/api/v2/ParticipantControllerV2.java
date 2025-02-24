package se.sodalabs.hub.api.v2;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sodalabs.hub.domain.v2.FeedbackDTO;
import se.sodalabs.hub.repository.RegisteredParticipant;
import se.sodalabs.hub.repository.SubmittedFeedback;
import se.sodalabs.hub.repository.service.ParticipantService;

@Tag(
    name = "CI/CD Course Central Hub",
    description = "Resources to be used by participant services.")
@RestController
@RequestMapping("/api/v2/participant")
public class ParticipantControllerV2 {

  private static final Gson gson = new Gson();

  @Autowired ParticipantService participantService;

  public ParticipantControllerV2() {}

  @PostMapping(
      path = "/{participantId}/feedback",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      parameters = {
        @Parameter(in = ParameterIn.PATH, name = "participantId", example = "demo-sodalabs:sha-1"),
      },
      summary = "",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FeedbackDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Feedback for a given exercise",
                          value =
                              """
                                {
                                  "exerciseIndex": "1",
                                  "happinessScore": "57"
                                }
                              """)
                    })
              }))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Exercise feedback was successfully received.",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = RegisteredParticipant.class),
                  examples = {
                    @ExampleObject(
                        name = "Feedback",
                        value =
                            """
                                      {
                                        "exerciseIndex": "1",
                                        "happinessScore": "57"
                                      }
                                  """)
                  })
            }),
        @ApiResponse(
            responseCode = "404",
            description = "A participant with the given ID could not be found.")
      })
  ResponseEntity<String> collectFeedback(
      @PathVariable("participantId") String participantId, @RequestBody FeedbackDTO feedbackDTO) {
    RegisteredParticipant foundRegisteredParticipant = participantService.getById(participantId);
    if (foundRegisteredParticipant != null) {
      String feedbackReceivedAt =
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("y-MM-dd HH:mm:ss"));
      SubmittedFeedback submittedFeedback = new SubmittedFeedback();
      submittedFeedback.setExerciseIndex(feedbackDTO.exerciseIndex());
      submittedFeedback.setTimestamp(feedbackReceivedAt);
      submittedFeedback.setHappinessScore(feedbackDTO.happinessScore());
      submittedFeedback.setComment(feedbackDTO.comment());
      foundRegisteredParticipant.addSubmittedFeedback(submittedFeedback);
      participantService.updateParticipant(foundRegisteredParticipant);
      return new ResponseEntity<>(foundRegisteredParticipant.toJsonString(), HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(
          gson.toJson("Participant with ID " + participantId + " could not be found."),
          HttpStatus.NOT_FOUND);
    }
  }
}
