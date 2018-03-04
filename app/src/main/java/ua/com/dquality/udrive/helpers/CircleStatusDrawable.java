package ua.com.dquality.udrive.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.viewmodels.StatusLevel;

public class CircleStatusDrawable extends Drawable {
    private StatusLevel mStatusLevel;
    private int mPercentage;
    private int mColorCircleCenterStart;
    private int mColorCircleCenterEnd;

    private int mColorGoldStart;
    private int mColorGoldEnd;

    private int mColorPlatinumStart;
    private int mColorPlatinumEnd;

    private int mColorGoldDefault;
    private int mColorPlatinumDefault;

    public CircleStatusDrawable(Context ctx, StatusLevel level, int percentage) {
        mStatusLevel = level;
        mPercentage = percentage;

        mColorCircleCenterStart = ContextCompat.getColor(ctx, R.color.colorCircleCenterStart);
        mColorCircleCenterEnd = ContextCompat.getColor(ctx, R.color.colorCircleCenterEnd);

        mColorGoldStart = ContextCompat.getColor(ctx, R.color.colorGoldStart);
        mColorGoldEnd = ContextCompat.getColor(ctx, R.color.colorGoldEnd);

        mColorPlatinumStart = ContextCompat.getColor(ctx, R.color.colorPlatinumStart);
        mColorPlatinumEnd = ContextCompat.getColor(ctx, R.color.colorPlatinumEnd);

        mColorGoldDefault = ContextCompat.getColor(ctx, R.color.colorCircleGoldDefault);
        mColorPlatinumDefault = ContextCompat.getColor(ctx, R.color.colorCirclePlatinumDefault);

    }

    @Override
    public void draw(Canvas canvas) {
        // Get the drawable's bounds
        int width = getBounds().width();
        int height = getBounds().height();
        final float radius = Math.min(width, height) / 4;

        int left = (int) ((width - 2*radius)/2);
        int top = (int) ((height - 2*radius)/2);

        final int arcWidth = (int)radius/2;
        int sweepAngel = (360/100) * mPercentage;

        switch (mStatusLevel){
            case Classic:
            {
                ShapeDrawable mDrawable3 = new ShapeDrawable(new ArcShape(-90, 360));
                mDrawable3.getPaint().setColor(mColorPlatinumDefault);
                mDrawable3.setBounds(left - 2 * arcWidth, top - 2 * arcWidth, (int)(left + 2 * radius + 2 * arcWidth), (int)(top + 2 * radius + 2 * arcWidth));
                mDrawable3.draw(canvas);
                break;
            }
            case Gold:
            case Platinum:{
                if(mStatusLevel == StatusLevel.Gold) {
                    ShapeDrawable mDrawable3 = new ShapeDrawable(new ArcShape(-90, 360));
                    mDrawable3.getPaint().setColor(mColorPlatinumDefault);
                    mDrawable3.setBounds(left - 2 * arcWidth, top - 2 * arcWidth, (int)(left + 2 * radius + 2 * arcWidth), (int)(top + 2 * radius + 2 * arcWidth));
                    mDrawable3.draw(canvas);
                }

                ShapeDrawable mDrawable31 = new ShapeDrawable(new ArcShape(-90, mStatusLevel == StatusLevel.Gold ? sweepAngel : 360));
                ShapeDrawable.ShaderFactory sf3 = new ShapeDrawable.ShaderFactory() {
                    @Override
                    public Shader resize(int width, int height) {
                        LinearGradient lg = new LinearGradient(0 , 0 , width, height,
                                mColorPlatinumStart, mColorPlatinumEnd,
                                Shader.TileMode.CLAMP);
                        return lg;
                    }
                };

                mDrawable31.setShaderFactory(sf3);
                mDrawable31.setBounds(left - 2 * arcWidth, top - 2 * arcWidth, (int)(left + 2 * radius + 2 * arcWidth), (int)(top + 2 * radius + 2 * arcWidth));
                mDrawable31.draw(canvas);
                break;
            }
        }


        if(mStatusLevel == StatusLevel.Classic) {
            ShapeDrawable mDrawable2 = new ShapeDrawable(new ArcShape(-90, 360));
            mDrawable2.getPaint().setColor(mColorGoldDefault);
            mDrawable2.setBounds(left - arcWidth, top - arcWidth, (int)(left + 2 * radius + arcWidth), (int)(top + 2 * radius + arcWidth));
            mDrawable2.draw(canvas);
        }

        ShapeDrawable mDrawable21 = new ShapeDrawable(new ArcShape(-90, mStatusLevel == StatusLevel.Classic ? sweepAngel : 360));

        ShapeDrawable.ShaderFactory sf2 = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient lg = new LinearGradient(0 , 0 , width, height,
                        mColorGoldStart, mColorGoldEnd,
                        Shader.TileMode.REPEAT);
                return lg;
            }
        };
        mDrawable21.setShaderFactory(sf2);

        mDrawable21.setBounds(left - arcWidth, top - arcWidth, (int)(left + 2 * radius + arcWidth), (int)(top + 2 * radius + arcWidth));
        mDrawable21.draw(canvas);


        ShapeDrawable mDrawable = new ShapeDrawable(new OvalShape());
        ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient lg = new LinearGradient(width/2, 0, width/2, height,
                        new int[]{ mColorCircleCenterStart, mColorCircleCenterStart, mColorCircleCenterEnd, mColorCircleCenterEnd },
                        null,
                        Shader.TileMode.CLAMP);
                return lg;
            }
        };
        mDrawable.setShaderFactory(sf);
        // If the color isn't set, the shape uses black as the default.
        //mDrawable.getPaint().setColor(ContextCompat.getColor(mContext, R.color.colorCircleCenterEnd));
        // If the bounds aren't set, the shape can't be drawn.


        mDrawable.setBounds(left, top, (int)(left + 2 * radius), (int)(top + 2 * radius));

        mDrawable.draw(canvas);
    }

    @Override
    public void setAlpha(int alpha) {
        // This method is required
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // This method is required
    }

    @Override
    public int getOpacity() {
        // Must be PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE
        return PixelFormat.OPAQUE;
    }
}
