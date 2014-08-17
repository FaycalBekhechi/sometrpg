package com.ziodyne.sometrpg.view.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Text extends Component {
  private final BitmapFont font;
  private final String characters;

  public Text(BitmapFont font, String characters) {

    this.font = font;
    this.characters = characters;
  }

  public BitmapFont getFont() {

    return font;
  }

  public String getCharacters() {

    return characters;
  }
}
