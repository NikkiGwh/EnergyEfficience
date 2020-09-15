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

    public int squareSize = 20;
    public int gapSize = 3;
    private Knot[][] MatrixMap = null;

    private Path pathMap = new Path();
    private Path pathObstacles = new Path();
    private Path pathTour = new Path();

    private Paint brushFill = new Paint();
    private Paint brushStroke = new Paint();
    private int numOfObstacles = 0;

    public CustomDrawable(int num) {
       this.numOfObstacles = num;
        brushFill.setAntiAlias(true);
        brushFill.setStyle(Paint.Style.FILL);


        brushStroke.setAntiAlias(true);
        brushStroke.setStyle(Paint.Style.STROKE);
        brushStroke.setStrokeWidth(2);
    }
    //initializes pathTour and modifies MatrixMap and DestKnot and StartKnot
    public void computePath(int ChoiceOfImplementation){
        //generate random Start and Destination
        Knot startKnot = new Knot(getRandomNumber(0,MatrixMap.length-1), getRandomNumber(0, MatrixMap[0].length-1));
        Knot destKnot = null;
        for(int i = 0; i != 1;){
            destKnot = new Knot(getRandomNumber(0,MatrixMap.length-1), getRandomNumber(0, MatrixMap[0].length-1));
            if(!destKnot.equals(startKnot))
                i=1;
        }
        switch (ChoiceOfImplementation)
        {
            case 1:
                //todo mal nachschauen ob MatrixMap danach auch verändert ist (call by reference)
                List<Knot> tour = DjikstraImplementation.computeSynchronouse(this.MatrixMap, startKnot, destKnot);
                for (Knot current :
                        tour) {
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
    private void generateMap(Rect bounds) {
        int mapWidth = bounds.width() / (squareSize+gapSize);
        int mapHeight = bounds.height() / (squareSize+gapSize);
        MatrixMap = new Knot[mapWidth][mapHeight];
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
        List<Knot> obs = new ArrayList<Knot>();
        Knot current = null;
        for(int i = 0; i < this.numOfObstacles;){
            current = new Knot(getRandomNumber(0, mapWidth-1), getRandomNumber(0, mapHeight-1));
            if(!obs.contains(current)){
                MatrixMap[current.getX()][current.getY()].setObstacle(true);
                pathObstacles.addRect(
                        current.getX()*(squareSize+gapSize)+ gapSize, current.getY()*(squareSize+gapSize)+ gapSize,
                        current.getX()*(squareSize+gapSize)+gapSize+squareSize,
                        current.getY()*(squareSize+gapSize)+gapSize+squareSize, Path.Direction.CCW);
                i++;
            }
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //creates matrix and initializes pathMap and pathObstacles
        generateMap(getBounds());
        computePath(1);

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
