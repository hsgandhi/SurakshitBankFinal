package asu.bank.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class ValidationUtillity {
	
	public ValidationBean checkDateFormat(String dateString)
	{
		ValidationBean validationBean = new ValidationBean();
			Date date = null;
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		        dateFormat.setLenient(false);
			    date = dateFormat.parse(dateString);
			} catch (Exception ex) {
			}
			if (date == null) {
				validationBean.setValid(false);
				validationBean.setMessage("DateTimeFormat.dob");
			}
		return validationBean;
	}

}
