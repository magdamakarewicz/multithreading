package com.enjoythecode.task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        //assumption that both searching and retrieving file contents (Producers)
        //and counting primitive types (Consumers) will be performed in three threads

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        String path = "src/main/resources";

        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        AtomicInteger producerCounter = new AtomicInteger(3);

        //adding three Producers to the thread pool
        executorService.submit(new Producer(path, queue, producerCounter));
        executorService.submit(new Producer(path, queue, producerCounter));
        executorService.submit(new Producer(path, queue, producerCounter));

        //adding three Consumers to the thread pool and receiving results of processing immediately
        Future<Map<String, Long>> result1 = executorService.submit(new Consumer(queue, producerCounter));
        Future<Map<String, Long>> result2 = executorService.submit(new Consumer(queue, producerCounter));
        Future<Map<String, Long>> result3 = executorService.submit(new Consumer(queue, producerCounter));

        Map<String, Long> res1;
        Map<String, Long> res2;
        Map<String, Long> res3;

        try {
            res1 = result1.get();
            res2 = result2.get();
            res3 = result3.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Result maps from three Consumers:");
        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);

        List<Map<String, Long>> maps = new ArrayList<>(Arrays.asList(res1, res2, res3));
        System.out.println("List of result maps: " + maps);

        System.out.println("Merged maps: " + mergeMaps(maps));

        executorService.shutdown();

    }

    public static Map<String, Long> mergeMaps(List<Map<String, Long>> listOfMaps) {
        return listOfMaps
                .stream()
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)));
    }

}