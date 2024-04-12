package com.example.demo.Algorithms;

import com.example.demo.Process;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class RoundRobin {
    public static Vector<Pair<String, Integer>> getGanttChart(ObservableList<Process> processes,int timeQuantum) {
        for (int i = 0; i < processes.size(); i++) {
            for (int j = 0; j < processes.size() - 1; j++) {
                if (processes.get(j).getArrival() > processes.get(j + 1).getArrival()) {
                    Process temp = processes.get(j);
                    processes.set(j, processes.get(j + 1));
                    processes.set(j + 1, temp);
                }
            }
        }
        Vector<Pair<String, Integer>> last = new Vector<>();
        Queue<Process> readyQueue = new LinkedList<>();
        int currentTime = 0;

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // add arriving processes to the ready queue
            while (!processes.isEmpty() && processes.get(0).getArrival() <= currentTime) {
                readyQueue.add(processes.remove(0));
            }

            // processing current time for each process in the ready queue
            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.poll();
                int cpuBurst = Math.min(currentProcess.getCpuBurst(), timeQuantum);
                last.add(new Pair<>(currentProcess.getName(), cpuBurst));
                currentTime += cpuBurst;
                currentProcess.setCpuBurst(currentProcess.getCpuBurst() - cpuBurst);

                // adding back to ready queue if burst time remains
                if (currentProcess.getCpuBurst() > 0) {
                    readyQueue.add(currentProcess);
                }
            } else {
                // if no process is in the ready queue, add an empty slot
                int nextArrival = processes.isEmpty() ? Integer.MAX_VALUE : processes.get(0).getArrival();
                int gap = Math.min(nextArrival - currentTime, timeQuantum);
                last.add(new Pair<>("empty", gap));
                currentTime += gap;
            }
        }
        return last;
    }
}