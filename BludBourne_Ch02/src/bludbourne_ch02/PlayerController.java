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
    
    /*
    The class implements InputProcessor and manages the player input and controls on the screen.

    Methods include:

    dispose:
    doActionMouseButtonPressed:
    doActionMouseButtonReleased:
    downPressed:
    downReleased:
    hide:
    keyDown:
    keyTyped:
    keyUp:
    leftPressed:
    leftReleased:
    mouseMoved:
    processInput:
    quitPressed:
    quitReleased:
    rightPressed:
    rightReleased:
    scrolled:
    selectMouseButtonPressed:
    selectMouseButtonReleased:
    setClickedMouseCoordinates:
    touchDown:
    touchDragged:
    touchUp:
    upPressed:
    upReleased:
    update:
    */

    // Declare constants.
    private final static String TAG = PlayerController.class.getSimpleName();

    // Declare enumerations.
    enum Keys 
    {
        LEFT, RIGHT, UP, DOWN, QUIT
    }

    enum Mouse
    {
        SELECT, DOACTION
    }

    // Declare list variables.
    private static HashMap<Keys, Boolean> keys = new HashMap<PlayerController.Keys, Boolean>();
    private static HashMap<Mouse, Boolean> mouseButtons = new HashMap<PlayerController.Mouse, Boolean>();
    
    // Declare object variables.
    private Vector3 lastMouseCoordinates;
    private Entity _player;

    // Initialize the hashmaps for inputs.
    
    static 
    {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.QUIT, false);
    };

    static
    {
        mouseButtons.put(Mouse.SELECT, false);
        mouseButtons.put(Mouse.DOACTION, false);
    };


    // Constructors below...

    public PlayerController(Entity player)
    {
        
        // The constructor ...
        
        //Gdx.app.debug(TAG, "Construction" );
        this.lastMouseCoordinates = new Vector3();
        this._player = player;
        
    }

    // Overriden methods below...
    
    @Override
    public boolean keyDown(int keycode)
    {
        
        if ( keycode == Input.Keys.LEFT || keycode == Input.Keys.A )
        {
            this.leftPressed();
        }
        
        if ( keycode == Input.Keys.RIGHT || keycode == Input.Keys.D )
        {
            this.rightPressed();
        }
        
        if ( keycode == Input.Keys.UP || keycode == Input.Keys.W )
        {
            this.upPressed();
        }
        
        if ( keycode == Input.Keys.DOWN || keycode == Input.Keys.S )
        {
            this.downPressed();
        }
        
        if ( keycode == Input.Keys.Q )
        {
            this.quitPressed();
        }

        return true;
        
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }
    
    @Override
    public boolean keyUp(int keycode)
    {
        
        if( keycode == Input.Keys.LEFT || keycode == Input.Keys.A )
        {
            this.leftReleased();
        }
        
        if ( keycode == Input.Keys.RIGHT || keycode == Input.Keys.D )
        {
            this.rightReleased();
        }
        
        if ( keycode == Input.Keys.UP || keycode == Input.Keys.W )
        {
            this.upReleased();
        }
        
        if ( keycode == Input.Keys.DOWN || keycode == Input.Keys.S )
        {
            this.downReleased();
        }
        
        if ( keycode == Input.Keys.Q )
        {
            this.quitReleased();
        }
        
        return true;
        
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }
    
    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
            
        //Gdx.app.debug(TAG, "GameScreen: MOUSE DOWN........: (" + screenX + "," + screenY + ")" );
        
        if ( button == Input.Buttons.LEFT || button == Input.Buttons.RIGHT )
        {
            this.setClickedMouseCoordinates(screenX, screenY);
        }

        //left is selection, right is context menu
        if ( button == Input.Buttons.LEFT )
        {
            this.selectMouseButtonPressed(screenX, screenY);
        }
        
        if ( button == Input.Buttons.RIGHT )
        {
            this.doActionMouseButtonPressed(screenX, screenY);
        }
        
        return true;
        
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        
        //left is selection, right is context menu
        if( button == Input.Buttons.LEFT )
        {
            this.selectMouseButtonReleased(screenX, screenY);
        }
        
        if ( button == Input.Buttons.RIGHT )
        {
            this.doActionMouseButtonReleased(screenX, screenY);
        }
        
        return true;
        
    }

    // Custom methods below...
    
    public void dispose()
    {

    }

    // Key presses:
    
    public void leftPressed()
    {
        keys.put(Keys.LEFT, true);
    }

    public void rightPressed()
    {
        keys.put(Keys.RIGHT, true);
    }

    public void upPressed()
    {
        keys.put(Keys.UP, true);
    }

    public void downPressed()
    {
        keys.put(Keys.DOWN, true);
    }
    public void quitPressed()
    {
        keys.put(Keys.QUIT, true);
    }

    // Key releases:

    public void leftReleased()
    {
        keys.put(Keys.LEFT, false);
    }

    public void rightReleased()
    {
        keys.put(Keys.RIGHT, false);
    }

    public void upReleased()
    {
        keys.put(Keys.UP, false);
    }

    public void downReleased()
    {
        keys.put(Keys.DOWN, false);
    }

    public void quitReleased()
    {
        keys.put(Keys.QUIT, false);
    }
    
    // Mouse general:
    
    public void setClickedMouseCoordinates(int x,int y)
    {
        lastMouseCoordinates.set(x, y, 0);
    }
    
    // Mouse presses:
    
    public void selectMouseButtonPressed(int x, int y)
    {
        mouseButtons.put(Mouse.SELECT, true);
    }

    public void doActionMouseButtonPressed(int x, int y)
    {
        mouseButtons.put(Mouse.DOACTION, true);
    }

    // Mouse releases:

    public void selectMouseButtonReleased(int x, int y)
    {
        mouseButtons.put(Mouse.SELECT, false);
    }

    public void doActionMouseButtonReleased(int x, int y)
    {
        mouseButtons.put(Mouse.DOACTION, false);
    }

    // Other:
    
    public static void hide()
    {
        
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.QUIT, false);
            
    }
    
    private void processInput(float delta)
    {

        //Keyboard input
        
        if ( keys.get(Keys.LEFT) )
        {
            //Gdx.app.debug(TAG, "LEFT key");
            _player.calculateNextPosition(Entity.Direction.LEFT, delta);
            _player.setState(Entity.State.WALKING);
            _player.setDirection(Entity.Direction.LEFT, delta);
        }
        
        else if ( keys.get(Keys.RIGHT) )
        {
            //Gdx.app.debug(TAG, "RIGHT key");
            _player.calculateNextPosition(Entity.Direction.RIGHT, delta);
            _player.setState(Entity.State.WALKING);
            _player.setDirection(Entity.Direction.RIGHT, delta);
        }
        
        else if( keys.get(Keys.UP) )
        {
            //Gdx.app.debug(TAG, "UP key");
            _player.calculateNextPosition(Entity.Direction.UP, delta);
            _player.setState(Entity.State.WALKING);
            _player.setDirection(Entity.Direction.UP, delta);
        }
        
        else if(keys.get(Keys.DOWN) )
        {
            //Gdx.app.debug(TAG, "DOWN key");
            _player.calculateNextPosition(Entity.Direction.DOWN, delta);
            _player.setState(Entity.State.WALKING);
            _player.setDirection(Entity.Direction.DOWN, delta);
        }
        
        else if(keys.get(Keys.QUIT) )
        {
            Gdx.app.exit();
        }
        
        else
        {
            _player.setState(Entity.State.IDLE);
        }

        // Mouse input
        if ( mouseButtons.get(Mouse.SELECT) )
        {
            //Gdx.app.debug(TAG, "Mouse LEFT click at : (" + lastMouseCoordinates.x + "," + lastMouseCoordinates.y + ")" );
            mouseButtons.put(Mouse.SELECT, false);
        }

    }
    
    public void update(float delta)
    {
        
        processInput(delta);
        //Gdx.app.debug(TAG, "update:: Next Position: (" + BludBourne._player.getNextPosition().x + "," + BludBourne._player.getNextPosition().y + ")" + "DELTA: " + delta);
    
    }

}
