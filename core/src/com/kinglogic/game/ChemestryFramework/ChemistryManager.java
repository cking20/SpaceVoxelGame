package com.kinglogic.game.ChemestryFramework;


import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Actors.Entities.Entity;
import com.kinglogic.game.Constants;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.Models.WorldState;
import com.kinglogic.game.Physics.EntityBody;
import com.kinglogic.game.Physics.Grid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by chris on 8/7/2018.
 */

/**
 * Requires the World Manager to be instantiated first as hasProperty uses the entities
 */
public class ChemistryManager {
    private static ChemistryManager instance;
    private ArrayList<ChemicalEvent> eventQueue;
    private ArrayList<MaterialModel> updateQueue;
    private float simulationStep = Constants.CHEMISTRY_SIM_STEP;
    private float deltaTime = 0;

    public enum EventTypes{
        SHOT, TRIGGERED, DAMAGED, EXPLODED, TOUCHED,
        ON_FIRE, WET, ELECTROCUTE,
        SEND_WATER, SEND_AIR, SEND_LAVA,
        SEND_FORCE, SEND_GRAVITY
    }
    public enum Elements{
        FIRE, WATER, ELECTRICITY, AIR, PLASMA
    }

    public static ChemistryManager ins() {
        if(instance == null)
            instance = new ChemistryManager();
        return instance;
    }
    private ChemistryManager(){
        eventQueue = new ArrayList<ChemicalEvent>();
        updateQueue = new ArrayList<MaterialModel>();
    }
    public void EnqueueEvent(ChemicalEvent event){
        eventQueue.add(event);
    }

    /**
     * Advance the simulation
     */
    public void Update(float delta){
        deltaTime += delta;
        if(deltaTime > simulationStep){
            //call output for every thing in the update QUEUE and the entities
            for(int i = 0; i < updateQueue.size(); i++){
                LinkedList<ChemicalEvent> newEvents = updateQueue.get(i).Output();
                //see if it should be kept
                if(!updateQueue.get(i).Update(delta))
                    updateQueue.remove(i);
                if(newEvents != null)
                    eventQueue.addAll(newEvents);
            }

            //step through every entity and check whats behind it(queue 'collision' event from the entity)
            synchronized (WorldManager.ins().currentLevel.entities) {
                HashSet<EntityBody> clone = (HashSet<EntityBody>) WorldManager.ins().currentLevel.entities.clone();
                for (EntityBody e : clone){
                   ChemicalEvent contact = new ChemicalEvent();
                   contact.event = EventTypes.TOUCHED;
                   contact.sentBy = e;
                   contact.position = e.myBody.getPosition();
                   eventQueue.add(contact);
                }

            }
            //step through the eventQueue and process each, adding anything affected to the update queue
            for (int i = 0; i < eventQueue.size(); i++) {
                //entities shouldnt need to be looped through to find whats effected

                //events should be sent to grids, so that the grid can propagate them as needed
                //  and so that the voxel can callback to neighbors
                ChemicalEvent cur = eventQueue.get(i);
                ArrayList<Grid> atPos = WorldManager.ins().getGridsAtWorldPos(cur.position);
                for(Grid g : atPos){
                    g.Recieve(cur);
                }
            }
            eventQueue.clear();


            deltaTime = 0;
        }
    }
    public void CheckThis(MaterialModel toCheck){
        updateQueue.add(toCheck);
    }

    public void dispose(){

    }



}
