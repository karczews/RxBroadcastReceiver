/**
 * Copyright (c) 2017-present, RxBroadcastReceiver Contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.github.karczews.rxbroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
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
        if (!Preconditions.checkLooperThread(observer)) {
            return;
        }

        final ReceiverDisposable disposable = new ReceiverDisposable(context, observer);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            context.registerReceiver(disposable, intentFilter);
        } else {
            context.registerReceiver(disposable, intentFilter, null, new Handler(Looper.myLooper()));
        }
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