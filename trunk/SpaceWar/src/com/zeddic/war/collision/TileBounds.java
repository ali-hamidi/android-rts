package com.zeddic.war.collision;

import com.zeddic.common.util.Polygon;
import com.zeddic.common.util.Polygon.PolygonBuilder;

public enum TileBounds {
  
  EMPTY(
      null,
      EdgeType.EMPTY,
      EdgeType.EMPTY,
      EdgeType.EMPTY,
      EdgeType.EMPTY),
  
  SOLID(
      new PolygonBuilder()
        .add(0, 0)
        .add(1, 0)
        .add(1, 1)
        .add(0, 1)
        .build(),
      EdgeType.SOLID,
      EdgeType.SOLID,
      EdgeType.SOLID,
      EdgeType.SOLID),
 
  LEFT_TRIANGLE(
      new PolygonBuilder()
        .add(0, 1)
        .add(1, 0)
        .add(1, 1)
        .build(),
      EdgeType.INTERSECTION,
      EdgeType.SOLID,
      EdgeType.SOLID,
      EdgeType.INTERSECTION);
  
  public Polygon shape;
  public EdgeType topEdge = EdgeType.EMPTY;
  public EdgeType bottomEdge = EdgeType.EMPTY;
  public EdgeType leftEdge = EdgeType.EMPTY;
  public EdgeType rightEdge = EdgeType.EMPTY;
  
  TileBounds(
      Polygon shape,
      EdgeType topEdge,
      EdgeType rightEdge,
      EdgeType bottomEdge,
      EdgeType leftEdge) {
    
    this.shape = shape;
    this.topEdge = topEdge;
    this.rightEdge = rightEdge;
    this.bottomEdge = bottomEdge;
    this.leftEdge = leftEdge;
  }
  
  public boolean isEmpty() {
    return shape == null;
  }

  public enum EdgeType { EMPTY, SOLID, INTERSECTION };

}
