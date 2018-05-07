package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by chris on 4/24/2018.
 */

public class SoundManager {
    private static SoundManager instance;
    private Music song;

    public static SoundManager ins(){
        if(instance == null)
            instance = new SoundManager();
        return instance;
    }

    private SoundManager(){
        song = Gdx.audio.newMusic(Gdx.files.internal("sounds/songs/Monitors.mp3"));
        song.setLooping(true);
        //song.play();
    }

    public void dispose(){
        song.stop();
        song.dispose();
    }


}
