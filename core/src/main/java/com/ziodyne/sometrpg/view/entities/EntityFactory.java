package com.ziodyne.sometrpg.view.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.Inject;
import com.ziodyne.sometrpg.logic.loader.models.AnimationSpec;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.animation.AnimationUtils;
import com.ziodyne.sometrpg.view.assets.AssetRepository;
import com.ziodyne.sometrpg.view.assets.MovementGuideAtlas;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.MapGridOverlay;
import com.ziodyne.sometrpg.view.components.MapSquareOverlay;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Shader;
import com.ziodyne.sometrpg.view.components.SpriteAnimation;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.components.Text;
import com.ziodyne.sometrpg.view.components.TileCursor;
import com.ziodyne.sometrpg.view.components.TiledMapComponent;
import com.ziodyne.sometrpg.view.components.ViewportPosition;
import com.ziodyne.sometrpg.view.components.ViewportSpaceSprite;
import com.ziodyne.sometrpg.view.components.VoidSprite;
import com.ziodyne.sometrpg.view.graphics.SpriteLayer;
import com.ziodyne.sometrpg.view.navigation.PathSegment;
import com.ziodyne.sometrpg.view.navigation.PathUtils;
import com.ziodyne.sometrpg.view.rendering.Sprite;

public class EntityFactory {
  private final Engine engine;
  private final AssetRepository repository;

  private static final Texture BLACK_BOX;

  static {
    Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(0,0,0,1);
    pixmap.fill();

    BLACK_BOX = new Texture(pixmap);

  }

  @Inject
  public EntityFactory(Engine engine, AssetRepository repository) {
    this.engine = engine;
    this.repository = repository;
  }

  public Entity createViewportText(String text, Vector2 position, int size) {

    BitmapFont font = repository.get("fonts/baked/futura-" + size + ".fnt", BitmapFont.class);
    ViewportPosition posComponent = new ViewportPosition(position);
    Text textComponent = new Text(font, text);

    ShaderProgram distanceFieldShader = repository.get("shaders/distance_field.json");
    Shader shaderComponent = new Shader(distanceFieldShader);

    return createEntity(posComponent, textComponent, shaderComponent);
  }

  public Entity createViewportText(String text, Vector2 position) {

    BitmapFont font = repository.get("fonts/baked/futura-64.fnt", BitmapFont.class);
    ViewportPosition posComponent = new ViewportPosition(position);
    Text textComponent = new Text(font, text);

    ShaderProgram distanceFieldShader = repository.get("shaders/distance_field.json");
    Shader shaderComponent = new Shader(distanceFieldShader);

    return createEntity(posComponent, textComponent, shaderComponent);
  }

  public Entity createCenteredText(String text, Vector2 position) {

    BitmapFont font = repository.get("fonts/baked/futura-16.fnt", BitmapFont.class);
    BitmapFont.TextBounds bounds = font.getBounds(text);
    Vector2 center = new Vector2(bounds.width / 2, 0);

    return createText(text, position, center);
  }

  public Entity createText(String text, Vector2 position, Vector2 offset) {

    BitmapFont font = repository.get("fonts/baked/futura-16.fnt", BitmapFont.class);
    Position posComponent = new Position(position.x + offset.x, position.y + offset.y);
    Text textComponent = new Text(font, text);

    ShaderProgram distanceFieldShader = repository.get("shaders/distance_field.json");
    Shader shaderComponent = new Shader(distanceFieldShader);

    return createEntity(posComponent, textComponent, shaderComponent);
  }

  public Entity createText(String text, Vector2 position, int size) {
    BitmapFont font = repository.get("fonts/baked/futura-" + size + ".fnt", BitmapFont.class);
    ShaderProgram distanceFieldShader = repository.get("shaders/distance_field.json", ShaderProgram.class);

    return createEntity(
      new Position(position.x, position.y),
      new Text(font, text),
      new Shader(distanceFieldShader)
    );
  }

  public HealthBar createHealthBar(RenderedCombatant renderedCombatant) {

    Combatant combatant = renderedCombatant.getCombatant();
    Vector2 pos = renderedCombatant.getPosition();

    float barHeight = 2f;
    Vector2 healthBarPos = new Vector2(pos.x, (pos.y - barHeight));
    Texture barTexture = repository.get("data/health_bar.png", Texture.class);
    Sprite sprite = new Sprite(barTexture, 28f * combatant.getHealthPct(), barHeight);
    SpriteComponent spriteComponent = new SpriteComponent(sprite, SpriteLayer.FOREGROUND.getZIndex() + 100);
    Position position = new Position(healthBarPos.x + 2, healthBarPos.y + barHeight);

    Entity entity = createEntity(spriteComponent, position);
    engine.addEntity(entity);
    return new HealthBar(engine, entity);
  }

  public RenderedCombatant createAnimatedUnit(BattleMap map, Combatant combatant, Set<UnitEntityAnimation> animations) {
    Entity result = new Entity();

    GridPoint2 pos = map.getCombatantPosition(combatant);
    if (pos == null) {
      throw new IllegalArgumentException("Must provide a combatant that is actually on the map.");
    }

    Position position = new Position(pos.x*32f, pos.y*32f);
    result.add(position);

    Map<AnimationType, Vector2> offsets = new HashMap<>();
    Map<AnimationType, Animation> anims = new HashMap<>();
    for (UnitEntityAnimation entityAnimation : animations) {
      AnimationType type = entityAnimation.getType();
      AnimationSpec spec = entityAnimation.getSpec();
      Texture tex = entityAnimation.getTexture();
      int[] offset = spec.getOffset();
      if (offset.length > 0) {
        offsets.put(type, new Vector2(offset[0], offset[1]));
      }

      anims.put(type, AnimationUtils.createFromSpec(tex, spec, entityAnimation.getGridSize()));
    }

    Animation idle = anims.get(AnimationType.IDLE);
    SpriteAnimation animationComponent = new SpriteAnimation(idle);
    result.add(animationComponent);

    Sprite sprite = new Sprite(idle.getKeyFrame(0), 32f, 32f);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);
    result.add(new SpriteComponent(sprite, SpriteLayer.FOREGROUND.getZIndex() + 100));

    /*
    ShaderProgram program = repository.get("shaders/color_tint.json", ShaderProgram.class);
    Shader shader = new Shader(program, (shaderProgram, delta) -> {
      float whiteAmount = 0.6f;
      shaderProgram.setUniformf("u_tintColor", new Color(whiteAmount, 0, 0, 1f));
    });
    result.add(shader);
    */

    BattleUnit battleUnit = new BattleUnit(combatant, anims, offsets);
    result.add(battleUnit);
    engine.addEntity(result);

    return new RenderedCombatant(combatant, position, battleUnit);
  }

  public Entity createMapAttackOverlay(Set<GridPoint2> locations) {
    Entity movementOverlay = new Entity();

    MapSquareOverlay movementOverlayComponent = new MapSquareOverlay(locations, new Color(1, 0, 0, 0.5f));
    movementOverlay.add(movementOverlayComponent);

    return movementOverlay;
  }

  /**
   * Create entities for the arrow that shows the player the path their unit will take when they confirm the move.
   * @param path The {@link com.ziodyne.sometrpg.logic.navigation.Path} to render
   * @return A {@link java.util.Set} of {@link Entity} representing each tile in the path.
   */
  public Set<Entity> createPathGuides(Path<GridPoint2> path) {

    List<PathSegment> segments = PathUtils.segmentPath(path);
    TextureAtlas atlas = repository.get("data/movement_guide.atlas");
    MovementGuideAtlas movementGuideAtlas = new MovementGuideAtlas(atlas);

    return IntStream.range(0, segments.size()) // For each segment, but we also need the index.
      .mapToObj((idx) -> {
        PathSegment seg = segments.get(idx);
        Entity ent = new Entity();

        // If we're approaching the end of the segments, draw an arrow head instead of a line to indicate the destination
        TextureRegion region;
        if (idx+1 == segments.size()) {
          region = movementGuideAtlas.getCapRegion(seg.getType());
        } else {
          region = movementGuideAtlas.getLineRegion(seg.getType());
        }

        Sprite sprite = new Sprite(region, region.getRegionWidth(), region.getRegionHeight());
        ent.add(new SpriteComponent(sprite, SpriteLayer.BACKGROUND));

        GridPoint2 point = seg.getPoint();
        ent.add(new Position(point.x * 32f, point.y * 32f));

        return ent;
      })
      .collect(Collectors.toSet());
  }

  public Entity createMapMovementOverlay(Set<GridPoint2> locations) {
    Entity movementOverlay = new Entity();

    MapSquareOverlay movementOverlayComponent = new MapSquareOverlay(locations);
    movementOverlay.add(movementOverlayComponent);

    return movementOverlay;
  }

  public Entity createMapSelector() {
    Entity mapSelectorEntity = new Entity();

    Texture texture = repository.get("grid_overlay.png");
    Sprite sprite = new Sprite(texture, 32f, 32f);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);

    mapSelectorEntity.add(new SpriteComponent(sprite, SpriteLayer.BACKGROUND));
    mapSelectorEntity.add(new Position());
    mapSelectorEntity.add(new TileCursor());

    return mapSelectorEntity;
  }

  public Entity createUnitSelector(GridPoint2 point) {
    Entity mapSelectorEntity = new Entity();

    Texture texture = repository.get("grid_overlay.png");
    Sprite sprite = new Sprite(texture, 32f, 32f);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);

    mapSelectorEntity.add(new SpriteComponent(sprite, SpriteLayer.BACKGROUND));
    mapSelectorEntity.add(new Position(point.x, point.y));
    mapSelectorEntity.add(new TileCursor());

    return mapSelectorEntity;
  }

  public Entity createMapGridOverlay(int rows, int columns, float size, GridPoint2 pos) {
    Entity overlayEntity = new Entity();

    MapGridOverlay gridOverlayComponent = new MapGridOverlay(rows, columns, size, 0.3f);
    overlayEntity.add(gridOverlayComponent);

    Position positionComponent = new Position(pos.x, pos.y);
    overlayEntity.add(positionComponent);

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
    Entity result = new Entity();

    for (Component comp : components) {
      result.add(comp);
    }

    return result;
  }

  public Entity createMapObject(RectangleMapObject object, TextureRegion region, int zIndex) {
    Entity mapObj = new Entity();

    Rectangle rect = object.getRectangle();
    Position pos = new Position(rect.x, rect.y);

    float width = region.getRegionWidth();
    float height = region.getRegionHeight();

    Sprite sprite = new Sprite(region, width, height);
    SpriteComponent spriteComponent = new SpriteComponent(sprite, zIndex);

    mapObj.add(pos);
    mapObj.add(spriteComponent);

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
    sprite.setAlpha(0.7f);

    ViewportSpaceSprite spriteComponent = new ViewportSpaceSprite(sprite);
    Position positionComponent = new Position(position.x, position.y);

    return createEntity(spriteComponent, positionComponent);
  }

  public Entity createPortraitAttackIcon(Vector2 screenSpacePosition) {
    Texture tex = repository.get("data/portraits/icons/attack.png");
    Sprite sprite = new Sprite(tex, 50, 50);
    ViewportSpaceSprite spriteComponent = new ViewportSpaceSprite(sprite);

    Position posComponent = new Position(screenSpacePosition.x, screenSpacePosition.y);

    return createEntity(spriteComponent, posComponent);
  }

  public Entity createPortraitMoveIcon(Vector2 screenSpacePosition) {
    Texture tex = repository.get("data/portraits/icons/move.png");
    Sprite sprite = new Sprite(tex, 50, 50);
    ViewportSpaceSprite spriteComponent = new ViewportSpaceSprite(sprite);

    Position posComponent = new Position(screenSpacePosition.x, screenSpacePosition.y);

    return createEntity(spriteComponent, posComponent);
  }

  public Entity createDockPortrait(Character character, Vector2 screenSpacePosition) {

    Texture tex = repository.get("data/portraits/" + character.getId() + ".png", Texture.class);
    if (tex == null) {
      throw new IllegalArgumentException("No portrait texture found for character: " + character.getId());
    }

    Sprite sprite = new Sprite(tex, 200, 200);
    ViewportSpaceSprite spriteComponent = new ViewportSpaceSprite(sprite);

    Position posComponent = new Position(screenSpacePosition.x, screenSpacePosition.y);

    return createEntity(spriteComponent, posComponent);
  }

  public Entity createTiledMap(TiledMap map, SpriteBatch batch) {
    Entity mapEntity = new Entity();

    TiledMapComponent tiledMapComponent = new TiledMapComponent(map, batch);
    mapEntity.add(tiledMapComponent);

    return mapEntity;
  }

  public Entity createStage(Stage gdxStage) {
    Entity entity = new Entity();

    com.ziodyne.sometrpg.view.components.Stage stageComponent = new com.ziodyne.sometrpg.view.components.Stage(gdxStage);
    entity.add(stageComponent);

    return entity;
  }
}
