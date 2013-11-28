package com.ziodyne.sometrpg.view.entities;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Texture;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Sprite;

public class EntityFactory {
  private final World world;

  public EntityFactory(World world) {
    this.world = world;
  }

  public Entity createUnit(Unit unit, String texturePath, int x, int y) {
    Entity unitEntity = world.createEntity();

    Sprite sprite = new Sprite("grid_overlay.png", 1, 1);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);
    unitEntity.addComponent(sprite);

    Position position = new Position(x, y);
    unitEntity.addComponent(position);

    return unitEntity;
  }
}
