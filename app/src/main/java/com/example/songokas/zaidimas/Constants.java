package com.example.songokas.zaidimas;

import android.content.res.Resources;

public class Constants
{
    public static final int WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static final int HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
    public static final int TOP_SPACE = HEIGHT / 18;

    public static final float PADDLE_HEIGHT = HEIGHT / 40f;
    public static final float PADDLE_WIDTH = WIDTH / 4.5f;
    public static final int PADDLE_YLOCATION = HEIGHT - HEIGHT / 9;

    public static final int LIVES = 3;
    public static final int BALL_RADIUS = HEIGHT / 70;
    public static final float SPACE_BETWEEN_BRICKS = 5.0f;
    public static final float BRICK_HEIGHT = HEIGHT / 15.0f;

    public static final int BALL_SPEED = 12;

    public static final float BALL_MAX_REBOUND_DEGREE = 70f;
}