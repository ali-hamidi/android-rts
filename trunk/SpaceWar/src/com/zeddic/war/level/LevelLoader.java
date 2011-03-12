package com.zeddic.war.level;

import java.io.IOException;


/**
 * Loads a level from a specified file.
 * 
 * @author baileys (Scott Bailey)
 */
public interface LevelLoader {
  
  /** Returns a level loaded from the specified file. */
  public Level load(String file) throws IOException;

}
