package com.ziodyne.sometrpg.view;

import com.badlogic.gdx.Game;
import com.ziodyne.sometrpg.view.screens.MainMenu;
import com.ziodyne.sometrpg.view.screens.TestBattle;

public class SomeTRPG extends Game {

	@Override
	public void create() {
		//setScreen(new TestBattle());
    setScreen(new MainMenu(new Director(this)));
	  //setScreen(new ChartTestScreen());
	}
}
