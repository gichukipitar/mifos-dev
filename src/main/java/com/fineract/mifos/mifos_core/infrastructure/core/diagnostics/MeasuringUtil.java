package com.fineract.mifos.mifos_core.infrastructure.core.diagnostics;

import org.springframework.util.StopWatch;

import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MeasuringUtil {

    private MeasuringUtil() {}

    public static void measure(Runnable r, Consumer<Duration> c) {
        measure(() -> {
            r.run();
            return null;
        }, c);
    }

    public static <T> T measure(Supplier<T> s, Consumer<Duration> c) {
        return measure(s, (result, timeTaken) -> c.accept(timeTaken));
    }

    public static <T> T measure(Supplier<T> s, BiConsumer<T, Duration> c) {
        StopWatch sw = new StopWatch();
        sw.start();
        T result = null;
        try {
            result = s.get();
        } finally {
            sw.stop();
            c.accept(result, Duration.ofMillis(sw.getTotalTimeMillis()));
        }
        return result;
    }

}
