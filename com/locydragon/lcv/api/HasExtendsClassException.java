package com.locydragon.lcv.api;


public class HasExtendsClassException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6541203900876910384L;

	/**
	* 构造方法
	* 本异常在已经有了一个集成的类时抛出
	*/
public HasExtendsClassException(String reason) {
	super(reason);
}
}
