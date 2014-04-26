package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.ziodyne.sometrpg.view.components.TimedProcess;

/**
 * Tick and execute {@link com.ziodyne.sometrpg.view.components.TimedProcess} on Artemis' time.
 */
public class TimedProcessRunnerSystem extends EntitySystem {
  @Mapper
  private ComponentMapper<TimedProcess> processMapper;

  public TimedProcessRunnerSystem() {

    super(Aspect.getAspectForAll(TimedProcess.class));
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entityImmutableBag) {
    for (int i = 0; i < entityImmutableBag.size(); i++) {
      Entity entity = entityImmutableBag.get(i);
      TimedProcess process = processMapper.get(entity);

      if (process.isReady()) {
        process.run();
        entity.deleteFromWorld();
      } else {
        process.tick();
      }
    }
  }

  @Override
  protected boolean checkProcessing() {

    return true;
  }
}
