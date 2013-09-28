package com.ziodyne.sometrpg;

import com.badlogic.gdx.Game;
import com.ziodyne.sometrpg.screens.MainMenu;

public class SomeTRPG extends Game {

	@Override
	public void create() {
		//setScreen(new TestBattle(this));
    setScreen(new MainMenu(this));
	}
}
