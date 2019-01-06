import java.util.LinkedList;
import java.util.Scanner;

/***
 * 功能描述：继承父类PCB，实现接口Comparable，时间轮转的PCB
 * @date 2018.10.30
 * @version 0.1
 */
class RRPCB extends PCB implements Comparable<Object>{
    RRPCB(int id,String pname,int daodatime,int fuwutime,int startblock,int blocktime){
        super(id,pname,daodatime,fuwutime,startblock,blocktime);
    }
    @Override
    public int compareTo(Object T){
        RRPCB st=(RRPCB)T;
        return this.daodatime-st.daodatime;
    }
}

/**
 * 功能描述：实现时间片轮转的核心代码，每一个时间片前，进程队列位移一次！
 * @date 2018.10.30
 * @version 最终代码
 */
class WeiYi{
     static void weiyi(LinkedList<RRPCB> t){
        RRPCB temp;
        temp=t.get(0);
        for (int i=0;i<t.size()-1;i++){
            t.set(i,t.get(i+1));
        }
        t.set(t.size()-1,temp);
    }
}
public class RR{
    public static void main(String[] args){
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++时间片轮转+++++++++++++++++++++++++++++++++++++++++++");
        Scanner input = new Scanner(System.in);
        LinkedList<RRPCB> RRPCBs = new LinkedList<>();
        LinkedList<RRPCB> beReadyPCB = new LinkedList<>();
        LinkedList<RRPCB> block=new LinkedList<>();
        LinkedList<String> strings=new LinkedList<>();
        RRPCBs.add(new RRPCB(1,"a",0,3,0,0));
        RRPCBs.add(new RRPCB(2,"b",2,6,0, 0));
        RRPCBs.add(new RRPCB(3,"c",4,4,0,0));
        RRPCBs.add(new RRPCB(4,"d",6,5,0,0));
        RRPCBs.add(new RRPCB(5,"e",8,2,0,0));
//        while (true) {
//            System.out.println("请依次输入进程ID，进程名，到达时间，服务时间,运行n时间后进入堵塞队列的时间，堵塞时间：");
//            try {
//                int id = input.nextInt();
//                String pname = input.next();
//                int dadatime = input.nextInt();
//                int fuwutime = input.nextInt();
//                int startblock=input.nextInt();
//                int blocktime=input.nextInt();
//                RRPCBs.add(new RRPCB(id,pname, dadatime, fuwutime,startblock,blocktime));
//                System.out.println("是否继续？（1继续，0结束）");
//                int i = input.nextInt();
//                if (i == 0) {
//                    RRPCB.sort(RRPCB::compareTo);
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
        for(int i=0;i<RRPCBs.size();i++){
            System.out.println("*\t\t"+RRPCBs.get(i).information()+"\t\t\t*");
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
        while (!RRPCBs.isEmpty()||!beReadyPCB.isEmpty()||!block.isEmpty()){
            //退出循环的条件为：进程队列与循环队列都为空
            runslice++;
            System.out.println("*******************时间切片为：" + runslice+"*************************");
            while (!RRPCBs.isEmpty()&&RRPCBs.get(0).daodatime<=runslice){
                  beReadyPCB.addFirst(RRPCBs.get(0));
                  RRPCBs.remove(0);
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
                System.out.println("当前正在进行的进程为：");
                System.out.println("进程ID\t进程名\t\t运行时间\t\t优先权\t\t状态");
                beReadyPCB.get(0).runtime++;
                beReadyPCB.get(0).state = "r";
                System.out.println(beReadyPCB.get(0).messages());
                if(beReadyPCB.size()!=1) {
                    System.out.println("进入等待队列的进程为：");
                    System.out.println("进程ID\t进程名\t\t运行时间\t\t优先权\t\t状态");
                    for (int i = 1; i < beReadyPCB.size(); i++) {
                        beReadyPCB.get(i).state="w";
                        System.out.println(beReadyPCB.get(i).messages());
                    }
                }
                if(!beReadyPCB.isEmpty()&&beReadyPCB.get(0).runtime!=beReadyPCB.get(0).fuwutime){
                    //如果队首进程没有结束或者堵塞，则对就绪队列进行位移操作
                    WeiYi.weiyi(beReadyPCB);
                }
                if(beReadyPCB.get(0).runtime == beReadyPCB.get(0).fuwutime||beReadyPCB.get(0).startblock==0) {
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