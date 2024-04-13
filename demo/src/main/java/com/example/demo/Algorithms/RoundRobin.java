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


        int current_time=0;
        while(processes.size()>0)
        {
            for(int i=0;i<processes.size();i++)
            {
                double min_roundRobinArrival=100000;
                int min_roundRobinArrival_index=-1;
                double current_time2=current_time+0.1;
                for(int j=0;j<processes.size();j++)
                {
                    if(min_roundRobinArrival>processes.get(j).getRoundRobinArivalTime()&&current_time2>=processes.get(j).getRoundRobinArivalTime())
                    {
                        min_roundRobinArrival=processes.get(j).getRoundRobinArivalTime();
                        min_roundRobinArrival_index=j;
                    }
                }

                if(min_roundRobinArrival_index==-1)
                {
                    Pair<String,Integer>pairToAdd = new Pair<>("empty", 1);
                    last.add(pairToAdd);
                    current_time++;
                }


                else if(processes.get(min_roundRobinArrival_index).getRemainingTime()>=timeQuantum)
                {
                    processes.get(min_roundRobinArrival_index).setRemainingTime(processes.get(min_roundRobinArrival_index).getRemainingTime()-timeQuantum);
                    current_time+=timeQuantum;
                    Pair<String,Integer>pairToAdd = new Pair<>(processes.get(min_roundRobinArrival_index).getName(), timeQuantum);
                    last.add(pairToAdd);
                    processes.get(min_roundRobinArrival_index).setRoundRobinArivalTime(current_time+0.1);
                    if(processes.get(min_roundRobinArrival_index).getRemainingTime()==0)
                    {
                        processes.remove(min_roundRobinArrival_index);
                    }
                }
                else
                {
                    current_time+=processes.get(min_roundRobinArrival_index).getRemainingTime();
                    Pair<String,Integer>pairToAdd = new Pair<>(processes.get(min_roundRobinArrival_index).getName(), processes.get(min_roundRobinArrival_index).getRemainingTime());
                    last.add(pairToAdd);
                    processes.get(min_roundRobinArrival_index).setRoundRobinArivalTime(current_time+0.1);
                    processes.remove(min_roundRobinArrival_index);

                }
            }
        }


        /*
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

         */
        return last;
    }
}