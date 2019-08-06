package bludbourne_ch02;

// LibGDX imports.
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

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

// Implements InputProcessor interface from LibGDX.
public class PlayerController implements InputProcessor
{
    
    /**
    * The class implements InputProcessor and manages the player input and controls on the screen.
    * PlayerController is responsible for handling all of the input events and providing mechanisms 
    * to process the events in the queue.  InputProcessor is an interface that should be implemented
    * in order to process input events, such as mouse cursor location changes, mouse button presses, 
    * and keyboard key presses from the input event handler.
    */
    
    /*
    Methods include:

    dispose:  The function clears resources associated with the input processor from memory.
    doActionMouseButtonPressed:  The function handles caching of the event for pressing the right mouse button.
    doActionMouseButtonReleased:  The function handles caching of the event for releasing the right mouse button.
    downPressed:  The function handles caching of the event for pressing the down "key".
    downReleased:  The function handles caching of the event for releasing the down "key".
    hide:  The function, associated with hiding a screen, disables related key processing, setting all cached 
        values to false.
    keyDown:  The function gets called when the user presses a key.  The function caches the relevant keypress 
        event(s) in the keys hashmap as active.
    keyTyped:  The function occurs when the user types a key.
    keyUp:  The function gets called when the user releases a key.  The function caches the relevant keypress 
        event(s) in the keys hashmap as inactive.
    leftPressed:  The function handles caching of the event for pressing the left "key".  Results in updating
        Keys.LEFT entry in keys hash map to true.
    leftReleased:  The function handles caching of the event for releasing the left "key".  Results in updating 
        Keys.LEFT entry in keys hash map to false.
    mouseMoved:  The function occurs when the user moves the mouse without pressing any buttons.
    processInput:  In the function, processing of the cached values of the keyboard and mouse input occurs.  
        First, calculation of the next position occurs, as explained in the previous section, in order to 
        avoid issues with two fast-moving game objects colliding and missing a collision check.  Then, the 
        state and direction of the player character are set.
    quitPressed:  The function handles caching of the event for pressing the Q "key" -- for quitting the game.
        Results in updating Keys.QUIT entry in keys hash map to true.
    quitReleased:  The function handles caching of the event for releasing the Q "key".  Results in updating 
        Keys.QUIT entry in keys hash map to false.
    rightPressed:  The function handles caching of the event for pressing the right "key".  Results in updating 
        Keys.RIGHT entry in keys hash map to true.
    rightReleased:  The function handles caching of the event for releasing the right "key".  Results in 
        updating Keys.RIGHT entry in keys hash map to false.
    scrolled:   The function occurs when the user scrolls the mouse wheel.
    selectMouseButtonPressed:  The function handles caching of the event for pressing the left mouse button.
        Results in updating Mouse.SELECT entry in mouseButtons hash map to true.
    selectMouseButtonReleased:  The function handles caching of the event for releasing the left mouse button.
        Results in updating Mouse.SELECT entry in mouseButtons hash map to false.
    setClickedMouseCoordinates:  The function stores in the Vector3 variable, lastMouseCoordinates, the x and 
        y coordinates where the user touched the screen, basing the origin in the upper left corner.
    touchDown:  The function occurs when the user touches the screen or presses a mouse button.  When clicking 
        the left or right button, the function stores the mouse coordinates.  Clicking the left button results 
        in updating the Mouse.SELECT entry in mouseButtons hash map to true.  Clicking the right button results 
        in updating the Mouse.DOACTION entry in mouseButtons hash map to true.
    touchDragged:  The function occurs when the user drags a finger across the screen or the mouse.
    touchUp:  The function occurs when the user lifts a finger or released a mouse button.  Clicking the left 
        button results in updating the Mouse.SELECT entry in mouseButtons hash map to false.  Clicking the 
        right button results in updating the Mouse.DOACTION entry in mouseButtons hash map to false.
    upPressed:  The function handles caching of the event for pressing the up "key".  Results in updating 
        Keys.UP entry in keys hash map to true.
    upReleased:  The function handles caching of the event for releasing the up "key".  Results in updating 
        Keys.UP entry in keys hash map to false.
    update:  The function gets called as part of the render() function in screens and processes cached keyboard 
        and mouse input.
    */

    // Declare constants.
    private final static String TAG = PlayerController.class.getSimpleName(); // Class name.

    // Declare enumerations.
    
    /** The enumeration stores values associated with key presses. */
    enum Keys
    {
        LEFT, RIGHT, UP, DOWN, QUIT
    }

    /** The enumeration stores values associated with mouse clicks (left = select, right = do action). */
    enum Mouse
    {
        SELECT, DOACTION
    }

    // Declare list variables.
    
    /** {@link Keys} 
     * Cached values for key presses. */
    private static HashMap<Keys, Boolean> keys; // = new HashMap<PlayerController.Keys, Boolean>();
    
    /** {@link MouseButtons} 
     * Cached values for mouse clicks. */
    private static HashMap<Mouse, Boolean> mouseButtons; // = new HashMap<PlayerController.Mouse, Boolean>();
    
    // Declare object variables.
    
    /** {@link LastMouseCoordinates} 
     * Coordinates (x and y) where the user touched the screen, basing the origin in the upper left corner. */
    private final Vector3 lastMouseCoordinates;
    
    /** {@link Player} 
     * Reference to the class containing information and methods related to the player. */
    private final Entity _player;

    // Constructors below...

    /**
     * 
     * The constructor initializes variables.
     * 
     * @param player  Reference to the entity class for the player.
     */
    
    // player = Reference to the entity class for the player.
    public PlayerController(Entity player)
    {
        
        // The constructor initializes variables.
        
        // Gdx.app.debug(TAG, "Construction" );
        
        // Initialize variables.
        this.lastMouseCoordinates = new Vector3();
        this._player = player;
        
        // Initialize hash maps.
        keys = new HashMap<>(); // <PlayerController.Keys, Boolean>
        mouseButtons = new HashMap<>(); // <PlayerController.Mouse, Boolean>
        
        // Initialize hash maps related to inputs (key presses and mouse clicks).
        keys.put(Keys.LEFT, false); // Whether user pressed left arrow or A key.
        keys.put(Keys.RIGHT, false); // Whether user pressed right arrow or D key.
        keys.put(Keys.UP, false); // Whether user pressed up arrow or W key.
        keys.put(Keys.DOWN, false); // Whether user pressed down arrow or S key.
        keys.put(Keys.QUIT, false); // Whether user pressed Q key (for quitting game).
        mouseButtons.put(Mouse.SELECT, false); // Whether user clicked left mouse button.
        mouseButtons.put(Mouse.DOACTION, false); // Whether user clicked right mouse button.
        
    }

    // Overriden methods below...
    
    /*
    The keyDown() and keyUp() pair of methods will process specific key presses and releases, 
    respectively, by caching them in a Hashtable object.  The Hashtable allows for processing 
    the input later, without losing keyboard key press or release events, and appropriately 
    removing redundant key events from the queue.
    */
    
    /**
     * 
     * The function gets called when the user presses a key.  The function caches the relevant key press
     * event(s) in the keys hashmap as active.
     * 
     * @param keycode  Code for key pressed.
     * @return  Whether the InputProcessor handled the input (true or false).
     */
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        /*
        The function gets called when the user presses a key.  The function caches the relevant key press 
        event(s) in the keys hashmap as active.
        
        Possible actions include:
        
        1.  Pressing the left arrow or A key.
        2.  Pressing the right arrow or D key.
        3.  Pressing the up arrow or W key.
        4.  Pressing the down arrow or S key.
        5.  Pressing the Q key -- quits the game.
        */
        
        boolean returnValue; // Whether the InputProcessor handled the input.
        
        // Set defaults.
        returnValue = false;
        
        // If user pressed the left arrow or A key, then...
        if ( keycode == Input.Keys.LEFT || keycode == Input.Keys.A )
        {
            // User pressed the left arrow or A key.
            
            // Call method related to pressing left "key".
            // Results in updating Keys.LEFT entry in keys hash map to true.
            this.leftPressed();
            
            // Flag as true, since input handled.
            returnValue = true;
        }
        
        // If user pressed the right arrow or D key, then...
        if ( keycode == Input.Keys.RIGHT || keycode == Input.Keys.D )
        {
            // User pressed the right arrow or D key.
            
            // Call method related to pressing right "key".
            // Results in updating Keys.RIGHT entry in keys hash map to true.
            this.rightPressed();
            
            // Flag as true, since input handled.
            returnValue = true;
        }
        
        // If user pressed the up arrow or W key, then...
        if ( keycode == Input.Keys.UP || keycode == Input.Keys.W )
        {
            // User pressed the up arrow or W key.
            
            // Call method related to pressing up "key".
            // Results in updating Keys.UP entry in keys hash map to true.
            this.upPressed();
            
            // Flag as true, since input handled.
            returnValue = true;
        }
        
        // If user pressed the down arrow or S key, then...
        if ( keycode == Input.Keys.DOWN || keycode == Input.Keys.S )
        {
            // User pressed the down arrow or S key.
            
            // Call method related to pressing down "key".
            // Results in updating Keys.DOWN entry in keys hash map to true.
            this.downPressed();
            
            // Flag as true, since input handled.
            returnValue = true;
        }
        
        // If user pressed the Q key, then...
        if ( keycode == Input.Keys.Q )
        {
            // User pressed the Q key.
            
            // Call method related to pressing Q key -- results in quitting game.
            this.quitPressed();
            
            // Flag as true, since input handled.
            returnValue = true;
        }

        // Return whether input handled.
        return returnValue;
        
    }

    /**
     * 
     * The function occurs when the user types a key.
     * 
     * @param character  Code related to the key typed.   One of the constants in Input.Keys.
     * @return  Whether the InputProcessor handled the input (true or false).
     */
    
    // keycode = Code related to the key typed.   One of the constants in Input.Keys.
    @Override
    public boolean keyTyped(char character)
    {
        // The function occurs when the user types a key.
        
        // Return the value indicating no handling of input.
        return false;
    }
    
    /**
     * 
     * The function gets called when the user releases a key.  The function caches the relevant key press
     * event(s) in the keys hashmap as inactive.
     * 
     * @param keycode  Code related to the key released.   One of the constants in Input.Keys.
     * @return  Whether the InputProcessor handled the input (true or false).
     */
    
    // keycode = Code related to the key released.   One of the constants in Input.Keys.
    @Override
    public boolean keyUp(int keycode)
    {
        
        /*
        The function gets called when the user releases a key.  The function caches the relevant key press
        event(s) in the keys hashmap as inactive.
        
        Possible actions include:
        
        1.  Releasing the left arrow or A key.
        2.  Releasing the right arrow or D key.
        3.  Releasing the up arrow or W key.
        4.  Releasing the down arrow or S key.
        5.  Releasing the Q key.
        */
        
        boolean returnValue; // Whether the InputProcessor handled the input.
        
        // Set defaults.
        returnValue = false;
        
        // If user released the left arrow or A key, then...
        if( keycode == Input.Keys.LEFT || keycode == Input.Keys.A )
        {
            // User released the left arrow or A key.
            
            // Call method related to releasing left "key".
            // Results in updating Keys.LEFT entry in keys hash map to false.
            this.leftReleased();
            
            // Flag as true, since input handled.
            returnValue = true;
        }
        
        // If user released the right arrow or D key, then...
        if ( keycode == Input.Keys.RIGHT || keycode == Input.Keys.D )
        {
            // User released the right arrow or D key.
            
            // Call method related to releasing right "key".
            // Results in updating Keys.RIGHT entry in keys hash map to false.
            this.rightReleased();
            
            // Flag as true, since input handled.
            returnValue = true;
        }
        
        // If user released the up arrow or W key, then...
        if ( keycode == Input.Keys.UP || keycode == Input.Keys.W )
        {
            // User released the up arrow or W key.
            
            // Call method related to releasing up "key".
            // Results in updating Keys.UP entry in keys hash map to false.
            this.upReleased();
            
            // Flag as true, since input handled.
            returnValue = true;
        }
        
        // If user released the down arrow or S key, then...
        if ( keycode == Input.Keys.DOWN || keycode == Input.Keys.S )
        {
            // User released the down arrow or S key.
            
            // Call method related to releasing down "key".
            // Results in updating Keys.DOWN entry in keys hash map to false.
            this.downReleased();
            
            // Flag as true, since input handled.
            returnValue = true;
        }
        
        // If user released the Q key, then...
        if ( keycode == Input.Keys.Q )
        {
            // User released the Q key.
            
            // Call method related to releasing Q key.
            this.quitReleased();
            
            // Flag as true, since input handled.
            returnValue = true;
        }
        
        // Return whether input handled.
        return returnValue;
        
    }

    /**
     * 
     * The function occurs when the user moves the mouse without pressing any buttons.
     * Notes:  The function does not get called on iOS.
     * 
     * @param screenX  Current X coordinate in the screen of the mouse.
     * @param screenY  Current Y coordinate in the screen of the mouse.
     * @return  Whether the InputProcessor handled the input (true or false).
     */
    
    // screenX = Current X coordinate in the screen of the mouse.
    // screenY = Current Y coordinate in the screen of the mouse.
    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        // The function occurs when the user moves the mouse without pressing any buttons.
        // Notes:  The function does not get called on iOS.

        // Return the value indicating no handling of input.
        return false;
    }
    
    /**
     * 
     * The function occurs when the user scrolls the mouse wheel.
     * Notes:  The function does not get called on iOS.
     * 
     * @param amount  The scroll amount, -1 or 1, depending on the direction the user scrolled the wheel.
     * @return  Whether the InputProcessor handled the input (true or false).
     */
    
    // amount = The scroll amount, -1 or 1, depending on the direction the user scrolled the wheel.
    @Override
    public boolean scrolled(int amount)
    {
        // The function occurs when the user scrolls the mouse wheel.
        // Notes:  The function does not get called on iOS.

        // Return the value indicating no handling of input.
        return false;
    }
    
    /*
    The touchDown() and touchUp() pair of methods will process specific mouse button
    presses and releases, respectively, by caching the position in a Hashtable object. 
    The Hashtable object allows for processing the input later, without losing mouse 
    button press or release events and appropriately removing redundant mouse press 
    events from the queue.
    */
    
    /**
     * 
     * The function occurs when the user touches the screen or presses a mouse button.
     * <br>When clicking the left or right button, the function stores the mouse coordinates.
     * <br>Clicking the left button results in updating the Mouse.SELECT entry in mouseButtons hash map to true.
     * <br>Clicking the right button results in updating the Mouse.DOACTION entry in mouseButtons hash map to true.
     * <br>
     * <br>Notes:  The button parameter will be Input.Buttons.LEFT on iOS.
     * 
     * @param screenX  The x coordinate where the user touched the screen, basing the origin in the upper left corner.
     * @param screenY  The y coordinate where the user touched the screen, basing the origin in the upper left corner.
     * @param pointer  Pointer for the event.
     * @param button  Button pressed.
     * @return  Whether the InputProcessor handled the input (true or false).
     */
    
    // screenX = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
    // screenY = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
    // pointer = Pointer for the event.
    // button = Button pressed.
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        
        /*
        The function occurs when the user touches the screen or presses a mouse button.
        When clicking the left or right button, the function stores the mouse coordinates.
        Clicking the left button results in updating the Mouse.SELECT entry in mouseButtons hash map to true.
        Clicking the right button results in updating the Mouse.DOACTION entry in mouseButtons hash map to true.
        
        Notes:  The button parameter will be Input.Buttons.LEFT on iOS.
        */
        
        boolean returnValue; // Whether the InputProcessor handled the input.
        
        // Set defaults.
        returnValue = false;
        
        // Gdx.app.debug(TAG, "GameScreen: MOUSE DOWN........: (" + screenX + "," + screenY + ")" );
        
        // If user clicked left or right button pressed on mouse, then...
        if ( button == Input.Buttons.LEFT || button == Input.Buttons.RIGHT )
        {
            // User clicked left or right button pressed on mouse.
            
            // Store in Vector3 variable, lastMouseCoordinates, the x and y coordinates where the user 
            // touched the screen, basing the origin in the upper left corner.
            this.setClickedMouseCoordinates(screenX, screenY);
            
            // Flag as true, since input handled.
            returnValue = true;
        }

        // Buttons ... Left is selection, right is context menu.
        
        // If user clicked left mouse button, then...
        if ( button == Input.Buttons.LEFT )
        {
            // User clicked left mouse button.
            
            // Call method related to pressing left mouse button.
            // Results in updating Mouse.SELECT entry in mouseButtons hash map to true.
            this.selectMouseButtonPressed(screenX, screenY);
        }
        
        // If user clicked right mouse button, then...
        if ( button == Input.Buttons.RIGHT )
        {
            // User clicked right mouse button.
            
            // Call method related to pressing right mouse button.
            // Results in updating Mouse.DOACTION entry in mouseButtons hash map to true.
            this.doActionMouseButtonPressed(screenX, screenY);
        }
        
        // Return whether input handled.
        return returnValue;
        
    }

    /**
     * 
     * <br>The function occurs when the user lifts a finger or released a mouse button.
     * <br>Clicking the left button results in updating the Mouse.SELECT entry in mouseButtons hash map to false.
     * <br>Clicking the right button results in updating the Mouse.DOACTION entry in mouseButtons hash map to
     * <br>false.
     * <br>
     * <br>Notes:  The button parameter will be Input.Buttons.LEFT on iOS.
     * 
     * @param screenX  The x coordinate where the user touched the screen, basing the origin in the upper left corner.
     * @param screenY  The y coordinate where the user touched the screen, basing the origin in the upper left corner.
     * @param pointer  Pointer for the event.
     * @param button  Button pressed.
     * @return  Whether the InputProcessor handled the input (true or false).
     */
    
    // screenX = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
    // screenY = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
    // pointer = Pointer for the event.
    // button = Button pressed.
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        
        /*
        The function occurs when the user lifts a finger or released a mouse button.
        Clicking the left button results in updating the Mouse.SELECT entry in mouseButtons hash map to false.
        Clicking the right button results in updating the Mouse.DOACTION entry in mouseButtons hash map to
        false.
        
        Notes:  The button parameter will be Input.Buttons.LEFT on iOS.
        */
        
        boolean returnValue; // Whether the InputProcessor handled the input.
        
        // Set defaults.
        returnValue = false;
        
        // Buttons ... Left is selection, right is context menu.
        
        // If user released left mouse button, then...
        if( button == Input.Buttons.LEFT )
        {
            // User released left mouse button.
            
            // Call method related to releasing left mouse button.
            // Results in updating Mouse.SELECT entry in mouseButtons hash map to false.
            this.selectMouseButtonReleased(screenX, screenY);
            
            // Flag as true, since input handled.
            returnValue = true;
        }
        
        // If user clicked right mouse button, then...
        if ( button == Input.Buttons.RIGHT )
        {
            // User released right mouse button.
            
            // Call method related to released right mouse button.
            // Results in updating Mouse.DOACTION entry in mouseButtons hash map to false.
            this.doActionMouseButtonReleased(screenX, screenY);
            
            // Flag as true, since input handled.
            returnValue = true;
        }
        
        // Return whether input handled.
        return returnValue;
        
    }
    
    /**
     * 
     * The function occurs when the user drags a finger across the screen or the mouse.
     * 
     * @param screenX  The x coordinate where the user touched the screen, basing the origin in the upper left corner.
     * @param screenY  The y coordinate where the user touched the screen, basing the origin in the upper left corner.
     * @param pointer  Pointer for the event.
     * @return  Whether the InputProcessor handled the input (true or false).
     */
    
    // screenX = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
    // screenY = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
    // pointer = Pointer for the event.
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        // The function occurs when the user drags a finger across the screen or the mouse.

        // Return the value indicating no handling of input.
        return false;
    }

    // Custom methods below...
    
    /**
     * The function clears resources associated with the input processor from memory.
     * <br>Actual disabling of the input processor occurs in the dispose() function of the MainGameScreen class.
     */
    public void dispose()
    {
        // The function clears resources associated with the input processor from memory.
        // Actual disabling of the input processor occurs in the dispose() function of the MainGameScreen class.
    }

    // Key presses:
    
    /**
     * The function handles caching of the event for pressing the left "key".
     * <br>Results in updating Keys.LEFT entry in keys hash map to true.
     */
    public void leftPressed()
    {
        // The function handles caching of the event for pressing the left "key".
        // Results in updating Keys.LEFT entry in keys hash map to true.
        keys.put(Keys.LEFT, true);
    }

    /**
     * The function handles caching of the event for pressing the right "key".
     * <br>Results in updating Keys.RIGHT entry in keys hash map to true.
     */
    public void rightPressed()
    {
        // The function handles caching of the event for pressing the right "key".
        // Results in updating Keys.RIGHT entry in keys hash map to true.
        keys.put(Keys.RIGHT, true);
    }

    /**
     * The function handles caching of the event for pressing the up "key".
     * <br>Results in updating Keys.UP entry in keys hash map to true.
     */
    public void upPressed()
    {
        // The function handles caching of the event for pressing the up "key".
        // Results in updating Keys.UP entry in keys hash map to true.
        keys.put(Keys.UP, true);
    }

    /**
     * The function handles caching of the event for pressing the down "key".
     * <br>Results in updating Keys.DOWN entry in keys hash map to true.
     */
    public void downPressed()
    {
        // The function handles caching of the event for pressing the down "key".
        // Results in updating Keys.DOWN entry in keys hash map to true.
        keys.put(Keys.DOWN, true);
    }
    
    /**
     * The function handles caching of the event for pressing the Q "key" -- for quitting the game.
     * <br>Results in updating Keys.QUIT entry in keys hash map to true.
     */
    public void quitPressed()
    {
        // The function handles caching of the event for pressing the Q "key" -- for quitting the game.
        // Results in updating Keys.QUIT entry in keys hash map to true.
        keys.put(Keys.QUIT, true);
    }

    // Key releases:

    /**
     * The function handles caching of the event for releasing the left "key".
     * <br>Results in updating Keys.LEFT entry in keys hash map to false.
     */
    public void leftReleased()
    {
        // The function handles caching of the event for releasing the left "key".
        // Results in updating Keys.LEFT entry in keys hash map to false.
        keys.put(Keys.LEFT, false);
    }

    /**
     * The function handles caching of the event for releasing the right "key".
     * <br>Results in updating Keys.RIGHT entry in keys hash map to false.
     */
    public void rightReleased()
    {
        // The function handles caching of the event for releasing the right "key".
        // Results in updating Keys.RIGHT entry in keys hash map to false.
        keys.put(Keys.RIGHT, false);
    }

    /**
     * The function handles caching of the event for releasing the up "key".
     * <br>Results in updating Keys.UP entry in keys hash map to false.
     */
    public void upReleased()
    {
        // The function handles caching of the event for releasing the up "key".
        // Results in updating Keys.UP entry in keys hash map to false.
        keys.put(Keys.UP, false);
    }

    /**
     * The function handles caching of the event for releasing the down "key".
     * <br>Results in updating Keys.DOWN entry in keys hash map to false.
     */
    public void downReleased()
    {
        // The function handles caching of the event for releasing the down "key".
        // Results in updating Keys.DOWN entry in keys hash map to false.
        keys.put(Keys.DOWN, false);
    }

    /**
     * The function handles caching of the event for releasing the Q "key".
     * <br>Results in updating Keys.QUIT entry in keys hash map to false.
     */
    public void quitReleased()
    {
        // The function handles caching of the event for releasing the Q "key".
        // Results in updating Keys.QUIT entry in keys hash map to false.
        keys.put(Keys.QUIT, false);
    }
    
    // Mouse general:
    
    /**
     * 
     * The function stores in the Vector3 variable, lastMouseCoordinates, the x and y coordinates where
     * the user touched the screen, basing the origin in the upper left corner.
     * 
     * @param x  The x coordinate where the user touched the screen, basing the origin in the upper left corner.
     * @param y  The y coordinate where the user touched the screen, basing the origin in the upper left corner.
     */
    
    // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
    // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
    public void setClickedMouseCoordinates(int x,int y)
    {
        // The function stores in the Vector3 variable, lastMouseCoordinates, the x and y coordinates where
        // the user touched the screen, basing the origin in the upper left corner.
        lastMouseCoordinates.set(x, y, 0);
    }
    
    // Mouse presses:
    
    /**
     * 
     * The function handles caching of the event for pressing the left mouse button.
     * <br>Results in updating Mouse.SELECT entry in mouseButtons hash map to true.
     * 
     * @param x  The x coordinate where the user touched the screen, basing the origin in the upper left corner.
     * @param y  The y coordinate where the user touched the screen, basing the origin in the upper left corner.
     */
    
    // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
    // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
    public void selectMouseButtonPressed(int x, int y)
    {
        // The function handles caching of the event for pressing the left mouse button.
        // <br>Results in updating Mouse.SELECT entry in mouseButtons hash map to true.
        mouseButtons.put(Mouse.SELECT, true);
    }

    /**
     * 
     * The function handles caching of the event for pressing the right mouse button.
     * <br>Results in updating Mouse.DOACTION entry in mouseButtons hash map to true.
     * 
     * @param x  The x coordinate where the user touched the screen, basing the origin in the upper left corner.
     * @param y  The y coordinate where the user touched the screen, basing the origin in the upper left corner.
     */
    
    // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
    // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
    public void doActionMouseButtonPressed(int x, int y)
    {
        // The function handles caching of the event for pressing the right mouse button.
        // Results in updating Mouse.DOACTION entry in mouseButtons hash map to true.
        mouseButtons.put(Mouse.DOACTION, true);
    }

    // Mouse releases:

    /**
     * 
     * The function handles caching of the event for releasing the left mouse button.
     * <br>Results in updating Mouse.SELECT entry in mouseButtons hash map to false.
     * 
     * @param x  The x coordinate where the user touched the screen, basing the origin in the upper left corner.
     * @param y  The y coordinate where the user touched the screen, basing the origin in the upper left corner.
     */
    
    // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
    // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
    public void selectMouseButtonReleased(int x, int y)
    {
        // The function handles caching of the event for releasing the left mouse button.
        // <br>Results in updating Mouse.SELECT entry in mouseButtons hash map to false.
        mouseButtons.put(Mouse.SELECT, false);
    }

    /**
     * 
     * The function handles caching of the event for releasing the right mouse button.
     * <br>Results in updating Mouse.DOACTION entry in mouseButtons hash map to false.
     * 
     * @param x  The x coordinate where the user touched the screen, basing the origin in the upper left corner.
     * @param y  The y coordinate where the user touched the screen, basing the origin in the upper left corner.
     */
    
    // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
    // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
    public void doActionMouseButtonReleased(int x, int y)
    {
        // The function handles caching of the event for releasing the right mouse button.
        // Results in updating Mouse.DOACTION entry in mouseButtons hash map to false.
        mouseButtons.put(Mouse.DOACTION, false);
    }

    // Other:
    
    /**
     * The function, associated with hiding a screen, disables related key processing, setting all cached
     * values to false.
     */
    public static void hide()
    {
        // The function, associated with hiding a screen, disables related key processing, setting all cached 
        // values to false.
        
        // Set all cached key entries to false.
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.QUIT, false);
            
    }
    
    /**
     * 
     * The processInput() method is the primary business logic that drives the class.  During the beginning
     * of every frame in the render loop, processInput() will be called before rendering any graphics.  In
     * the function, processing of the cached values of the keyboard and mouse input occurs.  First, 
     * calculation of the next position occurs, as explained in the previous section, in order to avoid 
     * issues with two fast-moving game objects colliding and missing a collision check.  Then, the state
     * and direction of the player character are set.
     * 
     * @param delta  Time span between the current and last frame in seconds.  Passed / populated automatically.
     */
    
    // delta = Time span between the current and last frame in seconds.  Passed / populated automatically.
    private void processInput(float delta)
    {

        /*
        The processInput() method is the primary business logic that drives the class.
        During the beginning of every frame in the render loop, processInput() will
        be called before rendering any graphics.  In the function, processing of the
        cached values of the keyboard and mouse input occurs.  First, calculation of
        the next position occurs, as explained in the previous section, in order to
        avoid issues with two fast-moving game objects colliding and missing a
        collision check.  Then, the state and direction of the player character are set.
        */
        
        // Keyboard input.
        
        // If the player pressed the left "key", then...
        // ... Indicated by the hash entry, Keys.LEFT, in keys, being equal to true.
        // ... Occurs when player presses the left arrow or A key.
        if ( keys.get(Keys.LEFT) )
        {
            
            // The player pressed the left "key".
            
            //Gdx.app.debug(TAG, "LEFT key");
            
            // Determine the next position value by using the current velocity and the time span between 
            // the current and last frame, moving the player to the left.  No actual collision detection
            // occurs.
            _player.calculateNextPosition(Entity.Direction.LEFT, delta);
            
            // Set the player state to walking (vs idle).
            _player.setState(Entity.State.WALKING);
            
            // Update the variable, _currentFrame, with the frame of the animation  sequence to display for 
            // the player, based on the direction (left) and time span between the current and last frame.
            _player.setDirection(Entity.Direction.LEFT, delta);
            
        }
        
        // Otherwise, if the player pressed the right "key", then...
        // ... Indicated by the hash entry, Keys.RIGHT, in keys, being equal to true.
        // ... Occurs when player presses the right arrow or D key.
        else if ( keys.get(Keys.RIGHT) )
        {
            
            // The player pressed the right "key".
            
            //Gdx.app.debug(TAG, "RIGHT key");
            
            // Determine the next position value by using the current velocity and the time span between 
            // the current and last frame, moving the player to the right.
            _player.calculateNextPosition(Entity.Direction.RIGHT, delta);
            
            // Set the player state to walking (vs idle).
            _player.setState(Entity.State.WALKING);
            
            // Update the variable, _currentFrame, with the frame of the animation  sequence to display for 
            // the player, based on the direction (right) and time span between the current and last frame.
            _player.setDirection(Entity.Direction.RIGHT, delta);
            
        }
        
        // Otherwise, if the player pressed the up "key", then...
        // ... Indicated by the hash entry, Keys.UP, in keys, being equal to true.
        // ... Occurs when player presses the up arrow or W key.
        else if( keys.get(Keys.UP) )
        {
            
            // The player pressed the up "key".
            
            //Gdx.app.debug(TAG, "UP key");
            
            // Determine the next position value by using the current velocity and the time span between 
            // the current and last frame, moving the player up.
            _player.calculateNextPosition(Entity.Direction.UP, delta);
            
            // Set the player state to walking (vs idle).
            _player.setState(Entity.State.WALKING);
            
            // Update the variable, _currentFrame, with the frame of the animation  sequence to display for 
            // the player, based on the direction (up) and time span between the current and last frame.
            _player.setDirection(Entity.Direction.UP, delta);
            
        }
        
        // Otherwise, if the player pressed the down "key", then...
        // ... Indicated by the hash entry, Keys.DOWN, in keys, being equal to true.
        // ... Occurs when player presses the down arrow or S key.
        else if(keys.get(Keys.DOWN) )
        {
            
            // The player pressed the down "key".
            
            //Gdx.app.debug(TAG, "DOWN key");
            
            // Determines the next position value by using the current velocity and the time span between 
            // the current and last frame, moving the player down.
            _player.calculateNextPosition(Entity.Direction.DOWN, delta);
            
            // Set the player state to walking (vs idle).
            _player.setState(Entity.State.WALKING);
            
            // Update the variable, _currentFrame, with the frame of the animation  sequence to display for 
            // the player, based on the direction (down) and time span between the current and last frame.
            _player.setDirection(Entity.Direction.DOWN, delta);
            
        }
        
        // Otherwise, if the player pressed the Q key, then...
        else if(keys.get(Keys.QUIT) )
        {
            
            // The player pressed the Q key.
            
            // Exit the application.
            Gdx.app.exit();
            
        }
        
        // Otherwise, ...
        else
        {
            // Set the player state to idle (vs walking).
            _player.setState(Entity.State.IDLE);
        }

        // Mouse input.
        
        // If the player pressed the left mouse button, then...
        // ... Indicated by the hash entry, Mouse.SELECT, in mouseButtons, being equal to true.
        if ( mouseButtons.get(Mouse.SELECT) )
        {
            
            // Player pressed the left mouse button, indicating a selection.
            
            //Gdx.app.debug(TAG, "Mouse LEFT click at : (" + lastMouseCoordinates.x + "," + lastMouseCoordinates.y + ")" );
            
            // Set the left mouse clicked state (selected) to released.
            mouseButtons.put(Mouse.SELECT, false);
            
        }

    }
    
    /**
     * 
     * The function gets called as part of the render() function in screens and processes cached keyboard
     * and mouse input.
     * 
     * @param delta  Time span between the current and last frame in seconds.  Passed / populated automatically.
     */
    
    // delta = Time span between the current and last frame in seconds.  Passed / populated automatically.
    public void update(float delta)
    {
        
        // The function gets called as part of the render() function in screens and processes cached
        // keyboard and mouse input.
        
        // Process cached keyboard and mouse input.
        processInput(delta);
        
        // Gdx.app.debug(TAG, "update:: Next Position: (" + BludBourne._player.getNextPosition().x + "," + BludBourne._player.getNextPosition().y + ")" + "DELTA: " + delta);
    
    }

}