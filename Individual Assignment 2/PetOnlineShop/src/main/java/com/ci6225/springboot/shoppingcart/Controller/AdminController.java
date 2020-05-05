package com.ci6225.springboot.shoppingcart.Controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ci6225.springboot.shoppingcart.dao.OrderDAO;
import com.ci6225.springboot.shoppingcart.dao.ProductDAO;
import com.ci6225.springboot.shoppingcart.entity.Puppy;
import com.ci6225.springboot.shoppingcart.form.ProductForm;
import com.ci6225.springboot.shoppingcart.model.OrderDetailsInfo;
import com.ci6225.springboot.shoppingcart.model.OrderInfo;
import com.ci6225.springboot.shoppingcart.pagination.Pagination;
import com.ci6225.springboot.shoppingcart.validator.ProductValidation;

@Controller
@Transactional
public class AdminController {
	
	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private ProductValidation productFormValidator;
	
	@InitBinder
	public void myInitBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target="+target);
		
		if (target.getClass() == ProductForm.class) {
			dataBinder.setValidator(productFormValidator);
		}
	}
	
	@RequestMapping(value = { "/admin/login" }, method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}
	
	@RequestMapping(value= {"/admin/account"},method = RequestMethod.GET)
	public String account(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println(userDetails.getPassword());
		System.out.println(userDetails.getUsername());
		System.out.println(userDetails.isEnabled());
		
		model.addAttribute("userDetails",userDetails);
		return "account";
	}
	
	@RequestMapping(value= {"/admin/orderList"}, method = RequestMethod.GET)
	public String orderList(Model model, @RequestParam(value="page",defaultValue="1")String pageStr) {
		int page = 1;
		try {
			page = Integer.parseInt(pageStr);
		}catch(Exception e) {
			
		}
		final int MAX_RESULT = 10;
		final int MAX_NAVIGATION_PAGE = 10;
		Pagination <OrderInfo> paginationResult = orderDAO.listOrderInfo(page,MAX_RESULT,MAX_NAVIGATION_PAGE);
		model.addAttribute("paginationResult", paginationResult);
		return "orderList";
	}
	
	@RequestMapping(value = {"/admin/product"}, method = RequestMethod.GET)
	public String product(Model model, @RequestParam(value="code", defaultValue = "")String code) {
		ProductForm productForm = null;
		
		if (code!= null && code.length() > 0) {
			Puppy puppy = productDAO.findProduct(code);
			if (puppy != null) {
				productForm = new ProductForm(puppy);
			}
		}
		if(productForm == null) {
			productForm = new ProductForm();
			productForm.setNewPuppy(true);
		}
		model.addAttribute("productForm", productForm);
		return "product";
	}
	
	@RequestMapping(value = { "/admin/product" }, method = RequestMethod.POST)
	   public String productSave(Model model, @ModelAttribute("productForm") @Validated ProductForm productForm,BindingResult result, final RedirectAttributes redirectAttributes) {
	 
	      if (result.hasErrors()) {
	         return "product";
	      }
	      try {
	         productDAO.save(productForm);
	      } catch (Exception e) {
	         Throwable rootCause = ExceptionUtils.getRootCause(e);
	         String message = rootCause.getMessage();
	         model.addAttribute("errorMessage", message);
	         // Show product form.
	         return "product";
	      }
	      return "redirect:/productList";
	}
	@RequestMapping(value = { "/admin/order" }, method = RequestMethod.GET)
	   public String orderView(Model model, @RequestParam("orderId") String orderId) {
	      OrderInfo orderInfo = null;
	      if (orderId != null) {
	         orderInfo = this.orderDAO.getOrderInfo(orderId);
	      }
	      if (orderInfo == null) {
	         return "redirect:/admin/orderList";
	      }
	      List<OrderDetailsInfo> details = this.orderDAO.listOrderDetails(orderId);
	      orderInfo.setDetails(details);
	 
	      model.addAttribute("orderInfo", orderInfo);
	 
	      return "order";
	   }
}
