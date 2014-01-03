package com.ziodyne.sometrpg.view.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @Type(value = TextureAsset.class, name = "texture"),
        @Type(value = TiledMapAsset.class, name = "tiledMap")
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

  public void setClazz(Class<T> clazz) {
    this.clazz = clazz;
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
}
