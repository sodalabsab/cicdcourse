package se.sodalabs.hub.domain;


import java.util.Random;

public enum Availability {
  busy,
  free;

  private static final Random randomizer = new Random();

  public static Availability randomAvailability() {
    Availability[] availabilities = values();
    return availabilities[randomizer.nextInt(availabilities.length)];
  }
}
