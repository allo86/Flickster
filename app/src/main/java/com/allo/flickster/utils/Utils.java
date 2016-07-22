package com.allo.flickster.utils;

import java.math.BigDecimal;

/**
 * Created by ALLO on 21/7/16.
 */
public class Utils {

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}
