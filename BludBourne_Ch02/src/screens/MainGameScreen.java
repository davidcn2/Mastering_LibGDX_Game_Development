package screens;

// LibGDX imports.
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

// LibGDX custom class imports.
import core.BaseGame;
import core.BaseScreen;

// Local project imports.
import bludbourne_ch02.Entity;
import bludbourne_ch02.MapManager;
import bludbourne_ch02.PlayerController;

/*
Interface (implements) vs Sub-Class (extends)...

The distinction is that implements means that you're using the elements of a Java Interface in your
class, and extends means that you are creating a subclass of the class you are extending. You can
only extend one class in your new class, but you can implement as many interfaces as you would like.

Interface:  A Java interface is a bit like a class, except a Java interface can only contain method
signatures and fields. An Java interface cannot contain an implementation of the methods, only the
signature (name, parameters and exceptions) of the method. You can use interfaces in Java as a way
to achieve polymorphism.

Subclass: A Java subclass is a class which inherits a method or methods from a Java superclass.
A Java class may be either a subclass, a superclass, both, or neither!

Polymorphism:  Polymorphism is the ability of an object to take on many forms. The most common use
of polymorphism in OOP occurs when a parent class reference is used to refer to a child class object.
Any Java object that can pass more than one IS-A test is considered to be polymorphic.

ArrayList supports dynamic arrays that can grow as needed.
*/

public class MainGameScreen extends BaseScreen { // Extends the BaseScreen class.
    
    /**
    * The class extends the basic functionality of a BaseScreen class and sets up the main game screen.
    * The MainGameScreen will act as the primary gameplay screen that displays the different maps and 
    * the player character moving around in them.  The MainGameScreen class represents the main gameplay 
    * screen used to display the game map, player avatar, and any UI components.
    * <br>
    * <br>1.  Initialization occurs in the constructor.
    * <br>2.  The first appearance of the screen (and upon becoming current) cause the execution of the function, 
    *     show().
    * <br>3.  The show() function sets up the viewport, camera, orthogonal tile map renderer, player, and controller.
    * <br>4.  During the first appearance of the screen, the reference of the show() function to 
    *     mapMgr.getCurrentMap() causes the default map to load via loadMap().
    * <br>3.  The main game logic occurs through the render() function. 
    */
    
    /*
    Important Objects:
    
    TiledMap:  Store the data from the tilemap file, which is loaded using a static method from the 
      TmxMapLoader class.
    OrthogonalTileMapRenderer:  Used to draw the contents of the various layers of the tilemap.
      The layers to be rendered are specified by an array of integers.
    OrthographicCamera:  Used to determine which region of a tilemap layer should be rendered, 
      analogous to the role of the Camera object that belongs to each Stage.
    
    Methods include:

    dispose:  Clears LibGDX resources from memory.
    hide:  * Provided by BaseScreen *
    isCollisionWithMapLayer:  Returns whether a collision occurs with the player hitbox and an object in 
      the collision layer of the current map, excluding portals.
    pause:  * Provided by BaseScreen *
    render:  Called every frame and acts as the primary location for rendering, updating, and checking 
      for collisions in the game lifecycle.
    resize:  * Provided by BaseScreen *
    resume:  * Provided by BaseScreen *
    setupViewport:  Computes the number of tiles to display in the application window (viewport).
    show:  Gets called when the screen becomes the current one for a Game.  The method sets up the viewport, 
      camera, orthogonal tile map renderer, player, and controller.
    update:  Occurs during the update phase (render method) and currently merely exists to override 
      the similarly named function in the BaseScreen parent class.  Does nothing.
    updatePortalLayerActivation:  Returns whether a collision occurs with the player hitbox and an object
      in the portal collision layer of the current map.  When a collision occurs, the method moves the 
      player to the starting position in the target map.
    */
    
    // Declare constants.
    private static final String TAG = MainGameScreen.class.getSimpleName(); // Class name.

    // Declare object variables.
    
    /** Camera to use with Tiled map. */
    private OrthographicCamera _camera;
    
    /** Reference to the player input class. */
    private PlayerController _controller;
    
    /** Sprite for the player actually drawn on map.  Contains current animation frame.  
     * Updated in setDirection() method in Entity. */
    private TextureRegion _currentPlayerFrame;
    
    /** Sprite for the player, used only for positional details.  Contains only first animation frame. */
    private Sprite _currentPlayerSprite;
    
    /** Reference to the map manager class. */
    private static MapManager _mapMgr;
    
    /** Renderer to use with Tiled map. */
    private OrthogonalTiledMapRenderer _mapRenderer;
    
    /** Reference to the entity class for the player. */
    private static Entity _player;
    
    /**
     * 
     * The constructor calls the BaseScreen constructor, sets defaults, and initializes the map manager.
     *   
     * Initializing the map manager involves:
     *   
     * <br>1.  Initializes variables.
     * <br>2.  Populates hash maps with relative paths of TiledMap files.
     * <br>3.  Copies base starting location of player (0, 0) to related hash maps for each TiledMap.
     * 
     * @param g  Reference to base game.
     * @param windowWidth  Width to use for stages.
     * @param windowHeight  Height to use for stages.
     */
    
    // g = Reference to base game.
    // windowWidth = Width to use for stages.
    // windowHeight = Height to use for stages.
    public MainGameScreen(BaseGame g, int windowWidth, int windowHeight)
    {

        /*
        The constructor calls the BaseScreen constructor, sets defaults, and initializes the map manager.
        
        Initializing the map manager involves:
        
        1.  Initializes variables.
        2.  Populates hash maps with relative paths of TiledMap files.
        3.  Copies base starting location of player (0, 0) to related hash maps for each TiledMap.
        */
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(g, windowWidth, windowHeight);

        // Set defaults.
        _camera = null;
        _mapRenderer = null;

        // Initialize map manager.
        _mapMgr = new MapManager();

    }

    // Inner classes below...
    
    /*
    Static members belong to the class, instead of a specific instance (object).  As a result, the inner 
    class can be referenced without an object.
    */
    
    /**
     * The inner class stores display dimension related information.
     */
    private static class VIEWPORT 
    {
        
        // The inner class stores display dimension related information.
        
        // Declare regular variables.
        
        /** Adjusted number of tiles to display across, centered on player. */
        static float viewportWidth;
        
        /** Adjusted number of tiles to display vertically, centered on player. */
        static float viewportHeight;
        
        /** (Base) number of tiles to display across, centered on player. */
        static float virtualWidth;
        
        /** (Base) number of tiles to display vertically, centered on player. */
        static float virtualHeight;
        
        /** Width of application window. */
        static float physicalWidth;
        
        /** Height of application window. */
        static float physicalHeight;
        
        /** Aspect ratio (width to height), using base tile counts. */
        static float aspectRatio;
        
    }
	
    // Methods below...
    
    /**
     * The method gets called when the screen becomes the current one for a Game.  The method sets up the
     * viewport, camera, orthogonal tile map renderer, player, and controller.
     * <br><br>
     * Whenever a new screen is set with the Game class, the hide() method will be called on the current 
     * screen, and a show() method will be called on the new screen.
     * <br><br>
     * During the first appearance of the screen, the reference of the show() function to 
     * mapMgr.getCurrentMap() causes the default map to load via loadMap().
     */
    @Override
    public void show()
    {
		
        /*
        The method gets called when the screen becomes the current one for a Game.  The method sets up the
        viewport, camera, orthogonal tile map renderer, player, and controller.
        
        Whenever a new screen is set with the Game class, the hide() method will be called on the current 
        screen, and a show() method will be called on the new screen.
        
        During the first appearance of the screen, the reference of the show() function to 
        mapMgr.getCurrentMap() causes the default map to load via loadMap().
        */
        
        // Set up the camera object, _camera.
        
        // Calculate the adjusted viewport dimensions.
        // Store all viewport details in the inner class, VIEWPORT.
        // Passes in base number of tiles across and down to display, centered on player.
        setupViewport(10, 10);

        // Instantiate orthographic camera.
        _camera = new OrthographicCamera();
        
        // Configure camera with the positive y-axis pointed up and using the calculated viewport dimensions.
        // viewportWidth = Number of tiles to display across, centered on player.
        // viewportHeight = Number of tiles to display vertically, centered on player.
        _camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        // Instantiate Tiled Map renderer with current map and scale.
        // Scale = 1 / (square side size, in pixels).  e.g. 1 / 16 pixels = 0.0625.
        // _mapMgr.getCurrentMap() ... Loads the TOWN map and sets the player starting location.
        _mapRenderer = new OrthogonalTiledMapRenderer(_mapMgr.getCurrentMap(), MapManager.UNIT_SCALE);
        
        // Sets the projection matrix for rendering, as well as the bounds of the map which should be rendered.
        // Make sure that the frustum spanned by the projection matrix coincides with the view bounds.
        
        // The setView() method bridges the rendered map with the viewport of the camera.
        _mapRenderer.setView(_camera);

        /*
        The unit scale is configured for the pixel dimensions of our world, so every tile unit on the map 
        will represent a square with side lengths of x pixels.
        */
        
        // Display scale.
        Gdx.app.debug(TAG, "UnitScale value is: " + _mapRenderer.getUnitScale());

        /*
        MainGameScreen will also contain a static instance of Entity that represents the
        player in the game.  The lifetime of the player will persist as we load different
        maps, so for now, it makes sense that the player object lives in this class.
        */
        
        // Instantiate player object.
        _player = new Entity();
        
        // Initialize map position properties (location) for player.
        // Set current position (in terms of tiles) based on default set in Tiled map.
        // Set next position to same value.
        _player.init(_mapMgr.getPlayerStartUnitScaled().x, _mapMgr.getPlayerStartUnitScaled().y);
        
        // Initialize player positional sprite (vs current animation).
        _currentPlayerSprite = _player.getFrameSprite();

        // Initialize player input object.
        _controller = new PlayerController(_player);
        
        // Configure to process all input events with an InputProcessor.
        Gdx.input.setInputProcessor(_controller);
    
    }

    /**
     * 
     * The render() method will be called every frame, and is the primary location for rendering, 
     * updating, and checking for collisions in the game lifecycle.  First, lock the viewport 
     * (camera location) to the current position of the player character.  Locking ensures that
     * the player is always in the middle of the screen.  Then, check whether the player has 
     * activated a portal, which will be handled with the MapManager class.  Also, check for 
     * collisions with the related layer of the map.  If any collisions occur, then do not update
     * the position of the player.  Update the camera information in the OrthogonalTiledMapRenderer 
     * object and then render the TiledMap object first (due to ordering requirements).
     * 
     * @param delta  Time span between the current and last frame in seconds.  Passed / populated automatically.
     */
    
    // delta = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void render(float delta)
    {

        /*
        The render() method will be called every frame, and is the primary location for rendering, 
        updating, and checking for collisions in the game lifecycle.  First, lock the viewport 
        (camera location) to the current position of the player character.  Locking ensures that
        the player is always in the middle of the screen.  Then, check whether the player has 
        activated a portal, which will be handled with the MapManager class.  Also, check for 
        collisions with the related layer of the map.  If any collisions occur, then do not update
        the position of the player.  Update the camera information in the OrthogonalTiledMapRenderer 
        object and then render the TiledMap object first (due to ordering requirements).
        */
        
        // Overdraw the area with the given glClearColor.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        
        // Clear the area using the specified buffer.  Supports multiple buffers.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Preferable to lock and center the _camera to the pixel position of the player.
        _camera.position.set(_currentPlayerSprite.getX(), _currentPlayerSprite.getY(), 0f);
        
        // Recalculate the projection and view matrix of the camera.
        _camera.update();

        // Adjust frame time to smooth animation.
        // Reduce player hitbox height to half for a better feel.
        _player.update(delta);
        
        // Get current animation frame for player.
        _currentPlayerFrame = _player.getFrame();
        
        // Determine whether a collision occurs with the player hitbox and an object in the portal 
        // collision layer of the current map.  When a collision occurs, move the player to the 
        // starting position in the target map.
        updatePortalLayerActivation(_player.boundingBox);

        // If player hitbox NOT intersecting object in collision map layer, then...
        if( !isCollisionWithMapLayer(_player.boundingBox) )
            {
            // Player hitbox NOT intersecting object in collision map layer.
                
            // Sets the current player position to the next.
            _player.setNextPositionToCurrent();
            }
        
        // Process cached input (keyboard and mouse).
        _controller.update(delta);

        //_mapRenderer.getBatch().enableBlending();
        //_mapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Sets the projection matrix for rendering, as well as the bounds of the map which should be rendered.
        _mapRenderer.setView(_camera);
        
        // Render the map.
        _mapRenderer.render();

        /*
        Draw the character to the screen, making sure to use the getBatch() call for when numerous 
        objects exist to update. By drawing in a batch update, the overhead of updating the textures 
        will be minimal, since the GPU will consume the texture updates at one time.  The alternative
        involves constantly throttling between updating and rendering separate textures.
        
        The setDirection() method in the Entity class, called whenever processing input / movement, 
        determines which animation frame to display.
        */
        
        // Set up batch for drawing.
        _mapRenderer.getBatch().begin();
        
        // Add command to batch to draw passed texture region (for player) at specified coordinates.
        _mapRenderer.getBatch().draw(_currentPlayerFrame, _currentPlayerSprite.getX(), _currentPlayerSprite.getY(), 1,1);
        
        // Finish batch-related rendering.
        _mapRenderer.getBatch().end();
        
    }

    /*
    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause() 
    {

    }

    @Override
    public void resume()
    {
    
    }
    */

    /**
     * The method clears LibGDX resources from memory and disables the input processor.
     */
    @Override
    public void dispose()
    {
        
        // The method clears LibGDX resources from memory and disables the input processor.
        
        _player.dispose(); // Clear the texture associated with the player from memory.
        _controller.dispose(); // Clear resources associated with the input processor from memory.
        _mapRenderer.dispose(); // Clear TiledMap renderer from memory.
        
        Gdx.input.setInputProcessor(null); // Disable input processor.
        
    }

    /**
     * 
     * The setupViewport() method helps with the bookkeeping of the inner class, VIEWPORT.
     * VIEWPORT acts simply as a convenience class for maintaining all the parameters
     * that compose the viewport for the camera.  The class will also account for the
     * skewing that can occur depending on the width to height ratio, updating the values
     * accordingly.
     * <br><br>
     * The method computes the number of tiles to display in the application window (viewport).
     * The virtual width corresponds to the number to display across the screen, centered on the player.
     * The virtual height corresponds to the number to display vertically on the screen, centered on the player.
     * The aspect ratio will adjust one of the base numbers passed to the method (viewport width or height).
     * <br><br>
     * Example:
     * Parameters ... width = 10, height = 10.
     * <br>
     * <br>1.  Starts with 5 tiles shown to the left of the player, 5 tiles shown to the right.
     * <br>2.  Starts with 5 tiles shown above the player, 5 tiles shown below.
     * <br><br>
     * Now, calculate aspect ratio adjustments.
     * <br><br>  
     * Viewport dimensions of 10 x 10.
     * (10, 10) = 10 / 10 = 1.000 aspect ratio.
     * <br><br>  
     * Assume physical application window dimensions of 800 x 600.
     * (800, 600) = 800 / 600 = 1.333 aspect ratio.
     * <br><br>  
     * Since physical aspect ratio exceeds viewport, adjust viewport width.  Viewport height remains the same.
     * Adjusted width = viewport height (10) x physical aspect ratio (1.333) = 10 x 1.333 = 13.33.
     * Height = 10.00.
     * 
     * @param width  (Base) number of tiles to display across, centered on player.  Aspect ratio may adjust.
     * @param height  (Base) number of tiles to display vertically, centered on player.  Aspect ratio may adjust.
     */
    
    // width = (Base) number of tiles to display across, centered on player.  Aspect ratio may adjust.
    // height = (Base) number of tiles to display vertically, centered on player.  Aspect ratio may adjust.
    private void setupViewport(int width, int height)
    {
    
        /*
        The setupViewport() method helps with the bookkeeping of the inner class, VIEWPORT.
        VIEWPORT acts simply as a convenience class for maintaining all the parameters
        that compose the viewport for the camera.  The class will also account for the
        skewing that can occur depending on the width to height ratio, updating the values
        accordingly.
        
        The method computes the number of tiles to display in the application window (viewport).
        The virtual width corresponds to the number to display across the screen, centered on the player.
        The virtual height corresponds to the number to display vertically on the screen, centered on the player.
        The aspect ratio will adjust one of the base numbers passed to the method (viewport width or height).
        
        Example:
        Parameters ... width = 10, height = 10.
        
        1.  Starts with 5 tiles shown to the left of the player, 5 tiles shown to the right.
        2.  Starts with 5 tiles shown above the player, 5 tiles shown below.
        
        Now, calculate aspect ratio adjustments.
        
        Viewport dimensions of 10 x 10.
        (10, 10) = 10 / 10 = 1.000 aspect ratio.
        
        Assume physical application window dimensions of 800 x 600.
        (800, 600) = 800 / 600 = 1.333 aspect ratio.
        
        Since physical aspect ratio exceeds viewport, adjust viewport width.  Viewport height remains the same.
        Adjusted width = viewport height (10) x physical aspect ratio (1.333) = 10 x 1.333 = 13.33.
        Height = 10.00.
        */
        
        // Make the viewport a percentage of the total display area.
        VIEWPORT.virtualWidth = width; // (Base) number of tiles to display across, centered on player.
        VIEWPORT.virtualHeight = height; // (Base) number of tiles to display vertically, centered on player.

        // Store dimensions of applicatio window, in pixels.
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth(); // Width of application window.
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight(); // Height of application window.

        // Store aspect ratio for current viewport (using base tile counts).
        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight); // e.g. 10 / 10 = 1.
        
        // Update viewport to prevent skewing.
        
        // Example...
        // Physical aspect ratio with 800 x 600 = 800 / 600 = 1.333.
        // Viewport aspect ratio with 10w, 10h = 10 / 10 = 1.000.
        // Results in letterbox left and right.
        
        // If physical aspect ratio exceeds viewport, then...
        if( VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio )
        {
            
            // Physical aspect ratio exceeds viewport.
            
            // Letterbox left and right.
                
            // Adjust viewport width.
            VIEWPORT.viewportWidth = height * (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight);
                
            // Keep viewport height.
            VIEWPORT.viewportHeight = height;
            
        }
        
        else
        {
            
            // Viewport aspect ratio exceeds physical.
            
            // Letterbox above and below.
            
            // Keep viewport width.
            VIEWPORT.viewportWidth = width;
            
            // Adjust viewport height.
            VIEWPORT.viewportHeight = width * (VIEWPORT.physicalHeight / VIEWPORT.physicalWidth);
            
        }
        
        // Display virtual, viewport, and physical dimensions.
        Gdx.app.debug(TAG, "WorldRenderer: virtual: (" + VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")" );
        Gdx.app.debug(TAG, "WorldRenderer: viewport: (" + VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")" );
        Gdx.app.debug(TAG, "WorldRenderer: physical: (" + VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")" );
    
    }

    /**
     * 
     * The method returns whether a collision occurs with the player hitbox and an 
     * object in the collision layer of the current map, excluding portals.
     * <br><br> 
     * The isCollisionWithMapLayer() method is called for every frame in the render()
     * method with the bounding box of the player character passed.  The bounding box 
     * acts as the rectangle that defines the hitbox of the player.  The method tests
     * the player hitbox against all rectangle objects on the collision layer of the 
     * TiledMap map.  If any of the rectangles overlap, then a collision occurred.
     * 
     * @param boundingBox  Rectangle that defines the hitbox of the player.
     * @return  Whether a collision occurs with the player hitbox.
     */
    
    // boundingBox = Rectangle that defines the hitbox of the player.
    private boolean isCollisionWithMapLayer(Rectangle boundingBox)
    {
        
        /*
        The method returns whether a collision occurs with the player hitbox and an 
        object in the collision layer of the current map, excluding portals.
        
        The isCollisionWithMapLayer() method is called for every frame in the render()
        method with the bounding box of the player character passed.  The bounding box 
        acts as the rectangle that defines the hitbox of the player.  The method tests
        the player hitbox against all rectangle objects on the collision layer of the 
        TiledMap map.  If any of the rectangles overlap, then a collision occurred.
        
        Returns true when a collision occurs with the player hitbox.
        Returns false when no collision occurs with the player hitbox.
        */
        
        boolean collision; // Whether collision occurred.
        MapLayer mapCollisionLayer; // Collision layer within the TiledMap (excludes portals).
        Rectangle rectangle; // Rectangle bounding box for current map object in collision checking loop.
        
        // Get a reference to the collision layer from the TiledMap.
        mapCollisionLayer =  _mapMgr.getCollisionLayer();

        // If collision layer exists, then...
        if ( mapCollisionLayer != null )
        {
            
            // Collision layer exists.
            
            // Default to no collision occurring.
            collision = false;
            
            // Loop through (possible) objects in map to check for collision.
            for ( MapObject object: mapCollisionLayer.getObjects() )
            {
                
                // If RectangleMapObject found (indicates collision bouding box), then...
                if (object instanceof RectangleMapObject)
                {
                    
                    // Convert to standard rectangle object.
                    rectangle = ((RectangleMapObject)object).getRectangle();
                    
                    //Gdx.app.debug(TAG, "Collision Rect (" + rectangle.x + "," + rectangle.y + ")");
                    //Gdx.app.debug(TAG, "Player Rect (" + boundingBox.x + "," + boundingBox.y + ")");
                    
                    // If player and map object bounding boxes intersect, then...
                    if ( boundingBox.overlaps(rectangle) )
                    {
                        
                        // Player and map object bounding boxes intersect.
                        
                        //Gdx.app.debug(TAG, "Map Collision!");

                        // Flag collision as occurring.
                        collision = true;
                        
                        // Exit loop.
                        break;
                        
                    }
                    
                } // End ... If RectangleMapObject found (indicates collision bouding box).
                
            } // End ... Loop through (possible) objects in map to check for collision.
            
        } // End ... If collision layer exists.
        
        else
        {
            
            // No collision layer exists.
            
            // Flag as no collision occurring.
            collision = false;
            
        }

        // Return whether collision occurring.
        return collision;
        
    }

    /**
     * 
     * The method returns whether a collision occurs with the player hitbox and an 
     * object in the portal collision layer of the current map.  When a collision
     * occurs, the method moves the player to the starting position in the target map.
     * <br><br> 
     * The updatePortalLayerActivation() method checks for collisions between portal 
     * objects and the player hitbox.  If a player walks over these special areas on 
     * the map, then an event will be triggered.  When portal activation occurs, the
     * method first caches the closest player spawn in the MapManager class.  The
     * caching helps during the transition from the old to the new location.  Then, 
     * the method loads the new map designated by the portal activation name, resetting
     * the player position, and setting the new map to be rendered in the next frame.
     * <br><br>
     * Summary of starting location logic:
     * <br>1.  Caches (stores) location in current map of closest spawn point, relative to
     * player position.
     * <br>2.  If new map not visited before, sets starting location to closest spawn point
     * to (0, 0).
     * <br>3.  If new map visited before, uses location stored in _playerStartLocationTable.
     * 
     * @param boundingBox  Rectangle that defines the hitbox of the player.
     * @return  Whether a collision occurs with the player hitbox and a portal.
     */
    
    // boundingBox = Rectangle that defines the hitbox of the player.
    private boolean updatePortalLayerActivation(Rectangle boundingBox)
    {
    
        /*
        The method returns whether a collision occurs with the player hitbox and an 
        object in the portal collision layer of the current map.  When a collision
        occurs, the method moves the player to the starting position in the target map.
        
        The updatePortalLayerActivation() method checks for collisions between portal 
        objects and the player hitbox.  If a player walks over these special areas on 
        the map, then an event will be triggered.  When portal activation occurs, the
        method first caches the closest player spawn in the MapManager class.  The
        caching helps during the transition from the old to the new location.  Then, 
        the method loads the new map designated by the portal activation name, resetting
        the player position, and setting the new map to be rendered in the next frame.
        
        Summary of starting location logic:
        1.  Caches (stores) location in current map of closest spawn point, relative to
        player position.
        2.  If new map not visited before, sets starting location to closest spawn point
        to (0, 0).
        3.  If new map visited before, uses location stored in _playerStartLocationTable.
        
        Returns true when a collision occurs with the player hitbox and a portal.
        Returns false when no collision occurs with the player hitbox and a portal.
        */
        
        boolean collision; // Whether collision occurred.
        String mapName; // Name of map to which portal object leads.
        MapLayer mapPortalLayer; // Collision layer within the TiledMap (only portals).
        Rectangle rectangle; // Rectangle bounding box for current map object in collision checking loop.
        
        // Get a reference to the portal (collision) layer from the TiledMap.
        mapPortalLayer =  _mapMgr.getPortalLayer();

        // If portal collision layer exists, then...
        if ( mapPortalLayer != null )
        {
            
            // Portal collision layer exists.
            
            // Default to no collision occurring.
            collision = false;

            // Loop through (possible) portal objects in map to check for collision.
            for( MapObject object: mapPortalLayer.getObjects())
            {
                
                // If RectangleMapObject found (indicates portal collision bouding box), then...
                if(object instanceof RectangleMapObject)
                {
                    
                    // Convert to standard rectangle object.
                    rectangle = ((RectangleMapObject)object).getRectangle();
                    
                    //Gdx.app.debug(TAG, "Collision Rect (" + rectangle.x + "," + rectangle.y + ")");
                    //Gdx.app.debug(TAG, "Player Rect (" + boundingBox.x + "," + boundingBox.y + ")");
                    
                    // If player and portal object bounding boxes intersect, then...
                    if( boundingBox.overlaps(rectangle) )
                    {
                        
                        // Player and portal object bounding boxes intersect.
                        
                        // Store name of map to which portal leads.
                        mapName = object.getName();
                        
                        // If map name exists, then...
                        if ( mapName != null )
                        {

                            // Map name exists.

                            // Player already visited map.
                            
                            // Cache the closest player spawn in the MapManager class.
                            // Convert from tiles to pixels before finding closest spawn location.
                            _mapMgr.setClosestStartPositionFromScaledUnits(_player.getCurrentPosition());
                            
                            // Load the new map designated by the portal activation name.
                            _mapMgr.loadMap(mapName);
                            
                            // Reset the player position (to the starting point in the target map).
                            _player.init(_mapMgr.getPlayerStartUnitScaled().x, _mapMgr.getPlayerStartUnitScaled().y);
                            
                            // Set the new map to be rendered in the next frame.
                            _mapRenderer.setMap(_mapMgr.getCurrentMap());
                            
                            // Display that portal was activated.
                            Gdx.app.debug(TAG, "Portal Activated");

                            // Flag collision as occurring between player and portal.
                            collision = true;
                            
                        }
                        
                        else
                        {
                            // Map name missing (null).
                            
                            // Display warning.
                            Gdx.app.debug(TAG, "Portal map name missing!");
                            
                            // Flag collision as NOT occurring between player and portal.
                            collision = false;
                        }
                        
                        // Exit loop.
                        break;
                        
                    } // End ... If player and portal object bounding boxes intersect.
                        
                } // End ... If RectangleMapObject found (indicates portal collision bouding box).
                    
            } // End ... Loop through (possible) portal objects in map to check for collision.
            
        } // End ... If portal collision layer exists.
        
        else
        {
            
            // No portal collision layer exists.
            
            // Flag as no collision occurring between player and portal.
            collision = false;
            
        }

        // Return whether collision occurring between player and portal.
        return collision;
            
    }
    
    /**
     * 
     * The function occurs during the update phase (render method) and currently merely exists to override
     * the similarly named function in the BaseScreen parent class.
     * 
     * @param dt  Time span between the current and last frame in seconds.  Passed / populated automatically.
     */
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt) 
    {
        
        /*
        The function occurs during the update phase (render method) and currently merely exists to override
        the similarly named function in the BaseScreen parent class.
        */
            
    }

}