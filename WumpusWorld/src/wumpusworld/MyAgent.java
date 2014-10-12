package wumpusworld;

import alice.tuprolog.InvalidTheoryException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
        @Override
        public int hashCode() {
            return x*10 + y;
        }

        //Compare only account numbers
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            coordinates other = (coordinates) obj;
            if (x != other.x || y != other.y)
                return false;
            return true;
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

        
        if(isSafe(cX, cY, newX, newY) || world.isVisited(newX, newY))
        {
            world.doAction(World.A_MOVE);
            return;
        }
        else if(world.hasStench(cX, cY) || world.hasBreeze(cX, cY)){
            determineWumpus();
            if(foundWumpus && !world.hasBreeze(cX, cY)){
                //wumpus has been found and there's no breeze to worry about.
                coordinates n = new coordinates(newX, newY);
                if(!n.compare(new coordinates(cX, cY))){
                    //Wumpus is not infront of us.
                    world.doAction(World.A_MOVE);
                    return;
                }
                else{
                    //Wumpus is infront of us.
                }   
            }
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
        if(!world.hasBreeze(_cX, _cY) && !world.hasStench(_cX, _cY)){
            return true;
        }        
        return false;
    }
    
    private boolean isVisited(coordinates c){
        for (coordinates v : visited) {
            if(c.compare(v))
                return true;
        }
        return false;
    }
    private boolean isTurnLeftValid(int _x, int _y, int _dir){
        _dir--;
        if(_dir < 0)
            _dir = 3;
        switch(_dir){
            case World.DIR_DOWN:
                _y--;
                break;
            case World.DIR_LEFT:
                _x--;
                break;
            case World.DIR_RIGHT:
                _x++;
                break;
            case World.DIR_UP:
                _y++;
                break;
        }
        if(world.isValidPosition(_x, _y)){
            return true;
        }
        return false;
    }
    
    private boolean isTurnRightValid(int _x, int _y, int _dir){
        _dir++;
        if(_dir > 3)
            _dir = 0;
        switch(_dir){
            case World.DIR_DOWN:
                _y--;
                break;
            case World.DIR_LEFT:
                _x--;
                break;
            case World.DIR_RIGHT:
                _x++;
                break;
            case World.DIR_UP:
                _y++;
                break;
        }
        if(world.isValidPosition(_x, _y)){
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
            if(isTurnRightValid(world.getPlayerX(), world.getPlayerY(), world.getDirection())){
                world.doAction(World.A_TURN_RIGHT);
                return true;
            }
            
            world.doAction(World.A_TURN_LEFT);
            return true;
        }
        return false;
    }
    
    private void determineWumpus(){
        if(!foundWumpus){
            //Get all visited stenches.
            List<coordinates> stenches = new ArrayList<>();
            for (coordinates v : visited) {
                if(world.hasStench(v.x, v.y))
                    stenches.add(v);
            }
            List<coordinates> validWumpusSpots = new ArrayList<>();
            Map<coordinates, Integer> commonSquares = new HashMap<>();
            for (coordinates v : stenches) {
                //Get surrounding squares for each stench
                List<coordinates> surrounding = getSurroundingSquares(v);
                for (coordinates u : surrounding) {
                    //If visites it's assumed to be safe.
                    if(world.isVisited(u.x, u.y)){
                        continue;
                    }
                    
                    if(!commonSquares.containsKey(u)){
                        commonSquares.put(u, 1);
                    }
                    else{
                        int value = commonSquares.get(u);
                        commonSquares.put(u, value + 1);
                    }
                }
            }
            //Any common squares?
            for(Entry<coordinates, Integer> entry : commonSquares.entrySet()) {
                if(entry.getValue() > 1)
                    validWumpusSpots.add(entry.getKey());
            }
            
            //one common square -> wumpus found.
            if(validWumpusSpots.size() == 1){
                foundWumpus = true;
                wumpusCoordinates = validWumpusSpots.get(0);
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
