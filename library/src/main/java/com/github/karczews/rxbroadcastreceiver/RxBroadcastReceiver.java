/*
 * Copyright (c) 2017-present, RxBroadcastReceiver Contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.github.karczews.rxbroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

class RxBroadcastReceiver implements ObservableOnSubscribe<Intent> {

    @NonNull
    private final Context context;
    @NonNull
    private final IntentFilter intentFilter;

    RxBroadcastReceiver(@NonNull final Context context, @NonNull final IntentFilter intentFilter) {
        this.context = context.getApplicationContext();
        this.intentFilter = intentFilter;
    }

    @Override
    public void subscribe(final ObservableEmitter<Intent> emitter) {
        if (!Preconditions.checkLooperThread(emitter)) {
            return;
        }
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                emitter.onNext(intent);
            }
        };

        if (Looper.myLooper() == Looper.getMainLooper()) {
            context.registerReceiver(receiver, intentFilter);
        } else {
            context.registerReceiver(receiver, intentFilter, null, new Handler(Looper.myLooper()));
        }
        emitter.setCancellable(new Cancellable() {
            @Override
            public void cancel() {
                context.unregisterReceiver(receiver);
            }
        });
    }
}