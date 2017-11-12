package com.example.demo.services.impl;

import com.example.demo.domain.SysUser;
import com.example.demo.services.SysUserRepository;
import com.example.demo.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class CustomUserService{
    @Resource
    SysUserRepository userRepository;
    @Resource
    JwtTokenUtil jwtTokenUtil;
    public Map findUserByName(SysUser sysUser, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        SysUser user = userRepository.findByUsername(sysUser.getUsername());
        if (user == null) {
            map.put("status", 1);
            map.put("msg", "登录失败,用户不存在");
            return map;
        }else if (!StringUtils.equals(user.getPassword(), sysUser.getPassword())) {
            map.put("status", 2);
            map.put("msg", "登录失败,密码错误");
            return map;
        }

        //登录成功，获取token
        map.put("status",0);
        map.put("msg", "Success");
//        map.put("token", jwtTokenUtil.create(sysUser));
        //缓存token
        request.getSession().setAttribute("token",jwtTokenUtil.create(user));

        return map;
    }
}