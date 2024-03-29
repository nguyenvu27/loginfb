package com.goplay.gamesdk.validator.validator;

import android.content.Context;
import android.text.TextUtils;

import com.goplay.gamesdk.R;
import com.goplay.gamesdk.validator.AbstractValidator;

public class NotEmptyValidator extends AbstractValidator {

    private static final int DEFAULT_ERROR_MESSAGE_RESOURCE = R.string.validator_empty;

    public NotEmptyValidator(Context c) {
        super(c, DEFAULT_ERROR_MESSAGE_RESOURCE);
    }

    public NotEmptyValidator(Context c, int errorMessage) {
        super(c, errorMessage);
    }

    @Override
    public boolean isValid(String text) {
        return !TextUtils.isEmpty(text);
    }
}
