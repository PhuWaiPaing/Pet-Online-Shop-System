package com.ci6225.springboot.shoppingcart.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
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
import com.ci6225.springboot.shoppingcart.form.CustomerForm;
import com.ci6225.springboot.shoppingcart.model.CartInfo;
import com.ci6225.springboot.shoppingcart.model.CustomerInfo;
import com.ci6225.springboot.shoppingcart.pagination.Pagination;
import com.ci6225.springboot.shoppingcart.model.ProductInfo;
import com.ci6225.springboot.shoppingcart.Utils.Utils;
import com.ci6225.springboot.shoppingcart.validator.CustomerValidation;

@Controller
@Transactional
public class MainController {
	
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private CustomerValidation customerValidation;
	
	@InitBinder
	public void myInitBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target="+target);
		
		if(target.getClass()==CartInfo.class) {
			
		}
		else if (target.getClass() == CustomerForm.class) {
			dataBinder.setValidator(customerValidation);
		}
	}
	@RequestMapping("/")
	public String home() {
		return "index";
	}
	
    @RequestMapping({ "/productList" })
    public String listProductHandler(Model model,
            @RequestParam(value = "name", defaultValue = "") String likeName,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 6;
        final int maxNavigationPage = 10;
 
        Pagination<ProductInfo> result = productDAO.queryProducts(page, maxResult, maxNavigationPage, likeName);
 
        model.addAttribute("paginationProducts", result);
        return "productList";
    }
    
	@RequestMapping({"/buyProduct"})
	public String listProductHandler(HttpServletRequest request, Model model, @RequestParam(value="code", defaultValue = " ")String code) {
		Puppy puppy = null;
		if (code != null && code.length() > 0) {
			puppy = productDAO.findProduct(code);
		}
		if (puppy != null) {
			
			CartInfo cartInfo = Utils.getCartInSession(request);
			ProductInfo productInfo = new ProductInfo(puppy);
			cartInfo.addProduct(productInfo, 1);
		}
		return "redirect:/shoppingCart";
	}
	
	@RequestMapping({"/shoppingCartRemoveProduct"})
	public String removeProductHandler(HttpServletRequest request, Model model, @RequestParam(value="code", defaultValue = "")String code) {
		Puppy puppy = null;
		if (code != null && code.length() > 0) {
			puppy = productDAO.findProduct(code);
		}
		if (puppy != null) {
			
			CartInfo cartInfo = Utils.getCartInSession(request);
			ProductInfo productInfo = new ProductInfo(puppy);
			cartInfo.removeProduct(productInfo);
		}
		return "redirect:/shoppingCart";
	}
	
	 @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.POST)
	 public String shoppingCartUpdateQty(HttpServletRequest request,Model model,@ModelAttribute("cartForm") CartInfo cartForm) {
	 
	      CartInfo cartInfo = Utils.getCartInSession(request);
	      cartInfo.updateQuantity(cartForm);
	 
	      return "redirect:/shoppingCart";
	   }
	
	@RequestMapping(value= {"/shoppingCart"}, method = RequestMethod.GET)
		public String shoppingCartHandler(HttpServletRequest request, Model model) {
	        CartInfo myCart = Utils.getCartInSession(request);
	 
	        model.addAttribute("cartForm", myCart);
	        return "shoppingCart";
	    }
	
	@RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.GET)
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {
 
        CartInfo cartInfo = Utils.getCartInSession(request);
      
        // Cart is empty.
        if (cartInfo.isEmpty()) {
             
            // Redirect to shoppingCart page.
            return "redirect:/shoppingCart";
        }
 
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        CustomerForm customerForm = new CustomerForm(customerInfo);
        
        model.addAttribute("customerForm", customerForm);
 
        return "shoppingCartCustomer";
    }
	
	@RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.POST)
    public String shoppingCartCustomerSave(HttpServletRequest request, Model model, @ModelAttribute("customerForm") @Validated CustomerForm customerForm,
            BindingResult result, final RedirectAttributes redirectAttributes) {
  
        // If has Errors.
		
		 if (result.hasErrors()) { 
			 customerForm.setValid(false); // Forward to reenter
			 return "shoppingCartCustomer"; 
			 }
		 
        customerForm.setValid(true);
        CartInfo cartInfo = Utils.getCartInSession(request);
        CustomerInfo customerInfo = new CustomerInfo(customerForm);
        cartInfo.setCustomerInfo(customerInfo);
 
        // Redirect to Confirmation page.
        return "redirect:/shoppingCartConfirmation";
    }
	
	@RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.GET)
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
 
        // Cart have no products.
        if (cartInfo == null ||cartInfo.isEmpty()) {
            // Redirect to shoppingCart page.
            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            // Enter customer info.
            return "redirect:/shoppingCartCustomer";
        }
        model.addAttribute("myCart", cartInfo);
        return "shoppingCartConfirmation";
    }
	
	@RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)
    @Transactional(propagation = Propagation.NEVER)
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
 
        if (cartInfo == null ||cartInfo.isEmpty()) {
            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            return "redirect:/shoppingCartCustomer";
        }
        try {
            orderDAO.saveOrder(cartInfo);
        } catch (Exception e) {
            return "shoppingCartConfirmation";
        }
        // Remove Cart In Session.
        Utils.removeCartInSession(request);
         
        // Store Last ordered cart to Session.
        Utils.storeLastOrderedCartInSession(request, cartInfo);
 
        return "redirect:/shoppingCartFinalize";
    }
 
    @RequestMapping(value = { "/shoppingCartFinalize" }, method = RequestMethod.GET)
    public String shoppingCartFinalize(HttpServletRequest request, Model model) {
 
        CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);
 
        if (lastOrderedCart == null || lastOrderedCart.isEmpty()) {
            return "redirect:/shoppingCart";
        }
        model.addAttribute("lastOrderedCart", lastOrderedCart);
        return "shoppingCartFinalize";
    }
    @RequestMapping(value = { "/productImage" }, method = RequestMethod.GET)
    public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
            @RequestParam("code") String code) throws IOException {
        Puppy puppy = null;
        if (code != null) {
            puppy = this.productDAO.findProduct(code);
        }
        if (puppy != null && puppy.getImage() != null) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(puppy.getImage());
        }
        response.getOutputStream().close();
    }
     


}
