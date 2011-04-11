package com.zeddic.war.level;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.AbstractGameObject;
import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.Path;
import com.zeddic.common.util.SimpleList;
import com.zeddic.common.util.Vector2d;

/**
 * A path that invaders should follow when attacking.
 * 
 * @author Scott Bailey
 */
public class InvadePath extends AbstractGameObject {

  private static final Color color = new Color(255, 255, 255, 255);
  public SimpleList<Vector2d> points = SimpleList.create(Vector2d.class);
  private Path path;
  
  public InvadePath(List<Vector2d> points) {
    
    path = new Path(points);
    path.setColor(color);
    path.setLineWidth(2);
    
    for (Vector2d point : points) {
      this.points.add(point);
    }
  }

  public static final class Builder {
    List<Vector2d> points = new ArrayList<Vector2d>();

    public Builder add(float x, float y) {
      points.add(new Vector2d(x, y));
      return this;
    }

    public InvadePath build() {
      return new InvadePath(points);
    }
  }

  @Override
  public void draw(GL10 gl) {
    path.draw(gl);
  }

  @Override
  public void update(long time) {
    // Nothing to update.
  }
}
