package ua.com.dquality.udrive.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

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
        mStatusLevel = level == null ? StatusLevel.Undefined : level;
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

        if(mStatusLevel == StatusLevel.Undefined){
            Rect rect = new Rect(left - 2 * arcWidth, top - 2 * arcWidth, (int)(left + 2 * radius + 2 * arcWidth), (int)(top + 2 * radius + 2 * arcWidth));
            DrawArcColor(canvas, rect, mColorPlatinumDefault);

            rect = new Rect(left - arcWidth, top - arcWidth, (int)(left + 2 * radius + arcWidth), (int)(top + 2 * radius + arcWidth));
            DrawArcColor(canvas, rect, mColorGoldDefault);

            rect = new Rect(left, top, (int)(left + 2 * radius), (int)(top + 2 * radius));
            ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {
                    return new LinearGradient(width/2, 0, width/2, height,
                            new int[]{ mColorCircleCenterStart, mColorCircleCenterStart, mColorCircleCenterEnd, mColorCircleCenterEnd },
                            null,
                            Shader.TileMode.CLAMP);
                }
            };
            DrawOvalGradient(canvas, rect, shaderFactory);

            return;
        }

        switch (mStatusLevel){
            case Classic:
            {
                Rect rect = new Rect(left - 2 * arcWidth, top - 2 * arcWidth, (int)(left + 2 * radius + 2 * arcWidth), (int)(top + 2 * radius + 2 * arcWidth));
                DrawArcColor(canvas, rect, mColorPlatinumDefault);
                break;
            }
            case Gold:
            case Platinum:{
                if(mStatusLevel == StatusLevel.Gold) {
                    Rect rect = new Rect(left - 2 * arcWidth, top - 2 * arcWidth, (int)(left + 2 * radius + 2 * arcWidth), (int)(top + 2 * radius + 2 * arcWidth));
                    DrawArcColor(canvas, rect, mColorPlatinumDefault);
                }

                Rect rect = new Rect(left - 2 * arcWidth, top - 2 * arcWidth, (int)(left + 2 * radius + 2 * arcWidth), (int)(top + 2 * radius + 2 * arcWidth));
                ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
                    @Override
                    public Shader resize(int width, int height) {
                        return new LinearGradient(0 , 0 , width, height,
                                mColorPlatinumStart, mColorPlatinumEnd,
                                Shader.TileMode.CLAMP);
                    }
                };
                DrawArcGradient(canvas, rect, shaderFactory, mStatusLevel == StatusLevel.Gold ? sweepAngel : 360);
                break;
            }
        }


        if(mStatusLevel == StatusLevel.Classic) {
            Rect rect = new Rect(left - arcWidth, top - arcWidth, (int)(left + 2 * radius + arcWidth), (int)(top + 2 * radius + arcWidth));
            DrawArcColor(canvas, rect, mColorGoldDefault);
        }

        Rect rect = new Rect(left - arcWidth, top - arcWidth, (int)(left + 2 * radius + arcWidth), (int)(top + 2 * radius + arcWidth));
        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                return new LinearGradient(0 , 0 , width, height,
                        mColorGoldStart, mColorGoldEnd,
                        Shader.TileMode.CLAMP);
            }
        };
        DrawArcGradient(canvas, rect, shaderFactory, mStatusLevel == StatusLevel.Classic ? sweepAngel : 360);



        rect = new Rect(left, top, (int)(left + 2 * radius), (int)(top + 2 * radius));
        shaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                return new LinearGradient(width/2, 0, width/2, height,
                        new int[]{ mColorCircleCenterStart, mColorCircleCenterStart, mColorCircleCenterEnd, mColorCircleCenterEnd },
                        null,
                        Shader.TileMode.CLAMP);
            }
        };
        DrawOvalGradient(canvas, rect, shaderFactory);
    }

    private void DrawArcColor(Canvas canvas, Rect rect, int colorIdentifier){
        DrawArcColor(canvas, rect, colorIdentifier, 360);
    }

    private void DrawArcColor(Canvas canvas, Rect rect, int colorIdentifier, int sweepAngel){
        ShapeDrawable drawable = new ShapeDrawable(new ArcShape(-90, sweepAngel));
        drawable.getPaint().setColor(colorIdentifier);
        drawable.setBounds(rect);
        drawable.draw(canvas);
    }

    private void DrawArcGradient(Canvas canvas, Rect rec, ShapeDrawable.ShaderFactory shaderFactory, int sweepAngel){
        ShapeDrawable drawable = new ShapeDrawable(new ArcShape(-90, sweepAngel));
        drawable.setShaderFactory(shaderFactory);
        drawable.setBounds(rec);
        drawable.draw(canvas);
    }

    private void DrawOvalGradient(Canvas canvas, Rect rec, ShapeDrawable.ShaderFactory shaderFactory){
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.setShaderFactory(shaderFactory);
        drawable.setBounds(rec);
        drawable.draw(canvas);
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
