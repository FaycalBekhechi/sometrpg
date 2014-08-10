package com.ziodyne.sometrpg.view.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.google.common.base.Preconditions;

/**
 * A timed process is a runnable that will be executed after a specified number of milliseconds. This runs
 * on the regular game loop, on the main thread.
 */
public class TimedProcess extends Component {
  private final Runnable onComplete;
  private float runningTime = 0L;
  private float ttl = 0L;

  public TimedProcess(Runnable onComplete, float ttl) {

    this.onComplete = onComplete;
    this.ttl = ttl;
  }

  public void tick() {
    float time = Gdx.graphics.getDeltaTime() * 1000;
    runningTime += time;
  }

  public boolean isReady() {
    return runningTime >= ttl;
  }

  public void run() {

    Preconditions.checkState(isReady(), "Process cannot be run until it is ready.");
    onComplete.run();
  }
}
