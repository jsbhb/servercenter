package com.zm.gateway.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zm.gateway.mapper.RoleMapper;
import com.zm.gateway.model.Role;
import com.zm.gateway.service.RoleService;

/**
 * @author
 * @version 1.0 2016/10/11
 * @description
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getUserRole(String userId) {
        List<Role> roles = roleMapper.getUserRole(userId);
        return roles;
    }
}
