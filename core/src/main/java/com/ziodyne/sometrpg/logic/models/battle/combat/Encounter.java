package com.ziodyne.sometrpg.logic.models.battle.combat;

/**
 * An encounter represents a fight that has not yet happened, and allows clients to trigger
 * that fight, or see some information about what will happen before it happens.
 */
public interface Encounter {
  public EncounterResult execute();
  public boolean defenderWillDodge();
}
