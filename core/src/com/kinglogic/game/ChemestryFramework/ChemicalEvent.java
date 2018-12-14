package com.kinglogic.game.ChemestryFramework;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by chris on 8/7/2018.
 */

public class ChemicalEvent {
    public ChemistryManager.EventTypes event;
    public Vector2 position;
    public Vector2 direction;
    public MaterialModel sentBy;
    public ChemistryManager.Elements element;

    public ChemicalEvent clone(){
        ChemicalEvent n = new ChemicalEvent();
        n.element = this.element;
        n.position = this.position.cpy();
        if(this.direction != null)
            n.direction = this.direction.cpy();
        n.event = this.event;
        n.sentBy = this.sentBy;
        return n;
    }
}
