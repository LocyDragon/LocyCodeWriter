package com.locydragon.lcv.api;

public class ClassLoader extends java.lang.ClassLoader{
	/**
	* ����һ����
	* <p>����һ���࣬�����Ϊcodes<br>
	* ��java.lang.ClassLoader.defineClass
	* @return ���������ص���
	*/

public Class<?> loadClass(String className, byte[] codes) {
	return super.defineClass(className, codes, 0, codes.length);
}
private ClassLoader() {}
/**
* ��ñ���ʵ������
* @return ���ر���ʵ������
*/
public static ClassLoader getInstance() {
	return new ClassLoader();
}
}
