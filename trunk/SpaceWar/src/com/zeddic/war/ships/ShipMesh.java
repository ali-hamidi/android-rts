package com.zeddic.war.ships;

import com.zeddic.common.opengl.Mesh;

public class ShipMesh extends Mesh {
  
  public ShipMesh() {

    float vertices[] = {
      -5f, 4f, -2f,   // 0
      -3f, 0f, 2f,    // 1
       4f, 0f, 0f,    // 2
      -5f, -4f, -2f,  // 3
    };
    
    short indices[] = {
        0, 1, 2,
        1, 3, 2,
        0, 3, 1,
        0, 3, 2,
    };

    setIndices(indices);
    setVertices(vertices);
  }
}

