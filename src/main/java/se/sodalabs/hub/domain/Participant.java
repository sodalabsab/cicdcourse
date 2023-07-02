package se.sodalabs.hub.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import se.sodalabs.hub.domain.Availability.availableTimeslots;

@Entity
public class Participant {
  private @Id String id;
  private String name;
  private String lastUpdatedAt;

  @ElementCollection
  @CollectionTable(name = "participant_availability")
  @MapKeyColumn(name = "timeslot")
  @Column(name = "availability")
  private Map<String, String> availability;

  private String avatarImg;

  private int lastHttpResponse;

  public Participant() {
    this.id = "";
    this.name = "";
    this.availability = new HashMap<>();
    availability.put(
        Availability.randomTimeslot().name(), Availability.randomAvailability().name());
    this.updateLastUpdatedAt();
    int avatarImgIndex = ThreadLocalRandom.current().nextInt(1, 14); // generate a random avatar img
    this.avatarImg = avatarImgIndex + ".png";
  }

  public Participant(String id, String name) {
    this.id = id;
    this.name = name;
    this.availability = new HashMap<>();
    availability.put(
        Availability.randomTimeslot().name(), Availability.randomAvailability().name());
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

  public Map<String, String> getAvailability() {
    return availability;
  }

  public void setAvailability(Map<String, String> newAvailabilityEntry) {
    int maxEntries = availableTimeslots.values().length;

    for (String key : newAvailabilityEntry.keySet()) {
      if (!Arrays.asList(Availability.availableTimeslots.values()).contains(key)
          || !Arrays.asList(Availability.availableValues.values())
              .contains(newAvailabilityEntry.get(key))) {

        throw new RuntimeException(
            "Could not map key "
                + key
                + " and value "
                + newAvailabilityEntry.get(key)
                + " to a valid availability setting.");
      }
    }

    for (String key : newAvailabilityEntry.keySet()) {
      if (this.availability.containsKey(key)) {
        this.availability.replace(key, newAvailabilityEntry.get(key));
        updateLastUpdatedAt();
        return;
      }
    }

    if (this.availability.size() >= maxEntries) {
      throw new RuntimeException("Only " + maxEntries + " availability entry/-ies are supported");
    }

    for (String key : newAvailabilityEntry.keySet()) {
      this.availability.put(key, newAvailabilityEntry.get(key));
    }

    updateLastUpdatedAt();
  }

  public String getAvatarImg() {
    return avatarImg;
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
        + ", 'currentAvailability': '"
        + this.availability
        + '\''
        + ", 'lastHttpResponse': '"
        + this.lastHttpResponse
        + "'}";
  }
}
