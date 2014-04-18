package com.ziodyne.sometrpg.view.entities;

import javax.xml.soap.Text;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.assets.AssetRepository;
import com.ziodyne.sometrpg.view.components.*;
import com.ziodyne.sometrpg.view.graphics.SpriteLayer;

import java.util.Set;

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

    Sprite sprite = new Sprite(texture, 1, 1, SpriteLayer.FOREGROUND);
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

  public Entity createAnimatedUnit(BattleMap map, Combatant combatant, Texture texture, int nFrames, int frameDims) {
    Entity result = world.createEntity();

    GridPoint2 pos = map.getCombatantPosition(combatant);
    if (pos == null) {
      throw new IllegalArgumentException("Must provide a combatant that is actually on the map.");
    }

    Position position = new Position(pos.x, pos.y);
    result.addComponent(position);

    result.addComponent(new BattleUnit(combatant));

    Array<TextureRegion> regions = new Array<>();
    for (int i = 0; i < nFrames; i++) {
      System.out.println("y:" + (i*frameDims));
      TextureRegion region = new TextureRegion(texture, 0, i*frameDims, frameDims, frameDims);
      regions.add(region);
    }

    Animation animation = new Animation(0.5f, regions, Animation.LOOP);
    SpriteAnimation animationComponent = new SpriteAnimation(animation);
    result.addComponent(animationComponent);

    Sprite sprite = new Sprite(regions.get(0), 1, 1, SpriteLayer.FOREGROUND);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);
    result.addComponent(sprite);

    return result;
  }

  public Entity createMapAttackOverlay(Set<GridPoint2> locations) {
    Entity movementOverlay = world.createEntity();

    MapSquareOverlay movementOverlayComponent = new MapSquareOverlay(locations, new Color(1, 0, 0, 0.5f));
    movementOverlay.addComponent(movementOverlayComponent);

    return movementOverlay;
  }

  public Entity createMapMovementOverlay(Set<GridPoint2> locations) {
    Entity movementOverlay = world.createEntity();

    MapSquareOverlay movementOverlayComponent = new MapSquareOverlay(locations);
    movementOverlay.addComponent(movementOverlayComponent);

    return movementOverlay;
  }

  public Entity createMapSelector() {
    Entity mapSelectorEntity = world.createEntity();

    Texture texture = repository.get("grid_overlay.png");
    Sprite sprite = new Sprite(texture, 1, 1, SpriteLayer.BACKGROUND);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);

    mapSelectorEntity.addComponent(sprite);
    mapSelectorEntity.addComponent(new Position());

    return mapSelectorEntity;
  }

  public Entity createUnitSelector(GridPoint2 point) {
    Entity mapSelectorEntity = world.createEntity();

    Texture texture = repository.get("grid_overlay.png");
    Sprite sprite = new Sprite(texture, 1, 1, SpriteLayer.BACKGROUND);
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
    Sprite sprite = new Sprite(region, width, height, SpriteLayer.FOREGROUND);

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

  public Entity createStage(Stage gdxStage) {
    Entity entity = world.createEntity();

    com.ziodyne.sometrpg.view.components.Stage stageComponent = new com.ziodyne.sometrpg.view.components.Stage(gdxStage);
    entity.addComponent(stageComponent);

    return entity;
  }
}
