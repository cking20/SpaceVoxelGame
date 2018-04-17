package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Physics.Grid;
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
            mainCamera.position.x = 0;
            mainCamera.position.y = 0;
            if(toTrack instanceof VoxelCollection) {
                /*
                mainCamera.translate(new Vector2(
                        toTrack.getX() + VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize,
                        toTrack.getY() + VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize));//best so far
                mainCamera.up.set(new Vector2(0,1).rotate(toTrack.getRotation()), 0);
                */
                mainCamera.translate(new Vector2(
                        toTrack.getX(),
                        toTrack.getY()));//best so far
                Vector2 rotTrans = new Vector2(
                        VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize,
                        VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize);
                rotTrans = rotTrans.rotate(toTrack.getRotation());
                mainCamera.translate(rotTrans);
                mainCamera.up.set(new Vector2(0,1).rotate(toTrack.getRotation()), 0);
            }else {
                mainCamera.translate(new Vector2(toTrack.getX(), toTrack.getY()));//best so far
                mainCamera.up.set(new Vector2(0,1).rotate(toTrack.getRotation()), 0);
            }
        }
    }
}
