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

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.DeadObjectException;
import android.os.HandlerThread;
import android.os.Looper;

import com.github.karczews.utilsverifier.UtilsVerifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.concurrent.Semaphore;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RxBroadcastReceiverTest {

    private static final IntentFilter testIntentFilter = new IntentFilter("testaction");

    private static final Intent testIntent1 = new Intent("testaction").putExtra("testData", 1);
    private static final Intent testIntent2 = new Intent("testaction").putExtra("testData", 2);
    private static final Intent testIntent3 = new Intent("testaction").putExtra("testData", 3);

    @Test
    public void shouldReceiveBroadcast() {
        //GIVEN
        final TestObserver<Intent> observer = RxBroadcastReceivers.fromIntentFilter(application, testIntentFilter)
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
        final Observable<Intent> observable = RxBroadcastReceivers.fromIntentFilter(application, testIntentFilter);

        //WHEN
        final TestObserver<Intent> observer = observable.test(true);
        application.sendBroadcast(testIntent1);
        application.sendBroadcast(testIntent2);
        application.sendBroadcast(testIntent3);
        //THEN
        observer.assertValueCount(0);
        observer.assertEmpty();
    }

    @Test
    public void shouldReturnErrorWhenSubscribeOnNonLooperThread() {
        //GIVEN
        final Observable<Intent> observable = RxBroadcastReceivers.fromIntentFilter(application, testIntentFilter)
                .subscribeOn(Schedulers.newThread());

        //WHEN
        final TestObserver<Intent> observer = observable.test();

        //THEN
        observer.awaitTerminalEvent();
        observer.assertTerminated();
    }

    @Test
    public void shouldReceiveBroadcastOnLooperThread() throws InterruptedException {
        //GIVEN
        final Semaphore beforeLooperPrepare = new Semaphore(0);
        final Semaphore afterLooperPrepare = new Semaphore(0);
        //due to robolectic dirty hack to subscription to be run really on TestHandlerThread due to robolectric
        final HandlerThread handlerThread = new HandlerThread("TestHandlerThread") {
            @Override
            protected void onLooperPrepared() {
                try {
                    beforeLooperPrepare.acquire();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                shadowOf(Looper.myLooper()).idle();
                afterLooperPrepare.release();
            }
        };

        handlerThread.start();
        final Observable<Intent> observable = RxBroadcastReceivers.fromIntentFilter(application, testIntentFilter)
                .subscribeOn(AndroidSchedulers.from(handlerThread.getLooper()));

        //WHEN
        final TestObserver<Intent> observer = observable.test();

        beforeLooperPrepare.release();
        afterLooperPrepare.acquire();

        assertEquals(0, shadowOf(handlerThread.getLooper()).getScheduler().size());
        application.sendBroadcast(testIntent1);
        assertEquals(1, shadowOf(handlerThread.getLooper()).getScheduler().size());
        application.sendBroadcast(testIntent2);
        assertEquals(2, shadowOf(handlerThread.getLooper()).getScheduler().size());
        application.sendBroadcast(testIntent3);
        assertEquals(3, shadowOf(handlerThread.getLooper()).getScheduler().size());

        shadowOf(handlerThread.getLooper()).idle();
        //THEN

        observer.assertValueCount(3);
        observer.assertValues(testIntent1, testIntent2, testIntent3);
    }

    @Test
    public void shouldSendErrorOnDisposalToThreadsUncaughtExceptionHandler() {
        // given context throws DeadSystemException when we try to unregister
        final UncaughtExceptionHandler exceptionHandler = new UncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
        final Application applicationSpy = Mockito.spy(RuntimeEnvironment.application);
        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws DeadObjectException {
                throw new DeadObjectException();
            }
        }).when(applicationSpy).unregisterReceiver(any(BroadcastReceiver.class));
        when(applicationSpy.getApplicationContext()).thenReturn(applicationSpy);

        final TestObserver<Intent> observer = RxBroadcastReceivers.fromIntentFilter(applicationSpy, testIntentFilter)
                .test();
        // when dispose occurs
        observer.dispose();

        // then Threads UncaughtExceptionHandler receives the error via RxJavaPlugins.
        exceptionHandler.assertCaughtExceptionWithCauseType(DeadObjectException.class);
    }

    @Test
    public void shouldBeHaveWellDefinedUtil() {
        UtilsVerifier.forClass(RxBroadcastReceivers.class)
                .withConstructorThrowing(AssertionError.class)
                .verify();
    }

}
