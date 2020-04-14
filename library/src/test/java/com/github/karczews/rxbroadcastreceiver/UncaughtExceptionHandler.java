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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final List<Throwable> mCaughtThrowables = new ArrayList<>();

    @Override
    public void uncaughtException(final Thread thread,
                                  final Throwable throwable) {
        mCaughtThrowables.add(throwable);
    }

    void assertCaughtExceptionWithCauseType(final Class<? extends Throwable> type) {
        boolean hasCause = false;
        for (final Throwable ex: mCaughtThrowables) {
            if (type.equals(ex.getCause().getClass())) {
                hasCause = true;
                break;
            }
        }

        assertTrue("no exception with cause of type " + type + ", currently caught exceptions are=" + mCaughtThrowables, hasCause);
    }
}
