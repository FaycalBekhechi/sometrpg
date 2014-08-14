package com.ziodyne.sometrpg.util;

import com.badlogic.gdx.Gdx;

public interface Logged {
  default void logError(String msg) {

    if (Gdx.app != null) {
      Gdx.app.error(getTag(), msg);
    }
  }

  default void logError(String msg, Throwable throwable) {

    if (Gdx.app != null) {
      Gdx.app.error(getTag(), msg, throwable);
    }
  }

  default void logDebug(String msg) {

    if (Gdx.app != null) {
      Gdx.app.debug(getTag(), msg);
    }
  }

  default String getTag() {

    return getClass().getSimpleName();
  }
}
