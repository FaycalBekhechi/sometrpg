package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.ziodyne.sometrpg.view.components.Stage;

public class StageRenderSystem extends IteratingSystem {

  public StageRenderSystem() {
    super(Family.getFamilyFor(Stage.class));
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    com.badlogic.gdx.scenes.scene2d.Stage gdxStage = entity.getComponent(Stage.class).getGdxStage();
    gdxStage.draw();
  }
}
