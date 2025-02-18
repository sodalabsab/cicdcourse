package se.sodalabs.hub.views.dashboard;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.ContentAlignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import se.sodalabs.hub.repository.RegisteredParticipant;
import se.sodalabs.hub.repository.service.ParticipantService;

@PageTitle("dashboard")
@Route(value = "/")
@PreserveOnRefresh
public class DashboardView extends ParticipantsContainer implements AfterNavigationObserver {

  @Autowired ParticipantService participantService;
  private final FlexLayout flexLayout = new FlexLayout();
  private final VerticalLayout verticalLayout = new VerticalLayout();
  private final HorizontalLayout attribution = createAttribution();
  private final int NUMBER_OF_COLUMNS = 2;

  public DashboardView() {
    addClassName("dashboard-view");
    setSizeFull();
  }

  @PostConstruct
  private void init() {
    verticalLayout.setHeightFull();
    verticalLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
    add(verticalLayout);
    participantService.addParticipantChangeListener(this::refreshDashboardView);
  }

  private void refreshDashboardView() {
    getUI()
        .ifPresent(
            ui ->
                ui.access(
                    () -> {
                      removeAll();
                      verticalLayout.removeAll();
                      flexLayout.removeAll();
                      flexLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
                      flexLayout.setWidthFull();
                      flexLayout.setJustifyContentMode(JustifyContentMode.CENTER);
                      flexLayout.setHeight("95%");
                      flexLayout.setAlignContent(ContentAlignment.START);

                      List<RegisteredParticipant> participants =
                          participantService.getAllParticipants();

                      for (RegisteredParticipant participant : participants) {
                        VerticalLayout card = new ParticipantCard(participant);
                        card.setMinWidth("calc(80% / " + NUMBER_OF_COLUMNS + ")");
                        card.setMaxWidth("calc(80% / " + NUMBER_OF_COLUMNS + ")");
                        card.setWidth("calc(80% / " + NUMBER_OF_COLUMNS + ")");
                        flexLayout.add(card);
                      }

                      verticalLayout.add(flexLayout);
                      if (!participants.isEmpty()) {
                        verticalLayout.add(attribution);
                      }
                      add(verticalLayout);
                    }));
  }

  private HorizontalLayout createAttribution() {
    HorizontalLayout footer = new HorizontalLayout();
    footer.setWidthFull();
    footer.setJustifyContentMode(JustifyContentMode.CENTER);
    footer.setAlignItems(Alignment.CENTER);
    Anchor imgAttribution =
        new Anchor(
            "https://www.freepik.com/free-vector/animal-hipster-set_3975549.htm",
            "Images by macrovector on Freepik");
    footer.add(imgAttribution);
    return footer;
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    refreshDashboardView();
  }
}
