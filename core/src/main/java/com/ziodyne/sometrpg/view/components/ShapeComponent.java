package com.ziodyne.sometrpg.view.components;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * A component that encapsulates a method enacted upon a ShapeRenderer.
 */
public class ShapeComponent extends Component {

  private final Consumer<ShapeRenderer> renderFunc;

  public ShapeComponent(Consumer<ShapeRenderer> renderFunc) {

    this.renderFunc = renderFunc;
  }

  public void execute(ShapeRenderer shapeRenderer) {
    renderFunc.accept(shapeRenderer);
  }
}
