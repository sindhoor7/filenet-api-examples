package search_functions;

import java.util.Calendar;
import java.util.Date;

import com.filenet.api.admin.CmSearchFunctionDefinition;
import com.filenet.api.engine.SearchFunctionHandler;

public class CustomSearchFunctionHandler implements SearchFunctionHandler{

	@Override
	public Object evaluate(CmSearchFunctionDefinition definition, Object[] parameters) {
		
		Date date = (Date) parameters[0];
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int days = calendar.get(Calendar.DATE);
		calendar.add(Calendar.MONTH, -1);
		int numberOfDays = days + calendar.getActualMaximum(Calendar.DATE);
		
		return numberOfDays;
	}

	@Override
	public String getFunctionName() {
		
		return "CmRpt::GetNumberOfDays";
	}

	@Override
	public boolean requiresTransaction() {
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<Integer> validate(CmSearchFunctionDefinition definition,
			Class[] parameterTypes) {
		
		if (parameterTypes == null || parameterTypes.length != 1) {
			throw new RuntimeException(
					"ParameterTypes must consist of one entry, a Date.");
		}
		
		if (parameterTypes[0] != Date.class) {
			throw new RuntimeException("Parameter must be of date class.");
		}
		
		return Integer.class;
	}

	
}
