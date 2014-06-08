package com.ziodyne.sometrpg.view.graphics;

/**
 * Enumeration of all sprite layers on the battlefield.
 *
 * These enums are declared in back to front order, so rearranging their declaration
 * rearranges the order in which they are drawn.
 */
public enum SpriteLayer {
  VOID,
  BACKGROUND,
  FOREGROUND,
  MENU
}
