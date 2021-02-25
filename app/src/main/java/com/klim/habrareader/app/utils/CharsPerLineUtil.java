package com.klim.habrareader.app.utils;

import android.text.Layout;
import android.util.Log;
import android.widget.TextView;

import com.klim.habrareader.BuildConfig;

/**
 * Helper utilities useful during debugging that compute the maximum number of characters per line
 * in a {@link TextView} or {@link Layout}.
 */
public class CharsPerLineUtil {
    private static final String TAG = "CharsPerLineUtil";

    public static final int RECOMMENDED_MIN_CPL = 45;
    public static final int RECOMMENDED_MAX_CPL = 75;

    /**
     * Compute the maximum number of characters per line in the given {@link TextView}.
     */
    public static int getMaxCharsPerLine(TextView textView) {
        return getMaxCharsPerLine(textView.getLayout());
    }

    /**
     * Compute the maximum number of characters per line in the given {@link Layout}.
     */
    public static int getMaxCharsPerLine(Layout layout) {
        int maxChars = 0;
        int maxIndex = -1;
        for (int i = layout.getLineCount() - 1; i >= 0; i--) {
            int chars = layout.getLineEnd(i) - layout.getLineStart(i);
            if (chars > maxChars) {
                maxChars = chars;
                maxIndex = i;
            }
        }
        if (BuildConfig.DEBUG && maxIndex >= 0) {
            CharSequence line = layout.getText().subSequence(
                    layout.getLineStart(maxIndex),
                    layout.getLineEnd(maxIndex));
            Log.d(TAG, "Max line: '" + line + "' (length=" + line.length() + ")");
        }
        return maxChars;
    }
}