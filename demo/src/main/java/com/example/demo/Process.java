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

    public Process(String name,int arrival,int cpuBurst,int priority,Color color){
        this.name = name;
        this.cpuBurst = cpuBurst;
        this.priority = priority;
        this.color = color;
        this.arrival = arrival;
        remainingTime = cpuBurst;
    }

    public int getArrival() {
        return arrival;
    }

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCpuBurst() {
        return cpuBurst;
    }

    public void setCpuBurst(int cpuBurst) {
        this.cpuBurst = cpuBurst;
    }
    @Override
    public Process clone(){
        Process process = new Process(this.name,this.arrival,this.cpuBurst,this.priority,this.color);
        return process;
    }
    public static int getAverageWaitingTime(Vector<Pair<String,Integer>>gantt,ObservableList<Process>processes){
        int avgWaitingTime=0;

        return avgWaitingTime;
    }
    public static int getAverageTurnAroundTime(Vector<Pair<String,Integer>>gantt,ObservableList<Process>processes){
        int avgTurnAround=0;

        return avgTurnAround;
    }
}
