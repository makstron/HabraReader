package com.klim.habrareader.app.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.klim.habrareader.App;

import java.util.Locale;

public class ResourceUtil {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap getBitmap(VectorDrawableCompat vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap getBitmap(Context context, @DrawableRes int drawableResId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableResId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawableCompat) {
            return getBitmap((VectorDrawableCompat) drawable);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("Unsupported drawable type");
        }
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId, Integer color, Integer width, Integer height) {
        Drawable drawable = null;
        if (color != null) {
            drawable = getColoredRes(context, drawableId, color);
        } else {
            drawable = ContextCompat.getDrawable(context, drawableId);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        if (width == null) {
            width = drawable.getIntrinsicWidth();
        }
        if (height == null) {
            height = drawable.getIntrinsicHeight();
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());


//        Paint pGrille = new Paint(Paint.ANTI_ALIAS_FLAG);
//        pGrille.setAntiAlias(true);
//        pGrille.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
//        pGrille.setStyle(Paint.Style.STROKE);
//        pGrille.setStrokeWidth(10);
//        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), pGrille);

        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap getBitmapFromVectorDrawableColored(Context context, int drawableId, int color, Integer width, Integer height) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));

        if (width == null) {
            width = drawable.getIntrinsicWidth();
        }
        if (height == null) {
            height = drawable.getIntrinsicHeight();
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

        drawable.draw(canvas);

        return bitmap;
    }

    public static Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = new Configuration(context.getResources().getConfiguration());
        conf.setLocale(desiredLocale);
        return context.createConfigurationContext(conf).getResources();
    }

    public static VectorDrawableCompat getLocalizedVector(Context context, Locale desiredLocale, int resource) {
        Configuration conf = new Configuration(context.getResources().getConfiguration());
        conf.setLocale(desiredLocale);
        return VectorDrawableCompat.create(context.createConfigurationContext(conf).getResources(), resource, null);
    }

    public static int getColor(int color) {
        return ContextCompat.getColor(App.Companion.getApp(), color);
    }

    public static Drawable getColoredRes(Context context, int resource, Integer color) {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, resource);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        if (color != null)
            DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }
}