package jbse.val;

import java.io.Serializable;

/**
 * An access to an array's length.
 * 
 * @author Pietro Braione
 *
 */
public final class AccessArrayLength extends AccessNonroot implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6328198481896162630L;
	private static final AccessArrayLength INSTANCE = new AccessArrayLength();
    
    private AccessArrayLength() {
        //nothing to do
    }
    
    public static AccessArrayLength instance() {
        return INSTANCE;
    }
    
    @Override
    public String toString() {
        return "length";
    }
}
