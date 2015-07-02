package com.projetoles.verso;

import java.lang.reflect.Field;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public final class FontsOverride {

    public static void setDefaultFont(Context context,
            String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }
    
    public static Typeface setAltheaRegular(Context context)
    {
         return Typeface.createFromAsset(context.getAssets(),"fonts/Althea-Regular.ttf");
    }

    protected static void replaceFont(String staticTypefaceFieldName,
            final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}