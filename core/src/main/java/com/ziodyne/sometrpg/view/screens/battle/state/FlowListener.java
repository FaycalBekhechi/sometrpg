package com.ziodyne.sometrpg.view.screens.battle.state;

import au.com.ds.ef.EasyFlow;
import au.com.ds.ef.StateEnum;
import au.com.ds.ef.StatefulContext;
import au.com.ds.ef.call.ContextHandler;

/**
 * An abstract class to ease the sharing of state between onEnter/onLeave event handlers.
 * @param <T>
 */
public abstract class FlowListener<T extends StatefulContext> {
  private final StateEnum state;

  protected FlowListener(StateEnum state) {
    this.state = state;
  }

  public abstract void onLeave(T conext);
  public abstract void onEnter(T context);

  public void bind(EasyFlow<T> flow) {
    flow.whenEnter(state, new ContextHandler<T>() {
      @Override
      public void call(T context) throws Exception {
        onEnter(context);
      }
    });

    flow.whenLeave(state, new ContextHandler<T>() {
      @Override
      public void call(T context) throws Exception {
        onLeave(context);
      }
    });
  }
}
