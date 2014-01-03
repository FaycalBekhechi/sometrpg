package com.ziodyne.sometrpg.logging;

import com.badlogic.gdx.Gdx;

public class GdxLogger implements Logger {
  private final Class<?> source;

  public GdxLogger(Class<?> source) {
    this.source = source;
  }

  @Override
  public void log(String msg) {
    Gdx.app.log(getTag(), msg);
  }

  @Override
  public void error(String msg) {
    Gdx.app.error(getTag(), msg);
  }

  @Override
  public void error(String msg, Throwable throwable) {
    Gdx.app.error(getTag(), msg, throwable);
  }

  @Override
  public void debug(String msg) {
    Gdx.app.debug(getTag(), msg);
  }

  private String getTag() {
    return source.getSimpleName();
  }
}
