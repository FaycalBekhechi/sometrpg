package com.ziodyne.sometrpg.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ziodyne.sometrpg.tween.ActorAccessor;

public class MainMenu implements Screen {
  private final Stage stage;
  private final Skin skin;
  private final TweenManager tweenManager;
  private final Label title;
  private final Button startGameButton;
  private final Button quitButton;
  private boolean menuInitialized = false;
  private boolean initializing = false;

  public MainMenu() {
    this.stage = new Stage();
    this.skin = new Skin(Gdx.files.internal("uiskin.json"));
    this.tweenManager = new TweenManager();

    title = new Label("Welcome to Some Tactical RPG", skin);
    title.setX((480 - title.getWidth()) / 2);
    title.setY(310f);

    float buttonHeight = 50;
    float buttonWidth = 200;

    startGameButton = new TextButton("Start Game", skin);
    startGameButton.setX((480 - buttonWidth)/2);
    startGameButton.setY(title.getY() - title.getHeight() - 110);
    startGameButton.setHeight(buttonHeight);
    startGameButton.setWidth(buttonWidth);
    startGameButton.getColor().a = 0;

    quitButton = new TextButton("Quit", skin);
    quitButton.setX((480 - buttonWidth)/2);
    quitButton.setY(startGameButton.getY() - startGameButton.getHeight() - 20);
    quitButton.setHeight(buttonHeight);
    quitButton.setWidth(buttonWidth);
    quitButton.getColor().a = 0;

    stage.addActor(title);
    stage.addActor(startGameButton);
    stage.addActor(quitButton);

    Tween.setCombinedAttributesLimit(4);
    Tween.registerAccessor(Actor.class, new ActorAccessor());
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    stage.act(Gdx.graphics.getDeltaTime());

    tweenManager.update(delta);
    stage.draw();

    if (!menuInitialized && !initializing) {
      initializing = true;
      Timeline.createSequence()
        .push(Tween.to(title, ActorAccessor.POSITION_Y, 0.5f).ease(TweenEquations.easeOutCubic).target(250f))
        .beginParallel()
          .push(Tween.to(startGameButton, ActorAccessor.OPACITY, 0.3f).target(1.0f))
          .push(Tween.to(quitButton, ActorAccessor.OPACITY, 0.3f).target(1.0f))
        .end()
        .setCallback(new TweenCallback() {
          @Override
          public void onEvent(int type, BaseTween<?> source) {
            if (type == COMPLETE) {
              menuInitialized = true;
            }
          }
        })
        .start(tweenManager);
    }

  }

  @Override
  public void resize(int width, int height) {
    stage.setViewport(width, height, true);
  }

  @Override
  public void show() {

  }

  @Override
  public void hide() {
    // TODO Auto-generated method stub

  }

  @Override
  public void pause() {
    // TODO Auto-generated method stub

  }

  @Override
  public void resume() {
    // TODO Auto-generated method stub

  }

  @Override
  public void dispose() {
    stage.dispose();
  }

}
