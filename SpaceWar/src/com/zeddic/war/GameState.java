/*
 * Copyright (C) 2010 Geo Siege Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zeddic.war;

import android.app.Activity;
import android.content.Context;

import com.zeddic.common.opengl.TextureLibrary;
import com.zeddic.common.util.ResourceLoader;
import com.zeddic.war.effects.Effects;
import com.zeddic.war.level.Level;

public class GameState {

  public static Effects effects = null;
  public static Context context = null;
  public static Activity activity;
  public static Stockpiles stockpiles = null;
  public static Level level = null;
  public static TextureLibrary textures = null;
  public static Camera camera;
  
  private static boolean loaded = false;
  private static int loadCount = 0;
  
  public static void setup(Activity activity) {
    
    GameState.activity = activity;
    GameState.context = activity;
    
    loadCount++;
    
    if (loaded) {
      return;
    }

    ResourceLoader.init(context);

    loaded = true;
  }
  
  public static void cleanup() {
    
    activity = null;
    context = null;
    
    if (!loaded) {
      return;
    }
    
    loadCount--;
    if (loadCount > 0) {
      return;
    }

    effects = null;
    context = null;
    loaded = false;
  }
}
