package com.ziodyne.sometrpg.view.entities;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.Inject;
import com.ziodyne.sometrpg.logic.loader.models.AnimationSpec;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.animation.AnimationUtils;
import com.ziodyne.sometrpg.view.assets.AssetRepository;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.MapGridOverlay;
import com.ziodyne.sometrpg.view.components.MapSquareOverlay;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.components.SpriteAnimation;
import com.ziodyne.sometrpg.view.components.TiledMapComponent;
import com.ziodyne.sometrpg.view.components.UnitSelector;
import com.ziodyne.sometrpg.view.components.ViewportSpaceSprite;
import com.ziodyne.sometrpg.view.components.VoidSprite;
import com.ziodyne.sometrpg.view.graphics.SpriteLayer;
import com.ziodyne.sometrpg.view.navigation.PathSegment;
import com.ziodyne.sometrpg.view.navigation.PathUtils;
import com.ziodyne.sometrpg.view.rendering.Sprite;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EntityFactory {
  private final World world;
  private final AssetRepository repository;

  private static final Texture BLACK_BOX;
  private static final Map<PathSegment.Type, Vector2> PATH_GUIDE_REGIONS = new HashMap<PathSegment.Type, Vector2>(){{
    put(PathSegment.Type.N2E, new Vector2(0, 0));
    put(PathSegment.Type.E, new Vector2(3, 0));
    put(PathSegment.Type.S, new Vector2(0, 1));
    put(PathSegment.Type.E2N, new Vector2(1, 2));
    put(PathSegment.Type.S2W, new Vector2(1, 2));
    put(PathSegment.Type.E2S, new Vector2(3, 3));
    put(PathSegment.Type.W2N, new Vector2(0, 2));
    put(PathSegment.Type.S2E, new Vector2(0, 2));
    put(PathSegment.Type.W, new Vector2(3, 0));
    put(PathSegment.Type.E2S, new Vector2(2, 2));
    put(PathSegment.Type.N2W, new Vector2(2, 2));
  }};

  private static final Map<PathSegment.Type, Vector2> PATH_CAP_REGIONS = new HashMap<PathSegment.Type, Vector2>() {{
    put(PathSegment.Type.N, new Vector2(1, 1));
    put(PathSegment.Type.S, new Vector2(2, 1));
    put(PathSegment.Type.W, new Vector2(2, 0));
    put(PathSegment.Type.E, new Vector2(1, 0));
  }};

  static {
    Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(0,0,0,1);
    pixmap.fill();

    BLACK_BOX = new Texture(pixmap);

  }

  @Inject
  public EntityFactory(World world, AssetRepository repository) {
    this.world = world;
    this.repository = repository;
  }

  public Entity createAnimatedUnit(BattleMap map, Combatant combatant, Set<UnitEntityAnimation> animations) {
    Entity result = world.createEntity();

    GridPoint2 pos = map.getCombatantPosition(combatant);
    if (pos == null) {
      throw new IllegalArgumentException("Must provide a combatant that is actually on the map.");
    }

    Position position = new Position(pos.x*32f, pos.y*32f);
    result.addComponent(position);

    Map<AnimationType, Animation> anims = new HashMap<>();
    for (UnitEntityAnimation entityAnimation : animations) {
      AnimationType type = entityAnimation.getType();
      AnimationSpec spec = entityAnimation.getSpec();
      Texture tex = entityAnimation.getTexture();

      anims.put(type, AnimationUtils.createFromSpec(tex, spec, entityAnimation.getGridSize()));
    }

    Animation idle = anims.get(AnimationType.IDLE);
    SpriteAnimation animationComponent = new SpriteAnimation(idle);
    result.addComponent(animationComponent);

    Sprite sprite = new Sprite(idle.getKeyFrame(0), 32f, 32f);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);
    result.addComponent(new SpriteComponent(sprite, SpriteLayer.FOREGROUND));

    result.addComponent(new BattleUnit(combatant, anims));

    return result;
  }

  public Entity createMapAttackOverlay(Set<GridPoint2> locations) {
    Entity movementOverlay = world.createEntity();

    MapSquareOverlay movementOverlayComponent = new MapSquareOverlay(locations, new Color(1, 0, 0, 0.5f));
    movementOverlay.addComponent(movementOverlayComponent);

    return movementOverlay;
  }

  public Set<Entity> createPathGuides(Path<GridPoint2> path) {

    List<PathSegment> segments = PathUtils.segmentPath(path);
    Texture texture = repository.get("data/arrow_sheet.png");
    return IntStream.range(0, segments.size())
      .mapToObj((idx) -> {
        PathSegment seg = segments.get(idx);
        Entity ent = world.createEntity();
        Vector2 segRegionPos;
        if (idx+1 == segments.size()) {
          segRegionPos = PATH_CAP_REGIONS.get(seg.getType());
        } else {
          segRegionPos = PATH_GUIDE_REGIONS.get(seg.getType());
        }

        if (segRegionPos == null) {
          segRegionPos = new Vector2();
        }

        TextureRegion region = new TextureRegion(texture, (int) segRegionPos.x * 32, (int) segRegionPos.y * 32, 32, 32);
        Sprite sprite = new Sprite(region, 32, 32);
        ent.addComponent(new SpriteComponent(sprite, SpriteLayer.BACKGROUND));

        GridPoint2 point = seg.getPoint();
        ent.addComponent(new Position(point.x * 32f, point.y * 32f));

        return ent;
      })
      .collect(Collectors.toSet());
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
    Sprite sprite = new Sprite(texture, 32f, 32f);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);

    mapSelectorEntity.addComponent(new SpriteComponent(sprite, SpriteLayer.BACKGROUND));
    mapSelectorEntity.addComponent(new Position());

    return mapSelectorEntity;
  }

  public Entity createUnitSelector(GridPoint2 point) {
    Entity mapSelectorEntity = world.createEntity();

    Texture texture = repository.get("grid_overlay.png");
    Sprite sprite = new Sprite(texture, 32f, 32f);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);

    mapSelectorEntity.addComponent(new SpriteComponent(sprite, SpriteLayer.BACKGROUND));
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

  public Entity createVoid(Viewport viewport) {
    Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(0, 0, 0.2f, 1);
    pixmap.fill();

    Texture fuglyBlue = new Texture(pixmap);
    Sprite sprite = new Sprite(fuglyBlue, viewport.getViewportWidth(), viewport.getViewportHeight());
    VoidSprite voidTag = new VoidSprite(sprite);
    Position position = new Position(0, 0);

    return createEntity(voidTag, position);
  }

  private Entity createEntity(Component... components) {
    Entity result = world.createEntity();

    for (Component comp : components) {
      result.addComponent(comp);
    }

    return result;
  }

  public Entity createMapObject(RectangleMapObject object, TextureRegion region, int zIndex) {
    Entity mapObj = world.createEntity();

    Rectangle rect = object.getRectangle();
    Position pos = new Position(rect.x, rect.y);

    float width = region.getRegionWidth();
    float height = region.getRegionHeight();

    Sprite sprite = new Sprite(region, width, height);
    SpriteComponent spriteComponent = new SpriteComponent(sprite, zIndex);

    mapObj.addComponent(pos);
    mapObj.addComponent(spriteComponent);

    return mapObj;
  }

  /**
   * Create an entity that renders the default menu background image
   * @param position The position in viewport coordinates (bottom-left)
   * @param width The width of the background
   * @param height The height of the background
   * @return The entity
   */
  public Entity createMenuBg(Vector2 position, float width, float height) {
    Sprite sprite = new Sprite(BLACK_BOX, width, height);
    sprite.setAlpha(0.5f);

    ViewportSpaceSprite spriteComponent = new ViewportSpaceSprite(sprite);
    Position positionComponent = new Position(position.x, position.y);

    return createEntity(spriteComponent, positionComponent);
  }

  public Entity createTiledMap(TiledMap map, SpriteBatch batch) {
    Entity mapEntity = world.createEntity();

    TiledMapComponent tiledMapComponent = new TiledMapComponent(map, batch);
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
