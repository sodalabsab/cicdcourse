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
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sodalabs.hub.domain.Availability;
import se.sodalabs.hub.domain.Participant;
import se.sodalabs.hub.repository.ConnectedParticipantsRepository;
import se.sodalabs.hub.views.dashboard.ParticipantListDataProvider;

@Tag(
    name = "CI/CD Course Central Hub",
    description = "Resources to be used by participant services.")
@RestController
@RequestMapping("/api/v1/participant")
public class ParticipantController {

  private static final Gson gson = new Gson();

  @Autowired ParticipantListDataProvider participantListDataProvider;

  @Autowired ConnectedParticipantsRepository connectedParticipantsRepository;

  public ParticipantController() {}

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
                    schema = @Schema(implementation = Participant.class),
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
                  schema = @Schema(implementation = Participant.class),
                  examples = {
                    @ExampleObject(
                        name = "Registered participant",
                        value =
                            """
                                {
                                  "id": "demo-sodalabs:sha-1",
                                  "name": "sodalabs",
                                  "lastUpdatedAt": "Thu Jun 08 22:54:41 CEST 2023",
                                  "currentAvailability": "happy",
                                  "avatarImg": "9.png",
                                  "lastHttpResponse": "201"
                                }
                            """)
                  })
            }),
        @ApiResponse(
            responseCode = "409",
            description = "A participant with the given ID has already been registered.")
      })
  ResponseEntity<String> registerNewParticipant(@RequestBody Participant connectedParticipant) {
    Participant foundParticipant =
        connectedParticipantsRepository.findById(connectedParticipant.getId()).orElse(null);
    if (foundParticipant == null) {
      connectedParticipant.setLastHttpResponse(HttpStatus.CREATED.value());
      connectedParticipantsRepository.save(connectedParticipant);
      participantListDataProvider.refreshAll();
      return new ResponseEntity<>(gson.toJson(connectedParticipant), HttpStatus.CREATED);
    } else {
      foundParticipant.setLastHttpResponse(HttpStatus.CONFLICT.value());
      connectedParticipantsRepository.save(foundParticipant);
      participantListDataProvider.refreshAll();
      return new ResponseEntity<>(
          gson.toJson(
              "Participant "
                  + foundParticipant.getName()
                  + " ("
                  + foundParticipant.getId()
                  + ") is already registered."),
          HttpStatus.CONFLICT);
    }
  }

  @PatchMapping("/")
  @Operation(
      summary =
          "Send a heartbeat as a registered participant, letting the hub know you're still alive.",
      parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "participantId", example = "demo-sodalabs:sha-1")
      },
      responses = {
        @ApiResponse(
            responseCode = "204",
            description = "Heartbeat successfully received.",
            content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "404")
      })
  ResponseEntity<String> heartbeat(@RequestHeader("participantId") String participantId) {
    Participant foundParticipant =
        connectedParticipantsRepository.findById(participantId).orElse(null);
    if (foundParticipant != null) {
      foundParticipant.setLastHttpResponse(HttpStatus.NO_CONTENT.value());
      connectedParticipantsRepository.save(foundParticipant);
      participantListDataProvider.refreshAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(
          gson.toJson(
              "Participant with id "
                  + participantId
                  + " was not found, could not register heartbeat."),
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
    Participant foundParticipant =
        connectedParticipantsRepository
            .findById(participantId)
            .orElseThrow(() -> new ParticipantNotFoundException(participantId));
    if (foundParticipant != null) {
      foundParticipant.setLastHttpResponse(HttpStatus.OK.value());
      connectedParticipantsRepository.save(foundParticipant);
      participantListDataProvider.refreshAll();
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
  ResponseEntity<String> deleteonnectedParticipant(@PathVariable String participantId) {
    Participant foundParticipant =
        connectedParticipantsRepository
            .findById(participantId)
            .orElseThrow(() -> new ParticipantNotFoundException(participantId));
    if (foundParticipant != null) {
      foundParticipant.setLastHttpResponse(HttpStatus.OK.value());
      connectedParticipantsRepository.delete(foundParticipant);
      participantListDataProvider.refreshAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(
          "Participant with id " + participantId + " was not found.", HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/")
  @Operation(
      summary = "Set the availability of a registered participant.",
      parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "participantId", example = "demo-sodalabs:sha-1")
      },
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Availability.class),
                    examples = {
                      @ExampleObject(name = "Participant's availability today", value = "busy")
                    })
              }),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Availability has been updated. New participant object returned.",
            content =
                @Content(
                    schema = @Schema(implementation = Participant.class),
                    examples = {
                      @ExampleObject(
                          name = "Registered participant",
                          value =
                              """
                            {
                              "id": "demo-sodalabs:sha-1",
                              "name": "sodalabs",
                              "lastUpdatedAt": "Thu Jun 08 22:54:41 CEST 2023",
                              "currentAvailability": "busy",
                              "avatarImg": "9.png",
                              "lastHttpResponse": "201"
                            }
                        """)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request. Make sure that the provided availability is valid."),
        @ApiResponse(
            responseCode = "404",
            description =
                "Could not find a registered participant with the provided participantId.")
      })
  ResponseEntity<String> setAvailability(
      @RequestHeader("participantId") String participantId,
      @RequestBody Map<String, String> availability) {
    Participant foundParticipant =
        connectedParticipantsRepository.findById(participantId).orElse(null);
    if (foundParticipant != null) {
      try {
        foundParticipant.setAvailability(availability);
        foundParticipant.setLastHttpResponse(HttpStatus.OK.value());
        connectedParticipantsRepository.save(foundParticipant);
        participantListDataProvider.refreshAll();
        return new ResponseEntity<>(foundParticipant.toString(), HttpStatus.OK);
      } catch (Exception e) {
        foundParticipant.setLastHttpResponse(HttpStatus.BAD_REQUEST.value());
        connectedParticipantsRepository.save(foundParticipant);
        participantListDataProvider.refreshAll();
        return new ResponseEntity<>(
            "Invalid availability: " + availability, HttpStatus.BAD_REQUEST);
      }
    } else {
      return new ResponseEntity<>(
          "Participant with id " + participantId + " was not found, can not set availability.",
          HttpStatus.NOT_FOUND);
    }
  }
}
