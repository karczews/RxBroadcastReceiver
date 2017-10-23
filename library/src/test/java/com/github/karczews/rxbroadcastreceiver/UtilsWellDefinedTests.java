package com.github.karczews.rxbroadcastreceiver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test utility method well defined
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public final class UtilsWellDefinedTests {

    @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {RxBroadcastReceivers.class},
                {Preconditions.class},
        });
    }

    public UtilsWellDefinedTests(final Class utilClass) {
        this.utilClass = utilClass;
    }

    private final Class utilClass;

    @Test
    public void shouldBeFinal() {
        assertTrue(Modifier.isFinal(utilClass.getModifiers()));
    }

    @Test
    public void shouldHavePrivateConstructor() throws NoSuchMethodException {
        assertThat(utilClass.getDeclaredConstructors().length, equalTo(1));
        final Constructor<?> constructor = utilClass.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    public void shouldFailToInstantiate() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        final Constructor<?> constructor = utilClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
            fail();
        } catch (final InvocationTargetException exception) {
            assertTrue(exception.getCause() instanceof AssertionError);
        }
    }

    @Test
    public void shouldHaveNoInstanceMethods() {
        for (final Method method : utilClass.getDeclaredMethods()) {
            if (method.getDeclaringClass().equals(utilClass) && !Modifier.isStatic(method.getModifiers())) {
                fail("found " + method.getName() + " instance method");
            }
        }
    }

}
