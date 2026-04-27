
package sjf_vs_priority;


    
    public class GanttBlock {
    public String pid;
    public int start;
    public int end;
    public java.awt.Color color;
 
    public GanttBlock(String pid, int start, int end, java.awt.Color color) {
        this.pid = pid;
        this.start = start;
        this.end = end;
        this.color = color;
    }
}
 

