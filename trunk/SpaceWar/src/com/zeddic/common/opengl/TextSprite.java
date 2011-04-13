package com.zeddic.common.opengl;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

/**
 * A simple sprite that can be used to write text on.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class TextSprite extends SimplePlane {

  private final CanvasTexture canvasTexture;
  private Paint paint;
  private float textSize = 32;
  private Align alignment = Align.LEFT;
  
  public TextSprite(int width, int height) {
    super(width, height);
    
    paint = new Paint();
    paint.setTextSize(textSize);
    paint.setAntiAlias(true);
    paint.setColor(Color.WHITE);
    
    canvasTexture = new CanvasTexture(width, height);
    setTexture(canvasTexture.getTexture());
  }
  
  /** Sets the font size. */
  public void setTextSize(float size) {
    this.textSize = size;
    paint.setTextSize(textSize);
  }
  
  /** Sets the color of the text displayed. */
  public void setTextColor(com.zeddic.common.opengl.Color color) {
    paint.setARGB(
        (int) color.a * 255,
        (int) color.r * 255,
        (int) color.g * 255,
        (int) color.b * 255);
  }
  
  /** Sets how the text should be aligned. */
  public void setTextAlignment(Align align) {
    this.alignment = align;
    paint.setTextAlign(align);
  }
  
  /** Sets the text. */
  public void setText(String text) {
    canvasTexture.clear();
    
    float y = this.height / 2;
    float x = 0;
    switch (alignment) {
      case LEFT: x = 0; break;
      case CENTER: x = width / 2; break;
      case RIGHT: x = width; break;
    }
    
    canvasTexture.getCanvas().drawText(text, x, y, paint);
    
    // After setting the text, the texture needs to be regenerated for the changes
    // to show up in open gl.
    canvasTexture.refresh();
  }
}
