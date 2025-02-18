package se.sodalabs.hub.views.dashboard;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import se.sodalabs.hub.repository.RegisteredParticipant;

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

    cardContent.add(participantInfoPanel);
    this.add(cardContent);
  }

  private static Span getFirstExerciseFeedbackSpan(RegisteredParticipant registeredParticipant) {
    int happinessScore = registeredParticipant.getSubmittedFeedback().getHappinessScore();
    String feedbackIndicator = happinessScore > 49 ? "👍" : "👎";
    return new Span(
        feedbackIndicator.concat(String.valueOf(happinessScore)).concat(" % exercise happiness"));
  }
}
