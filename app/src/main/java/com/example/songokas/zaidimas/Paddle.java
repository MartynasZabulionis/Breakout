package com.example.songokas.zaidimas;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Paddle
{
    private Rect image;
    private float height, width;
    private int left, top, right, bottom;
    private Paint paint;

    public Paddle(int r, int g, int b, float height, float width, int ylocation)
    {
        this.left = Constants.WIDTH / 2 - (int) (width / 2f);
        this.top = ylocation - (int) (height / 2f);
        this.right = Constants.WIDTH / 2 + (int) (width / 2f);
        this.bottom = ylocation + (int) (height / 2f);

        image = new Rect(left, top, right, bottom);


        this.height = height;
        this.width = width;


        paint = new Paint();
        paint.setColor(Color.rgb(r, g, b));
    }
    public void draw(Canvas canvas)
    {
        canvas.drawRect(image, paint);
    }

    public void update(boolean dir)
    {
        int speed = 10 * (dir ? 1 : -1);
        if (left + speed > 0 && right + speed < Constants.WIDTH)
        {
            left += speed;
            right += speed;
            image.set(left, top, right, bottom);
        }
    }
    public boolean isOnPad(int x, int y)
    {
        return x >= left && x <= right && y >= top;
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
