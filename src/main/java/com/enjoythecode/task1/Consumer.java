package com.enjoythecode.task1;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Consumer implements Callable<Map<String, Long>> {

    private static List<String> RAW_TYPES =
            new ArrayList<>(Arrays.asList("int", "double", "float", "byte", "short", "long", "boolean", "char"));

    private final BlockingQueue<String> blockingQueue;

    private final AtomicInteger producerCounter;

    public Consumer(BlockingQueue<String> blockingQueue, AtomicInteger producerCounter) {
        this.blockingQueue = blockingQueue;
        this.producerCounter = producerCounter;
    }

    @Override
    public Map<String, Long> call() throws Exception {
        Map<String, Long> result = new HashMap<>();
        while (true) {
            String content = blockingQueue.poll();
            if (content == null && producerCounter.get() == 0) {
                content = blockingQueue.poll();
                if (content == null) {
                    break;
                }
            }
            if (content == null) {
                continue;
            }
            Map<String, Long> singleContentResult = getTypeFromString(content);
            for (String key : singleContentResult.keySet()) {
                result.put(key, result.getOrDefault(key, 0L) + singleContentResult.get(key));
            }
        }
        return result;
    }

    private Map<String, Long> getTypeFromString(String content) {
        Map<String, Long> result = new HashMap<>();
        for (String s : RAW_TYPES) {
            Matcher matcher = Pattern.compile(s).matcher(content);
            while (matcher.find()) {
                result.put(s, result.getOrDefault(s, 0L) + 1);
            }
        }
        return result;
    }

}
