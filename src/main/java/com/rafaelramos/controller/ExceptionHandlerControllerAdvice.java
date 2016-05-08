package com.rafaelramos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.rafaelramos.controller.resource.StatusResponse;
import com.rafaelramos.service.exception.EntityNotFoundException;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
	
	private static Logger logger = LoggerFactory.getLogger(ExceptionHandlerControllerAdvice.class);
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public @ResponseBody StatusResponse handleEntityNotFound(EntityNotFoundException e) {
		logger.info(e.getMessage(), e);
		return new StatusResponse("error");
	}

}
