package com.ziodyne.sometrpg.view.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ziodyne.sometrpg.logic.loader.models.AnimationSpec;
import com.ziodyne.sometrpg.view.assets.models.CharacterSprites;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @Type(value = TextureAsset.class, name = "texture"),
    @Type(value = TiledMapAsset.class, name = "map"),
    @Type(value = GameSpecAsset.class, name = "gameSpec"),
    @Type(value = SpriteSheetAsset.class, name = "spriteSheet"),
    @Type(value = CharacterSpritesAsset.class, name = "characterSprites"),
    @Type(value = ArmiesAsset.class, name = "armies"),
    @Type(value = TextureAtlasAsset.class, name = "atlas"),
    @Type(value = ChapterAsset.class, name = "chapter"),
    @Type(value = SoundAsset.class, name = "sound"),
    @Type(value = MusicAsset.class, name = "music")
})
public abstract class Asset<T> {
  private String type;
  private String path;
  private Class<T> clazz;

  protected Asset(String type, Class<T> clazz) {
    this.type = type;
    this.clazz = clazz;
  }

  public Class<T> getClazz() {
    return clazz;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(type)
        .append(path)
        .append(clazz)
        .build();
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Asset)) {
      return false;
    }

    Asset otherAsset = (Asset)other;
    return new EqualsBuilder()
        .append(type, otherAsset.type)
        .append(path, otherAsset.path)
        .append(clazz, otherAsset.clazz)
        .isEquals();
  }
}
