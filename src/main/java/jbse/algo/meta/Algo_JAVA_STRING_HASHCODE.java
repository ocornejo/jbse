package jbse.algo.meta;

import static jbse.algo.Util.exitFromAlgorithm;
import static jbse.algo.Util.throwVerifyError;

import java.util.function.Supplier;

import jbse.algo.Algo_INVOKEMETA_Nonbranching;
import jbse.algo.InterruptException;
import jbse.mem.Objekt;
import jbse.mem.State;
import jbse.mem.exc.ThreadStackEmptyException;
import jbse.val.Primitive;
import jbse.val.Reference;

/**
 * Meta-level implementation of {@link java.lang.String#hashCode()}.
 * 
 * @author Pietro Braione
 */
public final class Algo_JAVA_STRING_HASHCODE extends Algo_INVOKEMETA_Nonbranching {
    @Override
    protected Supplier<Integer> numOperands() {
        return () -> 1;
    }

    private Primitive hash; //set by cookMore

    @Override
    protected void cookMore(State state) throws ThreadStackEmptyException, InterruptException {
        try {
            final Reference thisReference = (Reference) this.data.operand(0);
            final Objekt thisObject = state.getObject(thisReference);
            if (thisObject.isSymbolic()) {
                //here the only sensible thing that we can do is to return the default hash code
                this.hash = thisObject.getObjektDefaultHashCode();
                //TODO possibly refine the state to ensure hash code semantics for strings based on potential equality
            } else {
                continueWithBaseLevelImpl(state); //executes the original String.hashCode implementation
            }
        } catch (ClassCastException e) {
            throwVerifyError(state);
            exitFromAlgorithm();
        }
    }

    @Override
    protected void update(State state) throws ThreadStackEmptyException {
        state.pushOperand(this.hash);
    }
}