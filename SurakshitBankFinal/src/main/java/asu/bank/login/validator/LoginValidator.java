package asu.bank.login.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import asu.bank.login.viewBeans.TrialBean;
import asu.bank.utility.ValidationBean;
import asu.bank.utility.ValidationUtillity;

@Component
public class LoginValidator implements Validator {
	
	@Autowired
	ValidationUtillity validationUtillity;

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		TrialBean trialbean = (TrialBean)arg0;
		ValidationBean validationBean = validationUtillity.checkDateFormat(trialbean.getDob());
		if(!validationBean.isValid())
			arg1.rejectValue("dob", validationBean.getMessage());
		}
}
