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
* ��ȡʵ������
* @param className ����
*/
public InterFaceWriter(String className) {
	this.className = className;
}
/**
* ��������
* @param className ����
*/
public void setClassName(String className) {
	this.className = className;
}
/**
* ��ȡ����
* @return ����
*/
public String getClassName() {
	return this.className;
}
/**
* ����һ������
* @param i ��������ݣ���: java.util.List
*/
public void addImport(String i) {
	if (this.imports.contains(i)) {
		return;
	}
	this.imports.add(i);
}
/**
* ��ȡ���е������
* @return ���е������
*/
public List<String> getImports() {
	return this.imports;
}
/**
* ���ð���
* @param packageName ����
*/
public void setPackageName(String packageName) {
	this.packageName = packageName;
}
/**
* ��ȡ����
* @return ����
*/
public String getPackageName() {
	return this.packageName;
}
/**
* ����һ������
* @param name ������
* @param returnParam ���ص���������:String,int,double��
* @param value ������ֵ(�β�)����: String[] args, int i, double d,File file;ע��ʹ��addImport����������
*/
public void addMethod(String name, String returnParam, String value) {
	this.methods.put(name, returnParam);
	this.methodValue.put(name, value);
}
/**
* ����ĳ�����ķ�����
* @param name ������
* @param returnParam ���ص���������addMethod����
*/
public void setReturnParam(String name, String returnParam) {
	if (!this.methods.containsKey(name)) {
		throw new NullPointerException("Cannot find the method: "+name);
	}
	this.methods.remove(name);
	this.methods.put(name, returnParam);
}
/**
* ����ĳ������ֵ
* @param name ������
* @param value ������ֵ����addMethod����
*/
public void setMethodValue(String name, String value) {
	if (!this.methodValue.containsKey(name)) {
		throw new NullPointerException("Cannot find the method: "+name);
	}
	this.methodValue.remove(name);
	this.methodValue.put(name, value);
}
/**
* �Ѵ���д��һ���ļ���
* @param path ��д���ļ���Ŀ¼����:C://class//items
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
	codes.add("public interface "+this.className+" {");
	for (String s : this.methods.keySet()) {
		codes.add(this.methods.get(s)+" "+s+"("+this.methodValue.get(s)+");");
	}
	codes.add("}");
}
}
