package com.goplay.gamesdk.validator.validator;

import java.util.regex.Pattern;

import android.content.Context;

import com.goplay.gamesdk.R;
import com.goplay.gamesdk.validator.AbstractValidator;
import com.goplay.gamesdk.validator.ValidatorException;

/**
 * This validator test value with custom Regex Pattern.
 */
public class RegExpValidator extends AbstractValidator {

    private static final int DEFAULT_ERROR_MESSAGE_RESOURCE = R.string.validator_regexp;
    private Pattern mPattern;

    public RegExpValidator(Context c) {
        super(c, DEFAULT_ERROR_MESSAGE_RESOURCE);
    }

    public RegExpValidator(Context c, int errorMessageRes) {
        super(c, errorMessageRes);
    }

    public void setPattern(String pattern) {
        mPattern = Pattern.compile(pattern);
    }

    public void setPattern(Pattern pattern) {
        mPattern = pattern;
    }

    @Override
    public boolean isValid(String text) throws ValidatorException {
        if (mPattern != null) {
            return mPattern.matcher(text).matches();
        }
        throw new ValidatorException("You can set Regexp Pattern first");
    }
}
