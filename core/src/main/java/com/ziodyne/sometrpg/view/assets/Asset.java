package com.ziodyne.sometrpg.view.assets;

public abstract class Asset<T> {
  private String type;
  private String filename;

  protected Asset(String type, Class<T> clazz) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }
}
