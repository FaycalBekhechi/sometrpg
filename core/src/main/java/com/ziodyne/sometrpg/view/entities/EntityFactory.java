package com.ziodyne.sometrpg.view.entities;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.google.inject.Inject;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.assets.AssetRepository;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.MapGridOverlay;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Sprite;
import com.ziodyne.sometrpg.view.components.TiledMapComponent;
import com.ziodyne.sometrpg.view.components.UnitSelector;

public class EntityFactory {
  private final World world;
  private final AssetRepository repository;

  @Inject
  public EntityFactory(World world, AssetRepository repository) {
    this.world = world;
    this.repository = repository;
  }

  public Entity createUnit(BattleMap map, Combatant combatant, Texture texture) {
    Entity unitEntity = world.createEntity();

    Sprite sprite = new Sprite(texture, 1, 1);
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

    Texture texture = repository.get("grid_overlay.png");
    Sprite sprite = new Sprite(texture, 1, 1);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);

    mapSelectorEntity.addComponent(sprite);
    mapSelectorEntity.addComponent(new Position());

    return mapSelectorEntity;
  }

  public Entity createUnitSelector(GridPoint2 point) {
    Entity mapSelectorEntity = world.createEntity();

    Texture texture = repository.get("grid_overlay.png");
    Sprite sprite = new Sprite(texture, 1, 1);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);

    mapSelectorEntity.addComponent(sprite);
    mapSelectorEntity.addComponent(new Position(point.x, point.y));
    mapSelectorEntity.addComponent(new UnitSelector());

    return mapSelectorEntity;
  }

  public Entity createMapGridOverlay(int rows, int columns, float size, GridPoint2 pos) {
    Entity overlayEntity = world.createEntity();

    MapGridOverlay gridOverlayComponent = new MapGridOverlay(rows, columns, size, 0.3f);
    overlayEntity.addComponent(gridOverlayComponent);

    Position positionComponent = new Position(pos.x, pos.y);
    overlayEntity.addComponent(positionComponent);

    return overlayEntity;
  }

  public Entity createMapObject(RectangleMapObject object, TextureRegion region, float scale) {
    Entity mapObj = world.createEntity();

    Rectangle rect = object.getRectangle();
    Position pos = new Position(rect.x*scale, rect.y*scale);

    float width = region.getRegionWidth()*scale;
    float height = region.getRegionHeight()*scale;
    Sprite sprite = new Sprite(region, width, height);

    mapObj.addComponent(pos);
    mapObj.addComponent(sprite);

    return mapObj;
  }

  public Entity createTiledMap(TiledMap map, SpriteBatch batch, float gridSquareSize) {
    Entity mapEntity = world.createEntity();

    TiledMapComponent tiledMapComponent = new TiledMapComponent(map, 1 / gridSquareSize, batch);
    mapEntity.addComponent(tiledMapComponent);

    return mapEntity;
  }
}
