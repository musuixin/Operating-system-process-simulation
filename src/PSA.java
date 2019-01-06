import java.util.*;
/***
 * 功能描述：继承父类PCB，实现接口Comparable。
 * @date 2018.10.29
 * @version 0.1
 *
 */
class PSAPCB extends PCB implements Comparable<Object> {
    PSAPCB(int id,String pname, int daodatime, int fuwutime,int priority,int startblack,int blacktime) {
        super(id,pname,daodatime,fuwutime,priority,startblack,blacktime);
    }
    void addPriority(){
        priority+=1;
    }
    void reducePriority(){
        priority-=3;
    }
    @Override
    /**
     * 功能描述：实现对队列的排序
     */
    public int compareTo(Object b){
        PCB st=(PCB)b;
        if(st.priority!=this.priority) {
            return (st.priority - this.priority);
        }
        else{
            return (this.daodatime-st.daodatime);
        }
    }
}
public class PSA {
    public static void main(String[] args) {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++优先权+++++++++++++++++++++++++++++++++++++++++++");
        Scanner input = new Scanner(System.in);
        LinkedList<PSAPCB> beReadyPCB = new LinkedList<>();
        LinkedList<PSAPCB> PSAPCBS=new LinkedList<>();
        LinkedList<PSAPCB> block =new LinkedList<>();
        LinkedList<String> strings=new LinkedList<>();
        PSAPCBS.add(new PSAPCB(1,"a",0,3,100,2,3));
        PSAPCBS.add(new PSAPCB(2,"b",2,6,100,1,4));
        PSAPCBS.add(new PSAPCB(3,"c",4,4,100,3,2));
        PSAPCBS.add(new PSAPCB(4,"d",6,5,100,4,5));
        PSAPCBS.add(new PSAPCB(5,"e",8,2,100,0,0));
//        while (true) {
//            System.out.println("请依次输入进程ID，进程名，到达时间，服务时间，优先权，运行n时间后进入堵塞队列的时间，堵塞时间：");
//            try {
//                int id = input.nextInt();
//                String pname = input.next();
//                int dadatime = input.nextInt();
//                int fuwutime = input.nextInt();
//                int priority = input.nextInt();
//                int startblock=input.nextInt();
//                int blocktime=input.nextInt();
//                PSAPCBS.add(new PSAPCB(id,pname, dadatime, fuwutime, priority,startblock,blocktime));
//                System.out.println("是否继续？（1继续，0结束）");
//                int i = input.nextInt();
//                if (i == 0) {
//                    PSAPCBS.sort((o1,o2)->(o1.daodatime-o2.daodatime));
//                    break;
//                }
//            }
//            catch (Exception e){
//                System.out.println("输入有误！程序非正常运行！请程序运行程序");
//                break;
//            }
//        }
        System.out.println("*******************************************进程运行初信息************************************************");
        System.out.println("*\t\t进程ID\t进程名\t\t到达时间\t\t优先权\t\t服务时间\t\tnS进行后堵塞\t\t堵塞时间\t*");
        for(int i=0;i<PSAPCBS.size();i++){
            System.out.println("*\t\t"+PSAPCBS.get(i).information()+"\t\t\t*");
        }
        System.out.println("*********************************************************************************************************");
        int runslice=0;
        //定义时间片
        /**
         * 每次进入循环之前进行检测
         * 符合3个条件：
         * 1.存放队列不为空
         * 2.就绪队列不为空
         * 3.堵塞队列不为空
         * 若3个队列都为空，则说明进程完全运行完毕，结束循环
         * @date 2018.11.8
         */
        while (!PSAPCBS.isEmpty()||!beReadyPCB.isEmpty()||!block.isEmpty()) {
            runslice++;
            System.out.println("*******************时间切片为：" + runslice+"*************************");
            while (!PSAPCBS.isEmpty()&&PSAPCBS.get(0).daodatime<=runslice){
                beReadyPCB.add(PSAPCBS.get(0));
                PSAPCBS.remove(0);
            }
            if(!block.isEmpty()){
                for(int i=0;i<block.size();i++){
                    if(block.get(i).blocktime<=0){
                        System.out.print("时间初从堵塞队列转入就绪队列的进程有：");
                        System.out.print(block.get(i).pname);
                        beReadyPCB.add(block.get(i));
                        block.remove(i);
                    }
                }
                System.out.println();
                if(!block.isEmpty()) {
                    System.out.print("进入堵塞队列的进程有：");
                    for (int i = 0; i < block.size(); i++) {
                        System.out.print(block.get(i).pname + " ");
                    }
                }
                System.out.println();
                for(int i=0;i<block.size();i++){
                    block.get(i).blocktime--;
                }
            }
            if(beReadyPCB.isEmpty()){
                System.out.println("当前没有程序运行");
            }
            if(!beReadyPCB.isEmpty()){
                beReadyPCB.sort(PSAPCB::compareTo);
                System.out.println("当前正在进行的进程为：");
                System.out.println("进程ID\t进程名\t\t运行时间\t\t优先权\t\t状态");
                beReadyPCB.get(0).runtime++;
                beReadyPCB.get(0).startblock--;
                beReadyPCB.get(0).state = "r";
                System.out.println(beReadyPCB.get(0).messages());
                beReadyPCB.get(0).reducePriority();
                if(beReadyPCB.size()!=1) {
                    System.out.println("进入就绪队列的进程为：");
                    System.out.println("进程ID\t进程名\t\t运行时间\t\t优先权\t\t状态");
                    for (int i = 1; i < beReadyPCB.size(); i++) {
                        beReadyPCB.get(i).state="w";
                        System.out.println(beReadyPCB.get(i).messages());
                        beReadyPCB.get(i).addPriority();
                    }
                }
                if (beReadyPCB.get(0).runtime == beReadyPCB.get(0).fuwutime||beReadyPCB.get(0).startblock==0) {
                    if(beReadyPCB.get(0).runtime == beReadyPCB.get(0).fuwutime) {
                        strings.add(beReadyPCB.get(0).pname);
                        beReadyPCB.remove(0);
                    }
                    if(!beReadyPCB.isEmpty()&&beReadyPCB.get(0).startblock==0&&beReadyPCB.get(0).blocktime!=0){
                        block.add(beReadyPCB.get(0));
                        beReadyPCB.remove(0);
                    }
                }
            }
            if (strings.size()!=0){
                System.out.print("时间末结束的进程有：");
                for(String s:strings){
                    System.out.print(s.toString()+"   ");
                }
                System.out.println();
            }
        }
    }
}