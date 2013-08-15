package com.ziodyne.sometrpg.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

public class SplashScreen implements Screen {
  private final Game game;
  private final Stage stage;
  
  public SplashScreen(Game game) {
    this.game = game;
    this.stage = new Stage(800, 480, true);
  }
  
  @Override
  public void render(float delta) {
    stage.act(delta);
    
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    // TODO Auto-generated method stub

  }

  @Override
  public void show() {
    Texture splashLogo = new Texture(Gdx.files.internal("data/libgdx.png"));
    splashLogo.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    
    TextureRegion logoTexRegion = new TextureRegion(splashLogo, 0, 0, 512, 275);
    
    Drawable splashDrawable = new TextureRegionDrawable(logoTexRegion);
    Image splashLogoImage = new Image(splashDrawable, Scaling.stretch);
    splashLogoImage.setFillParent(true);
    splashLogoImage.getColor().a = 0f;
    
    splashLogoImage.addAction(Actions.sequence(
        Actions.fadeIn(0.75f),
        Actions.delay(1.75f),
        Actions.fadeOut(0.75f)
    ));
    
    stage.addActor(splashLogoImage);
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
    // TODO Auto-generated method stub

  }

}
