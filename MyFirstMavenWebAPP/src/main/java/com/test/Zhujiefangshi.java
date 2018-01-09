package com.test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class Zhujiefangshi{
	
	@RequestMapping(value="/helloAnnotation")
	public String hello(String name,Model model){
		System.out.println(name);
		model.addAttribute("message", "Hello World! as annotation");
		return "helloWorld";
	}
}
