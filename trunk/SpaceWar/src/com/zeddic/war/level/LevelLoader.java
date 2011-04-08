package com.zeddic.war.level;

/**
 * Loads a level from a specified file.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public interface LevelLoader {
  
  /** Returns a level loaded from the specified file. */
  public Level load(String file);

}
