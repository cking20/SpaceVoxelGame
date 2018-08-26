package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kinglogic.game.ChemestryFramework.ChemistryManager;
import com.kinglogic.game.Player.PlayerBody;
import com.kinglogic.game.TestInputProcessor;

import java.util.ArrayList;


/**
 * Created by chris on 4/1/2018.
 */

public class GameManager {
    private static GameManager instance;
    private ArrayList<PlayerBody> thePlayers;
    private boolean gameOver = false;

    public static GameManager ins() {
        if(instance == null)
            instance = new GameManager();
        return instance;
    }

    private GameManager(){
        thePlayers = new ArrayList<PlayerBody>();
//        thePlayers.add(new PlayerBody("player", new Vector2(0,0)));
        CameraManager.ins();
        ResourceManager.ins();
        WorldManager.ins();
        ChemistryManager.ins();
        PersistenceManager.ins().LoadWorld("infinity");
//        WorldManager.ins().addEntityToWorld(thePlayers.get(0));
//        WorldManager.ins().addEntityToWorld(thePlayer);
//        WorldManager.ins().ApplyLightToBody(thePlayer.myBody);
//        CameraManager.ins().Track(thePlayers.get(0).view);
        GUIManager.ins();
        SoundManager.ins();
        ControllerManager.ins();
    }

//    public PlayerBody getThePlayer(){
//        return thePlayer;
//    }
    public PlayerBody addPlayer(){
        PlayerBody player = new PlayerBody("player", new Vector2(0,0));
        thePlayers.add(player);
        WorldManager.ins().addEntityToWorld(player);
        //todo implement this after player's views are originized
//        CameraManager.ins().addToWatch(player.view);
        CameraManager.ins().Track(getPlayer().view);
        return player;
    }
    public void removePlayer(int playerID){
        PlayerBody toRemove = thePlayers.get(playerID);
        thePlayers.remove(playerID);
        WorldManager.ins().removeEntityFromWorld(toRemove);
        //todo implement this after player's views are originized
//        CameraManager.ins().removeFromWatch(toRemove.view);
    }
    public PlayerBody[] getPlayers(){
        PlayerBody[] players = new PlayerBody[thePlayers.size()];
        for (int i = 0; i < thePlayers.size(); i++) {
            players[i] = thePlayers.get(i);
        }
        return players;
    }

    public PlayerBody getPlayer(int id){

        while(id >= thePlayers.size())
            addPlayer();
        return thePlayers.get(id);
    }

    /**
     * Should be used for keyboard input only
     * @return
     */
    public PlayerBody getPlayer(){
        if(thePlayers.size() == 0)
            this.addPlayer();
        return thePlayers.get(0);
    }

    public void Update(float delta){
        if(gameOver){
            gameOver = !gameOver;
            ControllerManager.ins().numBlocks = 0;
            //todo show game over screen
//            thePlayer.myBody.setTransform(0,0,0);
        }
    }
    public void GameOver(){
        gameOver = true;
    }

    public void dispose(){
        ResourceManager.ins().dispose();
        WorldManager.ins().dispose();
        GUIManager.ins().dispose();
        SoundManager.ins().dispose();
        ControllerManager.ins().dispose();
        ChemistryManager.ins().dispose();
    }

}
