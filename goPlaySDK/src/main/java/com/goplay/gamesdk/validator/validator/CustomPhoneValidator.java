package com.goplay.gamesdk.validator.validator;

import android.content.Context;

import com.goplay.gamesdk.R;
import com.goplay.gamesdk.validator.AbstractValidator;

public class CustomPhoneValidator extends AbstractValidator {

	private static final int DEFAULT_ERROR_MESSAGE_RESOURCE = R.string.validator_phone;

	public CustomPhoneValidator(Context c) {
		super(c, DEFAULT_ERROR_MESSAGE_RESOURCE);
	}

	public CustomPhoneValidator(Context c, int errorMessageRes) {
		super(c, errorMessageRes);
	}

	@Override
	public boolean isValid(String phone) {
		return phone.startsWith("0");
	}
}
