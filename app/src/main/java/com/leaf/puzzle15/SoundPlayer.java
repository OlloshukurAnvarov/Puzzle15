package com.leaf.puzzle15;

import android.media.MediaPlayer;

public class SoundPlayer {
    private static boolean isActive = false;
    private MediaPlayer mediaPlayer;

    public SoundPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void play(){
        if (mediaPlayer != null && isActive) {
            mediaPlayer.setVolume(0.4f, 0.4f);
            mediaPlayer.start();
        }
    }

    public static void setIsActive(boolean isActive) {
        SoundPlayer.isActive = isActive;
    }
}
