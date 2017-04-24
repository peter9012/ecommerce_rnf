package com.rf.core.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class InvocationCountListener implements IAnnotationTransformer {

	private static final Logger logger = LogManager.getLogger(InvocationCountListener.class
			.getName());
	
	static int count = 0;
	

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		if (testMethod.getName().contains("testPlaceAnAdhocOrderFromConsultant") ||
				testMethod.getName().contains("testPlaceAnAdhocOrderFromPC") ||
				testMethod.getName().contains("testPlaceAnAdhocOrderFromRC")){
			logger.info("InvocationCountListener activated for "+testMethod.getName());
			setInvocationCount(annotation,testMethod.getName());
		}
	}
	
	public void setInvocationCount(ITestAnnotation annotation, String methodName){
		if(!StringUtils.isEmpty(System.getProperty("invoCount"))){
			count = Integer.parseInt(System.getProperty("invoCount"));
		}
		else{
			count = annotation.getInvocationCount();
		}
		annotation.setInvocationCount(count);
		logger.info("Invocation Count is set to " + count + " for " + methodName);
	}
	
	
	public static int getInvocationCount(){
         return count;		
	}
	
}