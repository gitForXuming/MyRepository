package com.test;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.VO.UserInfoVO;

@Controller
@RequestMapping("/user" )
public class UserController{

	@RequestMapping(value="/list")
	public String list(){
		System.out.println("查询用户信息");
		return "user_list";
	}

	@RequestMapping(value="/add")
	public String add(HttpServletRequest request,String username,String password,Model model){
		String contentPath = request.getContextPath();
		request.getSession().setAttribute("logon", "1");
		model.addAttribute("contentPath", contentPath);
		return "user_add";
	}

	@RequestMapping(value="/addsubmit")
	public String add2(UserInfoVO userInfo){

		System.out.println("编号："+userInfo.getNumber());
		System.out.println("姓名："+userInfo.getUsername());
		System.out.println("密码："+userInfo.getPassword());
		return "user_add";
	}

	@RequestMapping(value="/ajaxQuery")
	public @ResponseBody UserInfoVO check(HttpServletRequest request, String number , Model model){
		System.out.println(request.getAttribute("errorMessage"));
		System.out.println(request.getSession().getAttribute("errorMessage"));

		model.addAttribute("errorMessage","超时");
		model.addAttribute("errorCode","errorCode1");
		UserInfoVO userInfo = new UserInfoVO();
		System.out.println(userInfo.getClass().isInstance(userInfo));
		if("001".equals(number)){

			userInfo.setNumber("001");
			userInfo.setIndex(001);
			userInfo.setUsername("陈燕玲");
			userInfo.setPassword("123456");

			model.addAttribute("userInfo", userInfo);
			Gson gson = new Gson();
			String json = gson.toJson(userInfo);
			System.out.println(json);
			userInfo = gson.fromJson(json ,userInfo.getClass());
			System.out.println(userInfo);
		}else{

		}
		userInfo.setErrorMessage(request.getAttribute("errorMessage")==null?null:(String)request.getAttribute("errorMessage"));
		return userInfo;
	}
}
