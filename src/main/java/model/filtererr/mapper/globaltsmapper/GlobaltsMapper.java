package model.filtererr.mapper.globaltsmapper;

import org.apache.ibatis.annotations.Param;

public interface GlobaltsMapper {
    Integer queryErrId(@Param("err_last_file") String err_last_file);
}
