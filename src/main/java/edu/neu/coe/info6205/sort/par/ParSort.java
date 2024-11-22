package edu.neu.coe.info6205.sort.par;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * CONSIDER tidy it up a bit.
 */
class ParSort {

    public static int cutoff = 1000;
    public static int maxDepth;
     static {
        
        int availableThreads = ForkJoinPool.commonPool().getParallelism();
        maxDepth = (int) (Math.log(availableThreads) / Math.log(2)); 
        System.out.println("Setting maxDepth based on available threads: " + maxDepth);
    }

    public static void sort(int[] array, int from, int to) {
        sort(array, from, to, 0); // 初始化遞迴深度為 0
    }

    private static void sort(int[] array, int from, int to, int depth) {
        // 當分割區域小於 cutoff 或遞迴深度達到 maxDepth 時，使用系統排序
        if (to - from < cutoff || depth >= maxDepth) {
            Arrays.sort(array, from, to);
        } else {
            // 將陣列分割成兩部分，並在新遞迴層級上平行處理
            int mid = from + (to - from) / 2;
            CompletableFuture<int[]> parsort1 = parsort(array, from, mid, depth + 1);
            CompletableFuture<int[]> parsort2 = parsort(array, mid, to, depth + 1);

            // 合併排序結果
            CompletableFuture<int[]> parsort = parsort1.thenCombine(parsort2, (xs1, xs2) -> {
                int[] result = new int[xs1.length + xs2.length];
                int i = 0, j = 0;
                for (int k = 0; k < result.length; k++) {
                    if (i >= xs1.length) {
                        result[k] = xs2[j++];
                    } else if (j >= xs2.length) {
                        result[k] = xs1[i++];
                    } else if (xs2[j] < xs1[i]) {
                        result[k] = xs2[j++];
                    } else {
                        result[k] = xs1[i++];
                    }
                }
                return result;
            });

            // 將合併結果複製回原始陣列
            parsort.whenComplete((result, throwable) -> System.arraycopy(result, 0, array, from, result.length));
            parsort.join();
        }
    }

    private static CompletableFuture<int[]> parsort(int[] array, int from, int to, int depth) {
        return CompletableFuture.supplyAsync(
                () -> {
                    int[] result = new int[to - from];
                    // TO IMPLEMENT
                    System.arraycopy(array, from, result, 0, result.length);
                    sort(result, 0, to - from, depth);
                    return result;
                }
        );
    }
}