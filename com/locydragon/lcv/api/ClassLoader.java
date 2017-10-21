package com.locydragon.lcv.api;

public class ClassLoader extends java.lang.ClassLoader{
	/**
	* 载入一个类
	* <p>载入一个类，其代码为codes<br>
	* 见java.lang.ClassLoader.defineClass
	* @return 返回所加载的类
	*/

public Class<?> loadClass(String className, byte[] codes) {
	return super.defineClass(className, codes, 0, codes.length);
}
private ClassLoader() {}
/**
* 获得本类实例对象
* @return 返回本类实例对象
*/
public static ClassLoader getInstance() {
	return new ClassLoader();
}
}
