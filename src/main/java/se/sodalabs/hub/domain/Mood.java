package se.sodalabs.hub.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Mood {
  private static final List<String> availableMoods =
      Arrays.asList("sad", "confused", "sleepy", "happy", "cool", "angry", "disappointed", "dead");

  public static String getRandomMood() {
    Collections.shuffle(availableMoods);
    return availableMoods.get(0);
  }

  public static boolean isValidMood(String mood) {
    return availableMoods.contains(mood);
  }
}
