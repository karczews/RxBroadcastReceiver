package com.github.karczews.rxbroadcastreceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import io.reactivex.Observable;

public class RxBroadcastReceivers {

    @NonNull
    @CheckResult
    public static Observable<Intent> fromIntentFilter(Context context, IntentFilter filter) {
        return new RxBroadcastReceiver(context, filter);
    }
}
