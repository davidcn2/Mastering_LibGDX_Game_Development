package bludbourne_ch02;

// LibGDX imports.
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class BludBourne_Ch02 
{
    
    /**
    * The class contains the entry / starting point for the application.
    * <br><br>
    * The LwjglApplication class will bootstrap itself with the starter class instance and the 
    * configuration passed into its constructor.  The LwjglApplication constructor will instantiate the 
    * various subsystem modules used in LibGDX, populating the Gdx (com.badlogic.gdx) environment class 
    * with static instances of the Application, Graphics, Audio, Input, Files, and Net modules.  The 
    * LwjglApplication constructor will then spawn a thread, acting as the main loop, that will run until 
    * the game exits.  The main loop thread represents the lifecycle of the game.
    * <br><br>
    * The Application class holds responsibility for setting up a window, handling resize events, rendering 
    * to the surfaces, and managing the application during its lifetime.  Specifically, Application will 
    * provide the modules for dealing with graphics, audio, input and file IO handling, logging facilities, 
    * memory footprint information, and hooks for extension libraries.
    * <br><br>
    * The LwjglApplication class acts as the backend implementation of the Application interface.  The 
    * backend package that LibGDX uses for the desktop is called LWJGL.  The interface becomes the entry point 
    * that the platform OS uses to load the game.  The LwjglApplicationConfiguration class provides a single 
    * point of reference for all the properties associates with the game on the desktop.
    * <br><br>
    * Per Mastering LibGDX Game Development, ...<br>
    * "The Application class is responsible for setting up a window, handling resize events, rendering to 
    * the surfaces, and managing the application during its lifetime.  Specifically, Application will 
    * provide the modules for dealing with graphics, audio, input and file I/O handling, logging facilities, 
    * memory footprint information, and hooks for extension libraries.
    * <br><br>
    * The Gdx class is an environment class that holds static instances of the Application, Graphics, Audio, 
    * Input, Files, and Net modules as a convenience for access throughout the game.  The LwjglApplication 
    * class is the backend implementation of the Application interface for the desktop.  The backend package 
    * that LibGDX uses for the desktop is called LWJGL.  This implementation for the desktop will provide 
    * cross-platform access to native APIs for OpenGL.  This interface becomes the entry point that the
    * platform OS uses to load your game.
    */

    /**
     * 
     * The function configures and launches the application.
     * 
     * @param args  Command-line arguments as an array of String objects.
     */
    
    // args = Command-line arguments as an array of String objects.
    public static void main(String[] args) 
    {
        
        // The function configures and launches the application.
        
        final int windowWidth = 800; // Starting width of application window.
        final int windowHeight = 600; // Starting height of application window.
        
        Application app; // LibGDX application object.
        LwjglApplicationConfiguration config; // LibGDX application configuration object.
        
        // Create application configuration object.
        config = new LwjglApplicationConfiguration();

        /* MLGD (Mastering LibGDX Game Development):
        The config object is an instance of the LwjglApplicationConfiguration class
        where we can set top-level game configuration properties, such as the title to display
        on the display window, as well as display window dimensions
        */
        
	// Change configuration settings.
	config.width = windowWidth; // Set width of application window.
	config.height = windowHeight; // Set height of application window.
	config.title = "BludBourne"; // Set title of application.
        config.forceExit = false; // Prevent default behavior of LWJGL 2 backend calling System.exit(-1).
        config.resizable = false; // Prevent maximizing and resizing of screen.
        // config.vSyncEnabled = false;
        
        /* MLGD (Mastering LibGDX Game Development):
        The useGL30 property is set to false so that we use the much more stable and mature implementation
        of OpenGL ES, version 2.0.
        */
        config.useGL30 = false; // No need for OpenGL ES 3.0 support.  Disable to avoid compatibility issues.
        
        //config.samples = 4; // Adjust sampling rate to improve anti-aliasing.
        
        // Launch game using configuration settings.
	// app = new LwjglApplication(new BludBourneGame(windowWidth, windowHeight), config);
        app = new LwjglApplication(new BludBourneGame(windowWidth, windowHeight), config);
        
        // Store object reference in the Gdx class.
        Gdx.app = app;
        
        /* MLGD (Mastering LibGDX Game Development):
        There are four values for the logging levels that represent various degrees of granularity for 
        application level messages output to standard out. 
        
        1.  LOG_NONE is a logging level where no messages are output.
        2.  LOG_ERROR will only display error messages.
        3.  LOG_INFO will display all messages that are not debug-level messages.
        4.  LOG_DEBUG is a logging level that displays all messages.
        */
        
        // Set application to pass through all logging messages.
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        
    }
    
}
