package org.enargit.javadoc.domain;

public class PackageRef {
	public String fqdn;
	
	public static PackageRef from(String fqdn) {
		PackageRef res = new PackageRef();
		res.fqdn = fqdn;
		return res;
	}
}
