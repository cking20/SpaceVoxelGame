package com.kinglogic.game.Managers;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

/**
 * Created by chris on 4/12/2018.
 */

public class IDs {
//    public static final String METAL_TEX = "metal";
//    public static final String TECH_TEX = "tech";
    public static final String ROCK_TEX = "rock";
    public static final String BASE_TEX = "base";
    public static final String GRID_TEX = "grid";
    public static final String GRASS_TEX = "grass";

    private static IDs instance;
    private ArrayList<String> allowedIDsList;
    private ArrayList<Color> allowedColorsList;

    public static IDs ins(){
        if(instance == null)
            instance = new IDs();
        return instance;
    }
    private IDs(){
        allowedIDsList = getIDList();
        allowedColorsList = getColorList();
    }


    public static ArrayList<String> getIDList(){
        ArrayList<String> list = new ArrayList<String>();
        list = ResourceManager.ins().getAllVoxelIDs();
        return list;
    }

    public static ArrayList<Color> getColorList(){
        ArrayList<Color> list = new ArrayList<Color>();
        list.add(Color.WHITE);
        list.add(Color.GRAY);
        list.add(Color.BLACK);
        list.add(Color.PURPLE);
        list.add(Color.CYAN);
        list.add(Color.GREEN);
        list.add(Color.YELLOW);
        list.add(Color.ORANGE);
        list.add(Color.RED);

        return list;
    }

    public int getNumBlockIds(){
        return allowedIDsList.size();
    }
    public int getNumColorIds(){
        return allowedColorsList.size();
    }
    public int getIDNum(String s){
        return allowedIDsList.indexOf(s);
    }
    public String getID(int i){
        if(i >= allowedIDsList.size()) return allowedIDsList.get(0);
        return allowedIDsList.get(i);
    }
    public Color getColor(int i){
        if(i >= allowedColorsList.size()) return allowedColorsList.get(0);
        return allowedColorsList.get(i);
    }
}
