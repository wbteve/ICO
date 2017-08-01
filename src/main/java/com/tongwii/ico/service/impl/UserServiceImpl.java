package com.tongwii.ico.service.impl;

import com.tongwii.ico.dao.UserMapper;
import com.tongwii.ico.model.User;
import com.tongwii.ico.service.UserService;
import com.tongwii.ico.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by Zeral on 2017-08-01.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

}