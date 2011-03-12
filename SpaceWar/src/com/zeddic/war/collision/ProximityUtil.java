package com.zeddic.war.collision;

import com.zeddic.game.common.PhysicalObject;
import com.zeddic.game.common.util.SimpleList;

public class ProximityUtil {

  // Reuse data structures across calls. This prevents the class from being
  // thread safe, but saves having to create new memory allocations at run
  // time which would kill game performance, especially since these methods
  // may be call many many times.
  //private static ProximityResult result = new ProximityResult();
  //private static GridSpot[] spots = new GridSpot[1000];
  //private static PhysicalObject[] nearbyObjects = new PhysicalObject[100];
  
  
  
  private static SimpleList<PhysicalObject> objects = SimpleList.create(PhysicalObject.class);
  private static SimpleList<CollisionCell> cells = SimpleList.create(CollisionCell.class);
  
  public static SimpleList<PhysicalObject> getNearbyObjects(
      Class<?> targetClass,
      float x,
      float y,
      float radius) {
    
    CollisionGrid grid = CollisionSystem.get().getGrid();
    grid.getCellsWithinRadius(x, y, radius, cells);

    PhysicalObject object;
    CollisionCell cell;
    float maxDistance = radius * radius;
    
    objects.clear();
    
    for ( int i = 0 ; i < cells.size ; i++) {
      
      cell = cells.items[i];
      int numObjects = cell.items.size;
      
      for (int j = 0; j < numObjects; j++) {
        
        object = cell.items.items[j];
        
        if (!object.active) {
          continue;
        }
        
        if (!targetClass.isInstance(object)) {
          continue;
        }

        float dX = x - object.x;
        float dY = y - object.y;
        float distanceSquared = dX * dX + dY * dY;
        
        if (distanceSquared > maxDistance) {
          continue;
        }
        
        objects.add(object);
      }
    }
    
    return objects;
  }
  
  public static PhysicalObject getClosest(Class<?> targetClass, float x, float y, float distance) {
    return getClosest(targetClass, x, y, distance, null);
  }
  
  public static PhysicalObject getClosest(Class<?> targetClass, float x, float y, float distance, PhysicalObject exclude) {
    
    float minDistanceSquared = Float.MAX_VALUE;
    PhysicalObject target = null;
    PhysicalObject object;
    SimpleList<PhysicalObject> result = getNearbyObjects(targetClass, x, y, distance);
    
    for (int i = 0; i < result.size; i++) {
      object = result.items[i];
      
      if (object == exclude) {
        continue;
      }

      float dX = x - object.x;
      float dY = y - object.y;
      float distanceSquared = dX * dX + dY * dY;
      if (distanceSquared < minDistanceSquared) {
        target = object;
        minDistanceSquared = distanceSquared;
      }
    }
    
    return target;
  }
  
  public static class ProximityResult {
    public PhysicalObject[] objects;
    public int hits;
  }
}
