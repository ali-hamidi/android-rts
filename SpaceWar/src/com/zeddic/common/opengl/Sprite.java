package com.zeddic.common.opengl;

import android.graphics.Bitmap;

/**
 * A 2D plane that displays a single texture on one face.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class Sprite extends SimplePlane {

  public Sprite(int width, int height, int resource) {
    super(width, height);
    setTexture(new Texture(resource));
  }
  
  public Sprite(int width, int height, Bitmap bitmap) {
    super(width, height);
    setTexture(new Texture(bitmap));
  }
}
