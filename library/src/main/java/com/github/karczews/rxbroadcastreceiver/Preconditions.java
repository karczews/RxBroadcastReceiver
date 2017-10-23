package com.github.karczews.rxbroadcastreceiver;

import android.os.Looper;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposables;

public final class Preconditions {
    private Preconditions() {
        throw new AssertionError("No util class instances for you!");
    }

    public static boolean checkLooperThread(final Observer observer) {
        if (Looper.myLooper() == null) {
            observer.onSubscribe(Disposables.empty());
            observer.onError(new IllegalStateException("Calling thread is not associated with Looper"));
            return false;
        } else {
            return true;
        }
    }

}
