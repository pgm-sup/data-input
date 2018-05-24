package com.wyc.controller;

import com.wyc.entity.Msg;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author haima
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model){
        Msg msg =  new Msg("浦东数据录入平台","上传数据","创建数据表");
        model.addAttribute("msg", msg);
        return "home";
    }
}
