package com.ziodyne.sometrpg.view.entities;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.GridPoint2;
import com.google.inject.Inject;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Sprite;
import com.ziodyne.sometrpg.view.components.TiledMapComponent;
import com.ziodyne.sometrpg.view.components.UnitSelector;

public class EntityFactory {
  private final World world;

  @Inject
  public EntityFactory(World world) {
    this.world = world;
  }

  public Entity createUnit(Unit unit, String texturePath, int x, int y) {
    Entity unitEntity = world.createEntity();

    Sprite sprite = new Sprite(texturePath, 1, 1);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);
    unitEntity.addComponent(sprite);

    Position position = new Position(x, y);
    unitEntity.addComponent(position);

    return unitEntity;
  }

  public Entity createMapSelector() {
    Entity mapSelectorEntity = world.createEntity();

    Sprite sprite = new Sprite("grid_overlay.png", 1, 1);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);

    mapSelectorEntity.addComponent(sprite);
    mapSelectorEntity.addComponent(new Position());

    return mapSelectorEntity;
  }

  public Entity createUnitSelector(GridPoint2 point) {
    Entity mapSelectorEntity = world.createEntity();

    Sprite sprite = new Sprite("grid_overlay.png", 1, 1);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);

    mapSelectorEntity.addComponent(sprite);
    mapSelectorEntity.addComponent(new Position(point.x, point.y));
    mapSelectorEntity.addComponent(new UnitSelector());

    return mapSelectorEntity;
  }

  public Entity createTiledMap(TiledMap map, SpriteBatch batch) {
    Entity mapEntity = world.createEntity();

    TiledMapComponent tiledMapComponent = new TiledMapComponent(map, 1 / 32f, batch);
    mapEntity.addComponent(tiledMapComponent);

    return mapEntity;
  }
}
