package com.github.karczews.rxbroadcastreceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import io.reactivex.Observable;

/**
 * Utility class containing static factory methods creating Observable
 * instances wrapping {@link android.content.BroadcastReceiver}s
 */
public final class RxBroadcastReceivers {

    private RxBroadcastReceivers() {
        throw new AssertionError("No util class instances for you!");
    }

    /**
     * Creates Observable that will register {@link android.content.BroadcastReceiver} for provided
     * {@link IntentFilter} when subscribed to. Observable will emit received broadcast as data {@link Intent}
     *
     * @param context used to register broadcast receiver to.
     * @param filter  {@link IntentFilter} used to select Intent broadcast to be received.
     */
    @NonNull
    @CheckResult
    public static Observable<Intent> fromIntentFilter(@NonNull final Context context,
                                                      @NonNull final IntentFilter filter) {
        return new RxBroadcastReceiver(context, filter);
    }
}
