package com.zeddic.war.collision;

public enum CollideBehavior {

  /**
   * The object should have no collision detection.
   */
  NONE,

  /**
   * An object that can hit other objects but can never be hit. 
   * This is useful when many of a given type of object may exist
   * in the world, but it doesn't matter if anything runs into them. 
   * For example, bullets, where you want to avoid wasted checks to see
   * if bullets have hit other bullets.
   */
  HIT_ONLY,
  
  /**
   * An object that can hit other objects and be hit. For example:
   * the players ship.
   */
  HIT_RECEIVE,

  /**
   * A large object that is stationary and can be run into by other
   * objects in the world.
   */
  STATIONARY
}
