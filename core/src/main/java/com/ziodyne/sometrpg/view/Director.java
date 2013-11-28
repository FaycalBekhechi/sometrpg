package com.ziodyne.sometrpg.view;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.Stack;

public class Director {
  private final Game game;
  private final Stack<Screen> screens = new Stack<Screen>();

  public Director(Game game) {
    this.game = game;
  }

  public void replaceScreen(Screen screen) {
    if (!screens.isEmpty()) {
      screens.pop();
    }
    screens.push(screen);
    game.setScreen(screen);
  }

  public void addScreen(Screen screen) {
    screens.push(screen);
    game.setScreen(screen);
  }

  public void popScreen() {
    if (!screens.isEmpty()) {
      screens.pop();

      if (!screens.isEmpty()) {
        game.setScreen(screens.peek());
      }
    }
  }
}
