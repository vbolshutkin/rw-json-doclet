package org.enargit.javadoc.domain;

public class ClassRef {
	public String fqdn;
	
	public static ClassRef from(String fqdn) {
		ClassRef res = new ClassRef();
		res.fqdn = fqdn;
		return res;
	}
	
}
