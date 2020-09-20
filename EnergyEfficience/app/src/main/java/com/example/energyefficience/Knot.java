package com.example.energyefficience;

import android.icu.lang.UCharacter;

import androidx.annotation.Nullable;

public class Knot {
    private int x;
    private int y;
    private Knot parent;
    private double fValue; //priority
    private double gValue;  //actual PathLength from Start Knot
    private boolean isObstacle = false;

    public boolean isObstacle() {
        return isObstacle;
    }
    public void setObstacle(boolean obstacle) {
        isObstacle = obstacle;
    }

    public double getgValue() {
        return gValue;
    }

    public void setgValue(double gValue) {
        this.gValue = gValue;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Knot getParent() {
        return parent;
    }

    public void setParent(Knot parent) {
        this.parent = parent;
    }

    public double getfValue() {
        return fValue;
    }

    public void setfValue(double fValue) {
        this.fValue = fValue;
    }

    Knot(int x, int y) {
        this.x = x;
        this.y = y;
        this.parent = null;
        this.gValue = 0; //actual pathlength from start knot, when never accessed it's -1
        this.fValue = 0;
    }
    Knot(int x, int y, boolean isObstacle) {
        this.x = x;
        this.y = y;
        this.parent = null;
        this.gValue = 0; //actual pathlength from start knot, when never accessed it's -1
        this.fValue = 0;
        this.isObstacle = isObstacle;
    }

    Knot() {
        this.x = 0;
        this.y = 0;
        this.parent = null;
        this.gValue = 0; //actual pathlength from start knot
        this.fValue = 0;
    }


    public int getRasterDistance(Knot k){
        return Math.abs(this.getX() - k.getX()) + Math.abs(this.getY()- k.getY());
    }

    public double getRealDistance(Knot k) {
        return Math.sqrt(Math.pow((this.x - k.x), 2) + Math.pow((this.y - k.y), 2));
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj  == null)
            return false;
        if(obj == this)
            return true;
        if(!obj.getClass().equals(getClass()))
            return false;
        Knot x = (Knot) obj;
        return this.getX() == x.getX() && this.getY() == x.getY();
    }
}
