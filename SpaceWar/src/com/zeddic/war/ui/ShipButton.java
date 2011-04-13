package com.zeddic.war.ui;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Paint.Align;

import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.common.opengl.TextSprite;
import com.zeddic.war.R;

public class ShipButton extends AbstractUiObject {
  
  private static final int WIDTH = 100;
  private static final int HEIGHT = 100;
  private static final int CONTENT_SIZE = 70;
  
  private static final Sprite base = new Sprite(WIDTH, HEIGHT, R.drawable.shipbutton);
  private final TextSprite text = new TextSprite(64, 64);
  private final Color color;
  private final int cost;
  
  public final Sprite sprite;
  
  public ShipButton(int texture, Color color, int cost) {
    
    this.width = WIDTH;
    this.height = HEIGHT;
    this.sprite = new Sprite(CONTENT_SIZE , CONTENT_SIZE, texture);
    this.color = color;
    this.cost = cost;
    setup();
  }
  
  private void setup() {
    text.setTextSize(30);
    text.setTextAlignment(Align.CENTER);
    text.setText("$" + cost);
  }
  
  @Override
  public void update(long time) {
    
  }
  
  @Override
  public void draw(GL10 gl) {

    base.x = x;
    base.y = y;
    base.setColor(color);
    base.draw(gl);
    
    sprite.x = x;
    sprite.y = y;
    sprite.setColor(color);
    sprite.draw(gl);
    
    text.x = x;
    text.y = y + 78;
    text.draw(gl);
  }
}
