package com.ziodyne.sometrpg.logic.loader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.ziodyne.sometrpg.logic.loader.models.CharacterGrowths;
import com.ziodyne.sometrpg.logic.loader.models.CharacterSpec;
import com.ziodyne.sometrpg.logic.loader.models.CharacterStats;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.CharacterGrowth;
import com.ziodyne.sometrpg.logic.models.Stat;


public class AssetUtils {

  private AssetUtils() { }

  public static Collection<Character> reifyCharacterSpecs(Collection<CharacterSpec> specs) {
    return specs.stream()
      .map(AssetUtils::reifyCharacterSpec)
      .collect(Collectors.toList());
  }

  public static Character reifyCharacterSpec(CharacterSpec spec) {
    return new Character(
      spec.getId(),
      mapStats(spec.getMaxStats()),
      mapGrowths(spec.getGrowths()),
      mapStats(spec.getStats()),
      spec.getName()
    );
  }

  private static CharacterGrowth mapGrowths(CharacterGrowths growths) {

    Map<Stat, Float> rates = new HashMap<>();


    rates.put(Stat.HP, growths.getHealth());
    rates.put(Stat.STRENGTH, growths.getStrength());
    rates.put(Stat.DEFENCE, growths.getDefense());
    rates.put(Stat.MOVEMENT, growths.getMovement());
    rates.put(Stat.SPEED, growths.getSpeed());
    rates.put(Stat.SKILL, growths.getSkill());

    return new CharacterGrowth(rates);
  }

  private static Map<Stat, Integer> mapStats(CharacterStats stats) {

    Map<Stat, Integer> statMap = new HashMap<>();

    statMap.put(Stat.HP, stats.getHealth());
    statMap.put(Stat.STRENGTH, stats.getStrength());
    statMap.put(Stat.DEFENCE, stats.getDefense());
    statMap.put(Stat.MOVEMENT, stats.getMovement());
    statMap.put(Stat.SPEED, stats.getSpeed());
    statMap.put(Stat.SKILL, stats.getSkill());
    statMap.put(Stat.LEVEL, stats.getLevel());

    return statMap;
  }
}
