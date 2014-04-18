package com.ziodyne.sometrpg.logic.loader.models;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum PlayMode {
  NORMAL(Animation.NORMAL),
  REVERSED(Animation.REVERSED),
  LOOP(Animation.LOOP),
  LOOP_REVERSED(Animation.LOOP_REVERSED),
  LOOP_PINGPONG(Animation.LOOP_PINGPONG),
  LOOP_RANDOM(Animation.LOOP_RANDOM);

  PlayMode(int gdxPlayMode) {

    this.gdxPlayMode = gdxPlayMode;
  }

  public static final Map<String, PlayMode> MODE_MAP = new HashMap<>();
  static {
    MODE_MAP.put("normal", NORMAL);
    MODE_MAP.put("reversed", REVERSED);
    MODE_MAP.put("loop", LOOP);
    MODE_MAP.put("loop_reversed", LOOP_REVERSED);
    MODE_MAP.put("ping_pong", LOOP_PINGPONG);
    MODE_MAP.put("random", LOOP_RANDOM);
  }

  @JsonCreator
  public static PlayMode forValue(String value) {
    return MODE_MAP.get(value);
  }

  private int gdxPlayMode;

  public int getGdxPlayMode() {

    return gdxPlayMode;
  }
}
