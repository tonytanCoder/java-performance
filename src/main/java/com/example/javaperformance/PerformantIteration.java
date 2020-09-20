package com.example.javaperformance;

import java.util.ArrayList;
import java.util.List;

public class PerformantIteration {
    private static int theSum = 0;

    public static void main(String[] args) {
        System.out.println("Starting microbenchmark on iterating over collections with a call to size() in each iteration");
        List<Integer> nums = new ArrayList<Integer>();
        for(int i=0; i<50000; i++) {
            nums.add(i);
        }

        System.out.println("Warming up ...");
        //warmup... make sure all JIT comliling is done before the actual benchmarking starts
        for(int i=0; i<10; i++) {
            iterateWithConstantSize(nums);
            iterateWithDynamicSize(nums);
        }

        //actual
        System.out.println("Starting the actual test");
        long constantSizeBenchmark = iterateWithConstantSize(nums);
        long dynamicSizeBenchmark = iterateWithDynamicSize(nums);
        System.out.println("Test completed... printing results");

        System.out.println("constantSizeBenchmark : " + constantSizeBenchmark);
        System.out.println("dynamicSizeBenchmark : " + dynamicSizeBenchmark);
        System.out.println("dynamicSizeBenchmark/constantSizeBenchmark : " + ((double)dynamicSizeBenchmark/(double)constantSizeBenchmark));
    }

    private static long iterateWithDynamicSize(List<Integer> nums) {
        int sum=0;
        long start = System.nanoTime();
        for(int i=0; i<nums.size(); i++) {
            // appear to do something useful
            sum += nums.get(i);
        }
        long end = System.nanoTime();
        setSum(sum);
        return end-start;
    }

    private static long iterateWithConstantSize(List<Integer> nums) {
        int count = nums.size();
        int sum=0;
        long start = System.nanoTime();
        for(int i=0; i<count; i++) {
            // appear to do something useful
            sum += nums.get(i);
        }
        long end = System.nanoTime();
        setSum(sum);
        return end-start;
    }

    // invocations to this method simply exist to fool the VM into thinking that we are doing something useful in the loop
    private static void setSum(int sum) {
        theSum = sum;
    }
}
