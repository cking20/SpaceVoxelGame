package com.kinglogic.game.Managers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kinglogic.game.Actors.SelectedBlockActor;
import com.kinglogic.game.Player.PlayerBody;

import java.util.ArrayList;

/**
 * Created by chris on 4/20/2018.
 */

public class GUIManager {
    private static GUIManager instance;
    private Stage worldStage;
    private OrthographicCamera viewCam;
    private FitViewport view;


//    private Image target;
    private ArrayList<SelectedBlockActor> selectedBlockActors;


    public Label numBlocksLabel;
    public Label positionLabel;



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

//        target = new Image(ResourceManager.ins().getVoxTex(IDs.getIDList().get(0)));
//        worldStage.addActor(target);
        selectedBlockActors = new ArrayList<SelectedBlockActor>();
//        PlayerBody[] players = GameManager.ins().getPlayers();
//        for (int i = 0; i < players.length; i++) {
//            SelectedBlockActor s = new SelectedBlockActor();
//            s.scaleBy(4f);
//            s.moveBy(0,Gdx.graphics.getHeight()-80);
//            selectedBlockActors.add(s);
//            worldStage.addActor(s);
//        }


        positionLabel = new Label("Pos: 0,0", ResourceManager.ins().ui, "default");
        positionLabel.moveBy(80,Gdx.graphics.getHeight()-40);
        worldStage.addActor(positionLabel);
        numBlocksLabel = new Label("Resources: 0", ResourceManager.ins().ui, "default");
        numBlocksLabel.moveBy(80,Gdx.graphics.getHeight()-70);
        worldStage.addActor(numBlocksLabel);

    }

    public void update(float delta){
        //Vector2 pos = screenToWorldCoords(targetPosition);
//        System.out.println(pos);
//        MoveToAction mta = new MoveToAction();
//        mta.setPosition(pos.x, pos.y);
//        mta.setDuration(0f);
//        target.addAction(mta);
        //todo optimize this so its not every frame and shows each player
        positionLabel.setText("Pos: ("+(int)GameManager.ins().getPlayer().myBody.getPosition().x+", "+(int)GameManager.ins().getPlayer().myBody.getPosition().y+")");
        numBlocksLabel.setText("Resources: "+ControllerManager.ins().numBlocks+" NumGrids:"+WorldManager.ins().currentLevel.grids.size() + " Sector: "+WorldManager.ins().currentLevel.getCenterX()+","+WorldManager.ins().currentLevel.getCenterY());
        PlayerBody[] players = GameManager.ins().getPlayers();
        for (int i = 0; i < players.length; i++) {
            if(i >= selectedBlockActors.size()) {
                System.out.println("adding selected block actor");
                SelectedBlockActor newActor = new SelectedBlockActor();
                newActor.scaleBy(1f);//was 4
                //todo check this might affect the build pos?
//                newActor.moveBy(8,8);
                newActor.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
                selectedBlockActors.add(newActor);
                worldStage.addActor(newActor);
            }
            SelectedBlockActor current = selectedBlockActors.get(i);
            if(players[i].buildMode) {
                Vector2 targetPosition = players[i].getBuildPosition();
                current.UpdateSelectedBlock(players[i].playersCurrentBlock, players[i].playersCurrentColor);
                targetPosition.x = targetPosition.x % Gdx.graphics.getWidth();
                targetPosition.y = targetPosition.y % Gdx.graphics.getHeight();
                if (targetPosition.x < 0)
                    targetPosition.x = Gdx.graphics.getWidth() - 1;
                if (targetPosition.y < 0)
                    targetPosition.y = Gdx.graphics.getHeight() - 1;
                current.setPosition(targetPosition.x, targetPosition.y);
            }else
                current.setPosition(-1000,-1000);

        }


        worldStage.act(delta);
    }

    public void render(){
        worldStage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        worldStage.getViewport().apply(true);
        worldStage.draw();
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
