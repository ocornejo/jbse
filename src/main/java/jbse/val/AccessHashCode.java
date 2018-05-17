package jbse.val;

import java.io.Serializable;

/**
 * An access to an object's field.
 * 
 * @author Pietro Braione
 *
 */
public final class AccessHashCode extends AccessNonroot implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4006952201236899061L;
	private static final AccessHashCode INSTANCE = new AccessHashCode();
    
    private AccessHashCode() {
        //nothing to do
    }
    
    public static AccessHashCode instance() {
        return INSTANCE;
    }
    
    @Override
    public String toString() {
        return "hashCode()";
    }
}
