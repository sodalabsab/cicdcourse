package se.sodalabs.hub.views.dashboard;

import com.vaadin.flow.data.provider.ListDataProvider;
import org.springframework.stereotype.Component;
import se.sodalabs.hub.repository.ConnectedParticipantsRepository;

@Component
public class ParticipantListDataProvider extends ListDataProvider {

  public ParticipantListDataProvider(ConnectedParticipantsRepository repository) {
    super(repository.findAll());
  }
}
