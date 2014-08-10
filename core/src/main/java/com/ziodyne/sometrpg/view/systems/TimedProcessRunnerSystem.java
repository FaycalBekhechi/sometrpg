package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.ziodyne.sometrpg.view.components.TimedProcess;

/**
 * Tick and execute {@link com.ziodyne.sometrpg.view.components.TimedProcess} on Artemis' time.
 */
public class TimedProcessRunnerSystem extends IteratingSystem {

  private final Engine engine;

  public TimedProcessRunnerSystem(Engine engine) {

    super(Family.getFamilyFor(TimedProcess.class));
    this.engine = engine;
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

    TimedProcess process = entity.getComponent(TimedProcess.class);

    if (process.isReady()) {
      process.run();
      engine.removeEntity(entity);
    } else {
      process.tick();
    }
  }

}
