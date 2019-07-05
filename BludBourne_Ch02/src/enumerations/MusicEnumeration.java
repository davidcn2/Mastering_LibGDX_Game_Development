package enumerations;

// Java imports.
import java.util.HashMap;
import java.util.Map;

public class MusicEnumeration 
{
    
    /*
    The file contains logic for enumerations related to music (mp3, ogg).
    Enumerations represent special Java types used to define collections of constants.
    More precisely, a Java enum type acts as a special kind of Java class.  An enum can 
    contain constants, methods, ...
    */
    
    // Enumerations related to music.
    public enum MusicEnum 
    {
        
        MUSIC_ELEGY_DM (0, "music/elegy_dm.mp3", "music/elegy_dm.ogg"), // Elegy DM music.
        MUSIC_HAPLY (1, "music/haply.mp3", "music/haply.ogg"), // Haply music.
        MUSIC_KAWARAYU (2, "music/kawarayu.mp3", "music/kawarayu.ogg"), // Kawarayu music.
        M31 (3, "music/m31.mp3", "music/m31.ogg") // M31 music.
        ; // semicolon needed when fields / methods follow

        private final int musicEnum; // Enumerations related to music.
        private final String mp3File; // Filename for music -- in mp3 format.
        private final String oggFile; // Filename for music -- in ogg format.
        private static final Map musicMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // musicEnum = Value to associate.
        // mp3File = Filename for music -- in mp3 format.
        // oggFile = Filename for music -- in ogg format.
        private MusicEnum(int musicEnum, String mp3File, String oggFile) 
        {
            // The constructor sets the values for each enumeration.
            this.musicEnum = musicEnum;
            this.mp3File = mp3File;
            this.oggFile = oggFile;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (MusicEnum musicEnum : MusicEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                musicMap.put(musicEnum.musicEnum, musicEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = MusicEnumeration.MusicEnum.MUSIC_HAPLY.getValue();
            
            // Return the numeric value for the enumeration.
            return musicEnum;
        }
        
        public String getValue_File_mp3() 
        {
            // The function returns the mp3 filename for the music.
            // Example for use:  String x = MusicEnumeration.MusicEnum.MUSIC_HAPLY.getValue_File_mp3();
            
            // Return the mp3 filename for the music.
            return mp3File;
        }
        
        public String getValue_File_ogg() 
        {
            // The function returns the ogg filename for the music.
            // Example for use:  String x = MusicEnumeration.MusicEnum.MUSIC_HAPLY.getValue_File_ogg();
            
            // Return the ogg filename for the music.
            return oggFile;
        }
        
        // music = Numeric value to convert to text.
        public static MusicEnum valueOf(int music) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (MusicEnum) musicMap.get(music);
        }
        
    }
    
}