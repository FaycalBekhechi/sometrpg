package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;

public class Stage extends Component {
  private final com.badlogic.gdx.scenes.scene2d.Stage gdxStage;

  public Stage(com.badlogic.gdx.scenes.scene2d.Stage gdxStage) {
    this.gdxStage = gdxStage;
  }

  public com.badlogic.gdx.scenes.scene2d.Stage getGdxStage() {
    return gdxStage;
  }
}
