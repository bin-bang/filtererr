package model.filtererr.Monitor;

import model.filtererr.Listener.FileListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Value;

public class FileMonitor {
    private FileListener fileListener;  //监听事件
    private String[] paths;    //监听的文件路径
    private FileAlterationMonitor monitor;

    @Value("${defaultInterval}")
    private long defaultInterval=5*60*1000;  //默认监听的时间间隔
    public FileMonitor() {
    }

    public FileMonitor(String[] paths) {
        this.paths = paths;
        this.monitor = new FileAlterationMonitor(defaultInterval);
    }

    public FileMonitor(String[] paths, long interval) {
        this.paths = paths;
        this.monitor = new FileAlterationMonitor(interval);
    }

    public void setFileListener(FileListener fileListener) {
        this.fileListener = fileListener;
    }

    public void start() {
        if (monitor == null) {
            throw new IllegalStateException("Listener must not be null");
        }
        if (paths == null) {
            throw new IllegalStateException("Listen path must not be null");
        }

        for (String path: paths) {
            FileAlterationObserver observer = new FileAlterationObserver(path);
            observer.addListener(fileListener);
            monitor.addObserver(observer);
        }

        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
