package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.sort.elementary.InsertionSortBasic;

import java.util.Random;

public class InsertionSortBenchmark {

    public static void main(String[] args) {
        
        int[] sizes = {1000, 2000, 4000, 8000, 16000};

        for (int size : sizes) {
            System.out.println("Benchmarking with array size: " + size);
          
            benchmarkSorting(size, "Random");
            benchmarkSorting(size, "Ordered");
            benchmarkSorting(size, "Partially Ordered");
            benchmarkSorting(size, "Reverse Ordered");
        }
    }

    private static void benchmarkSorting(int size, String scenario) {
        
        InsertionSortBasic<Integer> sorter = InsertionSortBasic.create();

        Integer[] array;
        switch (scenario) {
            case "Random":
                array = generateRandomArray(size);
                break;
            case "Ordered":
                array = generateOrderedArray(size);
                break;
            case "Partially Ordered":
                array = generatePartiallyOrderedArray(size);
                break;
            case "Reverse Ordered":
                array = generateReverseOrderedArray(size);
                break;
            default:
                throw new IllegalArgumentException("Unknown scenario: " + scenario);
        }

        Benchmark_Timer<Integer[]> benchmark = new Benchmark_Timer<>(
                "Insertion Sort Benchmark: " + scenario,
                null, 
                arr -> sorter.sort(arr), 
                null 
        );

        double averageTime = benchmark.runFromSupplier(() -> array.clone(), 10); 
        System.out.printf("Average time for %s: %.4f ms%n", scenario, averageTime);
    }

    private static Integer[] generateRandomArray(int size) {
        Random random = new Random();
        return random.ints(size, 1, 100000).boxed().toArray(Integer[]::new);
    }

    private static Integer[] generateOrderedArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = i; 
        }
        return array;
    }

    private static Integer[] generatePartiallyOrderedArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = i; 
        }
       
        Random random = new Random();
        for (int i = 0; i < size / 10; i++) {
            int index1 = random.nextInt(size);
            int index2 = random.nextInt(size);
           
            int temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
        }
        return array;
    }

    private static Integer[] generateReverseOrderedArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i - 1; 
        }
        return array;
    }
}
