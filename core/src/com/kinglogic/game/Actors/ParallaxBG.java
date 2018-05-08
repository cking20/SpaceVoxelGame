package com.kinglogic.game.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kinglogic.game.Managers.CameraManager;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Screens.GameScreen;

/**
 * Created by chris on 5/2/2018.
 */

public class ParallaxBG extends Image {
    private static final float LAYER_SPEED_DIFFERENCE = .5f;
    private int x, y,originX, originY, rotation;
    private float scaleX, scaleY;
    Array<Texture> layers;
    private float speedX, speedY = 0;
    private float scrollX, scrollY = 0;
    public ParallaxBG(){
        //derived from  https://libgdx.info/Parallax/
        super();
        layers = ResourceManager.ins().getBackground();
        for (int i = 0; i < layers.size; i++) {
            layers.get(i).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        x = y = originX = originY = rotation = 0;

        scaleX = scaleY = 1;
//        flipX = flipY = false;
    }
    public void setSpeed(float newSpeedX, float newSpeedY){
        speedX = newSpeedX;
        speedY = newSpeedY;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        scaleX = CameraManager.ins().mainCamera.zoom;
        scaleY = CameraManager.ins().mainCamera.zoom* Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
        scrollX+=speedX;
        scrollY+=speedY;
        for(int i = 0;i<layers.size;i++) {
            batch.draw(layers.get(i),
                    CameraManager.ins().mainCamera.position.x-CameraManager.ins().mainCamera.viewportWidth*scaleX,
                    CameraManager.ins().mainCamera.position.y-CameraManager.ins().mainCamera.viewportHeight*scaleY,
                    getOriginX(),
                    getOriginY(),
                    (int)(CameraManager.ins().mainCamera.viewportWidth*scaleX*1.75f),
                    (int)(CameraManager.ins().mainCamera.viewportHeight*scaleY*1.75f),
                    getScaleX(),
                    getScaleY(),
                    getRotation(),
                    (int) (scrollX + i*this.LAYER_SPEED_DIFFERENCE *scrollX),
                    (int) (scrollY + i*this.LAYER_SPEED_DIFFERENCE *scrollY),
                    layers.get(i).getWidth(),
                    layers.get(i).getHeight(),
                    false,false);
        }
    }

}
