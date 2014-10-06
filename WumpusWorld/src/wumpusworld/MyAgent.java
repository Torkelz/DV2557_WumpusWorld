package wumpusworld;

import alice.tuprolog.Int;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;
import alice.tuprolog.event.OutputEvent;
import alice.tuprolog.event.OutputListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contans starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan HagelbÃ¤ck
 */
public class MyAgent implements Agent
{
    private World w;
    
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;
    }
    
    /**
     * Asks your solver agent to execute an action.
     */
    public void doAction()
    {
        Prolog engine = new Prolog();

        String theory = " stench(1, 2).\n"+
                " stench(2, 1).\n"+
                " breeze(1, 1).\n"+
                " breeze(1, 4).\n";
                
        String theory2 = " has_stench(X, Y) :- stench(X, Y).\n" +
                " has_stench(X, Y) :- stench(X, Z), has_stench(Z, Y).\n";
        try {
            Theory t = new Theory(theory);
                        Theory t3 = new Theory(theory2);

            
            Theory t2 = new Theory( MyAgent.class.getResourceAsStream("resources/KB.pl"));
            engine.addTheory(t);
            engine.addTheory(t3);

            SolveInfo info = engine.solve("has_stench(1, 2).");
            
            System.out.println(info);
            
            while (info.isSuccess()){
                System.out.println("solution: "+info.getSolution()+" - bindings: "+info);
                if (engine.hasOpenAlternatives()){
                    info=engine.solveNext();
                } 
                else {
                    break;
                }
            }
            
            
        } catch (InvalidTheoryException | MalformedGoalException | NoSolutionException | NoMoreSolutionException ex) {
            Logger.getLogger(MyAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();
        
        //Basic action:
        //Grab Gold if we can.
        if (w.hasGlitter(cX, cY))
        {
            w.doAction(World.A_GRAB);
            return;
        }
        
        //Basic action:
        //We are in a pit. Climb up.
        if (w.isInPit())
        {
            w.doAction(World.A_CLIMB);
            return;
        }
        
        //Test the environment
        if (w.hasBreeze(cX, cY))
        {
            System.out.println("I am in a Breeze");
        }
        if (w.hasStench(cX, cY))
        {
            System.out.println("I am in a Stench");
        }
        if (w.hasPit(cX, cY))
        {
            System.out.println("I am in a Pit");
        }
        if (w.getDirection() == World.DIR_RIGHT)
        {
            System.out.println("I am facing Right");
        }
        if (w.getDirection() == World.DIR_LEFT)
        {
            System.out.println("I am facing Left");
        }
        if (w.getDirection() == World.DIR_UP)
        {
            System.out.println("I am facing Up");
        }
        if (w.getDirection() == World.DIR_DOWN)
        {
            System.out.println("I am facing Down");
        }
        
        //Random move actions
        int rnd = (int)(Math.random() * 5);
        if (rnd == 0) 
        {
            w.doAction(World.A_TURN_LEFT);
            return;
        }
        if (rnd == 1)
        {
            w.doAction(World.A_TURN_RIGHT);
            return;
        }
        if (rnd >= 2)
        {
            w.doAction(World.A_MOVE);
            return;
        }
    }
}
