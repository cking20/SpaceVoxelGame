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
    private float oldMainCamRotation = 0;

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

    public void Update(float delta){
        if(toTrack != null){
//            mainCamera.rotate(1f);
//            mainCamera.position.x = toTrack.getX();
//            mainCamera.position.y = toTrack.getY();
//            mainCamera.translate(-mainCamera.position.x,-mainCamera.position.y);
            mainCamera.position.x = 0;
            mainCamera.position.y = 0;
//            mainCamera.rotate(-oldMainCamRotation);
            mainCamera.translate((new Vector2(toTrack.getX(),toTrack.getY())));
            mainCamera.up.set(new Vector2(0,1).rotate(toTrack.getRotation()), 0);
//            mainCamera.rotate(toTrack.getRotation());
//            oldMainCamRotation = toTrack.getRotation();
        }
//        mainCamera.update(true);


    }
}
