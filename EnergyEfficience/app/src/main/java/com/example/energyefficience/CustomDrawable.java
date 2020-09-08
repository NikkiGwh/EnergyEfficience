package com.example.energyefficience;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomDrawable extends Drawable {
    public Path getPathKnots() {
        return pathKnots;
    }

    public void setPathKnots(Path pathKnots) {
        this.pathKnots = pathKnots;
    }

    public Path getPathEdge() {
        return pathEdge;
    }

    public void setPathEdge(Path pathEdge) {
        this.pathEdge = pathEdge;
    }
    private Path pathKnots = new Path();
    private Path pathEdge = new Path();
    private Paint brushEdge = new Paint();
    private Paint brushKnots = new Paint();
    private int num = 0;
    public CustomDrawable(int num) {
       this.num = num;
        brushEdge.setAntiAlias(true);
        brushEdge.setColor(Color.MAGENTA);
        brushEdge.setStyle(Paint.Style.STROKE);
        brushEdge.setStrokeWidth(8f);
        brushKnots.setAntiAlias(true);
        brushKnots.setColor(Color.RED);
        brushKnots.setStyle(Paint.Style.STROKE);
        brushKnots.setStrokeWidth(8f);
    }

    private Path generateRandomKnots(int num, Rect bounds) {
        Path p = new Path();
        int flag;
        //x muss zwischen Rect.x und Rect.x + width liegen und y analog

        List<Point> li = new ArrayList<Point>();
        for (int i = 0; i < num;) {
            flag = 0;
            Point knot = new Point();
            knot.x = getRandomNumber(bounds.left, bounds.right);
            knot.y = getRandomNumber(bounds.top, bounds.bottom);
            for (Point current : li) {
                if(current.equals(knot))
                    flag = 1;
            }
            if(flag != 1)
            {
                i++;
                li.add(knot);
                p.addCircle(knot.x, knot.y, 5, Path.Direction.CCW);
            }
        }
        return p;
    }

    private int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if(pathKnots.isEmpty())
            this.pathKnots = generateRandomKnots(num, getBounds());
        canvas.drawPath(pathEdge, brushEdge);
        canvas.drawPath(pathKnots, brushKnots);
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
