package com.goplay.gamesdk.validator.validator;

import android.content.Context;
import android.text.TextUtils;

import com.goplay.gamesdk.R;
import com.goplay.gamesdk.validator.AbstractValidator;

public class IdOrPassportValidator extends AbstractValidator {

	private static final int DEFAULT_ERROR_MESSAGE_RESOURCE = R.string.validator_id_passport;

	public IdOrPassportValidator(Context c) {
		super(c, DEFAULT_ERROR_MESSAGE_RESOURCE);
	}

	public IdOrPassportValidator(Context c, int errorMessageRes) {
		super(c, errorMessageRes);
	}

	@Override
	public boolean isValid(String idOrPassport) {
        return !(TextUtils.equals(idOrPassport, "123456789") || TextUtils.equals(idOrPassport, "1234567890") || TextUtils.equals(idOrPassport, "0123456789") || TextUtils.equals(idOrPassport, "12345678")
                || TextUtils.equals(idOrPassport, "1234567"));
	}
}
