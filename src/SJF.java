/**                   _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                    O\ = /O
 *                ____/`---'\____
 *              .   ' \\| |// `.
 *               / \\||| : |||// \
 *             / _||||| -:- |||||- \
 *               | | \\\ - /// | |
 *             | \_| ''\---/'' | |
 *              \ .-\__ `-` ___/-. /
 *           ___`. .' /--.--\ `. . __
 *        ."" '< `.___\_<|>_/___.' >'"".
 *       | | : `- \`.;`\ _ /`;.`/ - ` : | |
 *         \ \ `-. \_ __\ /__ _/ .-` / /
 * ======`-.____`-.___\_____/___.-`____.-'======
 *                    `=---='
 * .............................................
 *          佛祖保佑             永无BUG
 */
import java.util.*;
/***
 * 功能描述：继承父类PCB，实现接口Comparable，时间轮转的PCB
 * @date 2018.11.5
 * @version 2.0
 */
class SJFPCB extends PCB implements Comparable<Object>{
    SJFPCB(int id,String pname, int daodatime, int fuwutime,int startblock,int blocktime) {
        super(id,pname,daodatime,fuwutime,startblock,blocktime);
        this.id=id;
        this.pname = pname;
        this.daodatime = daodatime;
        this.fuwutime = fuwutime;
    }
    /**
     * 功能描述 :  按到进程所需要的剩余时间进行排序
     * @return 排序标志
     */
    @Override
    public int compareTo(Object o) {
        SJFPCB st=(SJFPCB)o;
        return (this.fuwutime-this.runtime)-(st.fuwutime-st.runtime);
        //此排序方法为定义成抢占型SJF每次时间片之前都要按剩余服务时间排序。
    }
}
public class  SJF {
    public static void main(String[] args){
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++短作业优先+++++++++++++++++++++++++++++++++++++++++++");
        Scanner input = new Scanner(System.in);
        LinkedList<SJFPCB> SJFPCBS= new LinkedList<>();
        //用于存放录入的进程
        LinkedList<SJFPCB> block=new LinkedList<>();
        //堵塞队列
        LinkedList<SJFPCB> beReadyPCB = new  LinkedList<>();
        //就绪队列
        LinkedList<String> strings=new  LinkedList<>();
        //存放已经完成的队列
        //测试队列
        while (true) {
            System.out.println("请依次输入进程ID，进程名，到达时间，服务时间，运行n时间后进入堵塞队列的时间，堵塞时间");
            try {
                int id = input.nextInt();
                String pname = input.next();
                int dadatime = input.nextInt();
                int fuwutime = input.nextInt();
                int startblock=input.nextInt();
                int blocktime=input.nextInt();
                SJFPCBS.add(new SJFPCB(id,pname, dadatime, fuwutime,startblock,blocktime));
                System.out.println("是否继续？（1继续，0结束）");
                int i = input.nextInt();
                if (i == 0) {
                    SJFPCBS.sort((o1,o2)->(o1.daodatime-o2.daodatime));
                    break;
                }
            }
            catch (Exception e){
                System.out.println("输入有误！程序非正常运行！请程序运行程序");
                break;
            }
        }
        System.out.println("*******************************************进程运行初信息************************************************");
        System.out.println("*\t\t进程ID\t进程名\t\t到达时间\t\t优先权\t\t服务时间\t\tnS进行后堵塞\t\t堵塞时间\t*");
        for(int i=0;i<SJFPCBS.size();i++){
            System.out.println("*\t\t"+SJFPCBS.get(i).information()+"\t\t\t*");
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
        while (!SJFPCBS.isEmpty()||!beReadyPCB.isEmpty()||!block.isEmpty()){
            //退出循环的条件为：fuwutime进程队列与循环队列都为空
            runslice++;
            System.out.println("*******************时间切片为：" + runslice+"*************************");
            while (!SJFPCBS.isEmpty()&&SJFPCBS.get(0).daodatime<=runslice){
                beReadyPCB.add(SJFPCBS.get(0));
                SJFPCBS.remove(SJFPCBS.get(0));
            }
            /**
             * @date 2018.11.8
             * 堵塞队列不为空进入
             * 首先把堵塞时间结束的进程转为就绪，继续运行
             * 输出 堵塞队列中的进程
             */
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
            /**
             * 若就绪队列为空，则——>没有进程运行
             */
            if(beReadyPCB.isEmpty()){
                System.out.println("当前没有进程运行");
            }
            /**
             * 就绪队列不为空，有程序运行
             * 对首进程将为正在运行的进程
             * @date 2018.11.12
             */
            if(!beReadyPCB.isEmpty()){
                beReadyPCB.sort(SJFPCB::compareTo);
                System.out.println("当前正在进行的进程为：");
                System.out.println("进程ID\t进程名\t\t运行时间\t\t优先权\t\t状态");
                beReadyPCB.get(0).runtime++;
                beReadyPCB.get(0).startblock--;
                beReadyPCB.get(0).state = "r";
                System.out.println(beReadyPCB.get(0).messages());
                /**
                 * 显示非队首的所有进程信息
                 */
                if(beReadyPCB.size()!=1) {
                    System.out.println("进入就绪队列的进程为：");
                    System.out.println("进程ID\t进程名\t\t运行时间\t\t优先权\t\t状态");
                    for (int i = 1; i < beReadyPCB.size(); i++) {
                        beReadyPCB.get(i).state="w";
                        System.out.println(beReadyPCB.get(i).messages());
                    }
                }
                //运行时间等于服务时间，堵塞时间完成
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