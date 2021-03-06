package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kinglogic.game.Actors.SelectedBlockActor;

/**
 * Created by chris on 4/20/2018.
 */

public class GUIManager {
    private static GUIManager instance;
    private Stage worldStage;
    private OrthographicCamera viewCam;
    private FitViewport view;

    public  Vector2 targetPosition = new Vector2(0,0);
    private Image target;
    private SelectedBlockActor selectedBlockActor;

    public String selectedBlockName = IDs.ins().getID(0);
    public Color selectedColor = IDs.ins().getColor(0);



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

        target = new Image(ResourceManager.ins().getVoxTex(IDs.getIDList().get(0)));
        worldStage.addActor(target);
        selectedBlockActor = new SelectedBlockActor();
        worldStage.addActor(selectedBlockActor);

    }

    public void update(float delta){
        //Vector2 pos = screenToWorldCoords(targetPosition);
//        System.out.println(pos);
//        MoveToAction mta = new MoveToAction();
//        mta.setPosition(pos.x, pos.y);
//        mta.setDuration(0f);
//        target.addAction(mta);
        //todo optimize this so its not every frame
        selectedBlockActor.UpdateSelectedBlock(selectedBlockName);
        target.setDrawable(selectedBlockActor.getSelectedDrawable());
        targetPosition.x = targetPosition.x % Gdx.graphics.getWidth();
        targetPosition.y = targetPosition.y % Gdx.graphics.getHeight();
        target.setColor(selectedColor);
        target.setPosition(targetPosition.x,targetPosition.y);

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
