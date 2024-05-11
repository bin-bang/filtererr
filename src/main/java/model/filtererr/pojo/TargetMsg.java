package model.filtererr.pojo;


public class TargetMsg {

    private int err_id;
    private String err_title;
    private String err_detail;
    private int err_count;
    private String err_last_file;
    private long err_last_time;
    private String mainplat;
    private String origin_ip;
    private long run_time;


    public int getErr_id() {
        return err_id;
    }

    public void setErr_id(int err_id) {
        this.err_id = err_id;
    }

    public String getErr_title() {
        return err_title;
    }

    public void setErr_title(String err_title) {
        this.err_title = err_title;
    }

    public String getErr_detail() {
        return err_detail;
    }

    public void setErr_detail(String err_detail) {
        this.err_detail = err_detail;
    }

    public int getErr_count() {
        return err_count;
    }

    public void setErr_count(int err_count) {
        this.err_count = err_count;
    }

    public String getErr_last_file() {
        return err_last_file;
    }

    public void setErr_last_file(String err_last_file) {
        this.err_last_file = err_last_file;
    }

    public long getErr_last_time() {
        return err_last_time;
    }

    public void setErr_last_time(long err_last_time) {
        this.err_last_time = err_last_time;
    }

    public String getMainplat() {
        return mainplat;
    }

    public void setMainplat(String mainplat) {
        this.mainplat = mainplat;
    }

    public String getOrigin_ip() {
        return origin_ip;
    }

    public void setOrigin_ip(String origin_ip) {
        this.origin_ip = origin_ip;
    }

    public long getRun_time() {
        return run_time;
    }

    public void setRun_time(long run_time) {
        this.run_time = run_time;
    }

    @Override
    public String toString() {
        return "TargetMsg{" +
                "err_id=" + err_id +
                ", err_title='" + err_title + '\'' +
                ", err_detail='" + err_detail + '\'' +
                ", err_count=" + err_count +
                ", err_last_file='" + err_last_file + '\'' +
                ", err_last_time=" + err_last_time +
                ", mainplat='" + mainplat + '\'' +
                ", origin_ip='" + origin_ip + '\'' +
                ", run_time=" + run_time +
                '}';
    }
}
