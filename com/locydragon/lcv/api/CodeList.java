package com.locydragon.lcv.api;

import java.util.ArrayList;


public class CodeList extends ArrayList<String> {
private static final long serialVersionUID = 1L;
/**
* 重写了toString方法而已
* 很无聊吧~不用list也是防止某些事情的发生
* @return toString()
*/
public String toString() {
	StringBuilder sb = new StringBuilder();
	for (int i = 0;i < super.size(); i++) {
		sb.append(super.get(i));
	}
	return sb.toString();
}
}
