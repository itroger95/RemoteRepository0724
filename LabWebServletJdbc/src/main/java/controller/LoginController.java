package controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import model.CustomerBean;
import model.CustomerService;

@Controller
@RequestMapping("/secure/login.controller")
@SessionAttributes(names={"user"})
public class LoginController {
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String method(String username, String password, Model model) {
//接收資料
//驗證資料
		Map<String, String> errors = new HashMap<String, String>();
		model.addAttribute("errors", errors);
		
		if(username==null || username.length()==0) {
			errors.put("username", "Please enter ID for login (mvc)");
		}
		if(password==null || password.length()==0) {
			errors.put("password", "Please enter PWD for login (mvc)");
		}

		if(errors!=null && !errors.isEmpty()) {
			return "login.failed";
		}
		
//呼叫model
		CustomerBean bean = customerService.login(username, password);
		
//根據model執行結果，導向view
		if(bean==null) {
			errors.put("password", "Login failed, please try again.");
			return "login.failed";
		} else {
			model.addAttribute("user", bean);
			return "login.success";
		}
	}
}
