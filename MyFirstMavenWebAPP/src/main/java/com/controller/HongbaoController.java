package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.VO.UserInfoVO;

@Controller
@RequestMapping("/hongbao" )
public class HongbaoController {
	
	@RequestMapping(value="/ajaxPut")
	public @ResponseBody String check(HttpServletRequest request, String totalMoney ,String totalCount , Model model){
		
		UserInfoVO userInfo = new UserInfoVO();
		System.out.println(userInfo.getClass().isInstance(userInfo));
	
		model.addAttribute("userInfo", userInfo);
		
		return "success";
	}
}
