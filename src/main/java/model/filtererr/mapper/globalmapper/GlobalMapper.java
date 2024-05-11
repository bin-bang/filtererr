package model.filtererr.mapper.globalmapper;

import org.apache.ibatis.annotations.Param;

public interface GlobalMapper {
    Integer queryErrId(@Param("err_last_file") String err_last_file);
}
