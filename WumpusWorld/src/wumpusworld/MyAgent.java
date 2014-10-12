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
        int cX = world.getPlayerX();
        int cY = world.getPlayerY();
        Coordinate c = new Coordinate(cX, cY);
        
        
        if(!helper.isVisited(c, visited)){
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
        
        if(helper.isSafe(cX, cY, newX, newY) || world.isVisited(newX, newY))
        {
            if(!world.isValidPosition(newX, newY)){
                 if(helper.wall())
                    return;
            }
                
            world.doAction(World.A_MOVE);
            return;
        }
        else if(world.hasStench(cX, cY) || world.hasBreeze(cX, cY)){
            Coordinate ret = helper.determineWumpus(visited);
            if(!ret.compare(new Coordinate(-1, -1))){
                foundWumpus = true;
                wumpusCoordinates = ret;
            }
            
            if(foundWumpus && !world.hasBreeze(cX, cY)){
                //wumpus has been found and there's no breeze to worry about.
                Coordinate n = new Coordinate(newX, newY);
                if(!n.compare(new Coordinate(cX, cY))){
                    //Wumpus is not infront of us.
                    world.doAction(World.A_MOVE);
                    return;
                }
                else{
                    //Wumpus is infront of us.
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
