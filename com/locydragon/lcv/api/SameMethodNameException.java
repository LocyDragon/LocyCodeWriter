package com.locydragon.lcv.api;

public class SameMethodNameException extends NullPointerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6511415588542963920L;
	/**
	* 构造方法
	* 本异常在有相同方法名时抛出
	*/
public SameMethodNameException(String reason) {
	super(reason);
}
}
