package com.kinglogic.game.ChemestryFramework;

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
    void Recieve(ChemicalEvent event);


}
