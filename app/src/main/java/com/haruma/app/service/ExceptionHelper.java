package com.haruma.app.service;

public class ExceptionHelper {

    public static void assertTest(boolean conditional, String message) throws RuntimeException {
        if (!conditional) {
            throw new RuntimeException(message);
        }
    }

}
