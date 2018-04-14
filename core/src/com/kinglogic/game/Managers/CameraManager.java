package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kinglogic.game.TestInputProcessor;

/**
 * Created by chris on 4/14/2018.
 */

public class CameraManager {
    private static CameraManager instance;
    public final OrthographicCamera mainCamera;
    private Actor toTrack;

    public static CameraManager ins() {
        if(instance == null)
            instance = new CameraManager();
        return instance;
    }

    private CameraManager(){
        mainCamera = new OrthographicCamera();
    }

    public void Track(Actor a){
        toTrack = a;
    }

    public void Update(){
        if(toTrack != null){
            mainCamera.position.set(new Vector2(toTrack.getX(),toTrack.getY()),mainCamera.position.z);
        }
        mainCamera.update();

    }
}
