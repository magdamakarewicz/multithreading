# Multithreading Tasks

This repository contains various tasks that demonstrate the use of multithreading in Java. The tasks showcase different aspects of concurrent programming, such as thread communication, synchronization, and resource sharing.

## Task 1: Producer-Consumer Communication

### Description

This task implements a Producer-Consumer pattern where multiple producer threads search for files within a given directory and pass them to consumer threads. The consumers then analyze the contents of these files to count the occurrences of primitive data types.

### Producers

- Each producer receives a directory as an argument and recursively searches for files within this directory.
- The found files are passed to the consumers for processing.
- The directory for the producers to search in is located in the `resources` package.

### Consumers

- Consumers analyze the contents of each file received from the producers.
- They count the occurrences of the following primitive data types: `int`, `byte`, `boolean`, `double`, `char`, `float`, `short`, and `long`.
- The counts are stored in a shared map where the key is the primitive type and the value is the number of occurrences.

### Termination

- The application can run multiple producers and multiple consumers.
- Consumers should terminate their operation when there are no more active producers and no files left to process.

### Main Class

- The Main class demonstrates the functionality of the application.
