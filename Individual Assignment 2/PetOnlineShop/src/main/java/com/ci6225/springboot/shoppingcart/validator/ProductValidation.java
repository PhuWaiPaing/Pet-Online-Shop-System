package com.ci6225.springboot.shoppingcart.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ci6225.springboot.shoppingcart.dao.ProductDAO;
import com.ci6225.springboot.shoppingcart.entity.Puppy;
import com.ci6225.springboot.shoppingcart.form.ProductForm;

@Component
public class ProductValidation implements Validator{
	
	@Autowired
	private ProductDAO productDAO;
	
	public boolean supports(Class<?>c) {
		return c == ProductForm.class;
	}
	
	public void validate(Object target, Errors errors) {
		ProductForm form = (ProductForm) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "puppyCode", "NotEmpty.productForm.puppyCode");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "puppyName", "NotEmpty.productForm.puppyName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "puppyGender", "NotEmpty.productForm.puppyGender");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "puppyPrice", "NotEmpty.productForm.puppyPrice");
		
		String puppyCode = form.getPuppyCode();
		if (puppyCode != null && puppyCode.length() > 0) {
			if (puppyCode.matches("\\s+")) {
				errors.rejectValue("puppyCode", "Pattern.productForm.puppyCode");
			}else if (form.isNewPuppy()) {
				Puppy puppy = productDAO.findProduct(puppyCode);
				if (puppy != null) {
					errors.rejectValue("puppyCode", "Duplicate.productForm.puppyCode");
				}
			}
		}
	}

}
