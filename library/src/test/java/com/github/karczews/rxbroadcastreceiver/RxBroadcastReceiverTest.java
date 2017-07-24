package com.github.karczews.rxbroadcastreceiver;

import android.content.Intent;
import android.content.IntentFilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import io.reactivex.observers.TestObserver;

import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class RxBroadcastReceiverTest {

    private static final IntentFilter testIntentFilter = new IntentFilter("testaction");

    private static final Intent testIntent1 = new Intent("testaction").putExtra("testData", 1);
    private static final Intent testIntent2 = new Intent("testaction").putExtra("testData", 2);
    private static final Intent testIntent3 = new Intent("testaction").putExtra("testData", 3);

    @Test
    public void shouldReceiveBroadcast() {
        //GIVEN
        TestObserver<Intent> observer = RxBroadcastReceivers.fromIntentFilter(application, testIntentFilter)
                .test();
        //WHEN
        application.sendBroadcast(testIntent1);
        application.sendBroadcast(testIntent2);
        application.sendBroadcast(testIntent3);
        //THEN
        observer.assertValueCount(3);
        observer.assertValues(testIntent1, testIntent2, testIntent3);
    }

    @Test
    public void shouldNotReceiveBroadcastAfterDisposed() {
        //GIVEN
        TestObserver<Intent> observer = RxBroadcastReceivers.fromIntentFilter(application, testIntentFilter)
                .test(true);

        //WHEN
        application.sendBroadcast(testIntent1);
        application.sendBroadcast(testIntent2);
        application.sendBroadcast(testIntent3);
        //THEN
        observer.assertValueCount(0);
        observer.assertEmpty();
    }
}
