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
* 获取一个NormalClassWriter的实例对象
* @param className 你的类名
*/
public NormalClassWriter(String className) {
	this.className = className;
}
/**
* 获取所写类的类名
* @return 返回这个writer所写的类名
*/
public String getClassName() {
	return this.className;
}
/**
* 设置所写static块的代码
* @param staticBlock 代码
*/
public void setStaticBlock(CodeList staticBlock) {
	this.staticBlock = staticBlock;
}
/**
* 获取所写static块的代码
* @return 所写static块的代码
*/
public CodeList getStaticBlock() {
	return this.staticBlock;
}
/**
* 设置所写的类名
* @param name 所写的类名
*/
public void setClassName(String name) {
	this.className = name;
}
/**
* 增加一个新的方法
* @param methodName 方法名
* @param code 方法内代码
* @param value 方法的值，比如: String[] args, int i,double double
* @param returnsType 返回的值，比如: void 再比如: int 再比如: Class<?> 注意使用导入方法导入类
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
* 增加一个新的方法
* @param methodName 方法名
* @param code 方法内代码
* @param value 方法的值，比如: String[] args, int i,double double
* @param isStatic 是否是被修饰为静态static的方法
* @param level 方法的等级，有private,protected,public
* @param returnsType 返回的值，比如: void 再比如: int 再比如: Class<?> 注意使用导入方法导入类
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
* 修改一个方法的值
* @param methodName 方法名
* @param value 方法的值，见addMethod方法
*/
public void setValueOfMethod(String methodName, String value) {
	if (!this.methodCode.containsKey(methodName)) {
		throw new NullPointerException("Cannot find the method: "+methodName);
	}
	this.methodValue.remove(methodName);
	this.methodValue.put(methodName, value);
}
/**
* 修改一个方法的代码
* @param methodName 方法名
* @param code 方法内代码
*/
public void setCodeOfMethod(String methodName, CodeList code) {
	if (!this.methodCode.containsKey(methodName)) {
		throw new NullPointerException("Cannot find the method: "+methodName);
	}
	this.methodCode.remove(methodName);
	this.methodCode.put(methodName, code);
}
/**
* 设置这个类是否能被继承
* @param extend false为不可继承，反之为可继承
*/
public void setCanExtend(boolean extend) {
	this.canExtend = extend;
}
/**
* 设置所写类的继承类
* @param className 继承的类名，如 ArrayList 注意使用addImport进行导入
* @exception HasExtendsClassException 如果已经有继承的类就会抛出此异常
*/
public void setExtendClass(String className) throws HasExtendsClassException {
	if (!this.extendsClass.equalsIgnoreCase("")) {
		throw new HasExtendsClassException("Has a extended class called "+extendsClass);
	}
	this.extendsClass = className;
}
/**
* 获取所写类的继承类
* @return 继承的类
*/
public String getExtendClass() {
	return this.extendsClass;
}
/**
* 增加一个接口
* @param name 接口名，如 Runnable 注意使用addImport进行导入
*/
public void addImplementClass(String name) {
	if (this.implementsClass.contains(name)) {
		return;
	}
	this.implementsClass.add(name);
}
/**
* 获取所有接口
* @return 所有接口
*/
public CodeList getImplementsClass() {
	return this.implementsClass;
}
/**
* 增加一个导入类
* @param 导入的类，如:java.util.List
*/
public void addImport(String importClass) {
	if (this.imports.contains(importClass)) {
		return;
	}
	this.imports.add(importClass);
}
/**
* 获取所有导入的类
* @return 所有导入的类
*/
public CodeList getImports() {
	return this.imports;
}
/**
* 设置包名
* @param name 包名
*/
public void setPackageName(String name) {
	this.packageName = name;
}
/**
* 获取包名
* @return 包名
*/
public String getPackageName() {
	return this.packageName;
}
/**
* 获取所写的类是否能够被继承
* @return 能否被继承
*/
public boolean getCanExtend() {
	return this.canExtend;
}
/**
* 写入代码
* @param path 写入代码的路径，如C://items//class
* @return 是否书写成功
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
