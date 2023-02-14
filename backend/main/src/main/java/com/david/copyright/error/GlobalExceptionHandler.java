package com.david.copyright.error;


import com.david.copyright.response.ResultBody;
import org.apache.shiro.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author DavidQ
 * @version 1.0.0
 * @ClassName GlobalExceptionHandler.java
 * @Description 自定义全局异常处理的类
 * @createTime 2021年05月08日 23:01:00
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResultBody bizExceptionHandler(HttpServletRequest req, BizException e){
        logger.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return ResultBody.error(e.getErrorCode(),e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpServletRequest req, NullPointerException e){
        logger.error("发生空指针异常！原因是:",e);
        return ResultBody.error(CommonEnum.BODY_NOT_MATCH);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpServletRequest req, IllegalArgumentException e){
        logger.error("发生参数异常！原因是:",e);
        return ResultBody.error(CommonEnum.BODY_NOT_MATCH);
    }


    /**
     * 处理shiro异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = ShiroException.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpServletRequest req, ShiroException  e){
        logger.error("发生shiro异常!原因是:",e.getMessage());
        return ResultBody.error(CommonEnum.SHIRO_ERROR);
    }

    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public ResultBody exceptionHandler(HttpServletRequest req, Exception e){
//        logger.error("未知异常！原因是:",e.getMessage());
//        return ResultBody.error(CommonEnum.INTERNAL_SERVER_ERROR);
//    }
}
