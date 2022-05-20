package com.example.demo.utils;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class testUtil {

    public static void main(String[] args) {
        AtomicReference<BigDecimal> totalIntegralNumber = new AtomicReference<>(BigDecimal.ZERO);

        totalIntegralNumber.updateAndGet(v -> new BigDecimal(String.valueOf(v)).add(BigDecimal.ONE));

        if(totalIntegralNumber.get().compareTo(BigDecimal.ZERO) == 1) {
            System.out.println(totalIntegralNumber.get());
        }

        System.out.println("123456789");

    }

}
