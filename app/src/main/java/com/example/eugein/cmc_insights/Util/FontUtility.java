package com.example.eugein.cmc_insights.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;



/**
 * Created by AH on 12/5/2017.
 */

public class FontUtility {

    public final static int LORA_BOLD = 1;
    public final static int LORA_BOLD_ITALIC = 2;
    public final static int LORA_ITALIC = 3;
    public final static int LORA_REGULAR = 4;
    public final static int MON_BLACK = 5;
    public final static int MON_BLACK_ITALIC = 6;
    public final static int MON_BOLD = 7;
    public final static int MON_BOLD_ITALIC = 8;
    public final static int MON_EXTRA_BOLD = 9;
    public final static int MON_EXTRA_BOLD_ITALIC = 10;
    public final static int MON_EXTRA_LIGHT = 11;
    public final static int MON_EXTRA_LIGHT_ITALIC = 12;
    public final static int MON_ITALIC = 13;
    public final static int MON_LIGHT = 14;
    public final static int MON_LIGHT_ITALIC = 15;
    public final static int MON_MEDIUM = 16;
    public final static int MON_MEDIUM_ITALIC = 17;
    public final static int MON_REGULAR = 18;
    public final static int MON_SEMI_BOLD = 19;
    public final static int MON_SEMI_BOLD_ITALIC = 20;
    public final static int MON_THIN = 21;
    public final static int MON_THIN_ITALIC = 22;

    static String lora_bold = "fonts/Lora-Bold.ttf";
    static String lora_bold_italic = "fonts/Lora-BoldItalic.ttf";
    static String lora_italic = "fonts/Lora-Italic.ttf";
    static String lora_regular = "fonts/Lora-Regular.ttf";
    static String mon_black = "fonts/Montserrat-Black.ttf";
    static String mon_black_italic = "fonts/Montserrat-BlackItalic.ttf";
    static String mon_bold = "fonts/Montserrat-Bold.ttf";
    static String mon_bold_italic = "fonts/Montserrat-BoldItalic.ttf";
    static String mon_ex_bold = "fonts/Montserrat-ExtraBold.ttf";
    static String mon_ex_bold_italic = "fonts/Montserrat-ExtraBoldItalic.ttf";
    static String mon_ex_light = "fonts/Montserrat-ExtraLight.ttf";
    static String mon_ex_light_italic = "fonts/Montserrat-ExtraLightItalic.ttf";
    static String mon_italic = "fonts/Montserrat-Italic.ttf";
    static String mon_light = "fonts/Montserrat-Light.ttf";
    static String mon_light_italic = "fonts/Montserrat-LightItalic.ttf";
    static String mon_medium = "fonts/Montserrat-Medium.ttf";
    static String mon_medium_italic = "fonts/Montserrat-MediumItalic.ttf";
    static String mon_regular = "fonts/Montserrat-Regular.ttf";
    static String mon_semi_bold = "fonts/Montserrat-SemiBold.ttf";
    static String mon_semi_bold_italic = "fonts/Montserrat-SemiBoldItalic.ttf";
    static String mon_thin = "fonts/Montserrat-Thin.ttf";
    static String mon_thin_italic = "fonts/Montserrat-ThinItalic.ttf";
    private static final String TAG = "check";

    public static Typeface setFontFace(Context context, int type) {
        Typeface typeface = null;
        switch (type) {
            case LORA_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), lora_bold);
                break;
            case LORA_BOLD_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), lora_bold_italic);
                break;
            case LORA_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), lora_italic);
                break;
            case LORA_REGULAR:
                typeface = Typeface.createFromAsset(context.getAssets(), lora_regular);
                break;
            case MON_BLACK:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_black);
                break;
            case MON_BLACK_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_black_italic);
                break;
            case MON_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_bold);
                break;
            case MON_BOLD_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_bold_italic);
                break;
            case MON_EXTRA_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_ex_bold);
                break;
            case MON_EXTRA_BOLD_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_ex_bold_italic);
                break;
            case MON_EXTRA_LIGHT:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_ex_light);
                break;
            case MON_EXTRA_LIGHT_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_ex_light_italic);
                break;
            case MON_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_italic);
                break;
            case MON_LIGHT:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_light);
                break;
            case MON_LIGHT_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_light_italic);
                break;
            case MON_MEDIUM:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_medium);
                break;
            case MON_MEDIUM_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_medium_italic);
                break;
            case MON_REGULAR:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_regular);
                break;
            case MON_SEMI_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_semi_bold);
                break;
            case MON_SEMI_BOLD_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_semi_bold_italic);
                break;
            case MON_THIN:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_thin);
                break;
            case MON_THIN_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), mon_thin);
                break;
        }
        return typeface;
    }




}
