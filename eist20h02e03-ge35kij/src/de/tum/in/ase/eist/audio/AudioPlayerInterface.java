package de.tum.in.ase.eist.audio;

public interface AudioPlayerInterface {

    public static final String BACKGROUND_MUSIC_FILE = "Music.wav";
    public static final String CRASH_MUSIC_FILE = "Crash.wav";
    
    String getCrashSoundFilePath();
    String getBackgroundMusicFilePath();
    void playBackgroundMusic();
    void stopBackgroundMusic();
    boolean isPlayingBackgroundMusic();
    void playCrashSound();
    boolean getCrashSoundPlayed();

}
