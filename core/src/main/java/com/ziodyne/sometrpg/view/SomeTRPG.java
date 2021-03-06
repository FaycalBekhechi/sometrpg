package com.ziodyne.sometrpg.view;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ziodyne.sometrpg.RootModule;
import com.ziodyne.sometrpg.view.screens.MainMenu;

public class SomeTRPG extends Game {

  @Override
	public void create() {
    Gdx.app.setLogLevel(Application.LOG_DEBUG);
    Injector injector = Guice.createInjector(new RootModule(this));
    Director director = injector.getInstance(Director.class);
    director.addScreen(injector.getInstance(MainMenu.class));
		//setScreen(new TestBattle());
	  //setScreen(new ChartTestScreen());
	}
}
