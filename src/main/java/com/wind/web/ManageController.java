package com.wind.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class ManageController extends MultiActionController{
	public ModelAndView init(HttpServletRequest req, 
            HttpServletResponse res) {
		return null;
	}
}
