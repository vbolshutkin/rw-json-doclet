package org.enargit.javadoc.domain;

public class MethodRef {
	public String fqdn;
	
	public static MethodRef from(String fqdn) {
		MethodRef res = new MethodRef();
		res.fqdn = fqdn;
		return res;
	}
}
