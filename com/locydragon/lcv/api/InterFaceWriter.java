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

public class InterFaceWriter {
private String className;
private List<String> imports = new ArrayList<>();
private String packageName;
private HashMap<String,String> methods = new HashMap<>();
private HashMap<String,String> methodValue = new HashMap<>();
private List<String> codes = new ArrayList<>();
/**
* 获取实例对象
* @param className 类名
*/
public InterFaceWriter(String className) {
	this.className = className;
}
/**
* 设置类名
* @param className 类名
*/
public void setClassName(String className) {
	this.className = className;
}
/**
* 获取类名
* @return 类名
*/
public String getClassName() {
	return this.className;
}
/**
* 增加一个导入
* @param i 导入的内容，如: java.util.List
*/
public void addImport(String i) {
	if (this.imports.contains(i)) {
		return;
	}
	this.imports.add(i);
}
/**
* 获取所有导入的类
* @return 所有导入的类
*/
public List<String> getImports() {
	return this.imports;
}
/**
* 设置包名
* @param packageName 包名
*/
public void setPackageName(String packageName) {
	this.packageName = packageName;
}
/**
* 获取包名
* @return 包名
*/
public String getPackageName() {
	return this.packageName;
}
/**
* 增加一个方法
* @param name 方法名
* @param returnParam 返回的类名，如:String,int,double等
* @param value 方法的值(形参)，如: String[] args, int i, double d,File file;注意使用addImport方法导入类
*/
public void addMethod(String name, String returnParam, String value) {
	this.methods.put(name, returnParam);
	this.methodValue.put(name, value);
}
/**
* 设置某方法的返回类
* @param name 方法名
* @param returnParam 返回的类名，见addMethod方法
*/
public void setReturnParam(String name, String returnParam) {
	if (!this.methods.containsKey(name)) {
		throw new NullPointerException("Cannot find the method: "+name);
	}
	this.methods.remove(name);
	this.methods.put(name, returnParam);
}
/**
* 设置某方法的值
* @param name 方法名
* @param value 方法的值，见addMethod方法
*/
public void setMethodValue(String name, String value) {
	if (!this.methodValue.containsKey(name)) {
		throw new NullPointerException("Cannot find the method: "+name);
	}
	this.methodValue.remove(name);
	this.methodValue.put(name, value);
}
/**
* 把代码写入一个文件内
* @param path 所写的文件的目录，如:C://class//items
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
	codes.add("public interface "+this.className+" {");
	for (String s : this.methods.keySet()) {
		codes.add(this.methods.get(s)+" "+s+"("+this.methodValue.get(s)+");");
	}
	codes.add("}");
}
}
