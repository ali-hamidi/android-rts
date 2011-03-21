package com.zeddic.war.level;

import com.zeddic.war.level.Level.LevelBuilder;

/**
 * A {@link LevelLoader} that returns a simple in-memory level
 * for testing purposes.
 * 
 * @author baileys (Scott Bailey)
 *
 */
public class MockLevelLoader implements LevelLoader {

  private static final int[][] SPOTS = {
      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0},
      {0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0},
      {0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
  };
  private static final int COLS = SPOTS[0].length;
  private static final int ROWS = SPOTS.length;
  
  @Override
  public Level load(String file) {
    
    LevelBuilder builder = new LevelBuilder();
    builder.withGridSize(ROWS, COLS);
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLS; col++) {   
        TileType type = TileType.EMPTY;
        
        // A border location.
        if (row == 0 || row == ROWS - 1 || col == 0 || col == COLS - 1) {
          type = TileType.SOLID_ROCK;
        }
        
        // The mock data specified it to be a rock.
        if (getSpot(row, col) == 1) {
          type = TileType.SOLID_ROCK;
        }
        
        builder.addTile(row, col, type);
      }
    }
    
    return builder.build();    
  }
  
  private static int getSpot(int row, int col) {
    if (row < SPOTS.length) {
      int[] sub = SPOTS[row];
      if (col < sub.length) {
        return sub[col];
      }
    }
    return 0;
  }
}
