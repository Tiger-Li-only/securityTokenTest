package com.example.demo.controller;

import com.example.demo.domain.Msg;
import com.example.demo.domain.SysUser;
import com.example.demo.services.SysUserRepository;
import com.example.demo.services.impl.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
public class LoginController {
  @Autowired
  CustomUserService customUserService;
  /**
   * 用户登录
   */
  @RequestMapping("/userLogin")
  @ResponseBody
  public Map login(SysUser sysUser, HttpServletRequest request) {
    Map<String, Object> map = customUserService.findUserByName(sysUser,request);
    return map;
  }

  /**
   * 该链接尝试获取登录用户,返回该认证用户的信息,请求该链接需要在header中放入x-authorization: token
   */
  @RequestMapping("/detail")
  @ResponseBody
  public Msg userDetail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(authentication)) {
      Msg msg = new Msg("访问失败", "权限不足", "这个页面只对管理员显示");
      return msg;
    }
    Msg msg = new Msg("访问成功", "欢迎管理员", "这个页面只对管理员显示");
    return msg;
  }

}