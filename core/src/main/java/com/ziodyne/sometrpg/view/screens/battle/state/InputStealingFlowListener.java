package com.ziodyne.sometrpg.view.screens.battle.state;

import au.com.ds.ef.StateEnum;
import au.com.ds.ef.StatefulContext;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

/**
 * A flow listener that usurps some general, systemwide input processor and cleanly reinstalls it
 * when the state is exited.
 */
public class InputStealingFlowListener<T extends StatefulContext> extends FlowListener<T> {
  private InputProcessor previousInputProcessor;

  public InputStealingFlowListener(StateEnum state) {
    super(state);
  }

  @Override
  public void onLeave(T context) {
    Gdx.input.setInputProcessor(previousInputProcessor);
  }

  @Override
  public void onEnter(T context) {
    previousInputProcessor = Gdx.input.getInputProcessor();
  }
}
