package com.ziodyne.sometrpg.view.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.view.entities.RenderedCombatant;

public class RenderedCombatantAccessor implements TweenAccessor<RenderedCombatant> {
  public static final int POS = 0;
  public static final int DAMAGE_TINT = 1;

  @Override
  public int getValues(RenderedCombatant positioned, int i, float[] floats) {
    if (i == POS) {
      Vector2 position = positioned.getPosition();
      floats[0] = position.x;
      floats[1] = position.y;
      return 2;
    } else {
      floats[0] = positioned.getTintAmount();
      return 1;
    }
  }

  @Override
  public void setValues(RenderedCombatant positioned, int i, float[] floats) {
    if (i == POS) {
      positioned.setPosition(new Vector2(floats[0], floats[1]));
    } else if (i == DAMAGE_TINT) {
      positioned.setTintAmount(floats[0]);
    }
  }
}
