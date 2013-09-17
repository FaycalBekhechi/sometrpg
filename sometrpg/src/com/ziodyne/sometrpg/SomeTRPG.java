package com.ziodyne.sometrpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.ziodyne.sometrpg.screens.Splash;
import com.ziodyne.sometrpg.screens.TestBattle;

public class SomeTRPG extends Game {
  private OrthographicCamera cam;
  
	@Override
	public void create() {
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 840, 400);
		setScreen(new TestBattle(this));
	}
}
