package bludbourne_ch02;

// LibGDX imports.
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

// Java imports.
import java.util.UUID;

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

public class Entity 
{
    
    /*
    The class contains information and methods related to the primary game objects, including player and 
    non-player characters in the game.  Both can move around in the world and interact with the environment.

    Regarding animations (from MLGD)...
    Construction an Animation object involves passing in the frame duration, TextureRegion references,
    and the loop type.  Each TextureRegion represents an indexed keyframe (one frame) in the animation cycle.
    An animation comprises cycling over the keyframes for the full animation duration.  Depending on
    individual needs, various types of play loops can be configured, such as playing the keyframes once in
    order, backwards, continuously, or in a random order.  The frame duration represents the time between
    frames -- how long each frame will be displayed, in seconds.  For example, an animation with four frames
    each having a duration of 0.25 seconds will give a total cycle of one second.
    
    Methods include:

    calculateNextPosition:
    dispose:
    getCurrentPosition:
    getFrame:
    getFrameSprite:
    init:
    initEntity:
    loadAllAnimations:
    loadDefaultSprite:
    setBoundingBoxSize:
    setCurrentPosition:
    setDirection:
    setNextPositionToCurrent:
    setState:
    update:
    */
	
    // Declare constants.
    
    // General:
    private static final String TAG = Entity.class.getSimpleName();
    
    private static final String DEFAULT_SPRITE_PATH = "assets/sprites/characters/Warrior.png";
    
    public final int FRAME_WIDTH = 16;
    public final int FRAME_HEIGHT = 16;
    
    // Declare enumerations.
    
    public enum State 
    {
        IDLE, WALKING
    }

    public enum Direction 
    {
        UP, RIGHT, DOWN, LEFT;
    }
    
    // Declare regular variables.
    private String _entityID;
    protected float _frameTime = 0f; // Accumulation of the deltas between frame updates.  Times animations.
      // Ranges from 0 to <5.
    
    // Declare object variables.
    // public static Rectangle boundingBox = new Rectangle();
    public Rectangle boundingBox = new Rectangle();
    
    protected TextureRegion _currentFrame = null;
    protected Sprite _frameSprite = null;
    protected State _state = State.IDLE;
    private Vector2 _velocity; // Speed of entity (pixels) per second.
    
    private Direction _currentDirection = Direction.LEFT; // Current direction of movement for the entity.
    private Direction _previousDirection = Direction.UP; // Previous direction of movement for the entity.

    protected Vector2 _currentPlayerPosition;
    protected Vector2 _nextPlayerPosition;
    
    private Animation _walkLeftAnimation;
    private Animation _walkRightAnimation;
    private Animation _walkUpAnimation;
    private Animation _walkDownAnimation;

    private Array<TextureRegion> _walkLeftFrames;
    private Array<TextureRegion> _walkRightFrames;
    private Array<TextureRegion> _walkUpFrames;
    private Array<TextureRegion> _walkDownFrames;

    public Entity()
    {
        
        // The constructor ...
        initEntity();
            
    }

    // Getters and setters below...
    
    public void setState(State state)
    {
        this._state = state;
    }

    public Sprite getFrameSprite()
    {
        return _frameSprite;
    }

    public TextureRegion getFrame()
    {
        return _currentFrame;
    }

    public Vector2 getCurrentPosition()
    {
        return _currentPlayerPosition;
    }

    public void setCurrentPosition(float currentPositionX, float currentPositionY)
    {
        _frameSprite.setX(currentPositionX);
        _frameSprite.setY(currentPositionY);
        this._currentPlayerPosition.x = currentPositionX;
        this._currentPlayerPosition.y = currentPositionY;
    }
    
    // Methods below...
    
    public final void initEntity()
    {
        
        //Gdx.app.debug(TAG, "Construction" );

        this._entityID = UUID.randomUUID().toString();
        this._nextPlayerPosition = new Vector2();
        this._currentPlayerPosition = new Vector2();
        //this.boundingBox = new Rectangle();
        this._velocity = new Vector2(2f,2f);

        Utility.loadTextureAsset(DEFAULT_SPRITE_PATH);
        loadDefaultSprite();
        loadAllAnimations();
        
    }

    // delta = Time span between the current and last frame in seconds.
    public void update(float delta)
    {
        
        /*
        The update() method will be called on any game object entity before rendering.
        The method adjusts the _frameTime to the range of 0 to <5 and reduces the height
        of the player hitbox to half.
        
        From MLGD:
        One of the states we need to maintain for smooth animation cycles is frameTime,
        which is simply the accumulation of the deltas between frame updates.  This 
        allows the animation to account for changes in the frame rate of the game. One
        quick note is that, depending on how long the game is playing, we do not want to 
        have a value increasing for the entire lifetime of the game.  An every increasing
        value has the potential for an overflow.  One simple solution is to mod the value 
        to five, essentially resetting the value every five seconds.
        */
        
        // The use of the modulus operator keeps the _frameTime in the range of 0 to <5.
        _frameTime = (_frameTime + delta) % 5; // Want to avoid overflow

        //Gdx.app.debug(TAG, "frametime: " + _frameTime );
        
        /*
        We want the hitbox to be at the feet for a better feel...
        
        Reduce the height of the hitbox to half, which provides a hitbox of a 
        rectangle from the waist to the bottom of the character.  Such a hitbox allows a 
        better feeling movement of the player and also looks much better when moving through 
        obstacles.
        */
        setBoundingBoxSize(0f, 0.5f);
        
    }

    public void init(float startX, float startY)
    {
        
        this._currentPlayerPosition.x = startX;
        this._currentPlayerPosition.y = startY;

        this._nextPlayerPosition.x = startX;
        this._nextPlayerPosition.y = startY;

        //Gdx.app.debug(TAG, "Calling INIT" );
        
    }

    // percentageWidthReduced = Percent by which to reduce width of hitbox.
    // percentageHeightReduced = Percent by which to reduce height of hitbox.
    public void setBoundingBoxSize(float percentageWidthReduced, float percentageHeightReduced)
    {
        
        /*
        The method reduces the hitbox size by the passed percentages for the width and height.
        
        From MLGD:
        The setBoundingBoxSize() method allows for the customizing of the hitbox for the
        different entities.  One example lies with the player character.  Based on the tileset 
        graphics, the default area of the hitbox for the player is the width and height of the 
        sprite.  The player hitbox default could cause issues when trying to traverse through 
        forested areas with collision rectangles spread about, as well as when blocking the player 
        from moving along the bottom of mountain ranges or on the top of lakes in the game.
        One solution is to reduce the height of the hitbox to half, which provides a hitbox of a 
        rectangle from the waist to the bottom of the character.  Such a hitbox allows a better 
        feeling movement of the player and also looks much better when moving through obstacles.
        */
        
        // Update the current bounding box (hitbox).
        
        float height; // Hitbox height.
        float heightReductionAmount; // Percent of height of hitbox to retain.
        float minX; // X-coordinate of hitbox.
        float minY; // Y-coordinate of hitbox.
        float width; // Hitbox width.
        float widthReductionAmount; // Percent of width of hitbox to retain.
        
        // Calculate percentages of width and height of hitbox to retain.
        widthReductionAmount = 1.0f - percentageWidthReduced; //.8f for 20% (1 - .20)
        heightReductionAmount = 1.0f - percentageHeightReduced; //.8f for 20% (1 - .20)

        // If width remains and less than 100% of original size, then...
        if ( widthReductionAmount > 0 && widthReductionAmount < 1 )
        {
            // Width remains ( > 0) and less than 100% of original size.
            
            // Calculate new width.
            width = FRAME_WIDTH * widthReductionAmount;
        }
        
        else
        {
            // Width remaining the same or adjustment outside of valid range.
            
            // Keep current width.
            width = FRAME_WIDTH;
        }

        // If height remains and less than 100% of original size, then...
        if ( heightReductionAmount > 0 && heightReductionAmount < 1 )
        {
            // Height remains ( > 0) and less than 100% of original size.
            
            // Calculate new height.
            height = FRAME_HEIGHT * heightReductionAmount;
        }
        
        else
        {
            // Height remaining the same or adjustment outside of valid range.
            
            // Keep current height.
            height = FRAME_HEIGHT;
        }

        // If width and, or height of hitbox equal to zero, then...
        if ( width == 0 || height == 0 )
        {
            // Width and, or height of hitbox equal to zero.
            
            // Display warning.
            Gdx.app.debug(TAG, "Width and Height are 0!! " + width + ":" + height);
        }

        // Need to account for the unit scale, since the map coordinates will be in pixels.
        
        // If scale exists, then...
        if ( MapManager.UNIT_SCALE > 0 ) 
        {
            
            // Scale exists.
            
            // Set x and y coordinates of hitbox, adjusting for unit scale.
            minX = _nextPlayerPosition.x / MapManager.UNIT_SCALE;
            minY = _nextPlayerPosition.y / MapManager.UNIT_SCALE;
            
        }
        
        else
        {
            
            // No scale exists.
            
            // Set x and y coordinates of hitbox.
            minX = _nextPlayerPosition.x;
            minY = _nextPlayerPosition.y;
            
        }

        // Set values for hitbox.
        boundingBox.set(minX, minY, width, height);
        
        //Gdx.app.debug(TAG, "SETTING Bounding Box: (" + minX + "," + minY + ")  width: " + width + " height: " + height);
        
    }

    private void loadDefaultSprite()
    {
        
        Texture texture;
        TextureRegion[][] textureFrames;
        
        texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
        textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
        _frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0,0,FRAME_WIDTH, FRAME_HEIGHT);
        _currentFrame = textureFrames[0][0];
        
    }

    private void loadAllAnimations()
    {
        
        /*
        The loadAllAnimations() method gets called only when first instantiating the entity objects
        and stores the movement-related animations.
        
        The split() static method in the TextureRegion class allows for using the individual sprites,
        by passing in the Texture and width and height dimensions for each sprite.  The method returns
        an array of TextureRegion objects representing the individual keyframes for the animation.
        The TextureRegion arrays allow for the creation of Animation objects.  For example, a
        TextureRegion with sixteen frames, four for each direction of player movement, can be broken
        into the four corresponding animations.
        */
        
        TextureRegion region; // Current texture frame in loop (ex. one frame of character walking up).
        Texture texture; // Texture from the asset manager with the walking player character (warrior).
        TextureRegion[][] textureFrames; // Two-dimensional array of texture regions for the walking
          // player character.
        
        // Walking animation...
        
        // Get the texture from the asset manager with the walking player character (warrior).
        texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
        
        // Split the texture region into frames (a two-dimensional array of texture regions).
        textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);

        // Initialize the texture region arrays for each of the walking directions.
        
        /*
        _walkDownFrames = new Array<TextureRegion>(4);
        _walkLeftFrames = new Array<TextureRegion>(4);
        _walkRightFrames = new Array<TextureRegion>(4);
        _walkUpFrames = new Array<TextureRegion>(4);
        */
        _walkDownFrames = new Array<>(4);
        _walkLeftFrames = new Array<>(4);
        _walkRightFrames = new Array<>(4);
        _walkUpFrames = new Array<>(4);

        // Loop through rows in texture. > Direction of movement.
        for (int i = 0; i < 4; i++) 
        {
            // Loop through columns in texture. > Frame (1 to n) of a movement (e.g. first frame moving left).
            for (int j = 0; j < 4; j++) 
            {
                
                // Get current texture frame.
                region = textureFrames[i][j];
                //Gdx.app.debug(TAG, "Got frame " + i + "," + j + " from " + sourceImage);
                
                // If no region data retrieved, then...
                if( region == null )
                {
                    // No region data retrieved.
                    
                    // Display warning.
                    Gdx.app.debug(TAG, "Got null animation frame " + i + "," + j);
                }
                
                // Depending on direction, ...
                switch(i)
                    {
                    case 0: // Walking down.
                        _walkDownFrames.insert(j, region);
                        break;
                    case 1: // Walking left.
                        _walkLeftFrames.insert(j, region);
                        break;
                    case 2: // Walking right.
                        _walkRightFrames.insert(j, region);
                        break;
                    case 3: // Walking up.
                        _walkUpFrames.insert(j, region);
                        break;
                    }
            } // End ... Loop through columns in texture. > Frame (1 to n) of a movement (e.g. first frame moving left).
            
        } // Loop through rows in texture. > Direction of movement.

        // Store animations.
        _walkDownAnimation = new Animation(0.25f, _walkDownFrames, Animation.PlayMode.LOOP);
        _walkLeftAnimation = new Animation(0.25f, _walkLeftFrames, Animation.PlayMode.LOOP);
        _walkRightAnimation = new Animation(0.25f, _walkRightFrames, Animation.PlayMode.LOOP);
        _walkUpAnimation = new Animation(0.25f, _walkUpFrames, Animation.PlayMode.LOOP);
        
    }

    public void dispose()
    {
        Utility.unloadAsset(DEFAULT_SPRITE_PATH);
    }

    // direction = Current direction of the entity.
    // deltaTime = Time span between the current and last frame in seconds.  Passed / populated automatically.
    public void setDirection(Direction direction,  float deltaTime)
    {
        
        /*
        The method deals with updating the animation keyframes based on the current cardinal direction.
        The method will be called every time when processing input from the event queue.  During every 
        frame of the render loop, the current TextureRegion frame that represents the player character 
        will be retrieved and rendered.  Based on the current facing direction, the method will guarantee 
        that the proper frame is set.  The funciton updates the variable, _currentFrame, with the frame
        of the animation sequence to display for the entity, based on the direction and timing.
        */
        
        // Update the previous and current directions.
        this._previousDirection = this._currentDirection;
        this._currentDirection = direction;

        // Look into the appropriate variable when changing position.
        // Get the appropriate frame from the animation sequence.
        
        // Depending on the current entity direction, ...
        switch (_currentDirection) 
            {
            case DOWN : // When moving down...
                _currentFrame = _walkDownAnimation.getKeyFrame(_frameTime);
                break;
            case LEFT : // When moving left...
                _currentFrame = _walkLeftAnimation.getKeyFrame(_frameTime);
                break;
            case UP : // When moving up...
                _currentFrame = _walkUpAnimation.getKeyFrame(_frameTime);
                break;
            case RIGHT : // When moving right...
                _currentFrame = _walkRightAnimation.getKeyFrame(_frameTime);
                break;
            default: // Invalid entity direction detected.
                System.out.println("Warning:  Unknown entity direction.");
                break;
            }
        
    }

    public void setNextPositionToCurrent()
    {
        setCurrentPosition(_nextPlayerPosition.x, _nextPlayerPosition.y);
        //Gdx.app.debug(TAG, "Setting nextPosition as Current: (" + _nextPlayerPosition.x + "," + _nextPlayerPosition.y + ")");
    }

    // currentDirection = Current direction of the entity.
    // deltaTime = Time span between the current and last frame in seconds.  Passed / populated automatically.
    public void calculateNextPosition(Direction currentDirection, float deltaTime)
    {
        
        /*
        The calculateNextPosition() method is called every time that player input is detected. 
        Sometimes, collisions are not detected during a frame update because the velocity value 
        is too fast to be calculated in the current frame.  By the time the next frame checks 
        the collision, the game objects have already passed through each other.  Basically, the 
        method represents one technique to deal with collisions between two
        moving objects in the game world.   The function “looks ahead” and predicts the next 
        position value by using our current velocity and the time to render the last frame.   
        Multiplying the current velocity vector, _velocity, and deltaTime scalar quantity using 
        the scl() method produces the travel distance (displacement).  An addition or subtraction
        of the distance to the next position based upon the current direction occurs.  If the new 
        position collides with an object, then the current position adjusts to prevent the issue.
        Otherwise, the values becomes the current position.
        
        scl = scalar (multiple for vector)
        */
        
        float testX;
        float testY;
        
        testX = _currentPlayerPosition.x; // Current x position of entity.
        testY = _currentPlayerPosition.y; // Current y position of entity.

        //Gdx.app.debug(TAG, "calculateNextPosition:: Current Position: (" + _currentPlayerPosition.x + "," + _currentPlayerPosition.y + ")"  );
        //Gdx.app.debug(TAG, "calculateNextPosition:: Current Direction: " + _currentDirection  );

        // Multiply the velocity vector by the time delta.
        // aka ... Multiple the current speed of the entity by the number of seconds passed to calculate
        //         distance moved.
        _velocity.scl(deltaTime);

        // Depending on current entity direction...
        switch (currentDirection) 
            
            {
            case LEFT : // When moving left...
                testX -=  _velocity.x; // Adjust potential (test) position of entity by calculated -x distance.
                break;
            case RIGHT : // When moving right...
                testX += _velocity.x; // Adjust potential (test) position of entity by calculated x distance.
                break;
            case UP : // When moving up...
                testY += _velocity.y; // Adjust potential (test) position of entity by calculated y distance.
                break;
            case DOWN : // When moving down...
                testY -= _velocity.y; // Adjust potential (test) position of entity by calculated -y distance.
                break;
            default: // Invalid entity direction detected.
                System.out.println("Warning:  Unknown entity direction.");
                break;
            }

        // Set next position of entity to test position.
        _nextPlayerPosition.x = testX;
        _nextPlayerPosition.y = testY;

        // Divide the velocity vector by the time delta -- reverses the speed increase.
        _velocity.scl(1 / deltaTime);
        
    }

}