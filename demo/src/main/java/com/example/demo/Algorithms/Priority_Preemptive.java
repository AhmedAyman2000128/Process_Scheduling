package com.example.demo.Algorithms;

import com.example.demo.Process;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.Vector;

public class Priority_Preemptive {
    public static Vector<Pair<String,Integer>> getganttChart(ObservableList<Process> processes){

        Vector<Pair<String,Integer>>last = new Vector<>();

        int total_time=0;
        for(int i=0;i<processes.size();i++)
        {
            total_time+= processes.get(i).getCpuBurst();
        }

        for(int i=0;i<processes.size();i++){
            for(int j=0;j<processes.size()-1;j++){
                if(processes.get(j).getArrival()>processes.get(j+1).getArrival()){
                    Process process = processes.get(j);
                    processes.set(j,processes.get(j+1));
                    processes.set(j+1,process);
                }
            }
        }


        int current_time=0;
        while (current_time<total_time)
        {
            int max_priority=10000;
            int max_priority_index=-1;

            for(int i=0;i<processes.size();i++)
            {
                if(processes.get(i).getArrival()<=current_time)
                {
                    if(max_priority>processes.get(i).getPriority())
                    {
                        max_priority=processes.get(i).getPriority();
                        max_priority_index=i;
                    }
                }
                else
                {
                    break;
                }
            }
            Pair<String, Integer> pairToAdd;
            if(max_priority_index==-1)
            {
                 pairToAdd = new Pair<>("empty", 1);
                current_time++;
                total_time++;
                last.add(pairToAdd);
            }
            else
            {
                 pairToAdd = new Pair<>(processes.get(max_priority_index).getName(), 1);
                last.add(pairToAdd);
                current_time++;
                processes.get(max_priority_index).setRemainingTime(processes.get(max_priority_index).getRemainingTime()-1);
                if(processes.get(max_priority_index).getRemainingTime()<=0)
                {
                    processes.remove(max_priority_index);
                }
            }


        }
        return last;
        /*
        Vector<Pair<String,Integer>>last2 = new Vector<>();
        for(int i=0;i<last.size();i++)
        {
            int sum=1;
            String process_name = last.get(i).getKey();
            while (true)
            {
                if(i+1>=last.size())
                {
                    break;
                }
                if(last.get(i).getKey().equals(last.get(i+1).getKey()))
                {
                    sum++;
                    i++;

                }
                else
                {
                    break;
                }


            }
            Pair<String, Integer> pairToAdd = new Pair<>(process_name, sum);
            last2.add(pairToAdd);

        }


        return last2;
        */

    }
}
