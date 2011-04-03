package com.zeddic.war;

import com.zeddic.common.opengl.Mesh;
import com.zeddic.common.opengl.Texture;

public class GridPlane extends Mesh {

  public GridPlane() {
    this(1, 1, 1, 1);
  }
   
  public GridPlane(float width, float height) {
    this(width, height, 1, 1);
  }

  public GridPlane(
      float width,
      float height,
      int widthSegments,
      int heightSegments) {

    short[] indices = new short[] { 0, 1, 2, 1, 3, 2 };
    float count = 50;
    float size = count * 32 / 2;
    float[] vertices = new float[] { -size, -size, 0.0f,
                                      size, -size, 0.0f,
                                     -size,  size, 0.0f,
                                      size, size, 0.0f };
    
    float textureScale = count / 2;
    
    float textureCoordinates[] = {
        0.0f, textureScale,
        textureScale, textureScale,
        0.0f, 0.0f,
        textureScale, 0.0f,
    };

   
    setIndices(indices);
    setVertices(vertices);
    setTextureCoordinates(textureCoordinates);
    setTexture(new Texture(R.drawable.grid2));
  }
}
