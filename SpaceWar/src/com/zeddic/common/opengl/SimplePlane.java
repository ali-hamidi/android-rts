package com.zeddic.common.opengl;

public class SimplePlane extends Mesh {
  
  public SimplePlane(int width, int height) {
    setDimensions(width, height);
    setTextureCoordinates();
  }

  private void setDimensions(int width, int height) {
    int halfWidth = width / 2;
    int halfHeight = height / 2;
    
    float vertices[] = {
        -halfWidth, -halfHeight, 0, // 0
         halfWidth, -halfHeight, 0, // 1
        -halfWidth,  halfHeight, 0, // 2
         halfWidth,  halfHeight, 0, // 3
    };
    
    short indices[] = {
        0, 1, 2,
        1, 3, 2,
    };

    setIndices(indices);
    setVertices(vertices);
  }
  
  private void setTextureCoordinates() {
    float textureCoordinates[] = {
        0.0f, 1.0f,
        1.0f, 1.0f,
        0.0f, 0.0f,
        1.0f, 0.0f,
    };

    setTextureCoordinates(textureCoordinates);
  }
}
