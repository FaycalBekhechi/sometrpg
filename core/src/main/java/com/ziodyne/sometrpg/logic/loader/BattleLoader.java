package com.ziodyne.sometrpg.logic.loader;

import com.ziodyne.sometrpg.logic.models.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.Battle;

import java.io.File;

public class BattleLoader {
  public Battle load(BattleMap map, File battleJson) {
    return new Battle();
  }
}
