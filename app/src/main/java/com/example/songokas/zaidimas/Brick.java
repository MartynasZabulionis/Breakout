package com.example.songokas.zaidimas;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.constraint.solver.widgets.Rectangle;

public class Brick
{
    private RectF image;
    private int left, top, right, bottom;
    private Paint paint;

    public Brick(int r, int g, int b, float left, float top, float right, float bottom)
    {
        image = new RectF(left, top, right, bottom);

        this.left = (int) left;
        this.top = (int) top;
        this.right = (int) right;
        this.bottom = (int) bottom;

        paint = new Paint();
        paint.setColor(Color.rgb(r, g, b));
    }
    public void draw(Canvas canvas)
    {
        canvas.drawRect(image, paint);
    }

    public void update()
    {

    }
    public int getLeft()
    {
        return left;
    }
    public int getTop()
    {
        return top;
    }
    public int getRight()
    {
        return right;
    }
    public int getBottom()
    {
        return bottom;
    }
}
