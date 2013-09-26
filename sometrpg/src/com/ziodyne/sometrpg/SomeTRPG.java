package com.ziodyne.sometrpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.ziodyne.sometrpg.screens.Splash;
import com.ziodyne.sometrpg.screens.TestBattle;

public class SomeTRPG extends Game {

	@Override
	public void create() {
		setScreen(new TestBattle(this));
	}
}
