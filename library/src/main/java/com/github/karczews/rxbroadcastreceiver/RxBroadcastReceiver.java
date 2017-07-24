package com.github.karczews.rxbroadcastreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

class RxBroadcastReceiver extends Observable <Intent> {

    @NonNull
    private final Context context;
    @NonNull
    private final IntentFilter intentFilter;

    public RxBroadcastReceiver(@NonNull final Context context, @NonNull final IntentFilter intentFilter) {
        this.context = context.getApplicationContext();
        this.intentFilter = intentFilter;
    }

    @Override
    protected void subscribeActual(final Observer <? super Intent> observer) {
        final ReceiverDisposable disposable = new ReceiverDisposable(context, observer);
        context.registerReceiver(disposable, intentFilter);
        observer.onSubscribe(disposable);
    }

    private static class ReceiverDisposable extends BroadcastReceiver implements Disposable {
        @NonNull
        private final AtomicBoolean disposed = new AtomicBoolean(false);
        @NonNull
        private final Observer <? super Intent> observer;
        @NonNull
        private final Context context;

        ReceiverDisposable(@NonNull final Context context, @NonNull final Observer <? super Intent> observer) {
            this.observer = observer;
            this.context = context;
        }
        @Override
        public void dispose() {
            if (disposed.compareAndSet(false, true)) {
                context.unregisterReceiver(ReceiverDisposable.this);
            }
        }

        @Override
        public boolean isDisposed() {
            return disposed.get();
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (!isDisposed()) {
                observer.onNext(intent);
            }
        }
    }
}