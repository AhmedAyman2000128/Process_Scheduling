# CPU Process Scheduler
## Overview
This program is a simulator for various scheduling algorithms that are used in many operating systems. It visualizes how the processor dynamically manages process execution and how different scheduling algorithms compare to each other in terms of average turnaround time and average waiting time.
### [Short demonstration video](https://drive.google.com/drive/folders/1E4hCgClAnyx_KT_Lk8KarcdJ2LjsbdIX)
### Program Exe file: [download](https://drive.google.com/file/d/1DEutPPfLBFKLZrjZdMwV7UXFNJO_uvGm/view)
### Program Jar file: [download](https://drive.google.com/file/d/1SoA9-jT3WJwsJuzk3-n2Eqv1OOAeCQ5R/view)
## Program Features
#### The program enables you to select from the following scheduling algorithms:
* [FCFS](https://github.com/AhmedAyman2000128/Process_Scheduling/blob/master/demo/src/main/java/com/example/demo/Algorithms/Fcfs.java) (First Come First Serve).
* [Preemptive SJF](https://github.com/AhmedAyman2000128/Process_Scheduling/blob/master/demo/src/main/java/com/example/demo/Algorithms/SJF_Preemptive.java) (preemptive Shortest Job first).
* [Non-Preemptive SJF](https://github.com/AhmedAyman2000128/Process_Scheduling/blob/master/demo/src/main/java/com/example/demo/Algorithms/SJF_NonPreemptive.java) (non-preemptive Shortest Job first).
* [Preemptive Priority](https://github.com/AhmedAyman2000128/Process_Scheduling/blob/master/demo/src/main/java/com/example/demo/Algorithms/Priority_Preemptive.java).
* [Non-Preemptive Priority](https://github.com/AhmedAyman2000128/Process_Scheduling/blob/master/demo/src/main/java/com/example/demo/Algorithms/Priority_NonPreemptive.java).
* [Round Robin](https://github.com/AhmedAyman2000128/Process_Scheduling/blob/master/demo/src/main/java/com/example/demo/Algorithms/RoundRobin.java).
#### The program also has two visualization modes:
* Static: shows the complete Gantt chart instantly with all the processes and their slots.
* Live: shows the chart being drawn live unit time by unit time, with the option to pause and add a process while running.
#### The Program enables the user to input data of each process like:
* Arrival time
* Burst time
* Priority (in case of priority scheduling)
* And input the quantum of the processor in case of round-robin
## User Interface Manual
1) First start by selecting the type of algorithm you want to simulate.</br>
![image](https://github.com/AhmedAyman2000128/Process_Scheduling/assets/13494749/11b6d8fd-727a-4d32-9cde-9b769b7e7973)
2) Then choose the visualization mode.</br>
![image](https://github.com/AhmedAyman2000128/Process_Scheduling/assets/13494749/f09ea53f-c01e-473b-bc16-bdd0720ac3df)
3) Enter the data of the processes one by one and click add.</br>
![image](https://github.com/AhmedAyman2000128/Process_Scheduling/assets/13494749/7e1b2a19-f7c9-4374-af7c-595e1c5fc5e3)
4) Once you finish entering the data, click simulate.</br>
![image](https://github.com/AhmedAyman2000128/Process_Scheduling/assets/13494749/0e2d1aa7-6adf-4844-bc74-eb7ae6c2e79c)
5) You can pause the simulation in the middle and enter a new process if you choose live mode.</br>
![image](https://github.com/AhmedAyman2000128/Process_Scheduling/assets/13494749/224c65d5-ae1f-468b-b861-b03e4eb776aa)
6) Once the simulation ends, you can click reset to start a new simulation.
## Contribution
If you encounter any error or want to make an enhancement, you are welcome to open an issue or fork the repository and make a pull request.
