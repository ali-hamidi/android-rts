package com.zeddic.common.opengl;

public class SimplePlane extends Mesh {
  
  protected final float width;
  protected final float height;
  
  public SimplePlane(float width, float height) {
    this.width = width;
    this.height = height;
    
    setDimensions(width, height);
    setTextureScale(1);
  }

  /** Setup vertices and indices. */
  private void setDimensions(float width, float height) {
    float halfWidth = width / 2;
    float halfHeight = height / 2;
    
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

  /**
   * Sets the scale of the texture. > 1 for larger, < 1 for smaller.
   */
  public void setTextureScale(float scale) {
    setTextureScale(scale, scale);
  }
  
  public void setTextureScale(float scaleX, float scaleY) {
    float textureCoordinates[] = {
        0.0f, 0.0f,
        scaleX, 0.0f,
        0.0f, scaleY,
        scaleX, scaleY,
    };

    setTextureCoordinates(textureCoordinates);
  }

  /**
   * Sets the plane's y translation so it's top side is at the
   * given world coordinate.
   */
  public void setTop(float top) {
    this.y = top + height / 2;
  }
  
  /**
   * Set the plane's x translation so it's left side is at the
   * given world coordinate.
   */
  public void setLeft(float left) {
    this.x = left + width / 2;
  }
  
  /**
   * Sets the plane's y translation so it's bottom side is at
   * the given world coordinate.
   */
  public void setBottom(float bottom) {
    this.y = bottom - height / 2;
  }
  
  /**
   * Sets the plane's x translation so it's right side is at the
   * given world coordinate.
   * @param right
   */
  public void setRight(float right) {
    this.x = right - width / 2;
  }
}
