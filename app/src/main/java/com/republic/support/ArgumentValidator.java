package com.republic.support;

import com.republic.support.exceptions.ArgumentException;

public class ArgumentValidator{

    private static final String NON_NULL_ARG_MSG = "Argument cannot be null";
    private static final String EMPTY_STRING_ARG_MSG = " cannot be empty";

    public <T> void VerifyNonNull(T... args) throws ArgumentException {
        for (T arg : args)
            VerifyNonNull(arg, NON_NULL_ARG_MSG);
    }

    public <T> void VerifyNonNull(T arg, String message) throws ArgumentException {
        if (arg == null) {
            throw new ArgumentException(NON_NULL_ARG_MSG);
        }
    }

    public void VerifyParamsNonNull(Object... arguments) throws ArgumentException {
        VerifyNonNull(arguments);

        for (Object argument : arguments) {
            VerifyNonNull(argument);
        }
    }

    public void VerifyNotEmptyString(String arg) throws ArgumentException {
        VerifyNotEmptyString(arg, arg + EMPTY_STRING_ARG_MSG);
    }


    public void VerifyNotEmptyString(String arg, String message) throws ArgumentException {
        if (arg.isEmpty()) {
            throw new ArgumentException(message);
        }
    }
}
