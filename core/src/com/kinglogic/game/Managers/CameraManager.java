package com.kinglogic.game.Managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;

import java.util.ArrayList;

/**
 * Created by chris on 4/14/2018.
 */

public class CameraManager {
    private static CameraManager instance;
    public final OrthographicCamera mainCamera;
    private Actor toTrack;
    private ArrayList<Actor> watch;
    private float oldMainCamRotation = 0;

    public static CameraManager ins() {
        if(instance == null)
            instance = new CameraManager();
        return instance;
    }

    private CameraManager(){
        mainCamera = new OrthographicCamera();
        watch = new ArrayList<Actor>();
        mainCamera.zoom = .5f;
    }

    public void Track(Actor a){
        watch.clear();
        toTrack = a;
    }

    public void Update(float delta){
        if(toTrack != null){
            mainCamera.position.x = 0;
            mainCamera.position.y = 0;
            if(toTrack instanceof VoxelCollection) {
                /*
                mainCamera.translate(new Vector2(
                        toTrack.getX() + VoxelCollection.maxSize / 2 * ResourceManager.VOXEL_PIXEL_SIZE,
                        toTrack.getY() + VoxelCollection.maxSize / 2 * ResourceManager.VOXEL_PIXEL_SIZE));//best so far
                mainCamera.up.set(new Vector2(0,1).rotate(toTrack.getRotation()), 0);
                */
                mainCamera.translate(new Vector2(
                        toTrack.getX(),
                        toTrack.getY()));//best so far
                Vector2 rotTrans = new Vector2(
                        VoxelCollection.maxSize / 2 * ResourceManager.VOXEL_PIXEL_SIZE,
                        VoxelCollection.maxSize / 2 * ResourceManager.VOXEL_PIXEL_SIZE);
                rotTrans = rotTrans.rotate(toTrack.getRotation());
                mainCamera.translate(rotTrans);
                mainCamera.up.set(new Vector2(0,1).rotate(toTrack.getRotation()), 0);
            }else {
                Vector2 center = new Vector2(toTrack.getX(), toTrack.getY());
                if(toTrack.getScaleX() > 0)
                    center.add(new Vector2(toTrack.getWidth()/2, toTrack.getHeight()/2).rotate(toTrack.getRotation()));
                else
                    center.add(new Vector2(-toTrack.getWidth()/2, toTrack.getHeight()/2).rotate(toTrack.getRotation()));
                mainCamera.translate(center);//best so far
                mainCamera.up.set(new Vector2(0,1).rotate(toTrack.getRotation()), 0);
            }
        }else{
            //todo implement this after player's views are originized
//            if(watch.size() > 0) {
//                int avgX = 0, avgY = 0;
//                for (int i = 0; i < watch.size(); i++) {
//                    avgX += watch.get(i).getX();
//                    avgY += watch.get(i).getY();
//                }
//                avgX /= watch.size();
//                avgY /= watch.size();
//                mainCamera.position.x = 0;
//                mainCamera.position.y = 0;
//                Vector2 center = new Vector2(avgX, avgY);
//                mainCamera.translate(center);//best so far
//                mainCamera.up.set(new Vector2(0, 1).rotate(watch.get(0).getRotation()), 0);
//            }

        }
    }
    public void ZoomIn(){
        mainCamera.zoom-=.05f;
        if(mainCamera.zoom < .5f)
            mainCamera.zoom = .5f;
    }
    public void ZoomOut(){
        mainCamera.zoom+=.05f;
//        if(mainCamera.zoom > 2.5f)
//            mainCamera.zoom = 2.5f;
    }

    //todo implement this after player's views are originized
//
//    public void addToWatch(Actor view){
//        toTrack = null;
//        watch.add(view);
//    }
//    public void removeFromWatch(Actor view){
//        watch.remove(view);
//    }
}
