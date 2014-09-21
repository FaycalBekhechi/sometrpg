package com.ziodyne.sometrpg.logic.models.battle.combat;

import com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MapCombatResolverTest {

  private Combatant attacker;
  private Combatant defender;

  @Before
  public void initCombatants() {
    attacker = new Combatant(ModelTestUtils.createCrappyUnit());
    defender = new Combatant(ModelTestUtils.createCrappyUnit());
  }

  @Test
  public void testComputeDamageSubtotal() throws Exception {
    final int baseDamage = 20;
    Attack crittingAttack = new Attack() {
      @Override
      public int getRange() {
        return Integer.MAX_VALUE;
      }

      @Override
      public int computeDamage(Combatant attacker, Combatant defender) {
        return baseDamage;
      }

      @Override
      public int computeHitChance(Combatant attacker, Combatant defender) {
        return 100;
      }

      @Override
      public int computeCritChance(Combatant attacker, Combatant defender) {
        return 100;
      }
    };

    //int criticalDamage = MapCombatResolver.computeDamageSubtotal(crittingAttack, attacker, defender);
    //Assert.assertEquals("A critical hit should do double damage.", baseDamage*2, criticalDamage);

    final int basicAttackDamage = 15;
    Attack regularAttack = new Attack() {
      @Override
      public int getRange() {
        return Integer.MAX_VALUE;
      }

      @Override
      public int computeDamage(Combatant attacker, Combatant defender) {
        return 15;
      }

      @Override
      public int computeHitChance(Combatant attacker, Combatant defender) {
        return 100;
      }

      @Override
      public int computeCritChance(Combatant attacker, Combatant defender) {
        return 0;
      }
    };

    //int regularDamage = MapCombatResolver.computeDamageSubtotal(regularAttack, attacker, defender);
    //Assert.assertEquals("A non critical regular attack that does not miss should do the base damage.", basicAttackDamage, regularDamage);
  }

}
