package enumerations;

// Java imports.
import java.util.HashMap;
import java.util.Map;

public class SoundEnumeration 
{
    
    /*
    The file contains logic for enumerations related to sounds.
    Enumerations represent special Java types used to define collections of constants.
    More precisely, a Java enum type acts as a special kind of Java class.  An enum can 
    contain constants, methods, ...
    */
    
    // Enumerations related to sounds.
    public enum SoundEnum 
    {
        
        SOUND_ATTACK (0, "sounds/attack.wav"), // Attack-related sound.
        SOUND_BLOCKED (1, "sounds/blocked.wav"), // Attack blocked sound.
        SOUND_BONESHIELD (2, "sounds/boneshield.wav"), // Sound related to attack from Bone Shield boss.
        SOUND_CLICK (3, "sounds/click.wav"), // Sound related to mouse-click.
        SOUND_COIN (4, "sounds/coin.wav"), // Sound related to picking up one or more coin(s).
        SOUND_CRITICAL (5, "sounds/critical.wav"), // Sound related to a critical hit.
        SOUND_DEFEAT (6, "sounds/defeat.wav"), // Sound related to a defeat.
        SOUND_FIRE (7, "sounds/fire.wav"), // Sound related to a fire spell.
        SOUND_HEAL (8, "sounds/heal.wav"), // Sound related to a heal spell.
        SOUND_HP_DRAIN (9, "sounds/hpdrain.wav"), // Sound related to a heal spell.
        SOUND_MISS (10, "sounds/miss.wav"), // Sound related to a miss.
        SOUND_MP_DRAIN (11, "sounds/mpdrain.wav"), // Sound related to a magic point drain attack.
        SOUND_RUN (12, "sounds/run.wav"), // Sound related to a run action (fleeing combat).
        SOUND_UNLOCK (13, "sounds/unlock.wav"), // Sound related to unlocking an object.
        SOUND_LIGHT (14, "sounds/lightning_a.wav"), // Sound related to light spell.
        SOUND_FREEZE (15, "sounds/freeze.wav"), // Sound related to freeze spell.
        SOUND_REFLECT (16, "sounds/rubberband.wav"), // Sound related to reflect spell.
        SOUND_ERROR (17, "sounds/error.wav") // Sound when trying to do something at the wrong time.
        ; // semicolon needed when fields / methods follow
        
        private final int soundEnum; // Enumerations related to sounds.
        private final String soundFilePath; // Relative path to the sound file.
        private static final Map soundMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // soundEnum = Value to associate.
        // soundFilePath = Relative path to the sound file.
        private SoundEnum(int soundEnum, String soundFilePath) 
        {
            // The constructor sets the values for each enumeration.
            this.soundEnum = soundEnum;
            this.soundFilePath = soundFilePath;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (SoundEnum soundEnum : SoundEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                soundMap.put(soundEnum.soundEnum, soundEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = SoundEnumeration.SoundEnum.SOUND_ATTACK.getValue();
            
            // Return the numeric value for the enumeration.
            return soundEnum;
        }
        
        public String getValue_FilePath() 
        {
            // The function returns the relative file path value for the enumeration.
            // Example for use:  String x = SoundEnumeration.SoundEnum.SOUND_ATTACK.getValue_FilePath();
            
            // Return the relative file path value for the enumeration.
            return soundFilePath;
        }
        
        // sound = Numeric value to convert to text.
        public static SoundEnum valueOf(int sound) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (SoundEnum) soundMap.get(sound);
        }
        
    }
    
}