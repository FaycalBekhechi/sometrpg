package com.ziodyne.sometrpg.view;

import com.badlogic.gdx.Game;
import com.ziodyne.sometrpg.view.screens.MainMenu;
import com.ziodyne.sometrpg.view.screens.TestBattle;

public class SomeTRPG extends Game {
  private final Director director;

  public SomeTRPG() {
    director = new Director(this);
  }

  @Override
	public void create() {
    director.addScreen(new MainMenu(director));
		//setScreen(new TestBattle());
	  //setScreen(new ChartTestScreen());
	}
}
