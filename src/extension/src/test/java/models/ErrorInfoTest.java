package models;

import junit.framework.TestCase;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

/**
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You are free to use, for and modify this project
 */
public class ErrorInfoTest extends TestCase {

    public void testToResponse() throws Exception {
        ErrorInfo errorInfo = new ErrorInfo("Bad request", HttpStatus.BAD_REQUEST);

        String expected = "{\"message\":\"Bad request\",\"status\":\"BAD_REQUEST\"}";
        String actual = errorInfo.toResponse().getEntity().toString();

        Assert.assertEquals(expected, actual);
    }
}
