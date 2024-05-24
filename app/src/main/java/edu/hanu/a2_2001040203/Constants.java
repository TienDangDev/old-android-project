package edu.hanu.a2_2001040203;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constants {
    public static final ExecutorService executor = Executors.newFixedThreadPool(4);
}
