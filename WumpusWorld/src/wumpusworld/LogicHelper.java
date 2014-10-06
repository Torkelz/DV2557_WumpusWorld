/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Torkelz / Smurfa
 */
public class LogicHelper {
    private final Prolog engine = new Prolog();
    public LogicHelper() throws InvalidTheoryException, IOException{
        engine.addTheory(new Theory( MyAgent.class.getResourceAsStream("resources/KB.pl")));
    }
    
    public int getMove(World _world){
        
        addPerception(_world);
        
        String sss = engine.getTheory().toString();
        return 0;
    }
    
    private void addPerception(World _world){
        int x = _world.getPlayerX();
        int y = _world.getPlayerY();
        
        try {
            SolveInfo info = engine.solve("has_visited(" + Integer.toString(x) + "," + Integer.toString(y) + ").");
            
            if(info.toString().compareTo("yes.") == 0)
                return;
            
        } catch (MalformedGoalException ex) {
            Logger.getLogger(LogicHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        boolean empty = true;
        String theory = "";
        if(_world.hasBreeze(x, y)){
            theory += PrologMap.BREEZE.getEntry(x, y);
            empty = false;
        }
        if(_world.hasPit(x, y)){
            theory += PrologMap.PIT.getEntry(x, y);
            empty = false;
        }
        if(_world.hasStench(x, y)){
            theory += PrologMap.STENCH.getEntry(x, y);
            empty = false;
        }
        if(_world.hasWumpus(x, y)){
            theory += PrologMap.WUMPUS.getEntry(x, y);
            empty = false;
        }
        if(_world.hasGlitter(x, y)){
            theory += PrologMap.GLITTER.getEntry(x, y);
            empty = false;
        }
        if(empty){
            theory += PrologMap.EMPTY.getEntry(x, y);
        }
        
        
        try {
            engine.addTheory(new Theory(theory));
        } catch (InvalidTheoryException ex) {
            System.out.println("Error when adding theory in getMove() " + ex.getMessage());
        }
    }
}
