package com.wellee.libbanner.utils;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.NonNull;

public class Utils {

    public static int dp2px(@NonNull Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(@NonNull Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    public static int getScreenWidth(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
