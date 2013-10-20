package com.ziodyne.sometrpg.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

public class Splash implements Screen {
  private static final float SPLASH_DURATION = 1.75f;
  private static final float SPLASH_FADE_DURATION = 0.75f;
  private final Game game;
  private final Stage stage;
  
  public Splash(Game game) {
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
        Actions.fadeIn(SPLASH_FADE_DURATION),
        Actions.delay(SPLASH_DURATION),
        Actions.fadeOut(SPLASH_FADE_DURATION),
        new Action() {
          @Override
          public boolean act(float dt) {
            // This dependency should be inverted somehow, but that seems
            // challenging
            game.setScreen(new MainMenu(game));
            return true;
          }
        }
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
