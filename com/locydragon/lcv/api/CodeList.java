package com.locydragon.lcv.api;

import java.util.ArrayList;


public class CodeList extends ArrayList<String> {
private static final long serialVersionUID = 1L;
/**
* ��д��toString��������
* �����İ�~����listҲ�Ƿ�ֹĳЩ����ķ���
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
