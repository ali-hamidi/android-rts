package com.zeddic.common.opengl;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * A simple sprite that can be used to write text on.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class TextSprite extends SimplePlane {

  private final CanvasTexture canvasTexture;
  private Paint paint;
  private float textSize = 32;
  
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
  public void setTextColor(int a, int r, int g, int b) {
    paint.setARGB(a, r, g, b);
  }
  
  /** Sets the text. */
  public void setText(String text) {
    canvasTexture.clear();
    canvasTexture.getCanvas().drawText(text, 0, canvasTexture.height / 2, paint);
    
    // After setting the text, the texture needs to be regenerated for the changes
    // to show up in open gl.
    canvasTexture.refresh();
  }
}
