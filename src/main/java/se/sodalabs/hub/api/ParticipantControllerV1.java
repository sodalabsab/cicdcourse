package se.sodalabs.hub.api;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sodalabs.hub.domain.ParticipantDTO;
import se.sodalabs.hub.repository.RegisteredParticipant;
import se.sodalabs.hub.repository.SubmittedFeedback;
import se.sodalabs.hub.repository.service.ParticipantService;

@Tag(
    name = "CI/CD Course Central Hub",
    description = "Resources to be used by participant services.")
@RestController
@RequestMapping("/api/v1/participant")
public class ParticipantControllerV1 {

  private static final Gson gson = new Gson();

  @Autowired ParticipantService participantService;

  public ParticipantControllerV1() {}

  @PostMapping(
      path = "/",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Register a new participant.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ParticipantDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Participant to register",
                          value =
                              """
                              {
                                "id": "demo-sodalabs:sha-1",
                                "name": "sodalabs"
                              }
                          """)
                    })
              }))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Returns the newly registered participant object.",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = RegisteredParticipant.class),
                  examples = {
                    @ExampleObject(
                        name = "Registered participant",
                        value =
                            """
                                {
                                  "id": "demo-sodalabs:sha-1",
                                  "name": "sodalabs",
                                  "registeredAt": "2023-11-20 11:20:44",
                                  "avatarImg": "9.png"
                                }
                            """)
                  })
            }),
        @ApiResponse(
            responseCode = "409",
            description = "A participant with the given ID has already been registered.")
      })
  ResponseEntity<String> registerNewParticipant(
      @RequestBody ParticipantDTO connectedParticipantDTO) {
    RegisteredParticipant foundRegisteredParticipant =
        participantService.getById(connectedParticipantDTO.id());
    if (foundRegisteredParticipant == null) {
      RegisteredParticipant registeredParticipant = new RegisteredParticipant();
      registeredParticipant.setId(connectedParticipantDTO.id());
      registeredParticipant.setName(connectedParticipantDTO.name());
      String registeredAt =
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("y-MM-dd HH:mm:ss"));
      registeredParticipant.setRegisteredAt(registeredAt);
      participantService.addParticipant(registeredParticipant);
      return new ResponseEntity<>(registeredParticipant.toJsonString(), HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(
          gson.toJson(
              "Participant "
                  + foundRegisteredParticipant.getName()
                  + " ("
                  + foundRegisteredParticipant.getId()
                  + ") is already registered."),
          HttpStatus.CONFLICT);
    }
  }

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
                    schema = @Schema(implementation = Integer.class),
                    examples = {
                      @ExampleObject(
                          name =
                              "Feedback for the exercise, a score (percentage) between 0 and 100",
                          value =
                              """
                                51
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
                                        "timestamp": "2023-12-08 17:05:31",
                                        "happinessScore": 55
                                      }
                                  """)
                  })
            }),
        @ApiResponse(
            responseCode = "404",
            description = "A participant with the given ID could not be found."),
        @ApiResponse(responseCode = "422", description = "Invalid value")
      })
  ResponseEntity<String> collectFeedback(
      @PathVariable("participantId") String participantId, @RequestBody Integer happinessScore) {
    RegisteredParticipant foundRegisteredParticipant = participantService.getById(participantId);
    if (happinessScore < 0 || happinessScore > 100) {
      return new ResponseEntity<>(
          gson.toJson("Please submit a score between 0 and 100"), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    if (foundRegisteredParticipant != null) {
      String feedbackReceivedAt =
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("y-MM-dd HH:mm:ss"));
      SubmittedFeedback submittedFeedback = new SubmittedFeedback();
      submittedFeedback.setTimestamp(feedbackReceivedAt);
      submittedFeedback.setHappinessScore(happinessScore);
      foundRegisteredParticipant.setSubmittedFeedback(submittedFeedback);
      participantService.updateParticipant(foundRegisteredParticipant);
      return new ResponseEntity<>(submittedFeedback.toJsonString(), HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(
          gson.toJson("Participant with ID " + participantId + " could not be found."),
          HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/{participantId}")
  @Operation(
      summary = "Fetch a registered participant.",
      parameters = {
        @Parameter(
            name = "participantId",
            description =
                "The ID of the participant to fetch. This was returned when registering the participant.",
            example = "demo-sodalabs:sha-1")
      })
  ResponseEntity<String> getConnectedParticipant(@PathVariable String participantId) {
    RegisteredParticipant foundParticipant = participantService.getById(participantId);
    if (foundParticipant != null) {
      return new ResponseEntity<>(gson.toJson(foundParticipant), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          "Participant with id " + participantId + " was not found.", HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{participantId}")
  @Operation(
      summary = "Delete (unregister) a registered participant.",
      parameters = {
        @Parameter(
            name = "participantId",
            description =
                "The ID of the participant to delete. This was returned when registering the participant.",
            example = "demo-sodalabs:sha-1")
      })
  ResponseEntity<String> deleteConnectedParticipant(@PathVariable String participantId) {
    RegisteredParticipant registeredParticipant = participantService.getById(participantId);
    if (registeredParticipant != null) {
      participantService.removeParticipant(registeredParticipant);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(
          "Participant with id " + participantId + " was not found.", HttpStatus.NOT_FOUND);
    }
  }
}
