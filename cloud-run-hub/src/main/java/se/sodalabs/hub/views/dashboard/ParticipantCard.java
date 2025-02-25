package se.sodalabs.hub.views.dashboard;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import se.sodalabs.hub.repository.RegisteredParticipant;
import se.sodalabs.hub.repository.SubmittedFeedback;

public class ParticipantCard extends VerticalLayout {

  public ParticipantCard(RegisteredParticipant registeredParticipant) {
    this.addClassName("card");
    this.setSpacing(true);
    this.getThemeList().add("spacing-m");

    // HEADER
    VerticalLayout cardHeader = new VerticalLayout();
    cardHeader.setWidthFull();
    cardHeader.setPadding(false);
    cardHeader.setSpacing(false);
    cardHeader.addClassName("header");

    Span name = new Span(registeredParticipant.getName());
    name.addClassName("name");
    cardHeader.add(name);
    this.add(cardHeader);

    // CONTENT
    HorizontalLayout cardContent = new HorizontalLayout();
    String participantAvatarImg = registeredParticipant.getAvatarImage();
    Image avatarImage =
        new Image("images/avatars/".concat(participantAvatarImg), registeredParticipant.getName());
    avatarImage.setHeight("120px");
    avatarImage.setWidth("120px");
    cardContent.add(avatarImage);

    VerticalLayout participantInfoPanel = new VerticalLayout();
    participantInfoPanel.setJustifyContentMode(JustifyContentMode.START);
    participantInfoPanel.setSpacing(false);
    participantInfoPanel.setPadding(false);

    if (registeredParticipant.getRegisteredAt() != null) {
      Span registeredAt = new Span("✅ Online at ".concat(registeredParticipant.getRegisteredAt()));
      participantInfoPanel.add(registeredAt);
    }

    if (registeredParticipant.getSubmittedFeedback() != null) {
      Span exerciseHappiness = getFirstExerciseFeedbackSpan(registeredParticipant);
      participantInfoPanel.add(exerciseHappiness);
    }

    if (registeredParticipant.getSubmittedFeedbackForExercise(1) != null) {
      Span secondExerciseHappiness = getSecondExerciseHappiness(registeredParticipant);
      participantInfoPanel.add(secondExerciseHappiness);
    }

    cardContent.add(participantInfoPanel);
    this.add(cardContent);
  }

  private static Span getFirstExerciseFeedbackSpan(RegisteredParticipant registeredParticipant) {
    return getExerciseFeedbackSpan(registeredParticipant, 0, "1️⃣");
  }

  private static Span getSecondExerciseHappiness(RegisteredParticipant registeredParticipant) {
    return getExerciseFeedbackSpan(registeredParticipant, 1, "2️⃣");
  }

  private static Span getExerciseFeedbackSpan(
      RegisteredParticipant registeredParticipant, int exerciseIndex, String exerciseIndicator) {
    SubmittedFeedback exerciseFeedback =
        registeredParticipant.getSubmittedFeedbackForExercise(exerciseIndex);

    int happinessScore = exerciseFeedback.getHappinessScore();
    String feedbackIndicator = happinessScore > 49 ? "👍" : "👎";

    String hasCommentIndicator = exerciseFeedback.getComment() == null ? "" : "💬";

    return new Span(
        exerciseIndicator
            .concat(" ")
            .concat(feedbackIndicator)
            .concat(hasCommentIndicator)
            .concat(String.valueOf(happinessScore))
            .concat(" % exercise happiness"));
  }
}
