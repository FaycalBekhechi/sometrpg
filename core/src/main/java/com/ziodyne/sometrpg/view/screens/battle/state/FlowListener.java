package com.ziodyne.sometrpg.view.screens.battle.state;

import au.com.ds.ef.EasyFlow;
import au.com.ds.ef.StateEnum;
import au.com.ds.ef.StatefulContext;
import au.com.ds.ef.call.ContextHandler;
import com.ziodyne.sometrpg.util.Logged;

/**
 * An abstract class to ease the sharing of state between onEnter/onLeave event handlers.
 * @param <T>
 */
public abstract class FlowListener<T extends StatefulContext> implements Logged {
  public static boolean DEBUG = true;
  private final StateEnum state;

  protected FlowListener(StateEnum state) {
    this.state = state;
  }

  public abstract void onLeave(T context);
  public abstract void onEnter(T context);

  public void bind(EasyFlow<T> flow) {
    flow.whenEnter(state, new ContextHandler<T>() {
      @Override
      public void call(T context) throws Exception {
        if (DEBUG) {
          logDebug("Entering state: " + state);
        }
        onEnter(context);
      }
    });

    flow.whenLeave(state, new ContextHandler<T>() {
      @Override
      public void call(T context) throws Exception {
        if (DEBUG) {
          logDebug("Leaving state: " + state);
        }
        onLeave(context);
      }
    });
  }
}
