package bludbourne_ch02;

// LibGDX imports.
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.*;

// Java imports.
import java.util.HashMap;

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

public class MapManager 
{
    
    /*
    The class provides helper methods for managing the different maps and map layers.  The class assists 
    in loading the TiledMap maps and includes methods for accessing the different MapLayer and MapObject 
    objects in the layers.

    Explanation from Mastering LibGDX Game Development:
    We are going to be using TiledMap (TMX format) maps that will then be rendered to a Screen instance 
    using the OrthogonalTiledMapRenderer class in the render thread.  Each map contains several MapLayer
    objects that are ordered with a 0-based index.  In the future, we will be drawing the MapLayer 
    objects by first querying them by name and then drawing them from the background up to the foreground
    MapLayer.  We have the other MapLayer objects (object layers in Tiled) that will not be rendered,
    but contain the MapObject objects (or more specifically RectangleMapObjects) used to test for 
    collisions, portal triggers, and player spawns. We can also see how the viewport only renders a portion 
    of the total map (depending on the units used when setting the width and height dimensions of the 
    viewport).  We are also going to store state information regarding the player start position and the 
    current player position using the Vector2 class.
    
    Switching from one map to another involves (focusing on positional operations):
    1.  Check for intersection player and portal tile in updatePortalLayerActivation.
    2.  Player intersects a portal tile (checks against objects in _portalLayer map layer).
    3.  Call setClosestStartPositionFromScaledUnits (which executes setClosestStartPosition) to cache the
        closest player spawn for the map being left.
    4.  Closest player spawn locations are stored in _playerStartLocationTable (hash map).
    5.  Call loadMap to perform core logic tied to "loading" map.
    6.  If loading map for first time, determine player location based on spawn layer (relative to 0, 0).
    7.  If loading map after first time, use position stored before activating portal (in 
        _playerStartLocationTable).
    
    Methods include:
    
    isPopulatedText:  Returns whether text parameter populated -- length greater than zero (and not null).
    loadMap:  Loads and gets the passed map from the asset manager (as necessary) and sets the player 
      starting location.
    setClosestStartPosition:  Sets the player starting location to the closest position of a player object 
      in the spawn layer.  Takes a base player location with pixel coordinates as a parameter.
    setClosestStartPositionFromScaledUnits:  Sets the player starting location to the closest position of 
      a player object in the spawn layer.  The method takes a base player location with tile (unit) 
      coordinates as a parameter and converts to pixels.
    */
    
    // Declare constants.
    
    // General:
    private static final String TAG = MapManager.class.getSimpleName();
    
    private final static String PLAYER_START = "PLAYER_START";
    public final static float UNIT_SCALE  = 1/16f; // Used to convert from pixels to map coordinates.
    
    // Map names (key values in hash map, _mapTable):
    private final static String TOP_WORLD = "TOP_WORLD";
    private final static String TOWN = "TOWN";
    private final static String CASTLE_OF_DOOM = "CASTLE_OF_DOOM";
    
    // Map layers:
    private final static String MAP_COLLISION_LAYER = "MAP_COLLISION_LAYER";
    private final static String MAP_SPAWNS_LAYER = "MAP_SPAWNS_LAYER";
    private final static String MAP_PORTAL_LAYER = "MAP_PORTAL_LAYER";
    
    // Declare list variables.
    
    // All maps for the game:
    
    // private Hashtable<String,String> _mapTable;
    // private Hashtable<String, Vector2> _playerStartLocationTable;
    private HashMap<String, String> _mapTable; // Hash map containing the relative paths to the actual TMX 
      // files located under the assets directory.
    private HashMap<String, Vector2> _playerStartLocationTable; // Hash map containing the closest player 
      // spawn point in the current loaded map, in pixels.
    
    // Declare regular variables.
    private String _currentMapName; // Name of current map.  Corresponds to key values in _mapTable.
    
    // Declare object variables.
    private Vector2 _closestPlayerStartPosition; // Closest player position to spawn location
    private MapLayer _collisionLayer; // Collision layer of the current Tiled map.
    private Vector2 _convertedUnits; // Starting player location in current map, converted from tiles (units) 
      // to pixels.
    private TiledMap _currentMap; // Tiled object for the current map.
    private Vector2 _playerStart; // Starting location of player (pixels).
    private Vector2 _playerStartPositionRect; // Proposed starting locations of player, based on spawn 
      // points (pixels).
    private MapLayer _portalLayer; // Portal layer of the current Tiled map.
    private MapLayer _spawnsLayer; // Entity spawning lawyer of the current Tiled map.

    public MapManager()
    {
        
        /*
        The constructor:
        
        1.  Initializes variables.
        2.  Populates hash maps with relative paths of TiledMap files.
        3.  Copies base starting location of player (0, 0) to related hash maps for each TiledMap.
        */

        // Set defaults.
        _playerStart = new Vector2(0, 0);
        _playerStartPositionRect = new Vector2(0,0);
        _closestPlayerStartPosition = new Vector2(0,0);
        _convertedUnits = new Vector2(0,0);
        _collisionLayer = null; // Clear collision layer.
        _currentMap = null; // Clear current (Tiled) map.
        _portalLayer = null; // Clear portal layer.
        _spawnsLayer = null; // Clear spawn layer.
        
        // _mapTable = new Hashtable();
        // _playerStartLocationTable = new Hashtable();
        
        // Initialize hash maps.
        _mapTable = new HashMap<>();
        _playerStartLocationTable = new HashMap<>();

        // Populate hash maps with relative paths of TiledMap files.
        _mapTable.put(TOP_WORLD, "assets/maps/topworld.tmx");
        _mapTable.put(TOWN, "assets/maps/town.tmx");
        _mapTable.put(CASTLE_OF_DOOM, "assets/maps/castle_of_doom.tmx");

        // Copy base starting location of player (0, 0) to related hash maps for each TiledMap.
        _playerStartLocationTable.put(TOP_WORLD, _playerStart.cpy());
        _playerStartLocationTable.put(TOWN, _playerStart.cpy());
        _playerStartLocationTable.put(CASTLE_OF_DOOM, _playerStart.cpy());
        
    }

    // Getters and setters below...
    
    public MapLayer getCollisionLayer()
    {
        // The function returns a reference to the collision layer of the current Tiled map.
        return _collisionLayer;
    }
    
    public TiledMap getCurrentMap()
    {
        
        // The function returns the current Tiled map object.  If not initialized yet (beginning of game),
        // loads the TOWN map and sets the player starting location.
        
        // If map loaded, then...
        if ( _currentMap == null )
        {
            
            // No map loaded.
            
            // Set name of current map to global constant for TOWN.
            _currentMapName = TOWN;
            
            // Load the TOWN map from the asset manager and set the player starting location.
            loadMap(_currentMapName);
            
        }
        
        // Return a reference to the current Tiled map object.
        return _currentMap;
        
    }
    
    public MapLayer getPortalLayer()
    {
        // The function returns a reference to the portal layer of the current Tiled map.
        return _portalLayer;
    }
    
    public Vector2 getPlayerStartUnitScaled()
    {
        
        // The function returns the scaled version (in tiles) of the player start location.
        
        Vector2 playerStart; // Copy of player start location, allowing for scaling without changing original 
        // values.
        
        // Copy player start location to local variable.
        playerStart = _playerStart.cpy();
        
        // Scale player start location from pixels to tiles.
        playerStart.set(_playerStart.x * UNIT_SCALE, _playerStart.y * UNIT_SCALE);
        
        // Return the scaled version of the player start location.
        return playerStart;
        
    }
    
    // Methods below...
    
    // text = Text to check whether populated.
    public static boolean isPopulatedText(String text)
    {
        // Return whether text parameter populated -- length greater than zero (and not null).
        return text != null && !text.isEmpty();
    }
    
    // mapName = Key value for map to load in hash map, _mapTable.
    public boolean loadMap(String mapName)
    {
        
        /*
        The method loads and gets the passed map from the asset manager (as necessary) and sets
        the player starting location.
        
        The loadMap() method verifies that the string passed in is a valid path and checks to see 
        whether the asset exists.  If the map asset exists, loading occurs.  Next, copying occurs 
        of the object references of the different layers for fast access later.  Layers include 
        collision, portal, and spawn.  Near the end, checking occurs to see whether the starting 
        location is set to (0, 0).  If the starting location is set to (0, 0), the player location 
        was not cached, nor was the map loaded (prior to calling the procedure).  If the player 
        location was not cached prior to executing the procedure, the method stores the location 
        closest to one in the spawn layer.
        
        Summary of player position:
        If loading map for first time, determine based on spawn layer.
        If loading map for second or later time, use position stored before activating portal.
        Prior positions stored in > _playerStartLocationTable.
        
        Returns true when a valid map is loaded.
        Returns false when an invalid name is passed or a map fails to load.
        */
        
        boolean loaded; // Whether valid map loaded.
        String mapFullPath; // Relative path for map to load.
        Vector2 start; // Starting location of player, in pixels.
        
        // Set defaults.
        loaded = false;
        
        // Set default starting location of player, in pixels.
        _playerStart.set(0, 0);

        // Get the relative path to the map to load from the hash map.
        mapFullPath = _mapTable.get(mapName);

        // If map key passed and exists in hash map (neither null nor empty), then...
        if ( isPopulatedText(mapFullPath) )
        {
            
            // Map key passed and exists in hash map (neither null nor empty).
            
            // If current TiledMap object already exists, then...
            if ( _currentMap != null )
            {
                // Current TiledMap object already exists.
                
                // Clear current TiledMap object from memory.
                _currentMap.dispose();
            }

            // Load the passed TMX file into asset manager as a TiledMap asset.
            Utility.loadMapAsset(mapFullPath);

            // If TMX file successfully loaded into asset manager, then...
            if ( Utility.isAssetLoaded(mapFullPath) )
            {
                
                // TMX file successfully loaded into asset manager.
                
                // Get the map asset from the manager and store in the current TiledMap object.
                _currentMap = Utility.getMapAsset(mapFullPath);
                
                // If map asset successfully retrieved, then...
                if ( _currentMap != null )
                {
                    
                    // Map asset successfully retrieved.
                    
                    // Set the current map name equal to the passed key.
                    _currentMapName = mapName;
                
                    // Store a reference to the collision layer (all objects, except portals).
                    _collisionLayer = _currentMap.getLayers().get(MAP_COLLISION_LAYER);

                    // If no collision layer exists, then...
                    if ( _collisionLayer == null )
                    {
                        // No collision layer exists.
                        
                        // Display warning.
                        Gdx.app.debug(TAG, "No collision layer!");
                    }

                    // Store a reference to the portal (specialty collision) layer.
                    _portalLayer = _currentMap.getLayers().get(MAP_PORTAL_LAYER);

                    // If no portal layer exists, then...
                    if ( _portalLayer == null )
                    {
                        // No portal layer exists.
                        
                        // Display warning.
                        Gdx.app.debug(TAG, "No portal layer!");
                    }

                    // Store a reference to the spawn layer.
                    _spawnsLayer = _currentMap.getLayers().get(MAP_SPAWNS_LAYER);

                    // If no spawn layer exists, then...
                    if ( _spawnsLayer == null )
                    {
                        // No spawn layer exists.
                        
                        // Display warning.
                        Gdx.app.debug(TAG, "No spawn layer!");
                    }

                    else
                    {

                        // Spawn layer exists.
                        
                        // Get player starting location (in pixels) for current map.
                        start = _playerStartLocationTable.get(_currentMapName);
                        
                        // If player starting location for current map set to (0, 0) -- not cached yet, then...
                        if ( start.isZero() )
                        {
                            
                            // Player starting location for current map set to (0, 0) -- not cached yet.
                            
                            // First visit to current map.
                            // Look for starting position in spawn layer.
                            
                            // Set player starting location to closest position based on spawn layer.
                            // Stores results in vector variable, _playerStartLocationTable.
                            setClosestStartPosition(_playerStart);
                            
                            // Copy player starting location to local variable.
                            start = _playerStartLocationTable.get(_currentMapName);
                            
                        }
                        
                        // Set starting location of player in current map.
                        _playerStart.set(start.x, start.y);
                        
                        /*
                        System.out.println("loadMap...");
                        System.out.println("_playerStart x: " + _playerStart.x + ", y: " + _playerStart.y);
                        System.out.println("_playerStartPositionRect x: " + _playerStartPositionRect.x + ", y: " + _playerStartPositionRect.y);
                        */

                    }

                    // Display starting location in current map.
                    Gdx.app.debug(TAG, "Player Start: (" + _playerStart.x + "," + _playerStart.y + ")");

                    // Flag map as successfully loaded.
                    loaded = true;
                    
                } // End ... If map asset successfully retrieved.
                
            } // End ... If TMX file successfully loaded into asset manager

            else
            {
                
                // TMX file failed to load into asset manager.
                
                // Display warning.
                Gdx.app.debug(TAG, "Map not loaded");
                
            }
            
        } // End ... If map key passed and exists in hash map (neither null nor empty).
        
        else
        {
            
            // Map key either not passed (null or empty) or missing from hash map.
            
            // Display warning.
            Gdx.app.debug(TAG, "Map is invalid");
            
        }
        
        // Return whether map loaded.
        return loaded;
        
    }

    // position = Vector with base player location, in pixels.
    private boolean setClosestStartPosition(final Vector2 position)
    {
        
        /*
        The method sets the player starting location to the closest position of a player object in the 
        spawn layer.  The method takes a base player location with pixel coordinates as a parameter.
        
        MLGD:
        The setClosestStartPosition() method will cache the closest spawn location to the player on the 
        current map.  This is used when the portal activation occurs in order to start the player in the 
        correct location when transitioning out of the new location, back to the previous location.
        
        MLGD:
        The method uses the dst2() method from the Vector2 class because, in general, when checking 
        distances between objects, we only care about the relative distance, not the absolute distance. 
        In order to get the absolute distance, we would need to take the square root of the value, and in 
        general, this is an expensive operation.
        */
        
        // Returns true when closest starting position found.
        // Returns false when closest starting position not found.
        
        boolean closestPos; // Whether closest starting position found.
        float distance; // Distance between current player location and spawn point.
        float shortestDistance; // Shortest distance found between current player location and any spawn point.
        
        // Set defaults.
        closestPos = false;
        shortestDistance = 0f;
        _playerStartPositionRect.set(0,0);
        _closestPlayerStartPosition.set(0,0);
        
        // Display base player location and current map name.
        Gdx.app.debug(TAG, "setClosestStartPosition INPUT: (" + position.x + "," + position.y + ") " + 
            _currentMapName);

        // Get last known position on the current map.
        // Go through all player start positions and choose closest to last known position.
        
        // Loop through spawn locations in current map.
        for( MapObject object: _spawnsLayer.getObjects())
        {
            
            // If looking at a player spawn location, then...
            if ( object.getName().equalsIgnoreCase(PLAYER_START) )
            {
                
                // Looking at a player spawn location.
                
                // Store current object location in player starting rectangle.
                // Sets values of _playerStartPositionRect vector.
                ((RectangleMapObject)object).getRectangle().getPosition(_playerStartPositionRect);
                
                /*
                System.out.println("setClosestStartPosition..." + temp);
                System.out.println("_playerStart x: " + _playerStart.x + ", y: " + _playerStart.y);
                System.out.println("_playerStartPositionRect x: " + _playerStartPositionRect.x + ", y: " + _playerStartPositionRect.y);
                */
                
                // Store distance between base player and object locations.
                distance = position.dst2(_playerStartPositionRect);

                // Display distance.
                Gdx.app.debug(TAG, "distance: " + distance + " for " + _currentMapName);

                // If distance less than shortest found so far or equal to zero, then...
                if ( distance < shortestDistance || shortestDistance == 0 )
                {
                    
                    // Distance less than shortest found so far or equal to zero.
                    
                    // Store object position in closest player starting location.
                    _closestPlayerStartPosition.set(_playerStartPositionRect);
                    
                    // Copy current to shortest distance.
                    shortestDistance = distance;
                    
                    // Flag closest starting position as found.
                    // A closer position may be located.
                    closestPos = true;                    
                    
                } // End ... If distance less than shortest found so far or equal to zero.
                
            } // End ... If looking at a player spawn location.
            
        } // End ... Loop through spawn locations in current map.
        
        // If closest starting position found, then...
        if (closestPos)
            
            // Closest starting position found.  Display coordinates and current map name.
            Gdx.app.debug(TAG, "closest START is: (" + _closestPlayerStartPosition.x + "," + 
              _closestPlayerStartPosition.y + ") " +  _currentMapName);
        
        // Store closest player starting location in hash map entry for current map.
        _playerStartLocationTable.put(_currentMapName, _closestPlayerStartPosition.cpy());
        
        // Return whether closest starting position found.
        return closestPos;
        
    }

    // position = Vector with base player location, in tiles (units).
    public boolean setClosestStartPositionFromScaledUnits(Vector2 position)
    {
        
        // The method sets the player starting location to the closest position of a player object in the 
        // spawn layer.  The method takes a base player location with tile (unit) coordinates as a 
        // parameter and converts to pixels.
        
        // Returns true when closest starting position found.
        // Returns false when closest starting position not found.
        
        /*
        if( UNIT_SCALE <= 0 )
            return;
        */
        
        // Convert base player location from tiles (units) to pixels.
        _convertedUnits.set(position.x / UNIT_SCALE, position.y / UNIT_SCALE);
        
        // Set player start location to the closest related position in the spawn layer.
        return setClosestStartPosition(_convertedUnits);
        
    }

}