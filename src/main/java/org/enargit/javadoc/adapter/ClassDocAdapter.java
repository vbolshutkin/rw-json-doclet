package org.enargit.javadoc.adapter;

import org.enargit.javadoc.domain.ClassVO;

import com.sun.javadoc.AnnotatedType;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.ParameterizedType;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.SourcePosition;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import com.sun.javadoc.TypeVariable;
import com.sun.javadoc.WildcardType;

public class ClassDocAdapter extends ProgramElementDocAdapter implements ClassDoc {

	private ClassVO classVO;
	
	public String typeName() {
		return classVO.name;
	}

	public String qualifiedTypeName() {
		return classVO.containingPackage + "." + classVO.name;
	}

	public String simpleTypeName() {
		return classVO.name;
	}

	public String dimension() {
		return "";
	}

	public boolean isPrimitive() {
		return false;
	}

	public ClassDoc asClassDoc() {
		return this;
	}

	public ParameterizedType asParameterizedType() {
		// TODO Auto-generated method stub
		return null;
	}

	public TypeVariable asTypeVariable() {
		// TODO Auto-generated method stub
		return null;
	}

	public WildcardType asWildcardType() {
		// TODO Auto-generated method stub
		return null;
	}

	public AnnotatedType asAnnotatedType() {
		// TODO Auto-generated method stub
		return null;
	}

	public AnnotationTypeDoc asAnnotationTypeDoc() {
		// TODO Auto-generated method stub
		return null;
	}

	public Type getElementType() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAbstract() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSerializable() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isExternalizable() {
		// TODO Auto-generated method stub
		return false;
	}

	public MethodDoc[] serializationMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	public FieldDoc[] serializableFields() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean definesSerializableFields() {
		// TODO Auto-generated method stub
		return false;
	}

	public ClassDoc superclass() {
		// TODO Auto-generated method stub
		return null;
	}

	public Type superclassType() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean subclassOf(ClassDoc cd) {
		// TODO Auto-generated method stub
		return false;
	}

	public ClassDoc[] interfaces() {
		// TODO Auto-generated method stub
		return null;
	}

	public Type[] interfaceTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public TypeVariable[] typeParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	public ParamTag[] typeParamTags() {
		// TODO Auto-generated method stub
		return null;
	}

	public FieldDoc[] fields() {
		// TODO Auto-generated method stub
		return null;
	}

	public FieldDoc[] fields(boolean filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public FieldDoc[] enumConstants() {
		// TODO Auto-generated method stub
		return null;
	}

	public MethodDoc[] methods() {
		// TODO Auto-generated method stub
		return null;
	}

	public MethodDoc[] methods(boolean filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public ConstructorDoc[] constructors() {
		// TODO Auto-generated method stub
		return null;
	}

	public ConstructorDoc[] constructors(boolean filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public ClassDoc[] innerClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	public ClassDoc[] innerClasses(boolean filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public ClassDoc findClass(String className) {
		// TODO Auto-generated method stub
		return null;
	}

	public ClassDoc[] importedClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	public PackageDoc[] importedPackages() {
		// TODO Auto-generated method stub
		return null;
	}

}
