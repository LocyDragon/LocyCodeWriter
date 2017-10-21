package com.locydragon.lcv.api;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class NormalClassWriter {
private String className;
private HashMap<String,CodeList> methodCode = new HashMap<>();
private HashMap<String,String> methodValue = new HashMap<>();
private HashMap<String,String> methodLevel = new HashMap<>();
private HashMap<String,Boolean> isStatic = new HashMap<>();
private HashMap<String,String> returns = new HashMap<>();
private CodeList staticBlock = new CodeList();
private String packageName = "";
private CodeList imports = new CodeList();
private CodeList implementsClass = new CodeList();
private String extendsClass = "";
private boolean canExtend = false;
private List<String> codes = new ArrayList<>();
/**
* ��ȡһ��NormalClassWriter��ʵ������
* @param className �������
*/
public NormalClassWriter(String className) {
	this.className = className;
}
/**
* ��ȡ��д�������
* @return �������writer��д������
*/
public String getClassName() {
	return this.className;
}
/**
* ������дstatic��Ĵ���
* @param staticBlock ����
*/
public void setStaticBlock(CodeList staticBlock) {
	this.staticBlock = staticBlock;
}
/**
* ��ȡ��дstatic��Ĵ���
* @return ��дstatic��Ĵ���
*/
public CodeList getStaticBlock() {
	return this.staticBlock;
}
/**
* ������д������
* @param name ��д������
*/
public void setClassName(String name) {
	this.className = name;
}
/**
* ����һ���µķ���
* @param methodName ������
* @param code �����ڴ���
* @param value ������ֵ������: String[] args, int i,double double
* @param returnsType ���ص�ֵ������: void �ٱ���: int �ٱ���: Class<?> ע��ʹ�õ��뷽��������
*/
public void addMethod(String methodName, CodeList code, String value, String returnsType) {
	if (this.methodCode.containsKey(methodName)) {
		throw new SameMethodNameException("There is a method called "+methodName+" now!You cannot add in!");
	}
	this.methodCode.put(methodName, code);
	if (value.equalsIgnoreCase("")) {
		this.methodValue.put(methodName, value);
	} else {
		this.methodValue.put(methodName, value);
	}
	if (!this.methodLevel.containsKey(methodName) && !this.isStatic.containsKey(methodName)) {
	this.methodLevel.put(methodName, "public");
	this.isStatic.put(methodName, true);
	}
	
	this.returns.put(methodName, returnsType);
}
/**
* ����һ���µķ���
* @param methodName ������
* @param code �����ڴ���
* @param value ������ֵ������: String[] args, int i,double double
* @param isStatic �Ƿ��Ǳ�����Ϊ��̬static�ķ���
* @param level �����ĵȼ�����private,protected,public
* @param returnsType ���ص�ֵ������: void �ٱ���: int �ٱ���: Class<?> ע��ʹ�õ��뷽��������
*/
public void addMethod(String methodName, CodeList code, String value, boolean isStatic, ClassLevel level, String returnsType) {
	this.isStatic.put(methodName, isStatic);
	switch(level) {
	case PRIVATE:
		this.methodLevel.put(methodName, "private");
	case PROTECTED:
		this.methodLevel.put(methodName, "protected");
	case PUBLIC:
		this.methodLevel.put(methodName, "public");
	}
	this.addMethod(methodName, code, value, returnsType);
}
/**
* �޸�һ��������ֵ
* @param methodName ������
* @param value ������ֵ����addMethod����
*/
public void setValueOfMethod(String methodName, String value) {
	if (!this.methodCode.containsKey(methodName)) {
		throw new NullPointerException("Cannot find the method: "+methodName);
	}
	this.methodValue.remove(methodName);
	this.methodValue.put(methodName, value);
}
/**
* �޸�һ�������Ĵ���
* @param methodName ������
* @param code �����ڴ���
*/
public void setCodeOfMethod(String methodName, CodeList code) {
	if (!this.methodCode.containsKey(methodName)) {
		throw new NullPointerException("Cannot find the method: "+methodName);
	}
	this.methodCode.remove(methodName);
	this.methodCode.put(methodName, code);
}
/**
* ����������Ƿ��ܱ��̳�
* @param extend falseΪ���ɼ̳У���֮Ϊ�ɼ̳�
*/
public void setCanExtend(boolean extend) {
	this.canExtend = extend;
}
/**
* ������д��ļ̳���
* @param className �̳е��������� ArrayList ע��ʹ��addImport���е���
* @exception HasExtendsClassException ����Ѿ��м̳е���ͻ��׳����쳣
*/
public void setExtendClass(String className) throws HasExtendsClassException {
	if (!this.extendsClass.equalsIgnoreCase("")) {
		throw new HasExtendsClassException("Has a extended class called "+extendsClass);
	}
	this.extendsClass = className;
}
/**
* ��ȡ��д��ļ̳���
* @return �̳е���
*/
public String getExtendClass() {
	return this.extendsClass;
}
/**
* ����һ���ӿ�
* @param name �ӿ������� Runnable ע��ʹ��addImport���е���
*/
public void addImplementClass(String name) {
	if (this.implementsClass.contains(name)) {
		return;
	}
	this.implementsClass.add(name);
}
/**
* ��ȡ���нӿ�
* @return ���нӿ�
*/
public CodeList getImplementsClass() {
	return this.implementsClass;
}
/**
* ����һ��������
* @param ������࣬��:java.util.List
*/
public void addImport(String importClass) {
	if (this.imports.contains(importClass)) {
		return;
	}
	this.imports.add(importClass);
}
/**
* ��ȡ���е������
* @return ���е������
*/
public CodeList getImports() {
	return this.imports;
}
/**
* ���ð���
* @param name ����
*/
public void setPackageName(String name) {
	this.packageName = name;
}
/**
* ��ȡ����
* @return ����
*/
public String getPackageName() {
	return this.packageName;
}
/**
* ��ȡ��д�����Ƿ��ܹ����̳�
* @return �ܷ񱻼̳�
*/
public boolean getCanExtend() {
	return this.canExtend;
}
/**
* д�����
* @param path д������·������C://items//class
* @return �Ƿ���д�ɹ�
*/
public boolean write(String path) {
	File classFile = new File(path);
	File input = new File(path+"//"+className+".java");
    classFile.getParentFile().mkdirs();
	if (input.exists()) {
		input.delete();
	}
	input.getParentFile().mkdirs();
	try {
		input.createNewFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	generatorCodes();
	try {
		FileWriter w = new FileWriter(input);
		for (String s : this.codes) {
			w.write(s);
		}
		w.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	File file = new File(classFile.getAbsolutePath()+"//"+this.className+".class");
	if (file.exists()) {
		file.delete();
	}
	try {
		file.createNewFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();  
	StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, null, null);
	Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(input));  
	boolean is = javaCompiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
	input.delete();
	return is;
}
private void generatorCodes() {
	if (!this.packageName.equalsIgnoreCase("")) {
		codes.add("package "+packageName+";");
	}
	for (String s : this.imports) {
		codes.add("import "+s+";");
	}
	if (this.canExtend) {
		codes.add("public class "+className+" ");
	} else {
		codes.add("public final class "+className+" ");
	}
	if (!this.extendsClass.equalsIgnoreCase("")) {
		codes.add("extends "+this.extendsClass);
	}
	boolean addImple = false;
	if (this.implementsClass.size() > 0) {
		codes.add("implements");
		addImple = true;
	}
	if (addImple) {
	for (String s : this.implementsClass) {
		codes.add(s);
	}
	}
	codes.add("{");
	for (String s : this.methodCode.keySet()) {
		String level = this.methodLevel.get(s);
		String returns = this.returns.get(s);
		String value = this.methodValue.get(s);
		codes.add(level+" "+returns+" "+s+"("+value+") {");
		for (String code :this.methodCode.get(s)) {
			codes.add(code+"\n");
		}
		codes.add("}");
	}
	if (this.staticBlock.size() > 0) {
		codes.add("static {");
		for (String code : this.staticBlock) {
			codes.add(code+"\n");
		}
		codes.add("}");
	}
	codes.add("}");		
}  
}
