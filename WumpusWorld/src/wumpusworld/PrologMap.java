/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;


/**
 *
 * @author Torkelz / Smurfa
 */
public enum PrologMap {
    BREEZE("perception(breeze,X,Y).\n"),
    STENCH("perception(stench,X,Y).\n"),
    GLITTER("perception(glitter,X,Y).\n"),
    PIT("perception(pit,X,Y).\n"),
    WUMPUS("perception(wumpus,X,Y).\n"),
    VISITED("visited(X,Y).\n");
    
    private String value;
    
    private PrologMap(String _value){
        this.value = _value;
    }
    
    public String getEntry(int x, int y){
        String s;
        s = value.replace("X", Integer.toString(x));
        s = s.replace("Y", Integer.toString(y));
        return s;
    }
    
    public String getValue(){
        return value;
    }
    
    public Theory getTheory() throws InvalidTheoryException{
        return new Theory(value);
    }
}
