package com.zeddic.war.level;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.zeddic.common.util.ResourceLoader;
import com.zeddic.war.level.Level.LevelBuilder;


public class FileLevelLoader implements LevelLoader {

  private static final String PROPERTY_SEPERATOR = ":";
  private static final String PROPERTY_LEVEL_NAME = "Name";
  private static final String PROPERTY_TILES = "Tiles";
  private static final String MAP_LINE_START = "[";
  private static final String MAP_LINE_END = "]";
  private static final String PROPERTY_ROWS = "Rows";
  private static final String PROPERTY_COLS = "Cols";
  
  
  @Override
  public Level load(String file) throws IOException {
    InputStream inputStream = ResourceLoader.loadAsset(file);
    Level loadedLevel = null;
    
    try {
      InputStreamReader inputReader = new InputStreamReader(inputStream);
      BufferedReader reader = new BufferedReader(inputReader);
      
      loadedLevel = parseMap(reader);

    } catch (IOException e) {
      throw e;
    } finally {
      inputStream.close();
    }
    
    return loadedLevel;
  }
  
  private String getValue(BufferedReader reader, String propName) throws IOException {
    SimpleProperty prop = loadProperty(reader.readLine());

    if (!prop.name.equalsIgnoreCase(propName)) {
      throw new IOException("Could not load property :" + propName);
    }
    
    return prop.value;

  }
  
  /**
   * Loads the name of the level.
   */
  /*private void loadLevelName(BufferedReader reader) throws IOException {
    SimpleProperty prop = loadProperty(reader.readLine());

    if (!prop.name.equalsIgnoreCase(PROPERTY_LEVEL_NAME)) {
      throw new IOException("Could not find level name!");
    }
    
    level.name = prop.value;
    
    reader.readLine();
  }*/
  
  private Level parseMap(BufferedReader reader) throws IOException {
    LevelBuilder loadedLevelBuilder = new LevelBuilder();
    int rows = 0;
    int cols = 0;
    
    String line;
    while ((line = reader.readLine()) != null) {
      SimpleProperty prop = loadProperty(line);
      if (prop.name.equalsIgnoreCase(PROPERTY_ROWS)) {
        rows = parseInt(prop.value);
      } else if (prop.name.equalsIgnoreCase(PROPERTY_COLS)) {
        cols = parseInt(prop.value);
      }
      else if (prop.name.equalsIgnoreCase(PROPERTY_TILES)) {
        if (rows == 0 || cols == 0) {
          throw new IOException("Invalid Map Size specified or Map Size Missing: "
              + rows + ", " + cols);
        }
        loadedLevelBuilder.withGridSize(rows, cols);
        for (int r = 0; r < rows; r++) {
          line = reader.readLine();
          for (int c = 0; c < cols; c++) {
            char tileType = line.charAt(c);
            TileType typeToCreate = TileType.EMPTY;
            switch (tileType) {
              case '1':
                typeToCreate = TileType.SOLID_ROCK;
                break;
            }
            loadedLevelBuilder.addTile(r, c, typeToCreate);
          }
        }
      }
    }
    
    return loadedLevelBuilder.build();
  }
  
  /**
   * Returns a single swarm loaded from the reader. Returns null if the
   * end of the file has been reached and there is no more swarms to be read.
   */
  /* private void parseMap2(BufferedReader reader) throws IOException {
    
    SwarmBuilder builder = new SwarmBuilder(enemyStockpile, level.map);
    
    String line;

    // A swarm is defined by a number of name/value properties, a pattern, 
    // then either a newline or an end of file. For example:
    //
    // Trigger:Delay
    // SpawnDelay:50
    // Pattern:
    // [xxx]
    // [x x]
    // [xxx]

    while ((line = reader.readLine()) != null) {
      
      SimpleProperty prop = loadProperty(line);
      if (prop.name.equalsIgnoreCase(PROPERTY_PATTERN_SCALE)) {
        builder.withSpawnScale(parseInt(prop.value));
        
      } else if (prop.name.equalsIgnoreCase(PROPERTY_TRIGGER)) {
        parseTriggerType(builder, prop.value);
        
      } else if (prop.name.equalsIgnoreCase(PROPERTY_SPAWN_TIME)) {
        builder.withSpawnTime(parseInt(prop.value));
      
      } else if (prop.name.equalsIgnoreCase(PROPERTY_SPAWN_DELAY)) {
        builder.withSpawnDelay(parseInt(prop.value));
        
      } else if (prop.name.equalsIgnoreCase(PROPERTY_TRIGGER_DEATH_RATIO)) {
        builder.withPreviousSwarmDeathRatio(parseFloat(prop.value));
        
      } else if (prop.name.equalsIgnoreCase(PROPERTY_PATTERN)) {
        builder.withPattern(parsePattern(reader));
        
        // Pattern marks the end of swarm data!
        return builder.build();
      } 
    }
    
    return null;
  } */
  
 
  /**
   * Parses a pattern until either the end of the file or a new line is reached.
   * @param loadedLevelBuilder 
   */
  /*private void parseMap(BufferedReader reader) throws IOException {
    
    StringBuilder pattern = new StringBuilder();
    String line = reader.readLine();
    
    boolean firstLine = true;
    while (line != null && line.length() > 0 ) {
    
      if (!line.endsWith(MAP_LINE_END) || !line.startsWith(MAP_LINE_START)) {
        throw new IOException(
            "Pattern line '" +line + "' did not have a line start/end character.");
      }
      
      if (firstLine) {
        firstLine = false;
      } else {
        pattern.append("\n");
      }
      
      String patternLine = line.substring(1, line.length() - 1);
      pattern.append(patternLine);
      
      line = reader.readLine();
    }
    
    SpawnPattern spawnPattern = new SpawnPattern();
    spawnPattern.parsePattern(pattern.toString());
    return spawnPattern;
  } */

  /**
   * Loads a key/value pair from a single line.
   */
  private SimpleProperty loadProperty(String line) throws IOException {
    
    int propertySplitPoint = line.indexOf(PROPERTY_SEPERATOR);
    if (propertySplitPoint == -1) {
      throw new IOException(
          String.format("Unable to find seperator %s when parsing line '%s'",
              PROPERTY_SEPERATOR, line));
    }
    
    // Get the name.
    String name = line.substring(0, propertySplitPoint);
    
    // Load the value, checking for a case where there is no value after
    // the separator.
    String value;
    if (line.length() - 1 == propertySplitPoint) {
      value = "";
    } else {
      value = line.substring(propertySplitPoint + 1);
    }
    
    return new SimpleProperty(name, value);
  }
  
  /**
   * Parses an int from string. Returns 0 on error.
   */
  private int parseInt(String value) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return 0;
    }
  }
  
  /**
   * Parses a float from string. Returns 0 on error.
   */
  private float parseFloat(String value) {
    try {
      return Float.parseFloat(value);
    } catch (NumberFormatException e) {
      return 0f;
    }
  }
  
  private class SimpleProperty {
    public final String name;
    public final String value;
    public SimpleProperty(String name, String value) {
      this.name = name;
      this.value = value;
    }
  }
}
