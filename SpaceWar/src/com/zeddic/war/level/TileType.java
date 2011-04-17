package com.zeddic.war.level;

import com.zeddic.war.collision.TileBounds;

public enum TileType {
  
  EMPTY(TileBounds.EMPTY),
  SOLID_ROCK(TileBounds.SOLID),
  EDGE(TileBounds.SOLID);
    
  public TileBounds bounds;
  
  TileType(TileBounds bounds) {
    this.bounds = bounds;
  }
}
