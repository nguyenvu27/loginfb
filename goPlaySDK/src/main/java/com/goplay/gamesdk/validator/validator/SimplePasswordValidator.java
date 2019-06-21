package com.goplay.gamesdk.validator.validator;

import android.content.Context;
import android.text.TextUtils;

import com.goplay.gamesdk.R;
import com.goplay.gamesdk.validator.AbstractValidator;

public class SimplePasswordValidator extends AbstractValidator {

	private static final int DEFAULT_ERROR_MESSAGE_RESOURCE = R.string.validator_simple_pw;

	public SimplePasswordValidator(Context c) {
		super(c, DEFAULT_ERROR_MESSAGE_RESOURCE);
	}

	public SimplePasswordValidator(Context c, int errorMessageRes) {
		super(c, errorMessageRes);
	}

	@Override
	public boolean isValid(String password) {
		return !TextUtils.equals(password, "123456");
	}
}
