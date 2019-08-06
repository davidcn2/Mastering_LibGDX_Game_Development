package bludbourne_ch02;

// LibGDX imports.
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

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

public final class Utility 
{
    
    /**
    * The class contains asset manager helper methods.
    * <br><br>
    * MLGD (Mastering LibGDX Game Development):
    * <br><br>
    * A.  The TextureLoader class represents the asset loader for textures.  One note is that
    * the texture data is loaded by default asynchronously, so make sure before you use
    * an asset with TextureLoader that it has been loaded.
    * <br><br>
    * B.  The InternalFileHandleResolver class is a nice convenience class for managing file 
    * handles when resolving paths with assets relative to the current working directory.
    * <br><br>
    * C.  The TiledMap class inherits from the Map class that is a generic map implementation.
    * The Map base class only contains map properties that describe general attributes and
    * map layers. The TiledMap class extends this functionality with additional fields for
    * tiles and tilesets. These tiles are stored as a 2D array of cells that contain references
    * to the tile as well as rotation and flip attributes.
    * <br><br>
    * D.  The TmxMapLoader class is a convenience class for loading TMX-based tilemaps
    * and storing them as the TiledMap instances.
    * <br><br>
    * E.  The AssetManager class manages the loading and storing of assets such as textures,
    * bitmap fonts, particle effects, pixmaps, UI skins, tile maps, sounds, and music.
    */

    /*
    Methods include:

    getMapAsset:  Returns the specified TiledMap object that exists in the asset manager.
    getTextureAsset:  Returns the specified Texture object that exists in the asset manager.
    isAssetLoaded:  Return a boolean value on whether the (passed) asset is currently loaded.
    isPopulatedText:  Returns whether text parameter populated -- length greater than zero (and not null).
    loadCompleted:   Wraps the progress of AssetManager as a percentage of completion.
    loadMapAsset:  Loads the (passed) TMX file as a TiledMap asset in the manager.
    loadTextureAsset:  Loads the (passed) image file as a Texture asset in the manager.
    numberAssetsQueued:  Wraps the number of assets left to load from the AssetManager queue.
    unloadAsset:  Unloads the passed asset from memory used by the asset manager.
    updateAssetLoading:  Wraps the update call in AssetManager.
    */
    
    // Declare constants.
    private static final String TAG = Utility.class.getSimpleName(); // Class name.
    
    // Declare object variables.
    
    /** {@link AssetManager}
     * Loads and stores assets like textures, bitmap fonts, tile maps, sounds, music, ... */
    public static final AssetManager ASSET_MANAGER = new AssetManager();
    
    /** {@link InternalFileHandleResolver}
     * Convenience object for managing file handles when resolving paths with assets relative to the current
     * working directory. */
    private static final InternalFileHandleResolver FILE_PATH_RESOLVER =  new InternalFileHandleResolver();
    
    // No constructor exists.
    
    // Getters and setters below...
    
    /**
     * 
     * The isAssetLoaded() method wraps the AssetManager method isLoaded() and will return a simple
     * Boolean value on whether the asset is currently loaded.
     * 
     * @param fileName  Name of asset to check whether loaded in asset manager.
     * @return  Whether the passed asset is currently loaded in asset manager.
     */
    
    // fileName = Name of asset to check whether loaded in asset manager.
    public static boolean isAssetLoaded(String fileName)
    {
        // The isAssetLoaded() method wraps the AssetManager method isLoaded() and will return a simple
        // Boolean value on whether the asset is currently loaded.
        return ASSET_MANAGER.isLoaded(fileName);
    }
    
    /**
     * 
     * The loadCompleted() method wraps the progress of AssetManager as a percentage of completion.
     * This can be used to update progress meter values when loading asynchronously.
     * 
     * @return  Progress of AssetManager loading -- as a percentage of completion.
     */
    public static float loadCompleted()
    {
        // The loadCompleted() method wraps the progress of AssetManager as a percentage of completion.
        // This can be used to update progress meter values when loading asynchronously.
        return ASSET_MANAGER.getProgress();
    }
    
    /**
     * 
     * The numberAssetsQueued() method wraps the number of assets left to load from the AssetManager queue.
     * 
     * @return  Number of assets left to load from the AssetManager queue.
     */
    public static int numberAssetsQueued()
    {
        // The numberAssetsQueued() method wraps the number of assets left to load from the AssetManager 
        // queue.
        return ASSET_MANAGER.getQueuedAssets();
    }
    
    /**
     * 
     * The updateAssetLoading() wraps the update call in AssetManager and can be called in a render() loop, 
     * if loading assets asynchronously in order to process the preload queue.
     * 
     * @return  Whether finished loading assets.
     */
    public static boolean updateAssetLoading()
    {
        // The updateAssetLoading() wraps the update call in AssetManager and can be called in a render() 
        // loop, if loading assets asynchronously in order to process the preload queue.
        return ASSET_MANAGER.update();
    }
    
    // Methods below...
    
    /**
     * 
     * The procedure returns the specified TiledMap object that exists in the asset manager.
     * 
     * @param mapFilenamePath  Filename for Tiled map object to retrieve from asset manager.
     * @return  The TiledMap object in the asset manager corresponding to the passed value in mapFilenamePath.
     */
    
    // mapFilenamePath = Filename for Tiled map object to retrieve from asset manager.
    public static TiledMap getMapAsset(String mapFilenamePath)
    {
        
        // The procedure returns the specified Tiled map object that exists in the asset manager.
        
        TiledMap map; // TiledMap object to load.

        // Set defaults.
        map = null;
        
        // If map object loaded into asset manager, then...
        if ( ASSET_MANAGER.isLoaded(mapFilenamePath) )
        {
            
            // Map object exists in asset manager.
            
            // Get the map object from the asset manager. 
            map = ASSET_MANAGER.get(mapFilenamePath,TiledMap.class);
            
        }

        else 
        {
            
            // Map object missing from asset manager.
            
            // Display warning related to Map not existing in asset manager.
            Gdx.app.debug(TAG, "Map is not loaded: " + mapFilenamePath );
            
        }

        // Return Tiled map object (or null if not loaded).
        return map;
        
    }
    
    /**
     * 
     * The procedure returns the specified Texture object that exists in the asset manager.
     * <br><br>
     * Example of textureFilenamePath = assets/sprites/characters/Warrior.png.
     * <br><br> 
     * The Texture object will include the entire image, consisting of one to many
     * different sprites.  In order to reference a specific sprite (for rendering), 
     * TextureRegion can be used to get access to a subregion of the Texture object.
     * 
     * @param textureFilenamePath  Key to texture to retrieve from asset manager.  Path of texture when loaded.
     * @return  The Texture object in the asset manager corresponding to the passed value in textureFilenamePath.
     */
    
    // textureFilenamePath = Key to texture to retrieve from asset manager.  Path of texture when loaded.
    public static Texture getTextureAsset(String textureFilenamePath)
    {
        
        /*
        The procedure returns the specified Texture object that exists in the asset manager.
        
        Example of textureFilenamePath = assets/sprites/characters/Warrior.png.
        
        The Texture object will include the entire image, consisting of one to many
        different sprites.  In order to reference a specific sprite (for rendering), 
        TextureRegion can be used to get access to a subregion of the Texture object.
        */
        
        Texture texture; // Texture object to load.

        // Set defaults.
        texture = null;
        
        // If Texture object loaded into asset manager, then...
        if ( ASSET_MANAGER.isLoaded(textureFilenamePath) )
        {
            
            // Texture object exists in asset manager.
            
            // Get the Texture object from the asset manager.
            texture = ASSET_MANAGER.get(textureFilenamePath, Texture.class);
            
        }
        
        else
        {
            
            // Texture object missing from asset manager.
            
            // Display warning related to Texture not existing in asset manager.
            Gdx.app.debug(TAG, "Texture is not loaded: " + textureFilenamePath );
            
        }

        // Return Texture object (or null if not loaded).
        return texture;
        
    }
    
    /**
     * 
     * The function returns whether text parameter populated -- length greater than zero (and not null).
     * 
     * @param text  Text to check whether populated.
     * @return  Whether text parameter populated -- length greater than zero (and not null).
     */
    
    // text = Text to check whether populated.
    public static boolean isPopulatedText(String text)
    {
        // The function returns whether text parameter populated -- length greater than zero (and not null).
        return text != null && !text.isEmpty();
    }
    
    /**
     * 
     * The loadMapAsset() method will take a TMX filename path relative to the working
     * directory.  The method loads the TMX file into the asset manager as a TiledMap 
     * asset, blocking until finished.  We can load these assets later asynchronously 
     * once we create a screen with a progress bar, instead of blocking on the render
     * thread.
     * <br><br>
     * Pairs with method, getMapAsset().
     * 
     * @param mapFilenamePath  Name of tmx file (relative to working directory) to load as a TiledMap asset.
     */
    
    // mapFilenamePath = Name of tmx file (relative to working directory) to load as a TiledMap asset.
    public static void loadMapAsset(String mapFilenamePath)
    {
        
        /*
        The loadMapAsset() method will take a TMX filename path relative to the working
        directory.  The method loads the TMX file into the asset manager as a TiledMap 
        asset, blocking until finished.  We can load these assets later asynchronously 
        once we create a screen with a progress bar, instead of blocking on the render
        thread.
        
        Pairs with method, getMapAsset().
        */
        
        // If name of tmx file passed (neither null nor empty), then...
        if ( isPopulatedText(mapFilenamePath) )
        {
            
            // Name of tmx file passed (neither null nor empty).
            
            // If passed file found, then...
            if ( FILE_PATH_RESOLVER.resolve(mapFilenamePath).exists() )
            {

                // Passed file found.
                
                // Load asset.
                
                // Assign custom asset loader to manager for the TileMap class.
                ASSET_MANAGER.setLoader(TiledMap.class, new TmxMapLoader(FILE_PATH_RESOLVER));
                
                // Add the given asset to the loading queue of the asset manager.
                ASSET_MANAGER.load(mapFilenamePath, TiledMap.class);

                // Until we add loading screen, just block until we load the map.
                ASSET_MANAGER.finishLoadingAsset(mapFilenamePath);
                
                // Display message about successful loading of (map) asset.
                Gdx.app.debug( TAG, "Map loaded!: " + mapFilenamePath );

            }

            else
            {
                
                // Passed file missing.
                
                // Display warning.
                Gdx.app.debug( TAG, "Map doesn't exist!: " + mapFilenamePath );
                
            }
        
        } // Name of tmx file passed (neither null nor empty).
        
    }
    
    /**
     * 
     * The loadTextureAsset() method takes an image filename path relative to the
     * working directory.  The method loads the image file into the asset manager
     * as a Texture asset, blocking until finished.
     * <br><br> 
     * The Texture object will include the entire image, consisting of one to many
     * different sprites.  In order to reference a specific sprite (for rendering), 
     * TextureRegion can be used to get access to a subregion of the Texture object.
     * <br><br> 
     * Pairs with method, getTextureAsset().
     * 
     * @param textureFilenamePath  Filename of image to load into Texture asset.  Key of texture in asset manager.
     */
    
    // textureFilenamePath = Filename of image to load into Texture asset.  Key of texture in asset manager.
    public static void loadTextureAsset(String textureFilenamePath)
    {
        
        /*
        The loadTextureAsset() method takes an image filename path relative to the
        working directory.  The method loads the image file into the asset manager
        as a Texture asset, blocking until finished.
        
        The Texture object will include the entire image, consisting of one to many
        different sprites.  In order to reference a specific sprite (for rendering), 
        TextureRegion can be used to get access to a subregion of the Texture object.
        
        Pairs with method, getTextureAsset().
        */
        
        // If name of image file passed (neither null nor empty), then..
        if ( isPopulatedText(textureFilenamePath) )
        {
            
            // Name of image file passed (neither null nor empty).
            
            // If passed file found, then...
            if ( FILE_PATH_RESOLVER.resolve(textureFilenamePath).exists() )
            {

                // Passed file found.
                
                // Load asset.
                
                // Assign custom asset loader to manager for the Texture class.
                ASSET_MANAGER.setLoader(Texture.class, new TextureLoader(FILE_PATH_RESOLVER));
                
                // Add the given asset to the loading queue of the asset manager.
                ASSET_MANAGER.load(textureFilenamePath, Texture.class);

                // Until we add loading screen, just block until we load the texture.
                ASSET_MANAGER.finishLoadingAsset(textureFilenamePath);

            }

            else
            {
                
                // Passed file missing.
                
                // Display warning.
                Gdx.app.debug(TAG, "Texture doesn't exist!: " + textureFilenamePath );
                
            }
            
        } // Name of image file passed (neither null nor empty).
        
    }
    
    /**
     * 
     * The unloadAsset() method is a helper method that takes advantage of the fact that
     * there is one static instance of AssetManager for all game assets.  This method will
     * check to see whether the asset is loaded, and if it is, then it will unload the asset from
     * memory.
     * 
     * @param assetFilenamePath  Asset to unload from memory / the asset manager.
     */
    
    // assetFilenamePath = Asset to unload from memory / the asset manager.
    public static void unloadAsset(String assetFilenamePath)
    {
        
        /*
        The unloadAsset() method is a helper method that takes advantage of the fact that
        there is one static instance of AssetManager for all game assets.  This method will
        check to see whether the asset is loaded, and if it is, then it will unload the asset from
        memory.
        */
        
        // If asset loaded into manager, then...
        if ( ASSET_MANAGER.isLoaded(assetFilenamePath) )
        
            {
            /*
            The unload() method of AssetManager will check the dependencies with a given asset, and 
            once the reference counter hits zero, call dispose() on the asset and remove it from the 
            manager.
            */
            ASSET_MANAGER.unload(assetFilenamePath);
            } 
        
        else 
        
            {
            // Asset not loaded into manager.
            Gdx.app.debug(TAG, "Asset is not loaded; Nothing to unload: " + assetFilenamePath );
            }
        
    }

}