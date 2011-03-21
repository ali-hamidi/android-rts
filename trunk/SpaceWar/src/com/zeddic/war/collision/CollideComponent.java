package com.zeddic.war.collision;

import com.zeddic.common.Component;
import com.zeddic.common.Entity;

public class CollideComponent extends Component {

  private final CollisionSystem collisionSystem;
  public final CollideBehavior behavior;
  public final Entity entity;
  public boolean enabled;
  public CollisionCell currentCell;

  private float lastX = 0;
  private float lastY = 0;
  
  public CollideComponent(
      Entity entity,
      CollideBehavior behavior) {
    this(CollisionSystem.get(), entity, behavior);
  }
  
  public CollideComponent(
      CollisionSystem collisionSystem,
      Entity entity,
      CollideBehavior behavior) {
    this.collisionSystem = collisionSystem;
    this.behavior = behavior;
    this.entity = entity;
  }
  
  public CollideBehavior getBehavior() {
    return behavior;
  }
  
  private void setEnabled(boolean enabled) {
    if (this.enabled && !enabled) {
      unregisterObject();
    } else if (!this.enabled && enabled) {
      registerObject();
    }
  }
  
  //public boolean isEnabled() {
  //  return enabled;
  //}
  
  
  /**
   * Registers the object with the collision management system so other
   * objects may collide with it.
   */  
  public void registerObject() {
    if (enabled || behavior == CollideBehavior.HIT_ONLY)
      return;
    
    collisionSystem.register(this);
    
    //collisionSystem.register(entity)
    
    enabled = true;
    

    
    /*if (type == CollisionManager.TYPE_HIT_RECEIVE || 
        type == CollisionManager.TYPE_RECEIVE_ONLY) {
      CollisionManager.get().addObject(this);
    } else if (type == CollisionManager.TYPE_STATIONARY) {
      CollisionManager.get().addStationaryObject(this);
    }
    
    inCollisionSystem = true;*/
  }

  public void unregisterObject() {
    if (!enabled || behavior == CollideBehavior.HIT_ONLY) {
      return;
    }
    
    collisionSystem.unregister(this);
    
    enabled = false;
  }

  @Override
  public void update(long time) {
    
    setEnabled(entity.enabled);
    
    if (entity.enabled && behavior != CollideBehavior.HIT_ONLY) {
      if (lastX != entity.x || lastY != entity.y) {
        collisionSystem.update(this);
      }
    }
    
    //if (entity.active) {
    //  setEnabled(true);
    //}
    
    //try {
      
      
      // CollisionManager.get().updatePosition(this);
      
      
      //if (type == CollisionManager.TYPE_HIT_ONLY || 
      //    type == CollisionManager.TYPE_HIT_RECEIVE) {
      //  CollisionManager.get().checkForCollision(this, time); 
      //} else if (type == CollisionManager.TYPE_RECEIVE_ONLY) {
        
      //}
    //} catch (Exception e) {
    //  Log.e(CollisionComponent.class.getName(),"Error detecting collision:", e);
    //}
  }
}

