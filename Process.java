import java.util.*;

class Process {
    String pid;     // اسم العملية 
    int bt;         // Burst Time وقت التنفيذ
    int at;         // Arrival Time وقت الوصول
    int tat;        // Turnaround Time
    int wt;         // Waiting Time
    int remaining; //الوقت المتبقي

    Process(String pid, int bt, int at) {
        this.pid = pid;
        this.bt = bt;
        this.at = at;
        this.remaining = bt;
    }
}

public class CPUSchedule {

    static Scanner sc = new Scanner(System.in);
    static int n;

    public static void main(String[] args) {
        List<Process> processes = getInput();
        roundRobin(processes, 2);           
        fcfs(processes);                       
        sjf(processes);                        
        srtf(processes);                      
    }

    public static List<Process> getInput() {
        System.out.print("Enter number of processes: ");
        n = sc.nextInt();
        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            int bt = sc.nextInt();
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            int at = sc.nextInt();
            processes.add(new Process("P" + (i + 1), bt, at));
        }
        return processes;
    }

    public static void printOutput(String name, List<String> gantt, List<Process> results) {
        System.out.println("\n== " + name + " ==");
        System.out.println("Gantt Chart: " + String.join(" | ", gantt));
        System.out.println("Process\tBT\tAT\tTAT\tWT");

        double totalTAT = 0, totalWT = 0;
        for (Process p : results) {
            System.out.printf("%-5s\t%d\t%d\t%d\t%d\n", p.pid, p.bt, p.at, p.tat, p.wt);
            totalTAT += p.tat;
            totalWT  += p.wt;
        }
        System.out.printf("Avg TAT = %.2f\n", totalTAT / results.size());
        System.out.printf("Avg WT  = %.2f\n", totalWT / results.size());
        System.out.println("*********************************");
    }

    // خوارزمية FCFS
    public static void fcfs(List<Process> input) {
         for (int i = 0; i < input.size() - 1; i++) {//lop for sort process by the Arrival Time using "Selection sort"
        int minIndex = i;
        for (int j = i + 1; j < input.size(); j++) {
            if (input.get(j).at < input.get(minIndex).at) {
                minIndex = j;
            }
        }
        if (minIndex != i) {
            Process temp = input.get(i);
            input.set(i, input.get(minIndex));
            input.set(minIndex, temp);
        }
    }

        int time = 0;             // to track current time
        double totalTAT = 0, totalWT = 0; // for calculating averages
         List<String> gantt = new ArrayList<>();

        // Compute TAT and WT for each process
        for (Process p : input) {
            if (time < p.at) {
                time = p.at;  // wait if cpu is idle
            }

            gantt.add(p.pid); // build gantt chart

            time += p.bt;              // update  time
            p.tat = time - p.at;       // calculate turnaround time
            p.wt = p.tat - p.bt;              // calculate waiting time

            totalTAT += p.tat;                // total turnaround time
            totalWT += p.wt;                  // total waiting time
        }

        printOutput("FCFS",gantt,input);
    }
}
    // خوارزمية SJF
    public static void sjf(List<Process> input) {
       
    }

    // خوارزمية SRTF
    public static void srtf(List<Process> input) {
       }

    // خوارزمية Round Robin
    public static void roundRobin(List<Process> input, int quantum) {
        List<Process> pList = new ArrayList<>();
        for (Process p : input) pList.add(new Process(p.pid, p.bt, p.at));

        int[] remBt = new int[n];
        for (int i = 0; i < n; i++) remBt[i] = pList.get(i).bt;

        boolean[] arrived = new boolean[n];
        boolean[] completed = new boolean[n];
        int time = 0;
        List<String> gantt = new ArrayList<>();
        List<Integer> queue = new LinkedList<>();
        Process[] result = new Process[n];
        int done = 0;

        while (done < n) {
            for (int i = 0; i < n; i++) {
                if (pList.get(i).at <= time && !arrived[i]) {
                    queue.add(i);
                    arrived[i] = true;
                }
            }

            if (queue.isEmpty()) {
                gantt.add("Idle");
                time++;
                continue;
            }

            int idx = queue.remove(0);
            int execTime = Math.min(quantum, remBt[idx]);

            for (int i = 0; i < execTime; i++) {
                gantt.add(pList.get(idx).pid);
                time++;
                remBt[idx]--;

                for (int j = 0; j < n; j++) {
                    if (pList.get(j).at <= time && !arrived[j]) {
                        queue.add(j);
                        arrived[j] = true;
                    }
                }
            }

            if (remBt[idx] > 0) {
                queue.add(idx);
            } else if (!completed[idx]) {
                int tat = time - pList.get(idx).at;
                int wt = tat - pList.get(idx).bt;
                pList.get(idx).tat = tat;
                pList.get(idx).wt = wt;
                result[idx] = pList.get(idx);
                completed[idx] = true;
                done++;
            }
        }

        printOutput("Round Robin", gantt, Arrays.asList(result));
    }
}
