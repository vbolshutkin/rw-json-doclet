package org.enargit.javadoc.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassVO extends DocVO {

	public String containingPackage;
	public String containingClass;
	
	public String name;
	public List<String> typeParameters; // FIXME change String to something meaningful (ClassRef)
	
	// ClassRef
	public List<String> hierarchy;
	public Collection<String> importedClasses;
	public List<String> nestedClasses;
	
	public Map<String, MethodVO> methods = new HashMap<String, MethodVO>();
	public Map<String, FieldVO> fields = new HashMap<String, FieldVO>();
	
}
