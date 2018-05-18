package jbse.mem.fragmented;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;

import jbse.mem.Clause;
import jbse.mem.ClauseAssume;
import jbse.mem.State;
import jbse.val.Access;
import jbse.val.AccessArrayMember;
import jbse.val.AccessLocalVariable;
import jbse.val.Expression;
import jbse.val.NarrowingConversion;
import jbse.val.Primitive;
import jbse.val.PrimitiveSymbolic;
import jbse.val.WideningConversion;

/**
 * Class that filters the clauses from the state
 * 
 * @author cornejo
 *
 */
public class ClauseFilter {

	private PathConditionFragmented pcf;
	protected EnumSet<FilterClauseEnum> filterClauseEnum =

			EnumSet.of(FilterClauseEnum.CLAUSE_ASSUME_EXPANDS, FilterClauseEnum.CLASS_INITIALIZED,
					FilterClauseEnum.CLASS_NOT_INITIALIZED);

	/*
	 * Filters not yet used FilterClauseEnum.CLAUSE_ASSUME_NULL,
	 * FilterClauseEnum.CLASS_ASSUME_REFERENCE_SYMBOLIC,
	 * FilterClauseEnum.CLASS_ASSUME_ALIASES);
	 */

	public ClauseFilter(State state) {
		this.pcf = new PathConditionFragmented(state);
	}

	private boolean checkAccessesOfPrimitiveSymbolic(PrimitiveSymbolic ps, HashSet<String> doneSymbols) {
		Iterator<Access> accessIterator = ps.getOrigin().iterator();
		// System.out.println("checkAccessesOfPrimitiveSymbolic: "+
		// ps.getOrigin().toString());
		while (accessIterator.hasNext()) {
			Access access = accessIterator.next();
			// System.out.println("Access: " + access.toString());

			// there is no filter for AccessStatic
			if (access instanceof AccessLocalVariable) {
				if (!containThisVariable((AccessLocalVariable) access)) {
					// System.out.println("Access with origin not allowed " + access.toString());
					return false;
				}
			} else if (access instanceof AccessArrayMember) {
				return checkArrayMember((AccessArrayMember) access, doneSymbols);
			}
		}
		// System.out.println("Primitive symbolic, with origin accepted: "+
		// ps.getOrigin().toString());
		return true;
	}

	public boolean checkArrayMember(AccessArrayMember access, HashSet<String> doneSymbols) {
		Primitive p = access.index();
		if (isClauseAccepted(p, doneSymbols))
			return true;
		return false;
	}

	public boolean containThisVariable(AccessLocalVariable alv) {
		if (alv.variableName().contains("this"))
			return true;
		return false;
	}

	public PathConditionFragmented filterClausesfromState() {

		for (Clause c : pcf.getState().getPathCondition()) {
			HashSet<String> doneSymbols = new HashSet<String>();

			// check if the clause should be filtered or not by its class
			if (isInClauseEnum(c.getClass())) {
				// clauses filtered by the enum
			} else if (c instanceof ClauseAssume) {
				Primitive p = ((ClauseAssume) c).getCondition();
				// System.out.println("P: "+ p.toString());

				if (isClauseAccepted(p, doneSymbols)) {
					pcf.addClauseToPathCondition(c);
				} else {
					// Clause NOT accepted
				}
			} else {
				// (c instanceof ClauseAssumeClassInitialized) || (c instanceof
				// ClauseAssumeClassNotInitialized)
			}
		}

		return !pcf.isEmpty() ? pcf : null;
	}

	private boolean isClauseAccepted(Primitive p, HashSet<String> doneSymbols) {

		if (p instanceof Expression) {
			return isExpressionAcepted((Expression) p, doneSymbols);
		} else if (p instanceof PrimitiveSymbolic) {
			if (!doneSymbols.contains(p.toString())) {
				// symbol not analyzed yet
				doneSymbols.add(p.toString());
				PrimitiveSymbolic ps = ((PrimitiveSymbolic) p);
				return checkAccessesOfPrimitiveSymbolic(ps, doneSymbols);
			} else {
				// symbol already analyzed
				return true;
			}
		} else if (p instanceof WideningConversion) {
			final WideningConversion pWiden = (WideningConversion) p;
			return isClauseAccepted(pWiden.getArg(), doneSymbols);
		} else if (p instanceof NarrowingConversion) {
			final NarrowingConversion pNarrow = (NarrowingConversion) p;
			return isClauseAccepted(pNarrow.getArg(), doneSymbols);
		} else { // (p instanceof Any || p instanceof Simplex)
			return true;
		}
	}

	/**
	 * 
	 * @param e
	 *            Expression to be analyzed
	 * @param doneSymbols
	 *            Symbols already checked
	 * @return
	 */
	private boolean isExpressionAcepted(Expression e, HashSet<String> doneSymbols) {
		final Primitive firstOp = e.getFirstOperand();
		final Primitive secondOp = e.getSecondOperand();

		boolean firstOpAccepted = false;
		boolean secondOpAccepted = false;

		if (firstOp != null) {
			firstOpAccepted = isClauseAccepted(firstOp, doneSymbols);
		}

		if (secondOp != null) {
			secondOpAccepted = isClauseAccepted(secondOp, doneSymbols);
		}

		return firstOpAccepted && secondOpAccepted;
	}

	private boolean isInClauseEnum(Class<?> cls) {
		if (this.filterClauseEnum.stream().filter(it -> cls == it.getCls()).findAny().orElse(null) == null) {
			return false;
		}
		return true;
	}

}
