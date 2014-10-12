/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Torkelz
 */
public class HelperFunctions {
    private World world;
    public HelperFunctions(World _world){
        this.world = _world;
    }
    
    public boolean isSafe(int _cX, int _cY, int _nX, int _nY){
        if(!world.hasBreeze(_cX, _cY) && !world.hasStench(_cX, _cY)){
            return true;
        }        
        return false;
    }
    
    public boolean isVisited(Coordinate _c, List<Coordinate> _visited){
        for (Coordinate v : _visited) {
            if(_c.compare(v))
                return true;
        }
        return false;
    }
    
    public boolean isTurnLeftValid(int _x, int _y, int _dir){
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
    
    public boolean isTurnRightValid(int _x, int _y, int _dir){
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
        
    public boolean wall(){
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
        for(Map.Entry<Coordinate, Integer> entry : commonSquares.entrySet()) {
            if(entry.getValue() > 1)
                validWumpusSpots.add(entry.getKey());
        }

        //one common square -> wumpus found.
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
        
        if(world.isValidPosition(c.x + 1, c.y))
            list.add(new Coordinate(c.x + 1, c.y));
        if(world.isValidPosition(c.x - 1, c.y))
            list.add(new Coordinate(c.x - 1, c.y));
        if(world.isValidPosition(c.x, c.y + 1))
            list.add(new Coordinate(c.x, c.y + 1));
        if(world.isValidPosition(c.x, c.y - 1))
            list.add(new Coordinate(c.x, c.y - 1));       
        return list;
    }
}
