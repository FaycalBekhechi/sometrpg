package com.ziodyne.sometrpg.view.assets.models;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ziodyne.sometrpg.view.entities.UnitEntityAnimation;

public class CharacterSprites {

  private List<CharacterSpriteBook> sprites;
  private Map<String, Set<UnitEntityAnimation>> animationsByCharacterId;

  public Set<UnitEntityAnimation> getAnimations(String characterId) {
    return animationsByCharacterId.get(characterId);
  }

  public void setAnimationsByCharacterId(Map<String, Set<UnitEntityAnimation>> animationsByCharacterId) {

    this.animationsByCharacterId = animationsByCharacterId;
  }

  public List<CharacterSpriteBook> getSprites() {

    return sprites;
  }

  public void setSprites(List<CharacterSpriteBook> sprites) {

    this.sprites = sprites;
  }
}
