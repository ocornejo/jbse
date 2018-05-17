package jbse.bc;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import jbse.bc.exc.FieldNotFoundException;
import jbse.bc.exc.InvalidIndexException;
import jbse.bc.exc.MethodNotFoundException;

class ClassFileBoolean extends ClassFilePrimitive implements Serializable {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1526308242251281143L;

	ClassFileBoolean() { super("boolean"); }	
}

class ClassFileByte extends ClassFilePrimitive implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1027188188237523624L;

	ClassFileByte() { super("byte"); }	
}

class ClassFileCharacter extends ClassFilePrimitive implements Serializable {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1460648497829228632L;

	ClassFileCharacter() { super("char"); }	
}

class ClassFileShort extends ClassFilePrimitive implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4958021061700183637L;

	ClassFileShort() { super("short"); }	
}

class ClassFileInteger extends ClassFilePrimitive implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3427230361140745922L;

	ClassFileInteger() { super("int"); }	
}

class ClassFileLong extends ClassFilePrimitive implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4572778947158381544L;

	ClassFileLong() { super("long"); }	
}

class ClassFileFloat extends ClassFilePrimitive implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3021932492630556320L;

	ClassFileFloat() { super("float"); }	
}

class ClassFileDouble extends ClassFilePrimitive implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5177691997532576585L;

	ClassFileDouble() { super("double"); }	
}

class ClassFileVoid extends ClassFilePrimitive implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2613351403254470721L;

	ClassFileVoid() { super("void"); }	
}


/**
 * A {@link ClassFile} for the primitive classes.
 * 
 * @author Pietro Braione
 *
 */
abstract class ClassFilePrimitive extends ClassFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3262049330127817622L;

	private static final String NO_CONSTANT_POOL = "Primitive classes have no constant pool.";
	
	private final String className;
	
	protected ClassFilePrimitive(String className) {
		this.className = className;
	}
	
	@Override
	public String getSourceFile() {
	    return "";
	}
	
	@Override
	public String getPackageName() {
		return ""; //TODO is it ok?
	}
	
	@Override
	public String getClassName() {
		return this.className;
	}
    
    @Override
    public boolean isArray() {
        return false;
    }

	@Override
	public boolean isInterface() {
		return false;
	}

	@Override
	public boolean isAbstract() {
		return true;
	}

	@Override
	public boolean isPublic() {
		return true;
	}

	@Override
	public boolean isPackage() {
		return false;
	}

	@Override
	public boolean isPrimitive() {
		return true;
	}

	@Override
	public boolean isSuperInvoke() {
		return false; //no meaning since objects of primitive classes have no methods
	}

	@Override
	public boolean isNested() {
		return false;
	}

	@Override
	public String classContainer() {
		return null;
	}

	@Override
	public boolean isStatic() {
		return false;
	}

	@Override
	public boolean hasMethodImplementation(Signature methodSignature) {
		return false;
	}

	@Override
	public boolean hasMethodDeclaration(Signature methodSignature) {
		return false;
	}

	@Override
	public boolean isMethodAbstract(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public boolean isMethodStatic(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public boolean isMethodPublic(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public boolean isMethodProtected(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public boolean isMethodPackage(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public boolean isMethodPrivate(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public boolean isMethodNative(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public Object[] getMethodAvailableAnnotations(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public ExceptionTable getExceptionTable(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public LocalVariableTable getLocalVariableTable(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public LineNumberTable getLineNumberTable(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public ConstantPoolValue getValueFromConstantPool(int index)
	throws InvalidIndexException {
		throw new InvalidIndexException(NO_CONSTANT_POOL);
	}

	@Override
	public byte[] getMethodCodeBySignature(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public boolean hasFieldDeclaration(Signature fieldSignature) {
		return false;
	}

	@Override
	public boolean isFieldFinal(Signature fieldSignature)
	throws FieldNotFoundException {
		throw new FieldNotFoundException(fieldSignature.toString());
	}

	@Override
	public boolean isFieldPublic(Signature fieldSignature)
	throws FieldNotFoundException {
		throw new FieldNotFoundException(fieldSignature.toString());
	}

	@Override
	public boolean isFieldProtected(Signature fieldSignature)
	throws FieldNotFoundException {
		throw new FieldNotFoundException(fieldSignature.toString());
	}

	@Override
	public boolean isFieldPackage(Signature fieldSignature)
	throws FieldNotFoundException {
		throw new FieldNotFoundException(fieldSignature.toString());
	}

	@Override
	public boolean isFieldPrivate(Signature fieldSignature)
	throws FieldNotFoundException {
		throw new FieldNotFoundException(fieldSignature.toString());
	}
	
	@Override
	public boolean isFieldStatic(Signature fieldSignature)
	throws FieldNotFoundException {
		throw new FieldNotFoundException(fieldSignature.toString());
	}

	@Override
	public boolean hasFieldConstantValue(Signature fieldSignature)
	throws FieldNotFoundException {
		throw new FieldNotFoundException(fieldSignature.toString());
	}

	@Override
	public int fieldConstantValueIndex(Signature fieldSignature)
	throws FieldNotFoundException {
		throw new FieldNotFoundException(fieldSignature.toString());
	}

	@Override
	public Signature[] getFieldsNonStatic() {
		return new Signature[0];
	}

	@Override
	public Signature[] getFieldsStatic() {
		return new Signature[0];
	}

	@Override
	public Signature getFieldSignature(int fieldRef)
	throws InvalidIndexException {
		throw new InvalidIndexException(NO_CONSTANT_POOL);
	}

	@Override
	public Signature[] getMethodSignatures() {
		return new Signature[0];
	}

	@Override
	public Signature getMethodSignature(int methodRef)
	throws InvalidIndexException {
		throw new InvalidIndexException(NO_CONSTANT_POOL);
	}

	@Override
	public Signature getInterfaceMethodSignature(int methodRef)
	throws InvalidIndexException {
		throw new InvalidIndexException(NO_CONSTANT_POOL);
	}

	@Override
	public String getClassSignature(int classRef) 
	throws InvalidIndexException {
		throw new InvalidIndexException(NO_CONSTANT_POOL);
	}

	@Override
	public String getSuperClassName() {
		return null;
	}

	@Override
	public List<String> getSuperInterfaceNames() {
		return Collections.emptyList();
	}

	@Override
	public int getLocalVariableLength(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}

	@Override
	public int getCodeLength(Signature methodSignature)
	throws MethodNotFoundException {
		throw new MethodNotFoundException(methodSignature.toString());
	}
}
