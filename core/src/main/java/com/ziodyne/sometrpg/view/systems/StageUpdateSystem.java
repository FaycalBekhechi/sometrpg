package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.ziodyne.sometrpg.view.components.Stage;

public class StageUpdateSystem extends IteratingSystem {

  public StageUpdateSystem() {
    super(Family.getFamilyFor(Stage.class));
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

    com.badlogic.gdx.scenes.scene2d.Stage stage = entity.getComponent(Stage.class).getGdxStage();

    stage.act(deltaTime);
  }
}
