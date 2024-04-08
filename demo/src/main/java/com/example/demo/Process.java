package com.example.demo;

import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Process {
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
}
