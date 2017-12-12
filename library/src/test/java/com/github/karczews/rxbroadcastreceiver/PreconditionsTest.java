package com.github.karczews.rxbroadcastreceiver;

import com.github.karczews.utilsverifier.UtilsVerifier;

import org.junit.Test;

public class PreconditionsTest {

    @Test
    public void shouldBeWellDefinedUtil() {
        UtilsVerifier.forClass(Preconditions.class)
                .withConstructorThrowing(AssertionError.class)
                .verify();
    }
}
