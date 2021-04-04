package com.example.common.vo;

import com.example.common.constant.CommonConstant;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: Killah
 * @Version: 1.0
 */
@Data
@Accessors(chain = true)
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 结果对象
     */
    private T result;

    /**
     * 消息描述
     */
    private String message;

    /**
     * 请求成功标志，与业务无关
     */
    private Boolean success = true;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 构造函数
     * @param code
     * @param result
     * @param message
     */
    private ResultVO(Integer code, T result, String message) {
        this.code = code;
        this.result = result;
        this.message = message;
    }

    /**
     * 成功返回
     * @return
     */
    public static ResultVO success() {
        return new ResultVO(CommonConstant.SUCCESS_CODE, null, null);
    }

    /**
     * 成功返回
     * @param result
     * @return
     */
    public static ResultVO success(Object result) {
        return new ResultVO(CommonConstant.SUCCESS_CODE, result, null);
    }

    /**
     * 失败返回
     * @param message
     * @return
     */
    public static ResultVO fail(String message) {
        return new ResultVO(CommonConstant.ERROR_CODE, null, message);
    }
}
