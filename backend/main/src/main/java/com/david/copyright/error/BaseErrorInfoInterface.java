package com.david.copyright.error;

/**
 * @author DavidQ
 * @version 1.0.0
 * @ClassName BaseErrorInfoInterface.java
 * @Description 定义一个基础的接口类，自定义的错误描述枚举类需实现该接口
 * @createTime 2021年05月08日 22:06:00
 */
public interface BaseErrorInfoInterface {
    /** 错误码*/
    String getResultCode();

    /** 错误描述*/
    String getResultMsg();
}
