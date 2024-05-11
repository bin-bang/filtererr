package model.filtererr.Listener;

import lombok.extern.slf4j.Slf4j;
import model.filtererr.service.TestService;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;


@Component
@Slf4j
public class FileListener implements FileAlterationListener {

    private String separator = "[/\\\\]";

    @Resource
    private TestService testService;


    @Value("${regular_expression}")
    private String[] patterns;

    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {
        File directory = fileAlterationObserver.getDirectory();
        log.info("开始监听" + directory);
        //testService.updateDelay();
    }

    @Override
    public void onDirectoryCreate(File file) {
        log.info(file.getName()+"目录被创建了");
    }

    @Override
    public void onDirectoryChange(File file) {
        log.info(file.getName()+"目录被修改了...");
    }

    @Override
    public void onDirectoryDelete(File file) {
        log.info(file.getName()+"目录被删除了...");
    }

    @Override
    public void onFileCreate(File file) {
        String[] ss = file.getAbsolutePath().split(separator);
        String writeFile=file.getAbsolutePath().substring(0,file.getAbsolutePath().length()-ss[ss.length-1].length()-1)+".txt";
        testService.mainFilter(file);
        testService.writeFileName(file.getName(),writeFile);
    }

    @Override
    public void onFileChange(File file) {
        log.info(file.getName()+"文件被修改了");
    }

    @Override
    public void onFileDelete(File file) {
        log.info(file.getName()+"文件被删除了...");
    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {
        File directory = fileAlterationObserver.getDirectory();
        log.info("结束监听" + directory);
    }

}
