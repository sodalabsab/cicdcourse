package se.sodalabs.hub.repository;

import com.google.gson.Gson;
import java.util.Random;

public class RegisteredParticipant {
  private String id;
  private String name;
  private String registeredAt;
  private String avatarImage;
  private SubmittedFeedback submittedFeedback;

  public RegisteredParticipant() {
    this.avatarImage = generateRandomAvatarImage();
  }

  public RegisteredParticipant(
      String id, String name, String registeredAt, SubmittedFeedback submittedFeedback) {
    this.id = id;
    this.name = name;
    this.registeredAt = registeredAt;
    this.avatarImage = generateRandomAvatarImage();
    this.submittedFeedback = submittedFeedback;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRegisteredAt() {
    return registeredAt;
  }

  public void setRegisteredAt(String registeredAt) {
    this.registeredAt = registeredAt;
  }

  public String getAvatarImage() {
    return avatarImage;
  }

  public void setAvatarImage(String avatarImage) {
    this.avatarImage = avatarImage;
  }

  public SubmittedFeedback getSubmittedFeedback() {
    return submittedFeedback;
  }

  public void setSubmittedFeedback(SubmittedFeedback submittedFeedback) {
    this.submittedFeedback = submittedFeedback;
  }

  private String generateRandomAvatarImage() {
    Random random = new Random();
    int imageNumber = random.nextInt(13) + 1; // Generates a random number between 1 and 13
    return imageNumber + ".png";
  }

  public String toJsonString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
