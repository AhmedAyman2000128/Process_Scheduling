package com.example.demo;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.Vector;

public class Process implements Cloneable{
    private String name ;
    private int cpuBurst;
    private int priority;
    private Color color;
    private int arrival;
    private int remainingTime;
    private int completionTime;

    public Process(String name,int arrival,int cpuBurst,int priority,Color color){
        this.name = name;
        this.cpuBurst = cpuBurst;
        this.priority = priority;
        this.color = color;
        this.arrival = arrival;
        remainingTime = cpuBurst;
        completionTime=0;
    }

    public int getArrival() {return arrival;}
    public void setArrival(int arrival) {this.arrival = arrival;}
    public int getRemainingTime() {return remainingTime;}
    public void setRemainingTime(int remainingTime) {this.remainingTime = remainingTime;}
    public Color getColor() {return color;}
    public void setColor(Color color) {this.color = color;}
    public int getPriority() {return priority;}
    public void setPriority(int priority) {this.priority = priority;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getCpuBurst() {return cpuBurst;}
    public int getCompletionTime() {return completionTime;}
    public void setCompletionTime(int completionTime) {this.completionTime= completionTime;}
    public void setCpuBurst(int cpuBurst) {this.cpuBurst = cpuBurst;}
    private static void getWaitingTime(Vector<Pair<String,Integer>>gantt,ObservableList<Process>processes){
        int completion_time=0;
        for(int i=0;i<gantt.size();i++)
        {
            completion_time+=gantt.get(i).getValue();
            for(int j=0;j<processes.size();j++)
            {
                if(gantt.get(i).getKey().equals(processes.get(j).getName()))
                {
                    processes.get(j).setCompletionTime(completion_time);
                }
            }
        }
    }
    public static float getAverageWaitingTime(Vector<Pair<String,Integer>>gantt,ObservableList<Process>processes){
        getWaitingTime(gantt,processes);
        float avgWaitingTime=0;
        for(int i=0;i<processes.size();i++)
        {
            avgWaitingTime+=(processes.get(i).getCompletionTime()-processes.get(i).getArrival()-processes.get(i).getCpuBurst());
        }
        avgWaitingTime=avgWaitingTime/(processes.size());
        return avgWaitingTime;
    }
    public static float getAverageTurnAroundTime(Vector<Pair<String,Integer>> gantt, ObservableList<Process>processes){
        getWaitingTime(gantt,processes);
        float avgTurnAround=0;
        for(int i=0;i<processes.size();i++)
        {
            avgTurnAround+=(processes.get(i).getCompletionTime()-processes.get(i).getArrival());
        }
        avgTurnAround=avgTurnAround/(processes.size());
        return avgTurnAround;
    }
    @Override
    public Process clone(){
        Process process = new Process(this.name,this.arrival,this.cpuBurst,this.priority,this.color);
        return process;
    }
}