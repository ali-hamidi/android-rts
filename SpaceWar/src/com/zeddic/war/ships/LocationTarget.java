package com.zeddic.war.ships;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.zeddic.game.common.PhysicalObject;
import com.zeddic.game.common.transistions.Range;
import com.zeddic.game.common.transistions.RangeConverter;
import com.zeddic.game.common.transistions.Transition;
import com.zeddic.game.common.transistions.Transitions;
import com.zeddic.game.common.util.Vector2d;

public class LocationTarget implements Target {

  private static final Paint PAINT;
  static {
    PAINT = new Paint();
    PAINT.setColor(Color.RED);
    PAINT.setStyle(Paint.Style.STROKE);
    PAINT.setStrokeWidth(1);
    PAINT.setAntiAlias(true);
  }
  
  private float x;
  private float y;
  private final Transition sizeTransition = new Transition(5, 25, 1000, Transitions.EASE_IN_OUT);
  private static final RangeConverter ALPHA = new RangeConverter(new Range(15, 25), new Range(255, 50));
  private final List<PhysicalObject> followers = new ArrayList<PhysicalObject>();
  
  // TODO(baileys): Change this to a composite.
  private Runnable reachedHandler;
  
  //SimpleList<PhysicalObjects> 
  
  public LocationTarget(float x, float y) {
    this.x = x;
    this.y = y;
    sizeTransition.setAutoReset(true);
  }

  @Override
  public float getX() {
    return x;
  }

  @Override
  public float getY() {
    return y;
  }

  @Override
  public void update(long time) {
    sizeTransition.update(time);
  }

  @Override
  public void draw(Canvas c) {
    float size = sizeTransition.get();
    
    PAINT.setAlpha((int) ALPHA.convert(size));
    PAINT.setStyle(Style.STROKE);
    c.drawCircle(x, y, size, PAINT);
    

    PAINT.setAlpha(255);
    
    int length = followers.size();


    for (int i = 0; i < length ; i++) {
      PhysicalObject follower = followers.get(i);
      
      float dX = x - follower.x;
      float dY = y - follower.y;
      
      if (dX * dX + dY * dY > 25 * 25) {
        Vector2d temp = new Vector2d(dX, dY);
        temp.normalize();
        temp.x *= 25;
        temp.y *= 25;
        c.drawLine(follower.x + temp.x, follower.y + temp.y, x, y, PAINT);
      }
    } 
    
    PAINT.setStyle(Style.FILL);
    c.drawCircle(x, y, 5, PAINT);
  }

  @Override
  public void set(float x, float y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void addFollower(PhysicalObject follower) {
    followers.add(follower);
  }

  @Override
  public void removeFollower(PhysicalObject follower) {
    followers.remove(follower);
    
    if (followers.size() == 0) {
      reachedHandler.run();
    }
  }

  @Override
  public void addReachedHandler(Runnable handler) {
    this.reachedHandler = handler;
  }
}
