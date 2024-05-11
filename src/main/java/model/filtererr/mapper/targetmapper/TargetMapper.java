package model.filtererr.mapper.targetmapper;

import model.filtererr.pojo.TargetMsg;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;


public interface TargetMapper {

    boolean addTarget(TargetMsg targetMsg);

    TargetMsg queryTarget(@Param("err_title") String err_title,
                    @Param("mainplat") String mainplat,
                    @Param("origin_ip") String origin_ip);

    int updateTarget(TargetMsg targetMsg);

    ArrayList<TargetMsg> queryErrZero(@Param("err_id") int err_id,@Param("run_time") long run_time);

    TargetMsg queryFile(@Param("file_name") String file_name);
}
