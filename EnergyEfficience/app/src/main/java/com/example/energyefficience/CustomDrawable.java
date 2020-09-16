package com.example.energyefficience;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomDrawable extends Drawable {
    private Rect Bounds = null;
    public int squareSize = 20;
    public int gapSize = 3;
    private Knot[][] MatrixMap = null;
    private Knot startKnot = null;
    private Knot destKnot = null;

    private Path pathMap = new Path();
    private Path pathObstacles = new Path();
    private Path pathTour = new Path();
    private Path pathStart = new Path();
    private Path pathDest = new Path();

    private Paint brushFill = new Paint();
    private Paint brushStroke = new Paint();
    public int numOfObstacles = 0;

    public CustomDrawable() {
        brushFill.setAntiAlias(true);
        brushFill.setStyle(Paint.Style.FILL);
        brushStroke.setAntiAlias(true);
        brushStroke.setStyle(Paint.Style.STROKE);
        brushStroke.setStrokeWidth(2);
    }
    //initializes pathTour and modifies MatrixMap and DestKnot and StartKnot
    public void computePath(int ChoiceOfImplementation){
        if(MatrixMap == null)
            return;
        pathTour = new Path();

        switch (ChoiceOfImplementation)
        {
            case 1:
                //todo mal nachschauen ob MatrixMap danach auch verändert ist (call by reference)
                List<Knot> tour = DjikstraImplementation.computeSynchronouse(this.MatrixMap, startKnot, destKnot);
                for (Knot current : tour)
                {
                    this.pathTour.addRect(
                            current.getX()* (squareSize+gapSize)+ gapSize, current.getY()*(squareSize+gapSize)+ gapSize, current.getX()*(squareSize+gapSize)+gapSize+squareSize,
                            current.getY()*(squareSize+gapSize)+gapSize+squareSize, Path.Direction.CCW );
                }
                return;
            default:
                return;
        }
    }

    //initializes MatrixMap, pathMap, pathObstacles
    public void generateMap() {
        if(Bounds == null)
            return;
        int mapWidth = Bounds.width() / (squareSize+gapSize);
        int mapHeight = Bounds.height() / (squareSize+gapSize);
        MatrixMap = new Knot[mapWidth][mapHeight];
        pathMap = new Path();
        pathTour = new Path();
        int s = 0;
        for(int x = 0; x < mapWidth; x++){
            for(int y = 0; y < mapHeight; y++){
                MatrixMap[x][y]= new Knot(x,y);
                    pathMap.addRect(
                            x*(squareSize+gapSize)+ gapSize,y*(squareSize+gapSize)+gapSize, x*(squareSize+gapSize)+gapSize+squareSize,
                            y*(squareSize+gapSize)+gapSize+squareSize, Path.Direction.CCW);
            }
        }
        //generate Obstacles
        pathObstacles = new Path();
        List<Knot> obs = new ArrayList<Knot>();
        Knot current = new Knot(getRandomNumber(0, mapWidth-1), getRandomNumber(0, mapHeight-1));
        for(int i = 0; i < this.numOfObstacles;){
            if(!obs.contains(current)){
                obs.add(current);
                MatrixMap[current.getX()][current.getY()].setObstacle(true);
                pathObstacles.addRect(
                        current.getX()*(squareSize+gapSize)+ gapSize, current.getY()*(squareSize+gapSize)+ gapSize,
                        current.getX()*(squareSize+gapSize)+gapSize+squareSize,
                        current.getY()*(squareSize+gapSize)+gapSize+squareSize, Path.Direction.CCW);
                i++;
            }
            current = new Knot(getRandomNumber(0, mapWidth-1), getRandomNumber(0, mapHeight-1));
        }
        //generate random Start and Destination
        pathDest = new Path();
        pathStart = new Path();
        for(int i = 0; i != 1;){
            startKnot = new Knot(getRandomNumber(0,MatrixMap.length-1), getRandomNumber(0, MatrixMap[0].length-1));
            if(!obs.contains(startKnot))
                i=1;
        }
        pathStart.addRect(startKnot.getX()* (squareSize+gapSize)+ gapSize, startKnot.getY()*(squareSize+gapSize)+ gapSize, startKnot.getX()*(squareSize+gapSize)+gapSize+squareSize,
                startKnot.getY()*(squareSize+gapSize)+gapSize+squareSize, Path.Direction.CCW );

        destKnot = null;
        for(int i = 0; i != 1;){
            destKnot = new Knot(getRandomNumber(0,MatrixMap.length-1), getRandomNumber(0, MatrixMap[0].length-1));
            if(!destKnot.equals(startKnot) && !obs.contains(destKnot))
                i=1;
        }
        pathDest.addRect(destKnot.getX()* (squareSize+gapSize)+ gapSize, destKnot.getY()*(squareSize+gapSize)+ gapSize, destKnot.getX()*(squareSize+gapSize)+gapSize+squareSize,
                destKnot.getY()*(squareSize+gapSize)+gapSize+squareSize, Path.Direction.CCW );
    }

    private int getRandomNumber(int min, int max) {
        if(min == 0)
            max++;
        return (int) (Math.random() * (max - min) + min);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //creates matrix and initializes pathMap and pathObstacles
        this.Bounds = getBounds();

        if(!pathMap.isEmpty())
        {
            brushStroke.setColor(Color.rgb(87,48,14));
            canvas.drawPath(pathMap, brushStroke);
        }
        if(!pathTour.isEmpty())
        {
            brushFill.setColor(Color.rgb(84,194,45));
            canvas.drawPath(pathTour, brushFill);
        }
        if(!pathStart.isEmpty())
        {
            brushFill.setColor(Color.rgb(51,153,255));
            canvas.drawPath(pathStart, brushFill);
        }
        if(!pathDest.isEmpty())
        {
            brushFill.setColor(Color.rgb(255, 0, 102));
            canvas.drawPath(pathDest, brushFill);
        }
        if(!pathObstacles.isEmpty())
        {
            brushFill.setColor(Color.rgb(0,0,0));
            canvas.drawPath(pathObstacles, brushFill);
        }

    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
