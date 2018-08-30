package com.example.songokas.zaidimas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static com.example.songokas.zaidimas.GameView.gameGoes;

public class Ball
{
    private Bitmap image;
    private int x, y;
    private int xVelocity;
    private int yVelocity;
    private int radius;
    private int speed;

    public Ball(int r, int g, int b, int radius, int x, int y, int speed)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = speed;

        ChangeBallVelocity((float) Math.random() * 180f);

        image = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(image);
        canvas.drawColor(Color.TRANSPARENT);

        Paint paint = new Paint();
        paint.setColor(Color.rgb(r, g, b));
        canvas.drawCircle(radius, radius, radius, paint);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y, null);
    }
    public void checkBounds()
    {
        boolean updatex = true, updatey = true;
        if (x + image.getWidth() > Constants.WIDTH)
        {
            xVelocity *= -1;
            x = Constants.WIDTH - image.getWidth();
            updatex = false;
        }
        else if (x < 0)
        {
            xVelocity *= -1;
            x = 0;
            updatex = false;
        }

        if (y < Constants.TOP_SPACE)
        {
            yVelocity *= -1;
            y = Constants.TOP_SPACE;
            updatey = false;
        }
        else if (y > Constants.HEIGHT - image.getHeight())
        {
            updatex = updatey = false;
            yVelocity *= -1;
            x = Constants.WIDTH / 2;
            y = Constants.PADDLE_YLOCATION - (int) (Constants.PADDLE_HEIGHT/2f) - Constants.BALL_RADIUS * 2 - 1;
            gameGoes = false;
            if (GameView.lives-- == 0)
                System.exit(0);
        }
        if (updatex)
            x += xVelocity;
        if (updatey)
            y += yVelocity;
    }
    private void BounceFromBrick(int type)
    {
        // dirindex 0: Left, 1: Top, 2: Right, 3: Bottom

        // Direction factors
        int mulx = 1;
        int muly = 1;

        if (xVelocity > 0)
        {
            // Ball is moving in the positive x direction
            if (yVelocity > 0)
            {
                // Ball is moving in the positive y direction
                // +1 +1
                if (type == 3 || type == 6)
                    xVelocity *= -1;
                else
                    yVelocity *= -1;

            }
            else// if (yVelocity < 0)
            {
                // Ball is moving in the negative y direction
                // +1 -1
                if (type == 3 || type == 4)
                    xVelocity *= -1;
                else
                    yVelocity *= -1;

            }
        }
        else// if (xVelocity < 0)
        {
            // Ball is moving in the negative x direction
            if (yVelocity > 0)
            {
                // Ball is moving in the positive y direction
                // -1 +1
                if (type == 5 || type == 6)
                    xVelocity *= -1;
                else
                    yVelocity *= -1;
            }
            else// if (yVelocity < 0)
            {
                // Ball is moving in the negative y direction
                // -1 -1
                if (type == 4 || type == 5)
                    xVelocity *= -1;
                else
                    yVelocity *= -1;
            }
        }
    }
    public void ChangeBallVelocity(float degree)
    {
        /*xVelocity = (int) (Math.sqrt(speed*speed * Math.abs(cof) * 0.8) * Math.signum(cof));
        yVelocity = (int) Math.sqrt(speed*speed * (1.0 - Math.abs(cof) * 0.8)) * -1;*/

        // Convert degree to the right one!
        if (degree >= 90f)
            degree = (degree - 90f) / 90f * Constants.BALL_MAX_REBOUND_DEGREE + 90f;
        else
            degree = 90f - (90f - degree) / 90f * Constants.BALL_MAX_REBOUND_DEGREE;

        xVelocity = (int) (speed * Math.cos(Math.toRadians(degree)));
        yVelocity = -(int) (speed * Math.sin(Math.toRadians(degree)));
    }
    public void update(int type, float degree)
    {
        if (type == 1)
        {
            ChangeBallVelocity(degree);

            x += xVelocity;
            y += yVelocity;
        }
        else if (type == 2)
        {
            xVelocity *= -1;
            x += xVelocity;
            y += yVelocity;
            checkBounds();
        }
        else if (type > 2 && type < 7)
        {
            BounceFromBrick(type);
            x += xVelocity;
            y += yVelocity;
        }
        else
            checkBounds();
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
    public int getXVelocity()
    {
        return xVelocity;
    }
    public int getYVelocity()
    {
        return yVelocity;
    }

    public int getRadius()
    {
        return radius;
    }
    public int getWidth()
    {
        return image.getWidth();
    }
    public int getHeight()
    {
        return image.getHeight();
    }
}
