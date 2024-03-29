package com.goplay.gamesdk.validator;

import android.content.Context;

/**
 * Class for creating new Validators
 * 
 * @author throrin19
 */
public abstract class AbstractValidator {

	private Context mContext;

	private int mErrorMessageRes;

	private String mErrorMessageString;

	public AbstractValidator(Context c, int errorMessageRes) {
		mContext = c;
		mErrorMessageRes = errorMessageRes;
		mErrorMessageString = mContext.getString(mErrorMessageRes);
	}

	public AbstractValidator(Context c, String errorMessageString) {
		mContext = c;
		mErrorMessageString = errorMessageString;
	}

	/**
	 * Can check if the value passed in parameter is valid or not.
	 * 
	 * @param value
	 *            {@link String} : the value to validate
	 * @return boolean : true if valid, false otherwise.
	 */
	public abstract boolean isValid(String value)
			throws ValidatorException;

	/**
	 * Used to retrieve the error message corresponding to the validator.
	 * 
	 * @return String : the error message
	 */
	public String getMessage() {
		return mErrorMessageString;
	}

	/**
	 * Sets the Context of the validator. Useful if we want to switch Context after a Configuration Change
	 */
	public void setContext(Context c) {
		this.mContext = c;
	}

	/**
	 * Gets the textview's context
	 * 
	 * @return Context
	 */
	public Context getContext() {
		return mContext;
	}
}
