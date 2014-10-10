package wumpusworld;

import alice.tuprolog.InvalidTheoryException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contans starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan HagelbÃ¤ck
 */
public class MyAgent implements Agent
{
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
        
        public boolean compare(coordinates _c){
            if(x == _c.x && y == _c.y)
                return true;
            else
                return false;
        }
    }
    
    private World world;
    private List<coordinates> visited = new ArrayList<>();
    //private LogicHelper logicHelper;
    private boolean foundWumpus = false;
    private coordinates wumpusCoordinates;
    
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param _world Current world state 
     */
    public MyAgent(World _world)
    {
//        try {
//            this.logicHelper = new LogicHelper();
//        } catch (InvalidTheoryException | IOException ex) {
//            System.out.println("Error when creating logichelper" + ex.getMessage());
//        }
        this.world = _world;
    }
    
    /**
     * Asks your solver agent to execute an action.
     */
    public void doAction()
    {
        //String action = logicHelper.getMove(world);
        
//        if(action.compareTo("") != 0){
//            world.doAction(action);
//            System.out.println("Our action is: " + action);
//            return;
//        }
        
        //Location of the player
        int cX = world.getPlayerX();
        int cY = world.getPlayerY();
        coordinates c = new coordinates(cX, cY);
        
        
        if(!isVisited(c)){
            visited.add(c);
        }
        
        //Basic action:
        //Grab Gold if we can.
        if (world.hasGlitter(cX, cY))
        {
            world.doAction(World.A_GRAB);
            return;
        }
        
        //Basic action:
        //We are in a pit. Climb up.
        if (world.isInPit())
        {
            world.doAction(World.A_CLIMB);
            return;
        }
        
        //Test the environment
        if (world.hasBreeze(cX, cY))
        {
            System.out.println("I am in a Breeze");
        }
        if (world.hasStench(cX, cY))
        {
            System.out.println("I am in a Stench");
        }
        if (world.hasPit(cX, cY))
        {
            System.out.println("I am in a Pit");
        }
        if (world.getDirection() == World.DIR_RIGHT)
        {
            System.out.println("I am facing Right");
        }
        if (world.getDirection() == World.DIR_LEFT)
        {
            System.out.println("I am facing Left");
        }
        if (world.getDirection() == World.DIR_UP)
        {
            System.out.println("I am facing Up");
        }
        if (world.getDirection() == World.DIR_DOWN)
        {
            System.out.println("I am facing Down");
        }
        int newX = world.getPlayerX();
        int newY = world.getPlayerY();
        switch(world.getDirection()){
            case World.DIR_DOWN:
                newY--;
                break;
            case World.DIR_LEFT:
                newX--;
                break;
            case World.DIR_RIGHT:
                newX++;
                break;
            case World.DIR_UP:
                newY++;
                break;
        }
        
        if(wall())
            return;

        
        if(isSafe(cX, cY, newX, newY) || (world.isVisited(newX, newY) && !world.hasBreeze(newX, newY) && !world.hasStench(newX, newY)))
        {
            world.doAction(World.A_MOVE);
            return;
        }
        else{
            
            
            return;
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        //Random move actions
//        int rnd = (int)(Math.random() * 5);
//        if (rnd == 0) 
//        {
//            world.doAction(World.A_TURN_LEFT);
//            return;
//        }
//        if (rnd == 1)
//        {
//            world.doAction(World.A_TURN_RIGHT);
//            return;
//        }
//        if (rnd >= 2)
//        {
//            world.doAction(World.A_MOVE);
//            return;
//        }
    }
    
    private boolean isSafe(int _cX, int _cY, int _nX, int _nY){
        if(!world.hasBreeze(_cX, _cY) && !world.hasStench(_cX, _cY))
            return true;
        
        return false;
    }
    
    private boolean isVisited(coordinates c){
        for (coordinates v : visited) {
            if(c.compare(v))
                return true;
        }
        return false;
    }
    
    private boolean wall(){
        int newX = world.getPlayerX();
        int newY = world.getPlayerY();
        switch(world.getDirection()){
            case World.DIR_DOWN:
                newY--;
                break;
            case World.DIR_LEFT:
                newX--;
                break;
            case World.DIR_RIGHT:
                newX++;
                break;
            case World.DIR_UP:
                newY++;
                break;
        }
        if(!world.isValidPosition(newX, newY)){
            
            int lX = world.getPlayerX();
            int lY = world.getPlayerY();
            int direction = world.getDirection() + 1;
            if(direction > 3)
                direction = 0;
            switch(direction){
                case World.DIR_DOWN:
                    lY--;
                    break;
                case World.DIR_LEFT:
                    lX--;
                    break;
                case World.DIR_RIGHT:
                    lX++;
                    break;
                case World.DIR_UP:
                    lY++;
                    break;
            }
            if(world.isValidPosition(lX, lY)){
                world.doAction(World.A_TURN_RIGHT);
                return true;
            }
//            lX = world.getPlayerX();
//            lY = world.getPlayerY();
//            direction = world.getDirection() - 1;
//            if(direction < 0)
//                direction = 3;
//            switch(direction){
//                case World.DIR_DOWN:
//                    lY--;
//                    break;
//                case World.DIR_LEFT:
//                    lX--;
//                    break;
//                case World.DIR_RIGHT:
//                    lX++;
//                    break;
//                case World.DIR_UP:
//                    lY++;
//                    break;
//            }
//            if(world.isValidPosition(lX, lY)){
                world.doAction(World.A_TURN_LEFT);
                return true;
            //}
        }
        return false;
    }
    
    private void determineWumpus(){
        if(!foundWumpus){
            List<coordinates> stenches = new ArrayList<>();
            for (coordinates v : visited) {
                if(world.hasBreeze(v.x, v.y))
                    stenches.add(v);
            }
            List<coordinates> validWumpusSpots = new ArrayList<>();
            
            for (coordinates v : stenches) {
                List<coordinates> surrounding = getSurroundingSquares(v);
                
                
            }
        }
    }
    private List<coordinates> getSurroundingSquares(coordinates c){
        List<coordinates> list = new ArrayList<>();
        
        if(world.isValidPosition(c.x + 1, c.y))
            list.add(new coordinates(c.x + 1, c.y));
        if(world.isValidPosition(c.x - 1, c.y))
            list.add(new coordinates(c.x - 1, c.y));
        if(world.isValidPosition(c.x, c.y + 1))
            list.add(new coordinates(c.x, c.y + 1));
        if(world.isValidPosition(c.x, c.y - 1))
            list.add(new coordinates(c.x, c.y - 1));       
        return list;
    }
}
