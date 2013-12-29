package com.ziodyne.sometrpg.view.entities;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.google.inject.Inject;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.components.BattleUnit;
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

  public Entity createUnit(BattleMap map, Combatant combatant, String texturePath) {
    Entity unitEntity = world.createEntity();

    Sprite sprite = new Sprite(texturePath, 1, 1);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);
    unitEntity.addComponent(sprite);

    GridPoint2 pos = map.getCombatantPosition(combatant);
    if (pos == null) {
      throw new IllegalArgumentException("Must provide a combatant that is actually on the map.");
    }

    Position position = new Position(pos.x, pos.y);
    unitEntity.addComponent(position);

    unitEntity.addComponent(new BattleUnit(combatant));

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

  public Entity createTiledMap(TiledMap map, SpriteBatch batch, float gridSquareSize) {
    Entity mapEntity = world.createEntity();

    TiledMapComponent tiledMapComponent = new TiledMapComponent(map, 1 / gridSquareSize, batch);
    mapEntity.addComponent(tiledMapComponent);

    return mapEntity;
  }
}
