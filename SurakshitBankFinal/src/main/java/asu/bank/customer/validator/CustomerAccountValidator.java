package asu.bank.customer.validator;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import asu.bank.customer.viewBeans.AccountDebitViewBean;
import asu.bank.customer.viewBeans.AccountGetTransactionDetailsBean;
import asu.bank.customer.viewBeans.AccountViewBean;

@Component
public class CustomerAccountValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object object, Errors errors) {
		
		if(object instanceof AccountViewBean)
		{
			AccountViewBean accountViewBean = (AccountViewBean)object;
			
			//if(accountViewBean.getCurrency() != new BigDecimal(1.0))
				//errors.rejectValue("currency", "Currency.notNull");
		}
		
		if(object instanceof AccountDebitViewBean)
		{
			AccountDebitViewBean accountDebitViewBean = (AccountDebitViewBean)object;
			
			//if(accountViewBean.getCurrency() != new BigDecimal(1.0))
				//errors.rejectValue("currency", "Currency.notNull");
		}
		
		if(object instanceof AccountGetTransactionDetailsBean)
		{
			AccountGetTransactionDetailsBean accountGetTransactionDetailsBean = (AccountGetTransactionDetailsBean)object;
			
			//if(accountViewBean.getCurrency() != new BigDecimal(1.0))
				//errors.rejectValue("currency", "Currency.notNull");
		}
		
	}


}
