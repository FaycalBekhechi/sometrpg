package com.ziodyne.sometrpg.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.ziodyne.sometrpg.components.Position;
import com.ziodyne.sometrpg.components.Sprite;
import com.ziodyne.sometrpg.components.TiledMapComponent;
import com.ziodyne.sometrpg.input.CameraMoveController;
import com.ziodyne.sometrpg.systems.SpriteRenderSystem;
import com.ziodyne.sometrpg.systems.TiledMapRenderSystem;
import com.ziodyne.sometrpg.tween.CameraAccessor;

public class TestBattle implements Screen {
  private final Game game;
  private final OrthographicCamera camera;
  private final TweenManager tweenManager;
  private final Position mapSelectorPos = new Position();
  private World world;
  private SpriteRenderSystem spriteRenderSystem;
  private TiledMapRenderSystem mapRenderSystem;
  private final SpriteBatch spriteBatch;

  public TestBattle(Game game) {
    this.game = game;
    this.tweenManager = new TweenManager();
    this.spriteBatch = new SpriteBatch();
    this.camera = new OrthographicCamera();
    camera.setToOrtho(false, 30, 20);

    Tween.registerAccessor(Camera.class, new CameraAccessor());

    spriteRenderSystem = new SpriteRenderSystem(camera, spriteBatch);
    mapRenderSystem = new TiledMapRenderSystem(camera);

    world = new World();
    world.setManager(new TagManager());
    world.setSystem(spriteRenderSystem, true);
    world.setSystem(mapRenderSystem, true);

    world.initialize();

    TiledMap tiledMap = new TmxMapLoader().load("maps/test/test.tmx");

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(new CameraMoveController(camera, world, tweenManager));

    Gdx.input.setInputProcessor(multiplexer);

    TiledMapComponent tiledMapComponent = new TiledMapComponent(tiledMap, 1 / 32f, spriteBatch);

    Entity tileSelector = world.createEntity();
    tileSelector.addComponent(mapSelectorPos);

    Sprite sprite = new Sprite("grid_overlay.png", 1, 1);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);

    tileSelector.addComponent(sprite);
    world.addEntity(tileSelector);

    Entity map = world.createEntity();
    map.addComponent(tiledMapComponent);
    world.addEntity(map);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

    camera.update();

    Vector3 unprojectedCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    camera.unproject(unprojectedCoords);

    mapSelectorPos.setX(unprojectedCoords.x);
    mapSelectorPos.setY(unprojectedCoords.y);

    tweenManager.update(delta);
    world.setDelta(delta);
    world.process();

    mapRenderSystem.process();
    spriteRenderSystem.process();
  }

  @Override
  public void show() {
  }

  @Override
  public void resize(int width, int height) {
  }

  @Override
  public void hide() {
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void dispose() {
  }
}
