package model.filtererr;

import model.filtererr.Listener.FileListener;
import model.filtererr.Monitor.FileMonitor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ContextFileListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private FileMonitor fileMonitor;
    @Resource
    private FileListener fileListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
            if (event.getApplicationContext().getParent()==null){
                fileMonitor.setFileListener(fileListener);
                fileMonitor.start();
            }
    }
}
