package com.ziodyne.sometrpg.logic.loader;

import com.ziodyne.sometrpg.logic.models.Map;
import com.ziodyne.sometrpg.logic.models.battle.Battle;

import java.io.File;

public class BattleLoader {
  public Battle load(Map map, File battleJson) {
    return new Battle();
  }
}
