package me.xatzdevelopments.util;

import io.netty.util.internal.ThreadLocalRandom;

import java.util.Random;

public class RandomUtil {

    private final Random random = new Random();

    public final int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public final long randomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    public final float randomFloat(float min, float max) {
        return (float)ThreadLocalRandom.current().nextDouble(min, max);
    }

    public final double randomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public double randomGaussian(double range, double average) {
        return random.nextGaussian() * range + average;
    }

}
