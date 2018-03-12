package com.supconit.kqfx.web.fxzf.search.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/confirm")
@Controller("taizhou_offsite_enforcement_confirm_controller")
public class ConfirmController {
	
	@RequestMapping(value = "confirm", method = RequestMethod.GET)
	public String display(ModelMap map) {
		
		return "/confirm/confirm";
	}

}
