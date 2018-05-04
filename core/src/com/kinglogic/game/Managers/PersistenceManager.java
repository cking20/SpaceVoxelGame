package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.kinglogic.game.Models.GridStateModel;
import com.kinglogic.game.Models.SectorState;
import com.kinglogic.game.Models.SectorStateModel;
import com.kinglogic.game.Models.WorldState;
import com.kinglogic.game.Models.WorldStateModel;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.Grid;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by chris on 5/2/2018.
 */

public class PersistenceManager {
    private static PersistenceManager instance;

    private ArrayList<String> worlds;

    public static PersistenceManager ins(){
        if(instance == null)
            instance = new PersistenceManager();
        return instance;

    }

    private PersistenceManager(){
        worlds = new ArrayList<String>();

        File saveData = new File(Gdx.files.getLocalStoragePath()+"/savedata");
        saveData.mkdirs();
        for (File f : saveData.listFiles()) {
            worlds.add(f.getName());
        }



    }
    public void SaveCurretWorldState(){
        synchronized (WorldManager.ins().currentLevel) {
            System.out.println("saving "+WorldManager.ins().currentLevel);
            File worldDir = new File(Gdx.files.getLocalStoragePath() + "/savedata/" + WorldManager.ins().getWorldName());
            worldDir.mkdirs();
            //save the world state
            File worldState = new File(Gdx.files.getLocalStoragePath() + "/savedata/" + WorldManager.ins().getWorldName() + "/state.txt");

            try {
                worldState.createNewFile();
                FileOutputStream worldOS = new FileOutputStream(worldState);
                PrintWriter worldPW = new PrintWriter(worldOS,false);
                worldPW.write(WorldStateModel.jsonifyWorldState(WorldManager.ins().currentLevel));
                worldPW.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    SaveLevel(WorldManager.ins().currentLevel.sectors[i][j]);
                }
            }
        }
    }
    public void LoadWorld(String toLoad){
        File worldDir = new File(Gdx.files.getLocalStoragePath() + "/savedata/" + toLoad);
        worldDir.mkdirs();
        //save the world state

        try {
            File worldState = new File(Gdx.files.getLocalStoragePath() + "/savedata/" + toLoad + "/state.txt");
            FileInputStream worldDat = new FileInputStream(worldState);
            Scanner leveldatScanner = new Scanner(worldDat);
            String in = "";
            while (leveldatScanner.hasNext()){
                in+=leveldatScanner.next();
            }
            leveldatScanner.close();
            WorldState state = WorldStateModel.unjsonifyWorldState(new JSONObject(in));
            //todo this is bad code, the level must be set in the WM before the secotrs are laoaded because their load uses the WorldManager's world name
            WorldManager.ins().currentLevel = state;
            state.LoadUpSectors();
            //return state;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            WorldManager.ins().currentLevel = new WorldState(toLoad, 0,0);
            WorldManager.ins().currentLevel.LoadUpSectors();
        }

    }

    public void SaveLevel(SectorState s){
        String levelPath = Gdx.files.getLocalStoragePath()+"/savedata/"+WorldManager.ins().getWorldName()+"/"+s.name;
        File leveldir = new File(levelPath);
//        if(!leveldir.delete())
//            System.err.println("couldnt delete old data");
//        leveldir.mkdirs();
        Gdx.files.local("/savedata/"+WorldManager.ins().getWorldName()+"/"+s.name).emptyDirectory(true);
//        if(!Gdx.files.local("/savedata/"+WorldManager.ins().getWorldName()+"/"+s.name).delete())
//            System.err.println("couldnt delete old data");
        Gdx.files.local("/savedata/"+WorldManager.ins().getWorldName()+"/"+s.name).mkdirs();
        File gridsFile = new File(levelPath+"/grids");
        gridsFile.mkdirs();
        File ent = new File(levelPath+"/entities");
        ent.mkdirs();
        File leveldat = new File(levelPath+"/levelstate.txt");
        try {
            leveldat.createNewFile();
            FileOutputStream leveldatOutputStream = new FileOutputStream(leveldat);
            PrintWriter leveldatOutputWriter = new PrintWriter(leveldatOutputStream,false);
            leveldatOutputWriter.write(SectorStateModel.jsonifySectorState(s));
            leveldatOutputWriter.close();
            ArrayList<Grid> grids = WorldManager.ins().currentLevel.GetGridsInSector(s);
                for(Grid g: grids){
                    File gridFile = new File(levelPath+"/grids/"+g.name+".txt");
                    gridFile.createNewFile();
                    FileOutputStream gridOut = new FileOutputStream(gridFile);
                    PrintWriter gridPw = new PrintWriter(gridOut, false);
                    gridPw.write(GridStateModel.jsonifyGrid(g));
                    gridPw.close();
                }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public SectorState LoadLevel(int x, int y){
        SectorState ret;
        String levelPath = Gdx.files.getLocalStoragePath()+"/savedata/"+WorldManager.ins().getWorldName()+"/"+(x+","+y);
        File leveldir = new File(levelPath);
        if(leveldir.exists()){
            //load
            try{
                File leveldat = new File(levelPath+"/levelstate.txt");
                FileInputStream leveldatIn = new FileInputStream(leveldat);
                Scanner leveldatScanner = new Scanner(leveldatIn);
                String in = "";
                while (leveldatScanner.hasNext()){
                    in+=leveldatScanner.next();
                }
                leveldatScanner.close();
                ret = SectorStateModel.unjsonifySectorState(new JSONObject(in));
                ArrayList<Grid> loaded = new ArrayList<Grid>();
                File gridDir = new File(levelPath+"/grids");
                File[] gridFiles = gridDir.listFiles();
                for (int i = 0; i < gridFiles.length; i++) {
                    String gridJson = "";
                    FileInputStream fis = new FileInputStream(gridFiles[i]);
                    Scanner scanner = new Scanner(fis);
                    while (scanner.hasNext()){
                        gridJson+=scanner.next();
                    }
                    scanner.close();
                    DynamicGrid d = GridStateModel.unjsonifyDynamicGrid(new JSONObject(gridJson));
                    loaded.add(d);
                }
                WorldManager.ins().currentLevel.PopulateGrids(loaded);
                return ret;
            }catch (FileNotFoundException e){
                e.printStackTrace();
                return new SectorState(x+","+y,x,y, true);
            }
        }else {
            //create new
            return new SectorState(x+","+y,x,y, true);
        }
    }
}
