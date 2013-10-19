package com.ziodyne.sometrpg.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ziodyne.sometrpg.components.Player;
import com.ziodyne.sometrpg.components.Position;
import com.ziodyne.sometrpg.components.Sprite;
import com.ziodyne.sometrpg.input.CameraMoveController;
import com.ziodyne.sometrpg.input.UnitController;
import com.ziodyne.sometrpg.systems.SpriteRenderSystem;
import com.ziodyne.sometrpg.systems.TiledMapRenderSystem;
import com.ziodyne.sometrpg.tween.CameraAccessor;

public class TestBattle implements Screen {
  private final Game game;
  private final OrthographicCamera camera;
  private final TweenManager tweenManager;
  private World world;
  private SpriteRenderSystem spriteRenderSystem;
  private TiledMapRenderSystem mapRenderSystem;
  private final SpriteBatch spriteBatch;

  public TestBattle(Game game) {
    this.game = game;
    this.tweenManager = new TweenManager();
    this.spriteBatch = new SpriteBatch();
    this.camera = new OrthographicCamera();
    camera.setToOrtho(false, 800, 400);

    Tween.registerAccessor(Camera.class, new CameraAccessor());

    spriteRenderSystem = new SpriteRenderSystem(camera, spriteBatch);
    mapRenderSystem = new TiledMapRenderSystem(camera);

    world = new World();
    world.setSystem(spriteRenderSystem, true);

    world.initialize();

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(new CameraMoveController(camera, world, tweenManager));
    multiplexer.addProcessor(new UnitController());

    Gdx.input.setInputProcessor(multiplexer);

    Entity testEntity = world.createEntity();

    testEntity.addComponent(new Position());
    testEntity.addComponent(new Sprite("data/libgdx.png"));
    testEntity.addComponent(new Player());
    testEntity.addToWorld();
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0,0,0.2f,1);
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

    tweenManager.update(delta);
    world.setDelta(delta);
    world.process();

    camera.update();
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
