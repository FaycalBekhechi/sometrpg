package com.ziodyne.sometrpg.models;

public class Map {
  private final Tile[][] tiles;
  private final int width;
  private final int height;
  
  public Map(int width, int height) {
    super();
    this.width = width;
    this.height = height;
    
    tiles = new Tile[width][height];
  }

  public Tile getTile(int x, int y) {
    if (x > width || y > height) {
      throw new IllegalArgumentException("Tile out of bounds.");
    }
    return tiles[x][y];
  }
}
