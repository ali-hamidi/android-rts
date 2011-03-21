package com.zeddic.war.collision;

import android.graphics.Canvas;

import com.zeddic.common.Entity;
import com.zeddic.common.util.SimpleList;
import com.zeddic.war.level.Level;

public class CollisionGrid {

  private final int cols;
  private final int rows;
  private final float width;
  private final float height;
	private CollisionCell[][] grid;
	
	public CollisionGrid(Level level) {
	  
	  this.rows = level.getTileRows();
	  this.cols = level.getTileCols();
	  this.width = level.getWidth();
	  this.height = level.getHeight();
	  
	  grid = new CollisionCell[rows][cols];
	  
	  for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {   
        CollisionCell cell = new CollisionCell(this, row, col);
        //cell.setBounds(level.getTile(row, col).getType().getBounds());
        grid[row][col] = cell;
      }
    }
	  
		calculateEdges();
	}
	
	private void calculateEdges() {
	  for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {   
        grid[row][col].calculateEdges();
      }
    }
	}
	
	public void collide(CollideComponent component) {	 
	  
	  // First: collide with any entities.
	  

	  // Second: collide with any tiles.
	  Entity entity = component.entity;
	  
    int minCol = convertMapToGridValue(entity.left());
    int maxCol = convertMapToGridValue(entity.right());
    int minRow = convertMapToGridValue(entity.top());
    int maxRow = convertMapToGridValue(entity.bottom());
 
    for (int row = minRow; row <= maxRow; row++) {
      for(int col = minCol; col <= maxCol; col++) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
          grid[row][col].collide(entity);
        }
      }
    }
	}
	
	/**
	 * Fetches a set of cells that are 'relevant' for collision checks for a given
	 * entity. Results are stored in the array <code>result</code> with the number
	 * of results in the array returned as an int.
	 */
  public int getReleventCells(CollideComponent component, SimpleList<CollisionCell> result) {
    Entity entity = component.entity;
    
    int minCol = convertMapToGridValue(entity.left());
    int maxCol = convertMapToGridValue(entity.right());
    int minRow = convertMapToGridValue(entity.top());
    int maxRow = convertMapToGridValue(entity.bottom());
    
    return getCells(minRow, minCol, maxRow, maxCol, result);
  }
  
 
  /**
   * Fetches all cells that are within a given radius of the a set world position.
   * Results are stored in the array <code>result</code> with the number
   * of results in the array returned as an int.
   */
  public int getCellsWithinRadius(
      float worldX,
      float worldY,
      float radius,
      SimpleList<CollisionCell> result) {
    
    int centerCol = convertMapToGridValue(worldX);
    int centerRow = convertMapToGridValue(worldY);
    int range = convertMapToGridValue(radius);
    
    int minRow = centerRow - range;
    int maxRow = centerRow + range;
    int minCol = centerCol - range;
    int maxCol = centerCol + range;
    
    return getCells(minRow, minCol, maxRow, maxCol, result);
  }
  
  /**
   * Places all cells within the given range into the supplied result array. The
   * number of results is returned as an int.
   */
  public int getCells(int minRow, int minCol, int maxRow, int maxCol, SimpleList<CollisionCell> result) {
    
    result.clear();

    CollisionCell cell;
    int hits = 0;
    for (int col = minCol; col <= maxCol; col++) {
      for (int row = minRow; row <= maxRow; row++) {
        cell = get(row, col);
        if (cell != null) {
          result.add(cell);
          hits++;
        }
      }
    }
    
    return hits;
  }
	
	public void draw(Canvas c) {
	  for (int row = 0; row < rows; row++) {
	    for(int col = 0; col < cols; col++) {
	      grid[row][col].draw(c);
	    }
	  }
	}
	
	private int convertMapToGridValue(float rawValue) {
    return (int) Math.floor(rawValue / CollisionSystem.SIZE);
  }
	
	public CollisionCell left(CollisionCell cell) {
    return get(cell.row, cell.col - 1);
  }
  
  public CollisionCell right(CollisionCell cell) {
    return get(cell.row, cell.col + 1);
  }
  
  public CollisionCell above(CollisionCell cell) {
    return get(cell.row - 1, cell.col);
  }
  
  public CollisionCell below(CollisionCell cell) {
    return get(cell.row + 1, cell.col);
  }
  
  public CollisionCell get(int row, int col) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return null;
    }
    
    return grid[row][col];
  }
  
  /**
   * Adds a movable object to the grid. Only its center point will
   * be used to place it in the grid.
   */
  public void add(CollideComponent component) {
    Entity entity = component.entity;
    CollisionCell cell = getCellForEntity(entity);
    
    if (cell != null) {
      cell.add(entity);
    }

    component.currentCell = cell;;
  }
  
  /**
   * Removes an object from the grid entirely.
   */
  public void remove(CollideComponent component) {
    Entity entity = component.entity;
    CollisionCell cell = getCellForEntity(entity);
    cell.remove(entity);
    
    component.currentCell = null;
  }
  
  /**
   * Updates an objects position in the grid.
   */
  public void update(CollideComponent component) {
    Entity entity = component.entity;
    CollisionCell cell = getCellForEntity(entity);

    if (cell != component.currentCell) {
      component.currentCell.remove(entity);
      
      if (cell != null) {
        cell.add(entity);
        component.currentCell = cell;
      }
    }
  }
  
  CollisionCell getCellForEntity(Entity entity) {
    int col = convertMapToGridValue(entity.x);
    int row = convertMapToGridValue(entity.y);

    return get(row, col);
  }
  
  /**
   * Updates an objects position in the grid.
   */
  /*private void updatePosition(CollisionComponent component, GridSpot newSpot) {
    if (component.type == CollisionManager.TYPE_HIT_RECEIVE ||
        component.type == CollisionManager.TYPE_RECEIVE_ONLY) {
      if (newSpot != component.gridSpot) {
        if (component.gridSpot != null) {
          component.gridSpot.remove(component.object);
        }
        if (newSpot != null) {
          newSpot.add(component.object);
        }
        component.gridSpot = newSpot;
      } 
    }
  } */
  
  /**
   * Returns an array of Grid positions of all the spots around the given
   * object. Note that some grid spots may be null (if it is out of bounds).
   * Returned spots will be placed in the provided nearby[] array.
   */
  /*public void getNearbyGridSpots(CollisionComponent component, GridSpot[] nearby) {
    int x = (int) (component.object.x / CollisionSystem.SIZE);
    int y = (int) (component.object.y / CollisionSystem.SIZE);
    
    if (x < 0 || x >= width || y < 0 || y >= height ) {
      nearby[1] = null;
      nearby[2] = null;
      nearby[3] = null;
      nearby[4] = null;
      return;
    }
 
    GridSpot here = grid[x][y];
    if (component.type == CollisionManager.TYPE_HIT_RECEIVE ||
        component.type == CollisionManager.TYPE_RECEIVE_ONLY) {
      updatePosition(component, here);
    }
    
    nearby[0] = here;

    nearby[1] = x - 1 >= 0 ? grid[x - 1][y] : null;
    nearby[2] = y - 1>= 0 ? grid[x][y - 1] : null;
    nearby[3] = x + 1 < width ? grid[x + 1][y] : null;
    nearby[4] = y + 1< height ? grid[x][y + 1] : null;
  } */
  
  /**
   * Returns a list of nearby grid spots that are within a given distance of
   * the starting world position. Returns the number of non-empty gridspots
   * that were found. Populated gridspots are put into nearby.
   */
  /*public int getNearbyGridSpots(
      float worldX,
      float worldY,
      float distance,
      GridSpot[] nearby) {
    
    int centerX = (int) (worldX / gridSize);
    int centerY = (int) (worldY / gridSize);

    int range = (int) (distance / gridSize);
    int minX = centerX - range;
    int maxX = centerX + range;
    int minY = centerY - range;
    int maxY = centerY + range;
    
    GridSpot spot;
    int hits = 0;
    for (int x = minX; x <= maxX; x++) {
      for (int y = minY; y <= maxY; y++) {
        spot = get(x, y);
        if (spot != null) {
          nearby[hits] = spot;
          hits++;
        }
      }
    }
    
    return hits;
  } */
  
  
  
  /**
   * Returns the grid represented by the world position.
   */
  /*public GridSpot getObjectsAtWorldPosition(float mapX, float mapY) {
    int x = (int) Math.floor(mapX / gridSize);
    int y = (int) Math.floor(mapY / gridSize);
    return get(x, y);
  }*/


  
}
