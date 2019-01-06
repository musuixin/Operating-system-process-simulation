/**
 * 功能描述：PCB的父类，包含PCB的主要信息，以及方法。
 * @date 2018.11.5
 * @version 2.0
 */
class PCB {
    int id;
    String pname;
    int daodatime;
    int fuwutime;
    int runtime=0;
    String state="w";
    int priority=0;
    int startblock;
    int blocktime;
    PCB(){
    }
    /**
     * 功能描述：用于非优先权进程调度的PCB构造函数，不包含优先权的构造，默认其为0
     */
    PCB(int id,String pname, int daodatime, int fuwutime,int startblock,int blocktime) {
        this.id=id;
        this.pname = pname;
        this.daodatime = daodatime;
        this.fuwutime = fuwutime;
        this.startblock=startblock;
        this.blocktime=blocktime;
    }
    /**
     * 功能描述：用于优先权调度的构造函数。
     */
    PCB(int id,String pname, int daodatime, int fuwutime,int priority,int startblock,int blocktime) {
        this.id=id;
        this.pname = pname;
        this.daodatime = daodatime;
        this.startblock=startblock;
        this.blocktime=blocktime;
        this.fuwutime = fuwutime;
        this.priority = priority;
    }
    /**
     * @return PCB原始信息
     */
    String information(){
        return id+"\t\t\t"+pname+"\t\t\t"+daodatime+"\t\t\t"+priority+"\t\t\t"+fuwutime+"\t\t\t\t"+startblock+"\t\t\t\t\t"+blocktime;
    }
    /**
     * @return PCB当前信息
     */
    String messages(){
        return id+"\t\t\t"+pname+"\t\t\t"+runtime+"\t\t\t"+priority+"\t\t\t"+state;
    }
}