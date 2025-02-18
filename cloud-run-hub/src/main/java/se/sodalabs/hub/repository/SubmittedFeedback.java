package se.sodalabs.hub.repository;

import com.google.gson.Gson;

public class SubmittedFeedback {
  private String timestamp;
  private int happinessScore;

  public SubmittedFeedback() {}

  public SubmittedFeedback(
      String timestamp,
      int happinessScore) {
    this.timestamp = timestamp;
    this.happinessScore = happinessScore;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public int getHappinessScore() {
    return happinessScore;
  }

  public void setHappinessScore(int happinessScore) {
    this.happinessScore = happinessScore;
  }

  public String toJsonString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
