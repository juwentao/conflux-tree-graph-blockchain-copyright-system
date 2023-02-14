package com.david.copyright.service;

import com.david.copyright.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.david.copyright.response.ResultBody;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DavidQ
 * @since 2022-03-09
 */
public interface IUserService extends IService<User> {
    public ResultBody certify(String id,String name, String idnumber);
}
