package com.example.songokas.zaidimas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    // Thread
    private MainThread thread;

    // Objects: ball, paddle, bricks
    private Ball ball;
    private Paddle paddle;
    private final List<Brick> brickList = new ArrayList<Brick>();

    public static boolean gameGoes = false;

    private boolean padDirection;
    private boolean movePad = false;

    private Rect r = new Rect();
    private Rect HUD_rect = new Rect();
    private Paint HUD_paint = new Paint();

    public static int level;
    public static int score;
    public static int lives;

    public GameView(Context context)
    {
        super(context);

        getHolder().addCallback(this);
        gameGoes = false;

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        level = 1;
        score = 0;
        lives = 3;

        // Create paddle
        paddle = new Paddle(100, 100, 255, Constants.PADDLE_HEIGHT, Constants.PADDLE_WIDTH, Constants.PADDLE_YLOCATION);

        // Create ball
        ball = new Ball(0, 255, 100,
                Constants.BALL_RADIUS, Constants.WIDTH / 2 - Constants.BALL_RADIUS,
                Constants.PADDLE_YLOCATION - (int) (Constants.PADDLE_HEIGHT/2f) - Constants.BALL_RADIUS * 2 - 1, Constants.BALL_SPEED);


        HUD_rect = new Rect(0, 0, Constants.WIDTH, Constants.TOP_SPACE);
        HUD_paint.setColor(Color.rgb(50, 50, 50));

        //ball = new Ball(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ball), size, size, false));



        // Create first row of bricks
        int bricks_num = 9;
        float all_spaces = Constants.SPACE_BETWEEN_BRICKS * (bricks_num - 1);
        float brick_width = ((float) Constants.WIDTH - all_spaces) / (float) bricks_num;
        float left = 0f, right = brick_width;
        float top = (float) Constants.TOP_SPACE;
        float bottom = top + Constants.BRICK_HEIGHT;
        for (int j = 0; j < 5; ++j)
        {
            for (int i = 0; i < bricks_num; ++i)
            {
                Brick brick = new Brick(255, 0, 0, left, top, right, bottom);
                brickList.add(brick);
                left += brick_width + Constants.SPACE_BETWEEN_BRICKS;
                right += brick_width + Constants.SPACE_BETWEEN_BRICKS;
            }
            top = bottom + Constants.SPACE_BETWEEN_BRICKS;
            bottom = top + Constants.BRICK_HEIGHT;
            left = 0f; right = brick_width;
        }
        /* Create second row of bricks
        bricks_num = 14;
        all_spaces = Constants.SPACE_BETWEEN_BRICKS * (bricks_num - 1);
        brick_width = ((float) Constants.WIDTH - all_spaces) / (float) bricks_num;
        left = 0.0f; right = brick_width;
        top = bottom + Constants.SPACE_BETWEEN_BRICKS;
        bottom = top + Constants.BRICK_HEIGHT;
        for (int i = 0; i < bricks_num; ++i)
        {

            Brick brick = new Brick(255, 0, 0, left, top, right, bottom);
            brickList.add(brick);
            left += brick_width + Constants.SPACE_BETWEEN_BRICKS;
            right += brick_width + Constants.SPACE_BETWEEN_BRICKS;
        }

        // Create second row of bricks
        bricks_num = 10;
        all_spaces = Constants.SPACE_BETWEEN_BRICKS * (bricks_num - 1);
        brick_width = ((float) Constants.WIDTH - all_spaces) / (float) bricks_num;
        left = 0.0f; right = brick_width;
        top = bottom + Constants.SPACE_BETWEEN_BRICKS;
        bottom = top + Constants.BRICK_HEIGHT;
        for (int i = 0; i < bricks_num; ++i)
        {

            Brick brick = new Brick(255, 0, 0, left, top, right, bottom);
            brickList.add(brick);
            left += brick_width + Constants.SPACE_BETWEEN_BRICKS;
            right += brick_width + Constants.SPACE_BETWEEN_BRICKS;
        }*/

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        while (retry)
        {
            try
            {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            retry = false;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        /*if (event.getAction() == MotionEvent.ACTION_DOWN && pad.isOnPad((int) event.getX(), (int) event.getY()))
            movePad = true;

        else if (event.getAction() == MotionEvent.ACTION_MOVE && movePad)
        {
                point.set((int) event.getX(), (int) event.getY());
        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
            movePad = false;*/

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
        {
            int x = (int) event.getX();
            padDirection = (x > Constants.WIDTH / 2);
            movePad = true;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            gameGoes = true;
            movePad = false;
        }
        //MotionEvent.B
        return true;
    }
    /*public static int getDistance(int x, int y, int x2, int y2)
    {
        return (int) Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
    }*/
    public void update()
    {
        if (!gameGoes)
            return;

        int x = ball.getX(), r = ball.getRadius(), y = ball.getY();
        int xc = x + r, yc = y + r;

        if (paddle.getTop() - yc < r && yc - paddle.getBottom() < r && paddle.getLeft() - xc < r && xc - paddle.getRight() < r)
        {
            if (yc <= r + paddle.getTop())
            {
                float ball_center_coord = xc - paddle.getLeft() + r;
                float paddle_right_coord = paddle.getRight() + r - paddle.getLeft() + r;

                float degree = (paddle_right_coord - ball_center_coord) / paddle_right_coord * 180f;
                ball.update(1, degree);
            }
            else
                ball.update(2, 0.0f);
        }

        else
        {
            boolean simpleUpdate = true;

            Iterator<Brick> iterator= brickList.iterator();
            Brick b;
            while (iterator.hasNext())
            {
                b = iterator.next();
                x = ball.getX(); y = ball.getY();
                xc = x + r; yc = y + r;

                if (yc - b.getBottom() < r && b.getTop() - yc < r && b.getLeft() - xc < r && xc - b.getRight() < r)
                {
                    // Calculate ysize
                    float ymin;
                    if (b.getTop() > y)
                        ymin = b.getTop();
                    else
                        ymin = y;

                    float ymax;
                    if (b.getBottom() < y + ball.getHeight())
                        ymax = b.getBottom();
                    else
                        ymax = y + ball.getHeight();


                    float ysize = ymax - ymin;

                    // Calculate xsize
                    float xmin;
                    if (b.getLeft() > x)
                        xmin = b.getLeft();
                    else
                        xmin = x;


                    float xmax;
                    if (b.getRight() < x + ball.getWidth())
                        xmax = b.getRight();
                    else
                        xmax = x + ball.getWidth();


                    float xsize = xmax - xmin;


                    // The origin is at the top-left corner of the screen!
                    // Set collision response
                    if (xsize > ysize)
                    {
                        if (yc > b.getTop() + Constants.BRICK_HEIGHT / 2)
                        {
                            // Bottom
                            ball.setY(y + (int) ysize + 1); // Move out of collision
                            ball.update(6, 0.0f);
                        }
                        else
                        {
                            // Top
                            //ball->y -= ysize + 0.01f; // Move out of collision
                            ball.setY(y - (int) ysize - 1);
                            ball.update(4, 0.0f);
                        }
                    }
                    else
                    {
                        if (xc < (b.getLeft() + b.getRight()) / 2)
                        {
                            // Left
                            //ball->x -= xsize + 0.01f; // Move out of collision
                            ball.setX(x - (int) xsize - 1);
                            ball.update(3, 0.0f);
                        }
                        else
                        {
                            // Right
                            //ball->x += xsize + 0.01f; // Move out of collision
                            ball.setX(x + (int) xsize + 1);
                            ball.update(5, 0.0f);
                        }
                    }

                    // Other stuff
                    ++score;
                    simpleUpdate = false;
                    iterator.remove();
                    b = null;
                    //break;
                }

            }
            if (simpleUpdate)
                ball.update(0, 0.0f);
        }
        if (movePad)
            paddle.update(padDirection);
    }
    //@Override
    private void drawTextCenter(String text, Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setTextSize(Constants.WIDTH / 11);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.rgb(255, 255, 255));
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        if (!gameGoes)
        {
            drawTextCenter("Press to start the game!", canvas);
        }

        canvas.drawRect(HUD_rect, HUD_paint);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setTextSize(Constants.TOP_SPACE * 4 / 5);
        canvas.drawText("Level: " + level + ", Score: " + score + ", Lives: " + lives, 0, Constants.TOP_SPACE * 4 / 5, paint);

        if (canvas != null && ball != null)
        {
            ball.draw(canvas);
        }
        for (Brick b: brickList)
            b.draw(canvas);

        paddle.draw(canvas);
    }
}
