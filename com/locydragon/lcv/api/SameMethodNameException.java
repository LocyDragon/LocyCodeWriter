package com.locydragon.lcv.api;

public class SameMethodNameException extends NullPointerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6511415588542963920L;
	/**
	* ���췽��
	* ���쳣������ͬ������ʱ�׳�
	*/
public SameMethodNameException(String reason) {
	super(reason);
}
}
