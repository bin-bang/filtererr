package model.filtererr.mapper.androidmapper;

import org.apache.ibatis.annotations.Param;

public interface AndroidMapper {
    Integer queryErrId(@Param("err_last_file") String err_last_file);
}
