package com.allo.flickster.utils.ui;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by ALLO on 20/7/16.
 */
public class Utils {

    public static float convertDPToPixels(Resources resources, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
