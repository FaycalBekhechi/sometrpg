package com.ziodyne.sometrpg.view.graphics;

/**
 * Enumeration of all sprite layers on the battlefield.
 *
 * These enums are declared in back to front order, so rearranging their declaration
 * rearranges the order in which they are drawn.
 */
public enum SpriteLayer {
  BACKGROUND(0),
  FOREGROUND(1),
  MENU(2000);

  private final int zIndex;

  SpriteLayer(int zIndex) {

    this.zIndex = zIndex;
  }

  public int getZIndex() {

    return zIndex;
  }
}
