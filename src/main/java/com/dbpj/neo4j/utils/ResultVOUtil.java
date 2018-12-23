package com.dbpj.neo4j.utils;

import com.dbpj.neo4j.vo.ResultVO;
import com.dbpj.neo4j.enums.ResultEnum;

/**
 * @Author: Jeremy
 * @Date: 2018/11/7 15:22
 */
public class ResultVOUtil {
    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }

    public static ResultVO error(ResultEnum resultEnum){
        return error(resultEnum.getCode(), resultEnum.getMessage());
    }
}
