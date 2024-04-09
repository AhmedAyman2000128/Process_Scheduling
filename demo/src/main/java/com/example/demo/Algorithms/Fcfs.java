package com.example.demo.Algorithms;

import com.example.demo.Process;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Vector;

public class Fcfs {
    public static Vector<Pair<String,Integer>> getganttChart(ObservableList<Process>processes){
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
        for(int i=0;i<processes.size();i++){
            if(processes.get(i).getArrival()>time) {
                last.add(new Pair("empty",processes.get(i).getArrival()-time));
                time = processes.get(i).getArrival();
            }
            last.add(new Pair(processes.get(i).getName(),processes.get(i).getCpuBurst()));
            time=time+processes.get(i).getCpuBurst();
        }
        return last;
    }
}
