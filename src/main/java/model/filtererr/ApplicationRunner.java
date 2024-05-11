package model.filtererr;

import lombok.extern.slf4j.Slf4j;
import model.filtererr.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;


@Component
@Slf4j
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

    @Value("${listening_path}")
    private String[] listening_path;

    @Resource
    private TestService testService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ArrayList<String> list=null;
        for(String s:listening_path){
            log.info("开始数据修复");
            list=new ArrayList<>();
            String path=s;
            File f=new File(path);


            if(!f.exists()){
                System.out.println(f.getName()+"not exits");
                return;
            }
            String writeFile=s+".txt";
            String line="";
            File w=new File(writeFile);
            if(!w.exists()){
                w.createNewFile();
            }
            try (BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(w),"UTF-8"))){
               while ((line=br.readLine())!=null){
                   list.add(line);
               }
            }
            File[] result = f.listFiles();
            assert result != null;
            for (File fs : result) {
                if (fs.isFile()) {
                    if (!list.contains(fs.getName())) {
                        testService.mainFilter(fs);
                        testService.writeFileName(fs.getName(), writeFile);
                    }
                }
            }
            log.info("修复数据成功");
        }
    }
}
