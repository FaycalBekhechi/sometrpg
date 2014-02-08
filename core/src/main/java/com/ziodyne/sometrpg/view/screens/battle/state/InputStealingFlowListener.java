package com.ziodyne.sometrpg.view.screens.battle.state;

import au.com.ds.ef.StateEnum;
import au.com.ds.ef.StatefulContext;
import au.com.ds.ef.err.LogicViolationError;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class InputStealingFlowListener<T extends StatefulContext> extends FlowListener<T> {
  private InputProcessor previousInputProcessor;

  public InputStealingFlowListener(StateEnum state) {
    super(state);
  }

  @Override
  public void onLeave(T context) throws LogicViolationError {
    Gdx.input.setInputProcessor(previousInputProcessor);
  }

  @Override
  public void onEnter(T context) throws LogicViolationError {
    previousInputProcessor = Gdx.input.getInputProcessor();
  }
}
