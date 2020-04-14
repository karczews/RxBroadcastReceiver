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

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

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
