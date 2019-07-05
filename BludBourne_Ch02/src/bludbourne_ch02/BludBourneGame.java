package bludbourne_ch02;

// LibGDX imports.
import com.badlogic.gdx.Gdx;

// LibGDX custom class imports.
import core.BaseGame;

// Local project imports.
import screens.MainGameScreen;

public class BludBourneGame extends BaseGame // Extends the BaseGame class.
{

    /*
    The class extends the basic functionality of a BaseGame class and creates a new GameScreen object
    with the BludBourne game.  The BludBourne game launches when the application starts.
    
    MLGD (Mastering LibGDX Game Development):
    The Game class is an abstract base class that wraps the ApplicationListener interface and delegates 
    the implementation of this interface to the Screen class.
    
    Methods include:
    
    create:  Sets up the skin and initializes and displays the title screen.
    createSkin:  Sets up the skin.
    dispose:  Occurs during the cleanup phase and clears objects from memory.
    disposeScreens:  Disposes of LibGDX objects in screens.
    */
    
    // Declare object variables.
    // public static final MainGameScreen _mainGameScreen = new MainGameScreen(this, 1, 1); // Reference to main game screen.
    public static MainGameScreen _mainGameScreen;
    
    // Declare regular variables.
    private final int windowWidth; // Width to use for stages.
    private final int windowHeight; // Height to use for stages.
    
    // windowWidth = Width to use for stages.
    // windowHeight = Height to use for stages.
    public BludBourneGame(int windowWidth, int windowHeight)
    {
        
        // The constructor sets the values for the starting width and height of the application window.
        
        // Store passed in class-level variables.
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        
    }
    
    @Override
    public void create() 
    {
        
        // The function sets up the skin and initializes and displays the main game screen.
        // The function is automatically called by the superclass.
        
        // Set up the skin.
        // createSkin();
        
        // Initialize introduction screen object.
        _mainGameScreen = new MainGameScreen(this, windowWidth, windowHeight);
        
        // MLGD:  The setScreen() method will check to see whether a screen is already currently active. 
        // If the current screen is already active, then it will be hidden, and the screen that was passed 
        // into the method will be shown.
        
        // Initialize and display the main game screen.
        setScreen(_mainGameScreen);
        
    }
    
    @Override
    public void dispose()
    {

        // The function occurs during the cleanup phase and clears objects from memory.
        // The function also disposes of additional LibGDX ojects, such as those related to sounds and music.
        // The function also clears memory related to the asset manager.
        
        // Dispose objects in screens from memory.
        disposeScreens();
        
        // Dispose of sound and music objects.
        // sounds.disposeAudio();
        
        // Clear LibGDX asset manager.
        // assetMgr.disposeAssetMgr();
        
        // Clear objects from memory.
        super.dispose();
        
        // Follow the LibGDX contention of exiting the game by using the following statement when quitting.
        // The function calls into the static instance of the application object, setting the running state 
        // of the game loop to false and subsequently moving to the next step, allowing the graceful exit of 
        // the game.
        Gdx.app.exit();

    }
    
    public void disposeScreens()
    {
        
        // The function disposes of LibGDX objects in screens.
        
        // Dispose of LibGDX objects related to main game screen.
        _mainGameScreen.dispose();
        
    }
    
}