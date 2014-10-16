package wumpusworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wumpusworld.HelperFunctions.ReturnValues;

/**
 * Contains starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan Hagelbäck feat. Torkelz / Smurfa
 */
public class MyAgent implements Agent
{    
    private final List<Coordinate> visited = new ArrayList<>();
    private final List<Coordinate> visitedNeighbours = new ArrayList<>();
    private final List<Coordinate> safeNeighbours = new ArrayList<>();
    private final List<String> actionQueue = new ArrayList<>();
    private final Map<Coordinate, Integer> dangerMap = new HashMap<>();
    private final HelperFunctions helper;
    private final World world;

    private boolean foundWumpus = false;
    private Coordinate wumpusCoordinates;
    
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
    @Override
    public void doAction()
    {
        Coordinate current = new Coordinate(world.getPlayerX(), world.getPlayerY());

        if(!visited.contains(current)){
            visited.add(current);
        }
        //Grab Gold if we can.
        if (world.hasGlitter(current.x, current.y))
        {
            world.doAction(World.A_GRAB);
            return;
        }
        //We are in a pit. Climb up.
        if (world.isInPit())
        {
            world.doAction(World.A_CLIMB);
            return;
        }
        //Go through all actions we have added.
        if (!actionQueue.isEmpty()){
            String action = actionQueue.get(0);
            world.doAction(action);
            actionQueue.remove(0);
            System.out.println("ActionQueue launhced: " + action);
            return;
        }
        //Try to locate the wumpus.
        if(!foundWumpus && world.wumpusAlive()){
            Coordinate coord = helper.determineWumpus(visited);
            if(!coord.compare(new Coordinate(-1, -1))){
                foundWumpus = true;
                wumpusCoordinates = coord;
            }
        }
        
        safeNeighbours.clear();
        visitedNeighbours.clear();
        dangerMap.clear();
        //Creates a representation of the current known world.
        for (Coordinate c : visited){
            List<Coordinate> neighbours = helper.getSurroundingSquares(c);
            for (Coordinate neighbour : neighbours){
                if (world.isUnknown(neighbour.x, neighbour.y) ){
                    if(!visitedNeighbours.contains(neighbour)){
                        visitedNeighbours.add(neighbour);
                        dangerMap.put(neighbour, 0);
                    }
                    
                    if(visitedNeighbours.contains(neighbour)){
                        if(world.hasBreeze(c.x, c.y)){
                            Integer value = dangerMap.get(neighbour) + 1;
                            dangerMap.put(neighbour, value);
                        }
                        if(world.hasStench(c.x, c.y)){
                            Integer value = dangerMap.get(neighbour) + 2;
                            dangerMap.put(neighbour, value);
                        }
                    }
                    
                    //Only stench
                    if(world.hasStench(c.x, c.y) && !world.hasBreeze(c.x, c.y)){
                        if (helper.safeStench(neighbour, wumpusCoordinates, foundWumpus) &&
                                !safeNeighbours.contains(neighbour)){
                            safeNeighbours.add(neighbour);
                        }
                    }
                    //Only breeze
                    else if(world.hasBreeze(c.x, c.y) && !world.hasStench(c.x, c.y)){
                        if (helper.safeBreeze(c) &&
                                !safeNeighbours.contains(neighbour)){
                            safeNeighbours.add(neighbour);
                        }
                    }
                    //Has both breeze and stench. DO NOTHING, MUY IMPORTANTE!
                    else if(!helper.isSafe(c)){ 

                    }
                    //Empty square
                    else{ 
                        safeNeighbours.add(neighbour);
                    }
                }
            }
        }
        //Get target coordinate.
        Coordinate newC = helper.getFacingCoordinate(current, world.getDirection());
        
        //Fills the action queue.
        if(safeNeighbours.isEmpty()){
            if(foundWumpus && world.wumpusAlive()){
                if(newC.compare(wumpusCoordinates)){
                    actionQueue.add(World.A_SHOOT);
                    return;
                }
                //Calculate the closest stench square.
                Coordinate closest = null;
                float distance = Float.MAX_VALUE;
                for( Coordinate v : visited){
                    if(world.hasStench(v.x, v.y)){
                        float d = (float) Math.sqrt(Math.pow((v.x - current.x), 2) + 
                                Math.pow((v.y - current.y), 2));
                        if(d < distance){
                            distance = d;
                            closest = v;
                        }
                    }
                }
                actionQueue.addAll(helper.goTo(closest, current, world.getDirection(), visited));
                int estimatedDir = world.getDirection();
                for(String s : actionQueue){
                    if("l".equals(s)){
                        estimatedDir--;
                    }
                    else if("r".equals(s)){
                        estimatedDir++;
                    }
                    
                    if(estimatedDir > 3){
                        estimatedDir = 0;
                    }
                    if(estimatedDir < 0){
                        estimatedDir = 3;
                    }
                }
                
                ReturnValues ret = helper.turnTo(closest, wumpusCoordinates, estimatedDir);
                actionQueue.addAll(ret.actions);
            }
            //If wumpus have not been found or is dead.
            //Target the least dangerous square.
            else{
                Coordinate closest = null;
                float distance = Float.MAX_VALUE;
                int maxDanger = Integer.MAX_VALUE;
                
                for(Coordinate v : visitedNeighbours){
                    float d = (float) Math.sqrt(Math.pow((v.x - current.x), 2) + Math.pow((v.y - current.y), 2));
                    
                    if(dangerMap.get(v) <= maxDanger && d < distance){
                        maxDanger = dangerMap.get(v);
                        distance = d;
                        closest = v;
                    }
                    
                }
                List<Coordinate> safeAndVisited = new ArrayList<>();
                safeAndVisited.addAll(visited);
                safeAndVisited.add(closest);
                actionQueue.addAll(helper.goTo(closest, current, world.getDirection(), safeAndVisited));
            }
        }
        //Target the closest safe square.
        else{
            Coordinate closest = null;
            float distance = Float.MAX_VALUE;
            for( Coordinate v : safeNeighbours){
                float d = (float) Math.sqrt(Math.pow((v.x - current.x), 2) +Math.pow((v.y - current.y), 2));
                if(d < distance){
                    distance = d;
                    closest = v;
                }
            }
            List<Coordinate> safeAndVisited = new ArrayList<>();
            safeAndVisited.addAll(visited);
            safeAndVisited.addAll(safeNeighbours);
            actionQueue.addAll(helper.goTo(closest, current, world.getDirection(), safeAndVisited));
            if(actionQueue.isEmpty()){
                actionQueue.add(World.A_MOVE);
            }
        }
        
        if(!actionQueue.isEmpty()){
            world.doAction(actionQueue.get(0));
            actionQueue.remove(0);
        }
    }    
}