package se.sodalabs.hub.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;
import java.util.Objects;

@Entity
public class Participant {
  private @Id String id;
  private String name;
  private String lastUpdatedAt;

  private String currentMood;

  private int lastHttpResponse;

  public Participant() {
    this.currentMood = Mood.getRandomMood();
    this.updateLastUpdatedAt();
  }

  public Participant(String id, String name) {
    this.id = id;
    this.name = name;
    this.currentMood = Mood.getRandomMood();
    this.updateLastUpdatedAt();
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    updateLastUpdatedAt();
    this.name = name;
  }

  public String getLastUpdatedAt() {
    return lastUpdatedAt;
  }

  public void setLastUpdatedAt(String lastUpdatedAt) {
    this.lastUpdatedAt = lastUpdatedAt;
  }

  private void updateLastUpdatedAt() {
    this.lastUpdatedAt = new Date().toString();
  }

  public int getLastHttpResponse() {
    return lastHttpResponse;
  }

  public void setLastHttpResponse(int lastHttpResponse) {
    updateLastUpdatedAt();
    this.lastHttpResponse = lastHttpResponse;
  }

  public String getCurrentMood() {
    return currentMood;
  }

  public void setCurrentMood(String currentMood) {
    updateLastUpdatedAt();
    this.currentMood = currentMood;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Participant participant)) return false;
    return Objects.equals(this.id, participant.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  @Override
  public String toString() {
    return "{"
        + "'id': "
        + this.id
        + ", 'name': '"
        + this.name
        + '\''
        + ", 'lastUpdatedAt': '"
        + this.lastUpdatedAt
        + '\''
        + ", 'currentMood': '"
        + this.currentMood
        + '\''
        + ", 'lastHttpResponse': '"
        + this.lastHttpResponse
        + "'}";
  }
}
