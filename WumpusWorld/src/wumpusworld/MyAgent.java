package wumpusworld;

import alice.tuprolog.InvalidTheoryException;
import java.io.IOException;

/**
 * Contans starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan HagelbÃ¤ck
 */
public class MyAgent implements Agent
{
    private World world;
    //private LogicHelper logicHelper;
    
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
        
        //Random move actions
        int rnd = (int)(Math.random() * 5);
        if (rnd == 0) 
        {
            world.doAction(World.A_TURN_LEFT);
            return;
        }
        if (rnd == 1)
        {
            world.doAction(World.A_TURN_RIGHT);
            return;
        }
        if (rnd >= 2)
        {
            world.doAction(World.A_MOVE);
            return;
        }
    }
}
