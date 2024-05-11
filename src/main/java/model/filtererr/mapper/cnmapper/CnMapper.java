package model.filtererr.mapper.cnmapper;

import org.apache.ibatis.annotations.Param;

public interface CnMapper {
    Integer queryErrId(@Param("err_last_file") String err_last_file);
}
