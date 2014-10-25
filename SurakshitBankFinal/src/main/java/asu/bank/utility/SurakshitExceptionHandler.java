package asu.bank.utility;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class SurakshitExceptionHandler {
	
	@ExceptionHandler(SurakshitException.class)
	public ModelAndView handleSurakshitException(SurakshitException suExp)
	{
		ModelAndView model =  new ModelAndView("Homepage/exception");
		
		model.addObject("errMsg", suExp.getErrorCode());
		
		return model;
	}
	
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleOtherExceptions(Exception exp)
	{
		ModelAndView model =  new ModelAndView("Homepage/exception");
		
		model.addObject("errMsg", exp.getMessage());
		
		return model;
	}
}
