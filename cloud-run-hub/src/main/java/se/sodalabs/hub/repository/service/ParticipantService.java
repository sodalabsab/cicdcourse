package se.sodalabs.hub.repository.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import se.sodalabs.hub.repository.RegisteredParticipant;

@Service
public class ParticipantService {
  private List<RegisteredParticipant> registeredParticipants = new ArrayList<>();
  private final List<ParticipantChangeListener> listeners = new ArrayList<>();

  public void addParticipant(RegisteredParticipant participant) {
    registeredParticipants.add(participant);
    notifyListeners();
  }

  public void removeParticipant(RegisteredParticipant participant) {
    registeredParticipants.remove(participant);
    notifyListeners();
  }

  public RegisteredParticipant getById(String participantId) {
    Optional<RegisteredParticipant> optionalParticipant =
        registeredParticipants.stream()
            .filter(participant -> participant.getId().equals(participantId))
            .findFirst();
    return optionalParticipant.orElse(null);
  }

  public boolean updateParticipant(RegisteredParticipant updatedParticipant) {
    for (int i = 0; i < registeredParticipants.size(); i++) {
      if (registeredParticipants.get(i).getId().equals(updatedParticipant.getId())) {
        registeredParticipants.set(i, updatedParticipant);
        notifyListeners();
        return true;
      }
    }
    return false;
  }

  public List<RegisteredParticipant> getAllParticipants() {
    return registeredParticipants;
  }

  public void addParticipantChangeListener(ParticipantChangeListener listener) {
    listeners.add(listener);
  }

  public void removeParticipantChangeListener(ParticipantChangeListener listener) {
    listeners.remove(listener);
  }

  public List<ParticipantChangeListener> getListeners() {
    return listeners;
  }

  public void notifyListeners() {
    for (ParticipantChangeListener listener : listeners) {
      listener.onParticipantChange();
    }
  }
}
