package com.naveedurrahman.popularmovies;

import android.app.Application;

import com.squareup.otto.Bus;

/**
 * Created by naveed on 5/3/16.
 */
public class App extends Application {

    private static Bus mEventBus;

    public static Bus getEventBus() {

        if (mEventBus == null) {
            mEventBus = new com.squareup.otto.Bus();
        }

        return mEventBus;
    }
}
