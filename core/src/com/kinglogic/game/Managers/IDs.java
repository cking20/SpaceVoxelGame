package com.kinglogic.game.Managers;

import java.util.ArrayList;

/**
 * Created by chris on 4/12/2018.
 */

public class IDs {
    public static final String METAL_TEX = "metal";
    public static final String TECH_TEX = "tech";
    public static final String ROCK_TEX = "rock";
    public static final String BASE_TEX = "base";

    private static IDs instance;
    private ArrayList<String> allowedIDsList;
    private int numBlockIds;

    public static IDs ins(){
        if(instance == null)
            instance = new IDs();
        return instance;
    }
    private IDs(){
        allowedIDsList = getIDList();
    }


    public static ArrayList<String> getIDList(){
        ArrayList<String> list = new ArrayList<String>();
        list.add(METAL_TEX);
        list.add(TECH_TEX);
        list.add(ROCK_TEX);
        list.add(BASE_TEX);
        return list;
    }

    public int getNumBlockIds(){
        return allowedIDsList.size();
    }
    public int getIDNum(String s){
        return allowedIDsList.indexOf(s);
    }

    public String getID(int i){
        System.out.println("returning "+allowedIDsList.get(i));
        return allowedIDsList.get(i);
    }
}
