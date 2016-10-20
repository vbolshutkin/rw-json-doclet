package org.enargit.javadoc.doclet;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.enargit.javadoc.domain.ClassVO;
import org.enargit.javadoc.domain.FieldVO;
import org.enargit.javadoc.domain.MethodVO;
import org.enargit.javadoc.domain.PackageVO;
import org.enargit.javadoc.domain.RootVO;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;
import com.sun.tools.doclets.standard.Standard;

public class JsonDoclet {

	private static boolean publicOnly = false;
	private static boolean includeFields = true;
	private static boolean includeStatic = false;
	private static boolean withCommentsOnly = true;
	private static String classesAnnotatedWith = "javax.ws.rs.Path";
	
	private static Set<String> referencedClasses = new HashSet<String>();

	private static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}
	
	
	private static MethodVO createMethodVO(MethodDoc method) {
		MethodVO methodVO = new MethodVO();
	
		methodVO.comment = method.getRawCommentText();
		methodVO.modifiers = method.modifiers();
		methodVO.name = method.name();
		methodVO.returnType = method.returnType().qualifiedTypeName();
		if (method.overriddenMethod() != null) {
			methodVO.overrided = method.overriddenMethod().name();
		}
		
		referencedClasses.add(method.returnType().qualifiedTypeName());
		if (method.returnType().asParameterizedType() != null) {
			Type[] types = method.returnType().asParameterizedType().typeArguments();
			for (int i = 0; i < types.length; i++) {
				Type type = types[i];
				referencedClasses.add(type.qualifiedTypeName());
			}
		}
		
		Parameter[] params = method.parameters();
		for (int i = 0; i < params.length; i++) {
			Parameter param = params[i];
			referencedClasses.add(param.type().qualifiedTypeName());
		}
		
		
		if (withCommentsOnly && isEmpty(methodVO.comment)) return null;
		
		return methodVO;
	}
	
	private static FieldVO createFieldVO(FieldDoc field) {
		FieldVO fieldVO = new FieldVO();
		fieldVO.comment = field.getRawCommentText();
		fieldVO.modifiers = field.modifiers();
		fieldVO.name = field.name();
		fieldVO.fieldType = field.type().qualifiedTypeName();
		
		referencedClasses.add(fieldVO.fieldType);
		
		if (withCommentsOnly && isEmpty(fieldVO.comment)) return null;
		
		return fieldVO;
	}
	
	private static ClassVO createClassVO(ClassDoc cls, PackageVO packageVO) {
		
		if (classesAnnotatedWith != null) {
			boolean found = false;
			for (AnnotationDesc annotation : cls.annotations()) {
				if (classesAnnotatedWith.equals(annotation.annotationType().qualifiedName())) {
					found = true;
					break;
				}
			}
			if (!found && !referencedClasses.contains(cls.qualifiedName())) return null;
		}
		
		boolean hasComment = false;
		
		ClassVO classVO = new ClassVO();
		classVO.comment = cls.getRawCommentText();
		classVO.containingPackage = cls.containingPackage().name();
		if (cls.containingClass() != null) {
			classVO.containingClass = cls.containingClass().qualifiedName();
		}
		
		if (!isEmpty(classVO.comment)) {
			hasComment = true;
		}
		
		for (MethodDoc method : cls.methods()) {
			if (!Modifier.isPublic(method.modifierSpecifier()) && publicOnly) continue;
			if (Modifier.isStatic(method.modifierSpecifier()) && !includeStatic) continue;
			MethodVO vo = createMethodVO(method);
			if (vo != null) {
				hasComment = true;
				classVO.methods.put(method.name(), vo);
			}
		}
		
		if (includeFields) {
			
			for (FieldDoc field : cls.fields()) {
				if (!Modifier.isPublic(field.modifierSpecifier()) && publicOnly) continue;
				if (Modifier.isStatic(field.modifierSpecifier()) && !includeStatic) continue;
				
				FieldVO vo = createFieldVO(field);
				if (vo != null) {
					hasComment = true;
					classVO.fields.put(field.name(), createFieldVO(field));
				}
			}
		}
	
		for (ClassDoc innerClass : cls.innerClasses()) {
			if (!Modifier.isPublic(innerClass.modifierSpecifier()) && publicOnly) continue;
			if (Modifier.isStatic(innerClass.modifierSpecifier()) && !includeStatic) continue;
			
			ClassVO vo = createClassVO(innerClass,packageVO);
			
			if (vo != null) {
				hasComment = true;
				packageVO.classes.put(innerClass.name(), vo);
			}
		}
		
		if (withCommentsOnly && !hasComment) return null;
		
		return classVO;
	}
	/** Hello world
	 */
	private static PackageVO createPackageVO(PackageDoc pack) {
		PackageVO packageVO = new PackageVO();
		packageVO.fqdn = pack.name();
		return packageVO;
	}
	
	// See http://docs.oracle.com/javase/7/docs/technotes/guides/javadoc/doclet/overview.html#options
	
	 /**
     * Sets the language version to Java 5.
     *
     * _Javadoc spec requirement._
     *
     * @return language version number
     */
    @SuppressWarnings("UnusedDeclaration")
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }

    /**
     * Sets the option length to the standard Javadoc option length.
     *
     * _Javadoc spec requirement._
     *
     * @param option input option
     * @return length of required parameters
     */
    @SuppressWarnings("UnusedDeclaration")
    public static int optionLength(String option) {
        for (Options opt : Options.values()) {
            if (opt.getOptionName().equalsIgnoreCase(option)) {
                return opt.getOptionLength();
            }
        }

        return Standard.optionLength(option);
    }

	
	@SuppressWarnings("UnusedDeclaration")
	public static boolean validOptions(String[][] options, DocErrorReporter errorReporter) {
        return Standard.validOptions(options, errorReporter);
    }
	
	static enum Options {
        TARGET_FILE("-target-file", 2);

        private final String optionName;
        private final int optionLength;

        Options(String name, int optionLength) {
            this.optionName = name;
            this.optionLength = optionLength;
        }

        public String getOptionName() {
            return optionName;
        }

        public int getOptionLength() {
            return optionLength;
        }

        public boolean isSet(String[][] options) {
            for (String[] option : options) {
                if (options.length > 0 && optionName.equals(option[0])) {
                    return true;
                }
            }

            return false;
        }

        public String getOption(String[][] options) {
            for (String[] option : options) {
                if (options.length > 1 && optionName.equals(option[0])) {
                    return option[1];
                }
            }
            return null;
        }
    }
	
	/**
	 * 
	 * @param root
	 * @return
	 * @throws JsonProcessingException
	 */
	public static boolean start(RootDoc root) throws JsonProcessingException {
		
		RootVO rootVO = new RootVO();
		
		for (ClassDoc cls : root.classes()) {
			
			PackageDoc packageDoc = cls.containingPackage();
			
			PackageVO packageVO;
			if (rootVO.packages.containsKey(packageDoc.name())) {
				packageVO = rootVO.packages.get(packageDoc.name());
			} else {
				packageVO = createPackageVO(packageDoc);
			}
			
			ClassVO classVO = createClassVO(cls,packageVO);
			packageVO.classes.put(cls.qualifiedName(), classVO);
			
			if (!rootVO.packages.containsKey(packageDoc.name()) && classVO != null) {
				rootVO.packages.put(packageDoc.name(), packageVO);
			}
		}
		
		// XXX Second pass to process referenced classes
		for (ClassDoc cls : root.classes()) {
			
			PackageDoc packageDoc = cls.containingPackage();
			
			PackageVO packageVO;
			if (rootVO.packages.containsKey(packageDoc.name())) {
				packageVO = rootVO.packages.get(packageDoc.name());
			} else {
				packageVO = createPackageVO(packageDoc);
			}
			
			ClassVO classVO = createClassVO(cls,packageVO);
			packageVO.classes.put(cls.qualifiedName(), classVO);
			
			if (!rootVO.packages.containsKey(packageDoc.name()) && classVO != null) {
				rootVO.packages.put(packageDoc.name(), packageVO);
			}
		}
		
		
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		String res = mapper.writer().writeValueAsString(rootVO);
				
		String target = Options.TARGET_FILE.getOption(root.options());
		if (target == null) {
			System.out.println(res);
		} else {
			try {
				FileUtils.writeStringToFile(new File(target), res);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
				
		
		return true;
    }
}
