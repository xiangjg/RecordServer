package com.jone.record.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UuidUtil {

	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	public static String getDateSqe(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

/*	public static void main(String[] args) {
		System.out.println(get32UUID());
	}*/
}
