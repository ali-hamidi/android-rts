package com.zeddic.war;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

import com.zeddic.common.opengl.AbstractGame;
import com.zeddic.common.opengl.Cube;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.common.opengl.TextureLibrary;
import com.zeddic.common.transistions.Transition;
import com.zeddic.common.transistions.Transitions.TransitionType;
import com.zeddic.war.collision.CollisionSystem;
import com.zeddic.war.level.MockLevelLoader;
import com.zeddic.war.ships.FighterShip;


public class WarGame extends AbstractGame {

  private static final float NEAR = 1;
  private static final float DEPTH = 750;
  private static final float DRAW_DEPTH = NEAR + (DEPTH / 2);
  
  private BattleCommandManager commandManager;
  private Camera camera = new Camera();
  
  // Temp vars for open gl testing.
  private Cube cube = new Cube(10, 10, 10);
  Transition transition = new Transition(50f, 50f, 1000, TransitionType.EASE_IN_OUT);
  Transition transX = new Transition(5f, 1000f, 5000, TransitionType.EASE_IN_OUT);
  Transition rot = new Transition(0f, 360f, 10000, TransitionType.LINEAR);

  private GridPlane grid = new GridPlane(2048,2048, 32, 32);
  private Planet planet = new Planet(50, 50);
  
  public WarGame() {
    init();
  }

  public void init() {
    
    // Temp objects for rending the cube.
    cube.setColor(242, 17, 73, 180);
    transition.setAutoReverse(true);
    rot.setAutoReset(true);
    transX.setAutoReverse(true);

    // Populate the GameState
    GameState.setScreen(800, 640);
    GameState.level = new MockLevelLoader().load("blah");
    CollisionSystem.get().initializeForLevel(GameState.level);
    commandManager = new BattleCommandManager();
    
    // Create the enemies and reusable game objects. 
    GameState.stockpiles = new Stockpiles();
    GameState.stockpiles.populate();

    FighterShip ship = GameState.stockpiles.ships.take(FighterShip.class);
    ship.x = 500;
    ship.y = 500;
    ship.enable();

    ship = GameState.stockpiles.ships.take(FighterShip.class);
    ship.x = 200;
    ship.y = 200;
    ship.enable();
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    TextureLibrary.get().init(gl, GameState.context);
    TextureLibrary.get().reload();
    //int texture = GameState.textures.getTexture(gl, R.drawable.grid2);
    //grid.setTexture(texture);

    gl.glEnable(GL10.GL_TEXTURE_2D);      //Enable Texture Mapping ( NEW )
    gl.glShadeModel(GL10.GL_SMOOTH);      //Enable Smooth Shading
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);  //Black Background
    gl.glClearDepthf(1.0f);           //Depth Buffer Setup
    gl.glEnable(GL10.GL_DEPTH_TEST);      //Enables Depth Testing
    gl.glDepthFunc(GL10.GL_LEQUAL);       //The Type Of Depth Testing To Do

    
    gl.glDisable(GL10.GL_DITHER);
    gl.glEnable(GL10.GL_BLEND);
    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    
    
    /*gl.glEnable(GL10.GL_TEXTURE_2D);
    gl.glShadeModel(GL10.GL_SMOOTH);      //Enable Smooth Shading
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);  //Black Background
    gl.glClearDepthf(1.0f);           //Depth Buffer Setup
    gl.glEnable(GL10.GL_DEPTH_TEST);      //Enables Depth Testing
    gl.glDepthFunc(GL10.GL_LEQUAL);       //The Type Of Depth Testing To Do

     
    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);  */
    
    
    gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 

 
    
    /*
     * By default, OpenGL enables features that improve quality
     * but reduce performance. One might want to tweak that
     * especially on software renderer.
     */
    //gl.glDisable(GL10.GL_DITHER);

    /*
     * Some one-time OpenGL initialization can be made here
     * probably based on features of this particular context
     */
    //gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

    ////gl.glClearColor(.5f, .5f, .5f, 1);
    //gl.glShadeModel(GL10.GL_SMOOTH);
    //gl.glEnable(GL10.GL_DEPTH_TEST);
    //gl.glEnable(GL10.GL_TEXTURE_2D);

  }
  
  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    
    super.onSurfaceChanged(gl, width, height);
    
    GameState.setScreen(width, height);
    
    gl.glViewport(0, 0, width, height); 

    gl.glMatrixMode(GL10.GL_PROJECTION);
    gl.glLoadIdentity();

    float far = NEAR + DEPTH;
    float nearWidth = NEAR * width / DRAW_DEPTH;
    float nearHeight = NEAR * height / DRAW_DEPTH;
    //gl.glFrustumf(-nearWidth/2, nearWidth/2, -nearHeight/2, nearHeight/2, NEAR, far);
    
    //gl.glOrthof(left, right, bottom, top, zNear, zFar)
    gl.glOrthof(0, width, 0, height, 0, 100);
  }
  
  @Override
  public void update(long time) {
    commandManager.update(time);
    GameState.stockpiles.update(time);
    
    transition.update(time);
    rot.update(time);
    transX.update(time);
    planet.update(time);
  }

  @Override
  public void draw(GL10 gl) {
    // Setup the Model View Matrix.
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);  
    gl.glMatrixMode(GL10.GL_MODELVIEW);
    gl.glLoadIdentity();
    
    // Translate the world so drawing takes placed in the middle of it's drawable depth.
    //gl.glTranslatef(0.0f, 0.0f, -DRAW_DEPTH);
    
    // Translate the world so 0, 0 corresponds to the bottom left of the screen.
    //gl.glTranslatef(-GameState.screenWidth / 2, -GameState.screenHeight / 2, 0);

    // Apply the camera.
    
    //camera.x = transX.get();
    //camera.y = transX.get();
    camera.apply(gl);

    //gl.glPushMatrix();
    grid.draw(gl);
    planet.draw(gl);
    //gl.glPopMatrix();
    
   // CollisionSystem.get().draw(gl);
   // commandManager.draw(gl);
    //GameState.level.draw(gl);
    GameState.stockpiles.draw(gl);
    
    camera.end(gl);
    
    // Any user interface elements, such as scores or a menu may be drawn here.
  }


  @Override
  public void onTouchEvent(MotionEvent e) {
    //camera.onTouchEvent(e);
    commandManager.onTouch(e);
  }
}
