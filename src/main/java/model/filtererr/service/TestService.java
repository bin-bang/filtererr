package model.filtererr.service;

import lombok.extern.slf4j.Slf4j;
import model.filtererr.mapper.androidmapper.AndroidMapper;
import model.filtererr.mapper.cnmapper.CnMapper;
import model.filtererr.mapper.globalmapper.GlobalMapper;
import model.filtererr.mapper.globaltsmapper.GlobaltsMapper;
import model.filtererr.mapper.targetmapper.TargetMapper;
import model.filtererr.pojo.TargetMsg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class TestService {

    @Resource
    private CnMapper cnMapper;
    @Resource
    private TargetMapper targetMapper;
    @Resource
    private GlobaltsMapper globaltsMapper;
    @Resource
    private AndroidMapper androidMapper;
    @Resource
    private GlobalMapper globalMapper;


    private String title = "";
    private int index;

    @Value("${regular_expression}")
    private String[] patterns;

    @Value("${err_type}")
    private String[] err_type;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Value("${origin_ip}")
    private String origin_ip;

    @Value("${listening_path}")
    private String path;

    private final static ThreadLocal<ConcurrentHashMap<String, TargetMsg>> threadLocal= new ThreadLocal<>();
    private static ExecutorService service=null;


    /*
     *判断是否存在错误标志
     * */
    private boolean isErr(String line) {
        if (line == null) {
            return false;
        }
        for (String s: err_type) {
            if (line.contains("["+s+"]")){
                return true;
            }
        }
        return false;
    }

    /*
     * 判断是否存在时间标头
     * */
    private boolean isTime(String line) {
        if(line==null) {
            return false;
        }
        if (line.length() < 10) {
            return false;
        }
        String temp = line.substring(0, 10);
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(temp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * 将错误日志的信息写入到文件中去
     * */
    /*public void writeFile(BufferedWriter bw, String line) {
        try {
            bw.write(line);
            bw.write("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    //统计错误信息
    private void statistics(Map<String, TargetMsg> map, String line, String fileName, String path) {
        if (line==null || line.length()<20) {
            return;
        }

        String title = "";
        int index;

        for (String s: err_type) {
            int len=s.length();
            if (line.contains(s)){
                index=line.indexOf(s)+len+2;
                if (line.length()>688){
                    title = line.substring(index,688);
                }else{
                    title=line.substring(index);
                }
            }
        }

        String separator = "[/\\\\]";
        String[] mainplats = path.split(separator);
        String mainplat = mainplats[mainplats.length - 2];
        TargetMsg targetMsg = new TargetMsg();


        if (mainplat.equals("mix_cn") || mainplat.equals("mix_cnts")) {
            Integer temp = cnMapper.queryErrId(fileName);
            if (temp == null) {
                targetMsg.setErr_id(0);
            } else {
                targetMsg.setErr_id(temp);
            }
        }
        if (mainplat.equals("mix_globalts") ) {
            Integer temp = globaltsMapper.queryErrId(fileName);
            if (temp == null) {
                targetMsg.setErr_id(0);
            } else {
                targetMsg.setErr_id(temp);
            }
        }
        if (mainplat.equals("mix_global")) {
            Integer temp = globalMapper.queryErrId(fileName);
            if (temp == null) {
                targetMsg.setErr_id(0);
            } else {
                targetMsg.setErr_id(temp);
            }
        }
        if (mainplat.equals("android_cn")) {
            Integer temp = androidMapper.queryErrId(fileName);
            if (temp == null) {
                targetMsg.setErr_id(0);
            } else {
                targetMsg.setErr_id(temp);
            }
        }


        targetMsg.setErr_title(title);
        targetMsg.setErr_detail("");
        if (map.containsKey(title)) {
            targetMsg.setErr_count(map.get(title).getErr_count() + 1);
        } else {
            targetMsg.setErr_count(1);
        }
        targetMsg.setErr_last_file(fileName);
        try {
            targetMsg.setErr_last_time(formatter.parse(line.substring(0, 19)).getTime() / 1000);
            targetMsg.setMainplat(mainplat);
            targetMsg.setOrigin_ip(origin_ip);
            targetMsg.setRun_time(System.currentTimeMillis() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        map.put(title, targetMsg);
    }

    //将错误信息更新到数据库到数据库
    private void addErrService(Map<String, TargetMsg> map) {
        if (map == null) {
            return;
        }
        for (Map.Entry<String, TargetMsg> entry : map.entrySet()) {
            TargetMsg err = targetMapper.queryTarget(entry.getKey(), entry.getValue().getMainplat(), entry.getValue().getOrigin_ip());
            if (err != null) {
                err.setErr_count(err.getErr_count() + entry.getValue().getErr_count());
                if ("mix_cn".equals(entry.getValue().getMainplat()) || "mix_cnts".equals(entry.getValue().getMainplat())) {
                    Integer temp = cnMapper.queryErrId(entry.getValue().getErr_last_file());
                    if (temp == null) {
                        err.setErr_id(0);
                    } else {
                        err.setErr_id(temp);
                    }
                }
                if ("mix_globalts".equals(entry.getValue().getMainplat()) ) {
                    Integer temp = globaltsMapper.queryErrId(entry.getValue().getErr_last_file());
                    if (temp == null) {
                        err.setErr_id(0);
                    } else {
                        err.setErr_id(temp);
                    }
                }
                if ( "mix_global".equals(entry.getValue().getMainplat())) {
                    Integer temp = globalMapper.queryErrId(entry.getValue().getErr_last_file());
                    if (temp == null) {
                        err.setErr_id(0);
                    } else {
                        err.setErr_id(temp);
                    }
                }
                if ("android_cn".equals(entry.getValue().getMainplat())) {
                    Integer temp = androidMapper.queryErrId(entry.getValue().getErr_last_file());
                    if (temp == null) {
                        err.setErr_id(0);
                    } else {
                        err.setErr_id(temp);
                    }
                }
                err.setErr_detail(entry.getValue().getErr_detail());
                err.setErr_last_file(entry.getValue().getErr_last_file());
                if (err.getErr_last_time() < (entry.getValue().getErr_last_time())) {
                    err.setErr_last_time(entry.getValue().getErr_last_time());
                }
                err.setRun_time(System.currentTimeMillis() / 1000);
                targetMapper.updateTarget(err);
            } else {
                targetMapper.addTarget(entry.getValue());
            }
        }
        log.info("将错误信息更新到数据库");
    }

    public void updateDelay() {
        service=Executors.newFixedThreadPool(50);
        long updateTime=System.currentTimeMillis()/1000-604800;
        ArrayList<TargetMsg> list = targetMapper.queryErrZero(0,updateTime);
        Vector<TargetMsg> vector=new Vector<>(list);
        final CountDownLatch latch=new CountDownLatch(vector.size());
        for (TargetMsg msg : vector) {
            service.execute(() -> {
                if ("mix_cn".equals(msg.getMainplat()) || "mix_cnts".equals(msg.getMainplat())) {
                    Integer id=cnMapper.queryErrId(msg.getErr_last_file());
                    if(id!=null){
                        msg.setErr_id(id);
                    }
                }
                if ("mix_globalts".equals(msg.getMainplat()) ) {
                    Integer id=globaltsMapper.queryErrId(msg.getErr_last_file());
                    if(id!=null){
                        msg.setErr_id(id);
                    }
                }
                if ( "mix_global".equals(msg.getMainplat()) ) {
                    Integer id=globalMapper.queryErrId(msg.getErr_last_file());
                    if(id!=null){
                        msg.setErr_id(id);
                    }
                }
                if ("android_cn".equals(msg.getMainplat())) {
                    Integer id=androidMapper.queryErrId(msg.getErr_last_file());
                    if(id!=null){
                        msg.setErr_id(id);
                    }
                }
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.shutdown();
        for (TargetMsg msg:vector) {
            if (msg.getErr_id()!=0){
                targetMapper.updateTarget(msg);
            }
        }
        log.info("成功更新错误关联id");
    }


    //主服务
    public void mainFilter(File file) {
        boolean isWirte=false;
        threadLocal.set(new ConcurrentHashMap<>());
        Map<String,TargetMsg> targetMsgMap =threadLocal.get();
        log.info("新增文件"+ file.getName());
        if (!(file.getName().endsWith(".zip"))) {
            log.info(file.getName() + "不是压缩文件");
            return;
        }

        for (int i = 0; i < patterns.length; i++) {
            if (Pattern.matches(patterns[i], file.getName())) {
                break;
            } else if (i == patterns.length - 1) {
                log.info(file.getName() + "不是错误文件");
                return;
            }
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)))) {

            while ((zipInputStream.getNextEntry()) != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(zipInputStream));

                String line;
                while ((line = br.readLine()) != null) {
                    if (isTime(line)) {
                        if (isErr(line)) {
                            for (String s: err_type) {
                                if (line.contains(s)){
                                    index=line.indexOf(s)+s.length()+2;
                                    break;
                                }
                            }
                            if(line.length()<=index || index<0){
                                    continue;
                            }
                            title = line.substring(index);
                            isWirte = true;
                            statistics(targetMsgMap, line, file.getName(), file.getPath());
                        } else {
                            isWirte = false;
                        }
                    }

                    if (isWirte) {
                        if (targetMsgMap.containsKey(title)) {
                            TargetMsg temp = targetMsgMap.get(title);
                            temp.setErr_detail(temp.getErr_detail() + line + "\n");
                            targetMsgMap.put(title, temp);
                        }
                    }
                }
            }
            addErrService(targetMsgMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("处理完成");
        threadLocal.remove();
    }

    public void writeFileName(String fileName,String writeFile) {
        File file=new File(writeFile);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            bw.write(fileName + "\n");
            log.info("将已处理文件的文件名写入文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





