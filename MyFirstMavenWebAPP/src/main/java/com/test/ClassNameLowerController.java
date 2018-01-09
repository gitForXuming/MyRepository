package com.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ClassNameLowerController implements Controller{

	@Override
	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		System.out.println("通过类名小写方式访问到了");
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("classNameLowerController");

		return mav;
	}

}
