package se.sodalabs.hub.domain;


import java.util.Random;

public enum Mood {
  sad,
  confused,
  sleepy,
  happy,
  cool,
  angry,
  disappointed,
  dead;

  private static final Random randomizer = new Random();

  public static Mood randomMood() {
    Mood[] moods = values();
    return moods[randomizer.nextInt(moods.length)];
  }
}
