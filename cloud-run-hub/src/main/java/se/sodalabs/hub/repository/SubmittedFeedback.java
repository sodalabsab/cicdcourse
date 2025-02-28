package se.sodalabs.hub.repository;

import com.google.gson.Gson;

public class SubmittedFeedback {
  private String timestamp;
  private int exerciseIndex;
  private int happinessScore;
  private String comment;

  public SubmittedFeedback() {}

  public SubmittedFeedback(
      String timestamp, int exerciseIndex, int happinessScore, String comment) {
    this.timestamp = timestamp;
    this.exerciseIndex = exerciseIndex;
    this.happinessScore = happinessScore;
    this.comment = comment;
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

  public int getExerciseIndex() {
    return exerciseIndex;
  }

  public void setExerciseIndex(int exerciseIndex) {
    this.exerciseIndex = exerciseIndex;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String toJsonString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
