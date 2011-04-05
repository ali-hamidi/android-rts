package com.zeddic.common.opengl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * An object that creates a OpenGL {@link Texture} that can be edited by 
 * using Android's {@link Canvas} class.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class CanvasTexture {

  private final Bitmap bitmap;
  private final Canvas canvas;
  private final Texture texture; 
  public final int width;
  public final int height;
  
  public CanvasTexture(int width, int height) {
    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
    canvas = new Canvas(bitmap);
    texture = new Texture(bitmap);
    this.width = width;
    this.height = height;
  }

  /**
   * Causes the texture to refresh to match the latest canvas. Changes
   * to the canvas will not be visible until refresh is called.
   */
  public void refresh() {
    texture.reload();
  }

  /**
   * Clears the current canvas and makes it transparent.
   */
  public void clear() {
    bitmap.eraseColor(Color.TRANSPARENT);
  }
  
  /**
   * Gets a canvas to draw on. {@link #refresh} must be called before
   * draw commands will become visible on the texture.
   */
  public Canvas getCanvas() {
    return canvas;
  }
  
  public Texture getTexture() {
    return texture;
  }
}
