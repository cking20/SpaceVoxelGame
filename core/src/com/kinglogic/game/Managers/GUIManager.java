package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by chris on 4/20/2018.
 */

public class GUIManager {
    private static GUIManager instance;
    private Stage worldStage;
    private OrthographicCamera viewCam;
    private FitViewport view;

    public  Vector2 targetPosition = new Vector2(200,200);
    private Image target;

    public static GUIManager ins() {
        if(instance == null)
            instance = new GUIManager();
        return instance;
    }

    private GUIManager(){
        worldStage = new Stage();
        viewCam = CameraManager.ins().mainCamera;
        view = new FitViewport(Gdx.graphics.getHeight(), Gdx.graphics.getHeight()*ResourceManager.ins().calculateAspectRatio(), viewCam);
        view.apply();

        target = new Image(ResourceManager.ins().getVoxTex(IDs.TECH_TEX));
        worldStage.addActor(target);

    }

    public void update(float delta){
        Vector2 pos = screenToWorldCoords(targetPosition);
//        System.out.println(pos);
//        MoveToAction mta = new MoveToAction();
//        mta.setPosition(pos.x, pos.y);
//        mta.setDuration(0f);
//        target.addAction(mta);
        target.setPosition(pos.x,pos.y);

        worldStage.act(delta);
    }

    public void render(){
        worldStage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        worldStage.getViewport().apply(true);
        worldStage.draw();
//        if(debug)
//            debugRenderer.render(worldPhysics, viewCam.combined);
    }
    public void resize(int width, int height){
        worldStage.getViewport().update(width,height, true);
    }
    public void dispose(){
        if(worldStage != null)
            worldStage.dispose();
    }

    public Vector2 screenToWorldCoords(Vector2 pos){
        return worldStage.screenToStageCoordinates(pos);
    }

}
