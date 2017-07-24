package controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import model.ProductBean;
import model.ProductService;
import model.misc.PrimitiveNumberEditor;

@Controller
@RequestMapping("/pages/product.controller")
public class ProductController {
	@InitBinder
	public void initializePropertyEditor(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(java.util.Date.class,
				"make", new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
		webDataBinder.registerCustomEditor(double.class,
				"price", new PrimitiveNumberEditor(Double.class, true));
		webDataBinder.registerCustomEditor(
				int.class, new PrimitiveNumberEditor(Integer.class, true));
	}
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String method(ProductBean bean, BindingResult bindingResult, Model model,
			@RequestParam(name="prodaction") String prodaction) {	
//接收資料
//轉換資料
		Map<String, String> errors = new HashMap<String, String>(); 
		model.addAttribute("errors", errors);
		
		if(bindingResult != null && bindingResult.hasErrors()) {
			if (bindingResult.getFieldError("id") != null) {
				errors.put("id", "Id必須是整數 (mvc)");
			}
			if (bindingResult.getFieldError("price") != null) {
				errors.put("price", "Price必須是整數 (mvc)");

			}
			if (bindingResult.getFieldError("make") != null) {
				errors.put("make", "Make必須是符合YYYY-MM-DD格式的日期 (mvc)");

			}
			if (bindingResult.getFieldError("expire") != null) {
				errors.put("expire", "Expire必須是整數 (mvc)");
			}
		}

//驗證資料
		if(prodaction!=null) {
			if(prodaction.equals("Insert") || prodaction.equals("Update") || prodaction.equals("Delete")) {
				if(bean.getId()==0 && !errors.containsKey("id")) {
					errors.put("id", "請輸入Id以便於執行"+prodaction+" (MVC)");
				}
			}
		}
		
		if(errors!=null && !errors.isEmpty()) {
			return "product.input";
		}
		
//呼叫Model
//根據Model執行結果，呼叫View
		if("Select".equals(prodaction)) {
			List<ProductBean> result = productService.select(bean);
			model.addAttribute("select", result);
			return "product.select";
		} else if(prodaction!=null && prodaction.equals("Insert")) {
			ProductBean result = productService.insert(bean);
			if(result==null) {
				errors.put("action", "Insert fail");
			} else {
				model.addAttribute("insert", result);
			}
			return "product.input";
		} else if(prodaction!=null && prodaction.equals("Update")) {
			ProductBean result = productService.update(bean);
			if(result==null) {
				errors.put("action", "Update fail");
			} else {
				model.addAttribute("update", result);
			}
			return "product.input";
		} else if(prodaction!=null && prodaction.equals("Delete")) {
			boolean result = productService.delete(bean);
			if(!result) {
				model.addAttribute("delete", 0);
			} else {
				model.addAttribute("delete", 1);
			}
			return "product.input";
		} else  {
			errors.put("action", "Unknown Action:"+prodaction);
			return "product.input";
		}
	}
}
