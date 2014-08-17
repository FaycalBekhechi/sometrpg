package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Text;

public class TextRenderSystem extends IteratingSystem {

  private final SpriteBatch batch;

  @Inject
  TextRenderSystem(SpriteBatch batch) {
    super(Family.getFamilyFor(Position.class, Text.class));
    this.batch = batch;
  }

  @Override
  public void update(float deltaTime) {

    batch.begin();
    super.update(deltaTime);
    batch.end();
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

    Position pos = entity.getComponent(Position.class);
    Text text = entity.getComponent(Text.class);
    BitmapFont font = text.getFont();
    font.draw(batch, text.getCharacters(), pos.getX(), pos.getY());
  }
}
