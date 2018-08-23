package com.kinglogic.game.ChemestryFramework;

import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

/**
 * Created by chris on 8/7/2018.
 */

public interface MaterialModel {
    /**
     * @return any new events to queue
     */
    LinkedList<ChemicalEvent> Output();

    /**
     * @return true iff this model needs to update again
     */
    boolean Update(float delta);

    /**
     * Handle the event and possibly change state
     * @param event
     */
    void Receive(ChemicalEvent event);

    /**
     * return the element of the model
     * @return
     */
    ChemistryManager.Elements getPrimaryElement();

    /**
     * React to a collision between models
     * @param model the model that touched this model
     */
    void ReactToTouch(MaterialModel model, Vector2 worldPosition);


}
