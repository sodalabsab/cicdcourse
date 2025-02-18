package se.sodalabs.hub.api;

public class ParticipantNotFoundException extends RuntimeException {
  public ParticipantNotFoundException(String id) {
    super("Could not find connected participant " + id);
  }
}
