package edu.neu.coe.info6205.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * CONSIDER tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {
        processArgs(args);
        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        Random random = new Random();
        int[] array = new int[2000000];
        ArrayList<Long> timeList = new ArrayList<>();
        for (int j = 50; j < 100; j++) {
            ParSort.cutoff = 10000 * (j + 1);
            // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
            long time;
            long startTime = System.currentTimeMillis();
            for (int t = 0; t < 10; t++) {
                for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                ParSort.sort(array, 0, array.length);
            }
            long endTime = System.currentTimeMillis();
            time = (endTime - startTime);
            timeList.add(time);


            System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t10times Time:" + time + "ms");

        }
        try {
            FileOutputStream fis = new FileOutputStream("./src/result.csv");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            int j = 0;
            for (long i : timeList) {
                String content = (double) 10000 * (j + 1) / 2000000 + "," + (double) i / 10 + "\n";
                j++;
                bw.write(content);
                bw.flush();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processArgs(String[] args) {
        // String[] xs = args;
        // while (xs.length > 0)
        //     if (xs[0].startsWith("-")) xs = processArg(xs);
        String[] remainingArgs = args;
        while (remainingArgs.length > 0 && remainingArgs[0].startsWith("-")) {
            remainingArgs = processArg(remainingArgs);
        }
    }

    private static String[] processArg(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid arguments: " + String.join(" ", args));
            return new String[0];  
        }

        String command = args[0];
        String value = args[1];
        processCommand(command, value);

        String[] remainingArgs = new String[args.length - 2];
        if (remainingArgs.length > 0) {
            System.arraycopy(args, 2, remainingArgs, 0, remainingArgs.length);
        }
        return remainingArgs;
    }

    private static void processCommand(String x, String y) {
        try {
            if (x.equalsIgnoreCase("-N")) {
                // 設置 cutoff 值
                int cutoff = Integer.parseInt(y);
                ParSort.cutoff = cutoff;
                System.out.println("Setting cutoff to " + cutoff);
            } else if (x.equalsIgnoreCase("-P")) {
                // 設置執行緒平行度
                int parallelism = Integer.parseInt(y);
                System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(parallelism));
                // 更新 maxDepth 根據平行度來設置最大遞迴深度
                ParSort.maxDepth = (int) (Math.log(parallelism) / Math.log(2));
                System.out.println("Setting parallelism to " + parallelism + " and max depth to " + ParSort.maxDepth);
            } else {
                System.out.println("Unknown command: " + x);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid value for " + x + ": " + y);
        }
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}