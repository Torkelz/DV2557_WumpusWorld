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
    BREEZE("breeze([X,Y])"),
    STENCH("stench([X,Y])"),
    GLITTER("glitter([X,Y])"),
    PIT("pit([X,Y])"),
    WUMPUS("wumpus([X,Y])"),
    VISITED("visited([X,Y])");
    
    private String value;
    
    private PrologMap(String _value){
        this.value = _value;
    }
    
    public String getEntry(int x, int y){
        String s;
        s = value.replace("X", Integer.toString(x));
        s = s.replace("Y", Integer.toString(y));
        return s + ".\n";
    }
    public String getNotEntry(int x, int y){
        String s;
        s = value.replace("X", Integer.toString(x));
        s = s.replace("Y", Integer.toString(y));
        return "not(" + s + ").\n";
    }
    
    public String getValue(){
        return value;
    }
    
    public String getAll(){
        String s;
        s = value.replace("[X,Y]", "X");
        return s + ".\n";
    }
    
    public Theory getTheory() throws InvalidTheoryException{
        return new Theory(value);
    }
}
