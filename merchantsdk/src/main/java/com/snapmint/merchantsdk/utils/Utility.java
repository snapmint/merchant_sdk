package com.snapmint.merchantsdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.DecimalFormat;

public class Utility {

    public static String setSingleDynamicValue(Context context, int textId, String value1) {
        return String.format(context.getResources().getString(textId), currencyFormat(value1));
    }

    public static String currencyFormat(String amount) {
        try {
            double value = Double.parseDouble(amount);
            DecimalFormat format = new DecimalFormat("0.#");
            DecimalFormat formatter = new DecimalFormat("#,###");
            return formatter.format(Double.parseDouble(format.format(value).replaceAll("[^\\d]", "")));
        } catch (Exception e) {
            return amount;
        }
    }

    public static String getNumberSuffix(int number) {
        // Handling special cases for 11, 12, and 13
        if (number >= 11 && number <= 13) {
            return "th";
        }

        // For other numbers, determine the suffix based on the last digit
        int lastDigit = number % 10;
        switch (lastDigit) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

}
