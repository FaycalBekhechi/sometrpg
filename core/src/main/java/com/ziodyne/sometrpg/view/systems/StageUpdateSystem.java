package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.ziodyne.sometrpg.view.components.Stage;

public class StageUpdateSystem extends EntitySystem {
  @Mapper
  private ComponentMapper<Stage> stageMapper;

  public StageUpdateSystem() {
    super(Aspect.getAspectForAll(Stage.class));
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entityImmutableBag) {
    for (int i = 0; i < entityImmutableBag.size(); i++) {
      Entity entity = entityImmutableBag.get(i);
      com.badlogic.gdx.scenes.scene2d.Stage stage = stageMapper.get(entity).getGdxStage();

      stage.act(world.getDelta());
    }
  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }
}
