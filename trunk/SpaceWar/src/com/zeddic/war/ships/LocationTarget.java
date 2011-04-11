package com.zeddic.war.ships;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.Entity;
import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.SimpleGeometry;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.common.transistions.Range;
import com.zeddic.common.transistions.RangeConverter;
import com.zeddic.common.transistions.Transition;
import com.zeddic.common.transistions.Transitions.TransitionType;
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.R;

public class LocationTarget implements Target {

  private static final Color color = new Color(255, 0, 0, 255);
  private static final Sprite dotSprite = new Sprite(40, 40, R.drawable.waypointdot);
  private static final Sprite ringSprite = new Sprite(40, 40, R.drawable.waypoint);
  private float x;
  private float y;
  private final Transition sizeTransition = new Transition(.5f, 1, 2000, TransitionType.EASE_IN_OUT);
  private static final RangeConverter ALPHA = new RangeConverter(new Range(.5f, 1f), new Range(1f, 0f));
  private final List<Entity> followers = new ArrayList<Entity>();
  private Runnable reachedHandler;

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
  public void draw(GL10 gl) {

    ringSprite.scale = sizeTransition.get();
    ringSprite.setColor(color);
    ringSprite.setAlpha(ALPHA.convert(ringSprite.scale));
    ringSprite.x = x;
    ringSprite.y = y;
    ringSprite.draw(gl);
    
    dotSprite.x = x;
    dotSprite.y = y;
    dotSprite.setColor(color);
    dotSprite.draw(gl);

    int length = followers.size();
    for (int i = 0; i < length ; i++) {
      Entity follower = followers.get(i);
      
      float dX = x - follower.x;
      float dY = y - follower.y;
      
      if (dX * dX + dY * dY > 32 * 32) {
        Vector2d temp = new Vector2d(dX, dY);
        temp.normalize();
        temp.x *= 32;
        temp.y *= 32;
        SimpleGeometry.drawLine(gl, follower.x + temp.x, follower.y + temp.y, x, y, color);
      }
    } 
  }

  @Override
  public void set(float x, float y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void addFollower(Entity follower) {
    followers.add(follower);
  }

  @Override
  public void removeFollower(Entity follower) {
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
