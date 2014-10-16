/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

/**
 *
 * @author Torkelz
 */
public class Coordinate {
    int x;
    int y;
    public Coordinate(){
        this.x = 0;
        this.y = 0;
    }
    
    public Coordinate(int _x, int _y){
        this.x = _x;
        this.y = _y;
    }
    
    public Coordinate(Coordinate _copy){
        this.x = _copy.x;
        this.y = _copy.y;
    }
    
    public boolean compare(Coordinate _c){
        if(x == _c.x && y == _c.y)
            return true;
        else
            return false;
    }
    @Override
    public int hashCode() {
        return x * 10 + y;
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
        Coordinate other = (Coordinate) obj;
        if (x != other.x || y != other.y)
            return false;
        return true;
    }
}
