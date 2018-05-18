package jbse.val;

import jbse.common.Type;
import jbse.val.exc.ValueDoesNotSupportNativeException;

/**
 * Class for references to heap objects (instances and arrays).
 */
public abstract class Reference extends Value {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6281632483004405721L;


	protected Reference(char type) {
		super(type);
	}
	
    protected Reference() {
        this(Type.REFERENCE);
    }


    /**
     * {@inheritDoc}
     */
	@Override
	public Object getValueForNative() throws ValueDoesNotSupportNativeException {
		throw new ValueDoesNotSupportNativeException();
	}
}