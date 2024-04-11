package com.example.demo.Algorithms;

import com.example.demo.Process;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.Vector;

public class Priority_NonPreemptive {
    private static final int  PRIORITY_MAX = 11;
    public static Vector<Pair<String,Integer>> getganttChart(ObservableList<Process> processes){
        for(int i=0;i<processes.size();i++){
            for(int j=0;j<processes.size()-1;j++){
                if(processes.get(j).getArrival()>processes.get(j+1).getArrival()){
                    Process process = processes.get(j);
                    processes.set(j,processes.get(j+1));
                    processes.set(j+1,process);
                }
            }
        }
        Vector<Pair<String,Integer>>last = new Vector<>();
        int time=0;
        while(processes.size()>0){
            int priority = PRIORITY_MAX;
            int processsIndex = -1;
            for(int i=0;i<processes.size();i++){
                if(processes.get(i).getArrival()<=time && processes.get(i).getPriority()<priority){
                    priority = processes.get(i).getPriority();
                    processsIndex = i;
                }
            }
            if(processsIndex == -1){
                int minArrival = Integer.MAX_VALUE;
                for(int i=0;i<processes.size();i++){
                    if(minArrival > processes.get(i).getArrival()){
                        minArrival = processes.get(i).getArrival();
                    }
                }
                last.add(new Pair("empty",minArrival-time));
                time = minArrival;
            }
            else{
                last.add(new Pair<>(processes.get(processsIndex).getName(),processes.get(processsIndex).getCpuBurst()));
                time = time + processes.get(processsIndex).getCpuBurst();
                processes.remove(processsIndex);
            }
        }
        return last;
    }
}
