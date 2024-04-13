package com.example.demo.Algorithms;

import com.example.demo.Process;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import java.util.Vector;


public class SJF_Preemptive {
    public static Vector<Pair<String, Integer>> getGanttChart(ObservableList<Process> processes) {
        Vector<Pair<String, Integer>> last = new Vector<>();

        int total_time = 0;
        for (int i = 0; i < processes.size(); i++) {
            total_time += processes.get(i).getCpuBurst();
        }

        for (int i = 0; i < processes.size(); i++) {
            for (int j = 0; j < processes.size() - 1; j++) {
                if (processes.get(j).getArrival() > processes.get(j + 1).getArrival()) {
                    Process process = processes.get(j);
                    processes.set(j, processes.get(j + 1));
                    processes.set(j + 1, process);
                }
            }
        }


        int current_time = 0;
        while (current_time < total_time) {
            int shortest_CpuBurst = 10000;
            int CpuBurst_index = -1;

            for (int i = 0; i < processes.size(); i++) {
                if (processes.get(i).getArrival() <= current_time) {
                    if (shortest_CpuBurst > processes.get(i).getCpuBurst()) {
                        shortest_CpuBurst = processes.get(i).getCpuBurst();
                        CpuBurst_index = i;
                    }
                } else {
                    break;
                }
            }
            Pair<String, Integer> pairToAdd;
            if (CpuBurst_index == -1) {
                pairToAdd = new Pair<>("empty", 1);
                current_time++;
                total_time++;
                last.add(pairToAdd);
            } else {
                pairToAdd = new Pair<>(processes.get(CpuBurst_index).getName(), 1);
                last.add(pairToAdd);
                current_time++;
                processes.get(CpuBurst_index).setRemainingTime(processes.get(CpuBurst_index).getRemainingTime() - 1);
                if (processes.get(CpuBurst_index).getRemainingTime() <= 0) {
                    processes.remove(CpuBurst_index);
                }
            }

        }
        return last;
    }
}