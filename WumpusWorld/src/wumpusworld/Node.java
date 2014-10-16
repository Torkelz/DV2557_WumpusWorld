/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to A*-algorithm.
 * @author Torkelz / Smurfa
 */
public class Node {
    public Coordinate coordinate;
    public List<Node> neighbours;
    public int g;
    public double f;
    
    public Node(Coordinate _coordinate){
        this.coordinate = new Coordinate(_coordinate);
        this.neighbours = new ArrayList<>();
        this.g = Integer.MAX_VALUE;
        this.f = 0;
    }

    //Compare only account numbers
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;

        }
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        Node other = (Node) obj;
        
        return !(coordinate.x != other.coordinate.x || coordinate.y != other.coordinate.y);
    }
}
