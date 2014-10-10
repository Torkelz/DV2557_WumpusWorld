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
    BREEZE("breeze", "([X,Y])"),
    STENCH("stench", "([X,Y])"),
    GLITTER("glitter", "([X,Y])"),
    PIT("pit","([X,Y])"),
    WUMPUS("wumpus","([X,Y])"),
    VISITED("visited","([X,Y])");
    
    private String value;
    private String identifier;
    private String par;
    
    private PrologMap(String _value, String _par){
        this.value = _value + _par;
        this.identifier = _value;
        this.par = _par;
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
    
    public String addEntry(int _x, int _y){
        String s;
        s = par.replace("X", Integer.toString(_x));
        s = s.replace("Y", Integer.toString(_y));
        return "add_" + identifier + s +".";
    }
    
    public String addNotEntry(int _x, int _y){
        String s;
        s = par.replace("X", Integer.toString(_x));
        s = s.replace("Y", Integer.toString(_y));
        return "add_not_" + identifier + s +".";
    }
    
    public String getValue(){
        return value;
    }
     public String getValue(int _x, int _y){
        String s;
        s = value.replace("X", Integer.toString(_x));
        s = s.replace("Y", Integer.toString(_y));
        return s;
    }
    
    public Theory getTheory() throws InvalidTheoryException{
        return new Theory(value);
    }
}
