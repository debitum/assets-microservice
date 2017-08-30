package com.debitum.assets;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class RandomValueHelper {

    final static Set<Long> picked = new HashSet<>();
    final static Set<Integer> pickedInt = new HashSet<>();
    final static Set<Double> pickedDouble = new HashSet<>();

    public static String randomString(String prefix) {

        return prefix + "-" + RandomStringUtils.random(
                8,
                true,
                true
        );
    }

    public static String randomString() {

        return randomString("");
    }

    public static Long randomLong() {
        Random random = new Random(System.nanoTime());
        Long value = (long) random.nextInt(Integer.MAX_VALUE);
        while (picked.contains(value)) {
            value = (long) random.nextInt(Integer.MAX_VALUE);
        }
        picked.add(value);
        return value;
    }

    public static Double randomDouble() {
        Random random = new Random(System.nanoTime());
        Double value = 300 + random.nextDouble();
        while (pickedDouble.contains(value)) {
            value = random.nextDouble();
        }
        pickedDouble.add(value);
        return value;
    }

    public static Double randomInvestmentAmount() {
        return 300 + randomDouble();
    }

    public static String randomLogin() {
        return RandomStringUtils.random(
                8,
                true,
                false
        ) + "@test-domain.com";
    }

    public static Date randomDate() {
        Random random = new Random(System.nanoTime());
        Long ms = -946771200000L + (Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
        return new Date(ms);
    }

    public static String randomText() {
        return RandomStringUtils.random(
                2000,
                true,
                true
        );
    }
}
