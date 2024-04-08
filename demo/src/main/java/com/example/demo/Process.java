package com.example.demo;

import javafx.scene.paint.Color;

public class Process {
    private String name ;
    private int cpuBurst;
    private int priority;
    private Color color;

    public Process(String name,int cpuBurst,int priority,Color color){
        this.name = name;
        this.cpuBurst = cpuBurst;
        this.priority = priority;
        this.color = color;
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
