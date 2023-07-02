package se.sodalabs.hub.domain;

import java.util.Random;

public class Availability {

  public enum availableTimeslots {
    Today;
  }

  public enum availableValues {
    busy,
    free;
  }

  private static final Random randomizer = new Random();

  public static Availability.availableValues randomAvailability() {
    availableValues[] values = availableValues.values();
    return values[randomizer.nextInt(values.length)];
  }

  public static Availability.availableTimeslots randomTimeslot() {
    availableTimeslots[] timeslots = availableTimeslots.values();
    return timeslots[randomizer.nextInt(timeslots.length)];
  }
}
