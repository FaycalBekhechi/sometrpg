package com.ziodyne.sometrpg.logic.models.battle.combat;

import java.util.Optional;

public class EncounterResult {

  private Optional<Encounter> counter;

  private boolean evaded;

  public EncounterResult(boolean evaded) {
    this.evaded = evaded;
    this.counter = Optional.empty();
  }

  public EncounterResult(boolean evaded, Encounter counter) {
    this.evaded = evaded;
    this.counter = Optional.ofNullable(counter);
  }

  public Optional<Encounter> getCounter() {
    return counter;
  }

  public boolean wasEvaded() {
    return evaded;
  }
}
