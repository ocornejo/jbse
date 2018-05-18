package jbse.val;

import jbse.common.Type;
import jbse.val.exc.ValueDoesNotSupportNativeException;

/**
 * Class representing a default value with unknown type. 
 * Used to initialize the local variable memory area to
 * cope with absence of type information. It is a singleton.
 * 
 * @author Pietro Braione
 */
public final class DefaultValue extends Value {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6335064931567720848L;
	private static DefaultValue instance = new DefaultValue();
	
	private DefaultValue() { 
		super(Type.UNKNOWN); 
	}
	
    public static DefaultValue getInstance() {
        return instance;
    }
	
	@Override
	public boolean equals(Object o) {
		return (o == instance);
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		return "<DEFAULT>";
	}

	@Override
	public Object getValueForNative() throws ValueDoesNotSupportNativeException {
		throw new ValueDoesNotSupportNativeException();
	}

	@Override
	public boolean isSymbolic() {
		return true;
	}

    @Override
    public String originFragmented() {
        throw new UnsupportedOperationException();
    }
	
}
