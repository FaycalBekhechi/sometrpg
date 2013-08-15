package com.ziodyne.sometrpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.ziodyne.sometrpg.screens.Splash;

public class SomeTRPG extends Game {
  private OrthographicCamera cam;
  
	@Override
	public void create() {
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 840, 400);
		setScreen(new Splash(this));
	}
}
