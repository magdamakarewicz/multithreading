package com.enjoythecode.task1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

    public class Producer implements Callable<Void> {

        private final String path;

        private final BlockingQueue<String> blockingQueue;

        private AtomicInteger producerCounter;

        public Producer(String path, BlockingQueue<String> blockingQueue, AtomicInteger producerCounter) {
            this.path = path;
            this.blockingQueue = blockingQueue;
            this.producerCounter = producerCounter;
        }

        @Override
        public Void call() throws Exception {
            processDir(path);
            producerCounter.decrementAndGet();
            return null;
        }

        public void processDir(String dirName) throws IOException {
            File path = new File(dirName);
            if (path.isDirectory()) {
                for (File f : Optional.ofNullable(path.listFiles()).orElse(new File[]{})) {
                    if (f.isDirectory()) {
                        processDir(f.getAbsolutePath());
                    } else {
                        blockingQueue.add(copyFileContent(f.getAbsolutePath()));
                    }
                }
            }
        }

        private String copyFileContent(String filePath) throws IOException {
            try (
                    BufferedReader in = new BufferedReader(new FileReader(filePath))
            ) {
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            }
        }

}
