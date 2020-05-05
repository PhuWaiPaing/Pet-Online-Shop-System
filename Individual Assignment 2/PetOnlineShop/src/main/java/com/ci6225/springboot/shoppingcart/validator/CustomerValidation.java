package com.ci6225.springboot.shoppingcart.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ci6225.springboot.shoppingcart.form.CustomerForm;

@Component
public class CustomerValidation implements Validator{
	
private EmailValidator emailValidator = EmailValidator.getInstance();
	
	public boolean supports(Class<?> c) {
		return c == CustomerForm.class;
	}
	
	public void validate(Object target, Errors errors) {
        CustomerForm custInfo = (CustomerForm) target;
 
        // Check the fields of CustomerForm class.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.customerForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.customerForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty.customerForm.address");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.customerForm.phone");
        if (!custInfo.getPhone().matches("(^$|[0-9]{8})")) {
        	errors.rejectValue("phone", "Pattern.customerForm.phonenumber");
        }
 
        if (!emailValidator.isValid(custInfo.getEmail())) {
            errors.rejectValue("email", "Pattern.customerForm.email");
        }
    }

}
