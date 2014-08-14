package com.ziodyne.sometrpg.view;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.google.inject.Inject;

import java.util.ArrayDeque;
import java.util.Deque;

public class Director {
  private final Game game;
  private final Deque<Screen> screens = new ArrayDeque<Screen>();

  @Inject
  public Director(Game game) {
    this.game = game;
  }

  public void replaceScreen(Screen screen) {
    if (!screens.isEmpty()) {
      Screen prev = screens.pop();
      prev.dispose();
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
      Screen previousScreen = screens.pop();
      previousScreen.dispose();
    }
    Screen newScreen = screens.peek();

    game.setScreen(newScreen);
  }
}
