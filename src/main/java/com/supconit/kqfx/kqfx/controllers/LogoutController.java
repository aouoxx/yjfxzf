package com.supconit.kqfx.kqfx.controllers;

import hc.safety.manager.SafetyManager;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("logout_controller")
@RequestMapping({"/logout"})
public class LogoutController {

  @Autowired
  private SafetyManager safetyManager;
  
  @Resource
  private HttpServletRequest request;

  @RequestMapping(method={RequestMethod.GET})
  public String logout() {
    this.safetyManager.logout();
    return "redirect:/platform/login";
  }
}