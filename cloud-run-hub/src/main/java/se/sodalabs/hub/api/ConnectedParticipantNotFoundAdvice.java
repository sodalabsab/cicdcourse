package se.sodalabs.hub.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ConnectedParticipantNotFoundAdvice {
  @ResponseBody
  @ExceptionHandler(ParticipantNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String connectedParticipantNotFoundHandler(
      ParticipantNotFoundException participantNotFoundException) {
    return participantNotFoundException.getMessage();
  }
}
