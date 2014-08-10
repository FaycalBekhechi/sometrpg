package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.VoidSprite;
import com.ziodyne.sometrpg.view.rendering.SpriteBatchRenderer;

public class VoidSpriteRenderSystem extends IteratingSystem {
  private final SpriteBatchRenderer renderer;

  public interface Factory {
    public VoidSpriteRenderSystem create(OrthographicCamera  camera);
  }

  @AssistedInject
  @SuppressWarnings("unchecked")
  public VoidSpriteRenderSystem(@Assisted OrthographicCamera camera, SpriteBatch batch) {

    super(Family.getFamilyFor(Position.class, VoidSprite.class));

    renderer = new SpriteBatchRenderer(camera, batch);
  }

  @Override
  public void update(float deltaTime) {

    renderer.begin();
    super.update(deltaTime);
    renderer.end();
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

    VoidSprite spriteComponent = entity.getComponent(VoidSprite.class);
    Position pos = entity.getComponent(Position.class);

    renderer.render(spriteComponent.getSprite(), new Vector2(pos.getX(), pos.getY()));
  }
}
