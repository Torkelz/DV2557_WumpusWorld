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
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Torkelz / Smurfa
 */
public class LogicHelper {
    class coordinates{
        int x;
        int y;
        public coordinates(){
            this.x = 0;
            this.y = 0;
        }
        public coordinates(int _x, int _y){
            this.x = _x;
            this.y = _y;
        }
    }
    
    
    private final Prolog engine = new Prolog();
    public LogicHelper() throws InvalidTheoryException, IOException{
        engine.addTheory(new Theory( MyAgent.class.getResourceAsStream("resources/KB.pl")));
    }
    
    public String getMove(World _world){
        int x = _world.getPlayerX();
        int y = _world.getPlayerY();
        addPerception(_world);
        
        int lookDirection = _world.getDirection();
        int lookX = x;
        int lookY = y;
        switch (lookDirection){
            case World.DIR_UP:{
                ++lookY;
                break;
            }
            case World.DIR_DOWN:{
                --lookY;
                break;
                }
            case World.DIR_LEFT:{
                --lookX;
                break;
            }
            case World.DIR_RIGHT:{
                ++lookX;
                break;
            }
        }

        try {
            //DEBUG
            String s = engine.getTheory().toString();
            SolveInfo info;
            
            
            info = engine.solve("wumpus(["+ Integer.toString(lookX) + "," + Integer.toString(lookY) + "]).");
            if(yes(info.toString())){
                String h = info.toString();
                int erxtra = 0;
            
                while (info.isSuccess()){
                    System.out.println("solution: "+info.getSolution()+" - bindings: "+info + "SOS: " + info.getSetOfSolution());
                    System.out.println("Extra: "+info.getVarValue("Z"));
                    if (engine.hasOpenAlternatives()){
                        info=engine.solveNext();
                    } 
                    else {
                        break;
                    }
                }
            }
            
            info = engine.solve("glitter([" + Integer.toString(x) + "," + Integer.toString(y) +"]).");
            if(yes(info.toString()))
            {
                return World.A_GRAB;
            }
            info = engine.solve("pit([" + Integer.toString(x) + "," + Integer.toString(y) +"]).");
            if(yes(info.toString()) && _world.isInPit())
            {
                return World.A_CLIMB;
            }
            
            info = engine.solve("perception(["+ Integer.toString(x) + "," + Integer.toString(y) + "]).");
            if(!yes(info.toString())){
                System.out.println("Safe(" + x + "," +y +")");
                
                coordinates c = getCoord(x, y, _world.getDirection());
                if(_world.isValidPosition(c.x, c.y)){                
                    return World.A_MOVE;
                }
                else{
                    /// Turn left
                    int dir = _world.getDirection();
                    dir--;
                    if(dir < 0) dir = 3;
                    c = getCoord(x, y, dir);
                    if(_world.isValidPosition(c.x, c.y)){
                        return World.A_TURN_LEFT;
                    }
                    /// Turn right
                    dir = _world.getDirection();
                    dir++;
                    if(dir > 3) dir = 0;
                    c = getCoord(x, y, dir);
                    if(_world.isValidPosition(c.x, c.y)){
                        return World.A_TURN_RIGHT;
                    }                    
                }
            }
        } 
        catch (Exception e) {
            System.out.println("Error when checking for safe squares " + e.getMessage());
        }
        
        
        
        
        
        
        
        
        
        
//        List<coordinates> possibleSquares = new ArrayList<>();
//        for(int i = World.DIR_UP; i < World.DIR_LEFT; ++i){
//            coordinates c = getCoord(x, y, i);
//            if(_world.isValidPosition(c.x, c.y))
//                possibleSquares.add(c);
//        }
//        List<coordinates> safeSquares = new ArrayList<>();
//        
//        for(coordinates c : possibleSquares){
//            try {
//                String s = "perception(X," + Integer.toString(x) + "," + Integer.toString(y) +").";
//                
//                SolveInfo info = engine.solve(s);
//                if(info.isSuccess()){
//                    safeSquares.add(c);
//                    System.out.println("Safe(" + x + "," +y +")");
//                }
//            } catch (Exception e) {
//                System.out.println("Error when checking for safe squares " + e.getMessage());
//            }
//        }
//        
//        
//        String sss = engine.getTheory().toString();
        return "";
    }
    
    private void addPerception(World _world){
        int x = _world.getPlayerX();
        int y = _world.getPlayerY();
        String theory = "";

        try{
            SolveInfo info;
            if(_world.hasBreeze(x, y)){
                String s = PrologMap.BREEZE.getEntry(x, y);
                info = engine.solve(s);
                if(!yes(info.toString())){
                    theory += s;
                }
            }
            else{
                String s = PrologMap.BREEZE.getNotEntry(x, y);
                info = engine.solve(s);
                if(yes(info.toString())){
                    theory += s;
                }
            }
            if(_world.hasPit(x, y)){
                String s = PrologMap.PIT.getEntry(x, y);
                info = engine.solve(s);
                if(!yes(info.toString())){
                    theory += s;
                }
            }
//            else{
//                String s = PrologMap.PIT.getNotEntry(x, y);
//                info = engine.solve(s);
//                if(yes(info.toString())){
//                    theory += s;
//                }
//            }
            if(_world.hasStench(x, y)){
                String s = PrologMap.STENCH.getEntry(x, y);
                info = engine.solve(s);
                if(!yes(info.toString())){
                    //theory += s;
                    
                    engine.solve("add_stench([" + Integer.toString(x) + "," + Integer.toString(y) +"]).");
                }
            }
            else{
                String s = PrologMap.STENCH.getNotEntry(x, y);
                info = engine.solve(s);
                if(yes(info.toString())){
                    theory += s;
                }
            }
            if(_world.hasWumpus(x, y)){
                String s = PrologMap.WUMPUS.getEntry(x, y);
                info = engine.solve(s);
                if(!yes(info.toString())){
                    theory += s;
                }
            }
            if(_world.hasGlitter(x, y)){
                String s = PrologMap.GLITTER.getEntry(x, y);
                info = engine.solve(s);
                if(!yes(info.toString())){
                    theory += s;
                }
            }
            String s = PrologMap.VISITED.getEntry(x, y);
            info = engine.solve(s);
            if(!yes(info.toString())){
                theory += s;
            }
        }
        catch (MalformedGoalException ex) {
            Logger.getLogger(LogicHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            engine.addTheory(new Theory(theory));
        } catch (InvalidTheoryException ex) {
            System.out.println("Error when adding theory in getMove() " + ex.getMessage());
        }
    }
    
    private boolean yes(String _string){
        if(_string.contains("yes.") || _string.contains("yes")){
            return true;
        }
        else{
            return false;
        }
    }
    private coordinates getCoord(int _x, int _y, int _direction){
        switch(_direction){
            case World.DIR_DOWN:
                return new coordinates(_x, _y-1);
            case World.DIR_UP:
                return new coordinates(_x, _y+1);
            case World.DIR_LEFT:
                return new coordinates(_x-1, _y);
            case World.DIR_RIGHT:
                return new coordinates(_x+1, _y);
            default:
                return new coordinates();
        }
    }
    
//    public List<coordinates> getAllPerceptions(PrologMap _perception) throws MalformedGoalException{
//        List<coordinates> list = new ArrayList<>();
//        
//        SolveInfo solve = engine.solve(_perception.getAll());
//        while(solve.isSuccess()){
//            
//        }        
//    }
}