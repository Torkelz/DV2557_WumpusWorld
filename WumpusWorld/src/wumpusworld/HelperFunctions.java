/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Torkelz / Smurfa
 */
public class HelperFunctions {
    public class ReturnValues{
        public List<String> actions;
        public int currentDir;
    }
    
    private World world;
    
    public HelperFunctions(World _world){
        this.world = _world;
    }
    
    public boolean isSafe(Coordinate _current){
        if(!world.hasBreeze(_current.x, _current.y) && !world.hasStench(_current.x, _current.y)){
            return true;
        }        
        return false;
    }
    
    /*
    * Determines if a coordinate is safe depending on if the wumpus has been found.
    * @return true if coordinate is safe
    */
    public boolean safeStench(Coordinate _current, Coordinate _wumpus,  boolean _wumpusFound){
        if (_wumpusFound && _current.compare(_wumpus)){
            return false;
        }
        else if (!_wumpusFound)
        {
            return false;
        }
        return true;
    }
    
    /*
    * Determines if an breeze has a pit discovered next to it.
    * @return true if at least one pit exists, false if no pit has been discovered
    */
    public boolean safeBreeze(Coordinate _current){
        List<Coordinate> neighbours = getSurroundingSquares(_current);
        
        for (Coordinate n : neighbours){
            if (world.isVisited(n.x, n.y) && world.hasPit(n.x, n.y)){
                return true;
            }
        }
        return false;
    }
    
    public boolean isVisited(Coordinate _c, List<Coordinate> _visited){
        for (Coordinate v : _visited) {
            if(_c.compare(v)){
                return true;
            }
        }
        return false;
    }
    
    public boolean isTurnLeftValid(Coordinate _current, int _dir){
        _dir--;        
        if(_dir < 0){
            _dir = 3;
        }
        Coordinate n = getFacingCoordinate(_current, _dir);
        if(world.isValidPosition(n.x, n.y)){
            return true;
        }
        return false;
    }
    
    public boolean isTurnRightValid(Coordinate _current, int _dir){
        _dir++;
        if(_dir > 3){
            _dir = 0;
        }
        Coordinate n = getFacingCoordinate(_current, _dir);
        if(world.isValidPosition(n.x, n.y)){
            return true;
        }
        return false;
    }
    
    /*
    * Handles if the player is facing the wall, and makes the player turn away.
    */
    public boolean wall(){
        Coordinate playerPosition = new Coordinate(world.getPlayerX(), world.getPlayerY());
        Coordinate c = getFacingCoordinate(playerPosition, world.getDirection());
        
        if(!world.isValidPosition(c.x, c.y)){
            if(isTurnRightValid(playerPosition, world.getDirection())){
                world.doAction(World.A_TURN_RIGHT);
                return true;
            }
            world.doAction(World.A_TURN_LEFT);
            return true;
        }
        return false;
    }
    
    /*
    * Determines if a wumpus can be found based on a set of coordinates.
    * @return the wumpus coordinate if found, (-1,-1) if not found
    */
    public Coordinate determineWumpus(List<Coordinate> _visited){
        //Get all visited stenches.
        List<Coordinate> stenches = new ArrayList<>();
        
        for (Coordinate v : _visited) {
            if(world.hasStench(v.x, v.y))
                stenches.add(v);
        }
        List<Coordinate> validWumpusSpots = new ArrayList<>();
        Map<Coordinate, Integer> commonSquares = new HashMap<>();
        for (Coordinate v : stenches) {
            //Get surrounding squares for each stench
            List<Coordinate> surrounding = getSurroundingSquares(v);
            for (Coordinate u : surrounding) {
                //If visited it's assumed to be safe.
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
        for(Map.Entry<Coordinate, Integer> entry : commonSquares.entrySet()) {
            if(entry.getValue() > 1)
                validWumpusSpots.add(entry.getKey());
        }
        //One common square -> wumpus found.
        if(validWumpusSpots.size() == 1){
            return validWumpusSpots.get(0);
        }
        else{
            //Not found, return invalid pos
            return new Coordinate(-1, -1);
        }
    }
    
    public List<Coordinate> getSurroundingSquares(Coordinate c){
        List<Coordinate> list = new ArrayList<>();
        
        if(world.isValidPosition(c.x + 1, c.y)){
            list.add(new Coordinate(c.x + 1, c.y));
        }
        if(world.isValidPosition(c.x - 1, c.y)){
            list.add(new Coordinate(c.x - 1, c.y));
        }
        if(world.isValidPosition(c.x, c.y + 1)){
            list.add(new Coordinate(c.x, c.y + 1));
        }
        if(world.isValidPosition(c.x, c.y - 1)){
            list.add(new Coordinate(c.x, c.y - 1));
        }
        
        return list;
    }
    
    /*
    * Finds a route for the player to a target coordinate. Utilize an A* internally.
    * @return a list of actions to go to the coordinate
    */
    public List<String> goTo(Coordinate _target, Coordinate _playerPos, int _direction,
            List<Coordinate> _visited){
        List<String> actions = new ArrayList<>();
        
        //Setup grid and relations
        List<Node> grid = new ArrayList<>();
        for (Coordinate v : _visited){
            grid.add(new Node(v));
        }
        for (Node g : grid){
            for (Node v : grid){
                if(v.coordinate.compare(new Coordinate(g.coordinate.x + 1, g.coordinate.y))){
                    g.neighbours.add(v);
                }
                if(v.coordinate.compare(new Coordinate(g.coordinate.x - 1, g.coordinate.y))){
                    g.neighbours.add(v);
                }
                if(v.coordinate.compare(new Coordinate(g.coordinate.x, g.coordinate.y + 1))){
                    g.neighbours.add(v);
                }
                if(v.coordinate.compare(new Coordinate(g.coordinate.x, g.coordinate.y - 1))){
                    g.neighbours.add(v);
                }
            }
        }
        
        //Search for best path using A*-algorithm
        List<Node> openList = new ArrayList<>();
        List<Node> closedList = new ArrayList<>();
        Node startNode = null;
        Node targetNode = null;
        for(Node n : grid){
            if(n.equals(new Node(_playerPos))){
                startNode = n;
            }
            if(n.equals(new Node(_target))){
                targetNode = n;
            }
        }
        openList.add(startNode);
        Map<Node, Node> path = new HashMap<>();
        while (!openList.isEmpty()){
            Node c = openList.get(0);
            openList.remove(0);
            if (c.coordinate.compare(_target)){
                return actions(path, targetNode,startNode, _direction);
            }
            closedList.add(c);
            for (Node n : c.neighbours){
                if (closedList.contains(n)){
                    continue;
                }
                int actionBetweenNodes = 1;
                int G = actionBetweenNodes + c.g;
                if (G < n.g){
                    n.g = G;
                    n.f = G + Math.sqrt(Math.pow((_target.x - n.coordinate.x), 2) + 
                            Math.pow((_target.y - n.coordinate.y), 2));
                    path.put(n, c);
                    if (!openList.contains(n)){
                        openList.add(n);
                    }
                }
            }
            Collections.sort(openList, DISTANCE);
        }
        return actions;
    }
    
    /*
    * Helper for goTo-function to build a list of actions.
    */
    private List<String> actions(Map<Node, Node> _path, Node _target, Node _start, int _playerDirection){
        List<String> actions = new ArrayList<>();
        List<Coordinate> coords = new ArrayList<>();
        
        //Get the path coordinates
        while(_path.containsKey(_target)){
            coords.add(_target.coordinate);
            _target = _path.get(_target);
        }
        Collections.reverse(coords);
        //Create a actionlist
        Coordinate start = _start.coordinate;
        for(Coordinate c : coords){
            
            if(!getFacingCoordinate(start, _playerDirection).compare(c)){
                ReturnValues ret = turnTo(start, c, _playerDirection);
                _playerDirection = ret.currentDir;
                actions.addAll(ret.actions);
                
            }
            actions.add(World.A_MOVE);
            start = c;
        }
        
        return actions;
    }
    
    public ReturnValues turnTo(Coordinate _start, Coordinate _end, int _playerDireciton){
        int startDir = _playerDireciton;
        
        startDir--;
        if(startDir < 0)
            startDir = 3;
        
        Coordinate n = getFacingCoordinate(_start, startDir);
        startDir = _playerDireciton;
        startDir++;
        if(startDir > 3)
            startDir = 0;
        Coordinate opposite = getFacingCoordinate(_start, startDir);
        
        ReturnValues ret = new ReturnValues();
        if(_end.compare(n)){
            //Found square when turning left.
            ret.actions = new ArrayList<>(Collections.nCopies(1, World.A_TURN_LEFT));
        }
        else if(_end.compare(opposite)){
            //Found square when turning right.
            ret.actions = new ArrayList<>(Collections.nCopies(1, World.A_TURN_RIGHT));
        }
        else{
            //Did not find right square when turning, must be behind us.
            ret.actions = new ArrayList<>(Collections.nCopies(2, World.A_TURN_RIGHT));
        }
        //update new direction.
        ret.currentDir = _playerDireciton;
        for( String s : ret.actions){
            if("l".equals(s)){
                ret.currentDir--;
                if(ret.currentDir < 0)
                    ret.currentDir = 3; 
            }
            else{
                ret.currentDir++;
                if(ret.currentDir > 3)
                    ret.currentDir = 0;
            }
        }
        
        return ret;
    }
    
    public Coordinate getFacingCoordinate(Coordinate _c, int _direction){
        Coordinate n = new Coordinate(_c);
        
        switch(_direction){
            case World.DIR_DOWN:
                n.y--;
                break;
            case World.DIR_LEFT:
                n.x--;
                break;
            case World.DIR_RIGHT:
                n.x++;
                break;
            case World.DIR_UP:
                n.y++;
                break;
        }
        
        return n;
    }
    
   public static Comparator<Node> DISTANCE = new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Double.compare(o1.f, o2.f);
            }
        };
}
