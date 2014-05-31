package com.ziodyne.sometrpg.logic.loader.models;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum PlayMode {
  NORMAL(Animation.PlayMode.NORMAL),
  REVERSED(Animation.PlayMode.REVERSED),
  LOOP(Animation.PlayMode.LOOP),
  LOOP_REVERSED(Animation.PlayMode.LOOP_REVERSED),
  LOOP_PINGPONG(Animation.PlayMode.LOOP_PINGPONG),
  LOOP_RANDOM(Animation.PlayMode.LOOP_RANDOM);

  PlayMode(Animation.PlayMode gdxPlayMode) {

    this.gdxPlayMode = gdxPlayMode;
  }

  private static final Map<String, PlayMode> MODE_MAP = new HashMap<>();
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

  private Animation.PlayMode gdxPlayMode;

  public Animation.PlayMode getGdxPlayMode() {

    return gdxPlayMode;
  }
}
