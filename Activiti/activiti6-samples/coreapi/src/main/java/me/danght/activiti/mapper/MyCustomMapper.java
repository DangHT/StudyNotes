package me.danght.activiti.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author DangHT
 * @date 2020/07/28
 */
public interface MyCustomMapper {

    @Select("SELECT * FROM ACT_RU_TASK")
    List<Map<String, Object>> findAll();

}
