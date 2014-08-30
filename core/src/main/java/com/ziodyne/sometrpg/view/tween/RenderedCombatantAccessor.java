package com.ziodyne.sometrpg.view.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.view.entities.RenderedCombatant;

public class RenderedCombatantAccessor implements TweenAccessor<RenderedCombatant> {
  public static final int POS = 0;

  @Override
  public int getValues(RenderedCombatant positioned, int i, float[] floats) {
    Vector2 position = positioned.getPosition();
    floats[0] = position.x;
    floats[1] = position.y;
    return 2;
  }

  @Override
  public void setValues(RenderedCombatant positioned, int i, float[] floats) {
    positioned.setPosition(new Vector2(floats[0], floats[1]));
  }
}
