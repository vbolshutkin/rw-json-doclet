package org.enargit.javadoc.domain;

import java.util.HashMap;
import java.util.Map;

public class PackageVO extends DocVO {
	public String fqdn;
	public Map<String, ClassVO> classes = new HashMap<String, ClassVO>(); 
}
