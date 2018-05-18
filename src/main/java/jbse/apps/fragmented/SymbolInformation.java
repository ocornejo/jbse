package jbse.apps.fragmented;

import java.io.Serializable;

import jbse.common.Type;

public class SymbolInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8409965258925993010L;
	private String id;
	private String name;
	private String type;
	private String typeParse;

	public SymbolInformation(String id, String name, String type) {
		this.id = id;
		this.name = name;

		setType(type);

		try {
			setTypeParse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setType(String type) {
		if (Type.isArray(type)) {
			setType(Type.getArrayMemberType(type));
		} else {
			this.type = Type.isPrimitive(type) ? Type.toPrimitiveBinaryClassName(type)
					: Type.getReferenceClassName(type);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTypeParse() {
		if (type.equals("byte")) {
			this.typeParse = "Byte.parseByte";
		} else if (type.equals("short")) {
			this.typeParse = "Short.parseShort";
		} else if (type.equals("int")) {
			this.typeParse = "Integer.parseInt";
		} else if (type.equals("long")) {
			this.typeParse = "Long.parseLong";
		} else if (type.equals("boolean")) {
			this.typeParse = "parseBoolean";
		} else if (type.equals("char")) {
			this.typeParse = "parseChar";
		} else if (type.equals("float")) {
			this.typeParse = "Float.parseFloat";
		} else if (type.equals("double")) {
			this.typeParse = "Double.parseDouble";
		} else {
			this.typeParse = null;
		}
	}

	public String getTypeAsObject() {
		String typeAsObject;
		if (type.equals("byte")) {
			typeAsObject = "Byte";
		} else if (type.equals("short")) {
			typeAsObject = "Short";
		} else if (type.equals("int") || type.equals("boolean")) {
			typeAsObject = "Integer";
		} else if (type.equals("long")) {
			typeAsObject = "Long";
		} else if (type.equals("char")) {
			typeAsObject = "Character";
		} else if (type.equals("float")) {
			typeAsObject = "Float";
		} else if (type.equals("double")) {
			typeAsObject = "Double";
		} else {
			typeAsObject = "";
		}
		return typeAsObject;
	}

	public String getTypeParse() {
		return typeParse;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return this.type + ":" + this.id;
	}

}
