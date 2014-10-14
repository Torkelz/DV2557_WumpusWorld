package wumpusworld;

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
    private World world;
    private List<Coordinate> visited = new ArrayList<>();
    private List<Coordinate> visitedNeighbours = new ArrayList<>();
    private List<Coordinate> safeNeighbours = new ArrayList<>();
    private List<String> actionQueue = new ArrayList<>();
    private boolean foundWumpus = false;
    private Coordinate wumpusCoordinates;
    private HelperFunctions helper;
    
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param _world Current world state 
     */
    public MyAgent(World _world)
    {
        this.world = _world;
        helper = new HelperFunctions(this.world);
    }
    
    /**
     * Asks your solver agent to execute an action.
     */
    public void doAction()
    {
        //Location of the player
        Coordinate current = new Coordinate(world.getPlayerX(), world.getPlayerY());
        
        //Basic action:
        //Grab Gold if we can.
        if (world.hasGlitter(current.x, current.y))
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
        
        //
        if (!actionQueue.isEmpty()){
            world.doAction(actionQueue.get(0));
            actionQueue.remove(0);
            return;
        }
        
        if(visited.size() > 4){
            helper.goTo(new Coordinate(3,1), current, world.getDirection(), visited);
        }
        
        
        safeNeighbours.clear();
        visitedNeighbours.clear();
        if(!foundWumpus){
            Coordinate ret = helper.determineWumpus(visited);
            if(!ret.compare(new Coordinate(-1, -1))){
                foundWumpus = true;
                wumpusCoordinates = ret;
            }
        }
        
        if(!helper.isVisited(current, visited)){
            visited.add(current);
        }
        for (Coordinate c : visited){
            List<Coordinate> neighbours = helper.getSurroundingSquares(c);
            for (Coordinate n : neighbours){
                if (world.isUnknown(n.x, n.y) ){
                    if(!visitedNeighbours.contains(n)){
                        visitedNeighbours.add(n);
                    }
                    
                    //Only stench
                    if(world.hasStench(c.x, c.y) && !world.hasBreeze(c.x, c.y)){
                        if (helper.safeStench(n, wumpusCoordinates, foundWumpus) &&
                                !safeNeighbours.contains(n)){
                            safeNeighbours.add(n);
                        }
                    }
                    //Only breeze
                    else if(world.hasBreeze(c.x, c.y) && !world.hasStench(c.x, c.y)){
                        if (helper.safeBreeze(c) &&
                                !safeNeighbours.contains(n)){
                            safeNeighbours.add(n);
                        }
                    }
                    else if(!helper.isSafe(c)){ //Has both breeze and stench

                    }
                    else{ //Empty square
                        safeNeighbours.add(n);
                    }
                }
            }
        }
        
        //Get target coordinate.
        Coordinate newC = helper.getFacingCoordinate(current, world.getDirection());
        
        if(helper.isSafe(current) || world.isVisited(newC.x, newC.y))
        {
            if(!world.isValidPosition(newC.x, newC.y)){
                 if(helper.wall()){
                    return;
                 }
            }
            world.doAction(World.A_MOVE);
            return;
        }
        else if(world.hasStench(current.x, current.y) || world.hasBreeze(current.x, current.y)){
            
            if(foundWumpus && !world.hasBreeze(current.x, current.y)){
                //wumpus has been found and there's no breeze to worry about.
                if(!newC.compare(wumpusCoordinates)){
                    //Wumpus is not infront of us.
                    world.doAction(World.A_MOVE);
                    return;
                }
                else{
                    //Wumpus is infront of us and no safe alternatives but shooting is left.
                    if(safeNeighbours.isEmpty()){
                        world.doAction(World.A_SHOOT);
                        world.doAction(World.A_MOVE);
                    }
                }   
            }
            else{
                //wumpus has not been found / there's a breeze
                
                //Do 180 turn.
                world.doAction(World.A_TURN_LEFT);
                world.doAction(World.A_TURN_LEFT);
                return;
            }
        }
        else{
            return;
        }
    }
}
