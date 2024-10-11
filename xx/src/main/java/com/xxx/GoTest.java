package com.xxx;

import android.content.Context;

public class GoTest {
    public static void init(
            Context ctx, String key, String value) {
        android.util.Log.i("sanbo", "in GoTest class , init method, ctx: " + ctx
                + "\r\nkey: " + key
                + "\r\nvalue: " + value
        );
    }
}
