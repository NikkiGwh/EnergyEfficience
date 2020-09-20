package com.example.energyefficience;


import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class DjikstraImplementation {
    public static List<Knot> computeSynchronouse( Knot[][] matrix, Knot start, Knot destination){
        // minHeap -- keeps the min element always on top for quick access for next knot
        //todo priorityQueue implementieren
        PriorityQueue<Knot> openList = new PriorityQueue<Knot>(11, new KnotComparator());
        openList.add(start);
        Knot current = start;
        Knot next = null;
        while(!openList.isEmpty() && !current.equals(destination))
        {
            current = openList.poll();
            for(int i = 0; i < 4; i++)
            {
                switch (i) {
                    case 0:
                        if(!(current.getX() - 1 < 0) && !matrix[current.getX() - 1][current.getY()].isObstacle())
                            next = matrix[current.getX() - 1][current.getY()];
                        break;
                    case 1:
                        if(!(current.getX() + 1 > matrix.length-1) && !matrix[current.getX() + 1][current.getY()].isObstacle())
                             next = matrix[current.getX() + 1][current.getY()];
                        break;
                    case 2:
                        if(!(current.getY() - 1 < 0) && !matrix[current.getX()][current.getY() - 1].isObstacle())
                            next = matrix[current.getX()][current.getY() - 1];
                        break;
                    case 3:
                        if(!(current.getY() + 1 > matrix[0].length-1) && !matrix[current.getX()][current.getY() + 1].isObstacle())
                            next = matrix[current.getX()][current.getY() + 1];
                        break;
                }
                int new_cost = (int)current.getgValue() + 1;
                if(next != null && next.getParent() == null &&(next.getgValue() == 0 || new_cost < next.getgValue()))
                {
                    next.setgValue(new_cost);
                    double fValue = new_cost + next.getRasterDistance(destination);
                    next.setfValue(fValue);
                    next.setParent(current);
                    openList.add(next);
                    //mal gucken ob das hier was bringt
                    matrix[next.getX()][next.getY()] = next;
                }
            }
        }

        return travelFromDestToStart(matrix, matrix[start.getX()][start.getY()], matrix[destination.getX()][destination.getY()]);
    }

    // Heuristiken zur Bewertung der geschätzten Restdistanz zum Ziel
    // Einfache Dreiecksgleichung (direkte Distanz)
    //-------------> Knots implementiert schon Funktion zur Berechnung der Distanz

    private static List<Knot> travelFromDestToStart(Knot[][] matrix, Knot start, Knot destination){
        List<Knot> pathBack = new ArrayList<Knot>();
        Knot current = destination;
        while(!current.equals(start) && current.getParent() != null){
            pathBack.add(current);
            current = current.getParent();
        }
        if(current.equals(start))
            pathBack.add(current);
        return  pathBack;
    }

    static class KnotComparator implements Comparator<Knot>{
        @Override
        public int compare(Knot knot, Knot t1) {
            if(knot.getfValue() < t1.getfValue())
            {
                return -1;
            }else if(knot.getfValue() > t1.getfValue()){
                return 1;
            }
            return 0;
        }
    }
}


