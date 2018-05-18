package jbse.apps.fragmented;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jbse.apps.exc.CouldNotFindTypeException;
import jbse.mem.Array;
import jbse.mem.Clause;
import jbse.mem.ClauseAssume;
import jbse.mem.ClauseAssumeReferenceSymbolic;
import jbse.mem.Klass;
import jbse.mem.Objekt;
import jbse.mem.State;
import jbse.mem.Variable;
import jbse.mem.fragmented.PathConditionFragmented;
import jbse.val.Expression;
import jbse.val.FunctionApplication;
import jbse.val.NarrowingConversion;
import jbse.val.Primitive;
import jbse.val.PrimitiveSymbolic;
import jbse.val.ReferenceSymbolic;
import jbse.val.WideningConversion;

public class SymbolsMapGenerator {

	private HashMap<String, SymbolInformation> symbolsMap = new HashMap<String, SymbolInformation>();

	public HashMap<String, SymbolInformation> getSymbolsMap(Collection<PathConditionFragmented> pathConditions) {

		for (PathConditionFragmented pcf : pathConditions) {
			obtainSymbolsForPathCondition(pcf);
		}
		return symbolsMap;
	}

	private void obtainSymbolsForPathCondition(PathConditionFragmented pcf) {
		HashSet<String> doneSymbols = new HashSet<String>();
		State s = pcf.getState();

		for (Clause c : pcf.getClausesOfPathCondition()) {
			if (c instanceof ClauseAssume) {

				Primitive cond = ((ClauseAssume) c).getCondition();
				formatPrimitiveForPathCondition(s, cond, doneSymbols);

			} else if (c instanceof ClauseAssumeReferenceSymbolic) {
				final ReferenceSymbolic ref = ((ClauseAssumeReferenceSymbolic) c).getReference();
				formatReferenceForPathCondition(ref, doneSymbols);

			} else { // (c instanceof ClauseAssumeClassInitialized) || (c instanceof
				// ClauseAssumeClassNotInitialized)

			}
		}
	}

	private void formatReferenceForPathCondition(ReferenceSymbolic r, HashSet<String> done) {
	}

	private void formatExpressionForPathCondition(State s, Expression e, HashSet<String> done) {
		Primitive firstOp = e.getFirstOperand();
		Primitive secondOp = e.getSecondOperand();

		if (firstOp != null)
			formatPrimitiveForPathCondition(s, firstOp, done);

		formatPrimitiveForPathCondition(s, secondOp, done);
	}

	private void formatPrimitiveForPathCondition(State s, Primitive p, HashSet<String> done) {
		if (p instanceof Expression) {
			formatExpressionForPathCondition(s, (Expression) p, done);
		} else if (p instanceof PrimitiveSymbolic) {
			if (done.contains(p.toString())) {
			} else {
				done.add(p.toString());
				String name = ((PrimitiveSymbolic) p).getOrigin().originFragmented();

				if (!symbolsMap.containsKey(name)) {

					try {
						int symbolMapSize = symbolsMap.size() + 1;
						String type;
						type = getType(s, p);
						String id = "V" + Integer.toString(symbolMapSize);
						SymbolInformation si = new SymbolInformation(id, name, type);
						symbolsMap.put(name, si);
					} catch (CouldNotFindTypeException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (p instanceof FunctionApplication) {
			formatFunctionApplicationForPathCondition(s, (FunctionApplication) p, done);
		} else if (p instanceof WideningConversion) {
			final WideningConversion pWiden = (WideningConversion) p;
			formatPrimitiveForPathCondition(s, pWiden.getArg(), done);
		} else if (p instanceof NarrowingConversion) {
			final NarrowingConversion pNarrow = (NarrowingConversion) p;
			formatPrimitiveForPathCondition(s, pNarrow.getArg(), done);
		} else {
			// (p instanceof Any || p instanceof Simplex)
		}
	}

	private String getType(State s, Primitive p) throws CouldNotFindTypeException {
		String pOrigin = ((PrimitiveSymbolic) p).getOrigin().toString();
		try {
			if (pOrigin.startsWith("[")) {
				return getStaticType(s, pOrigin);
			} else {
				return getObjectType(s, pOrigin);
			}
		} catch (CouldNotFindTypeException e) {
			return getObjectType(s, pOrigin);
		}
	}

	private String getStaticType(State s, String pOrigin) throws CouldNotFindTypeException {
		for (Entry<String, Klass> entryStatic : s.getStaticMethodArea().entrySet()) {
			Klass staticObject = entryStatic.getValue();
			if (staticObject.getOrigin() == null)
				continue;

			String valueOrigin = staticObject.getOrigin().toString();

			String root = "";
			Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(valueOrigin);

			while (m.find())
				root = m.group(1);

			if (!root.equals("") && pOrigin.contains(root)) {
				String[] path = pOrigin.split("\\.");

				for (Entry<String, Variable> entryFields : staticObject.fields().entrySet()) {
					String fieldName = entryFields.getValue().getName();
					if (fieldName.equals(path[path.length - 1])) {
						return entryFields.getValue().getType();
					}
				}
			}
		}
		throw new CouldNotFindTypeException(pOrigin);
	}

	private String getObjectType(State s, String pOrigin) throws CouldNotFindTypeException {

		pOrigin = pOrigin.replace("{ROOT}:this", "");

		if (pOrigin.contains("hashCode()")) {
			return "I";
		}

		for (Entry<Long, Objekt> entryHeap : s.getHeap().entrySet()) {
			Objekt heapObject = entryHeap.getValue();

			String[] path = pOrigin.split("\\.");

			for (Entry<String, Variable> entryFields : heapObject.fields().entrySet()) {
				String fieldName = entryFields.getValue().getName();
				if (fieldName.equals(path[path.length - 1])) {
					return entryFields.getValue().getType();
				}
			}
		}

		String type = "";
		int length = 0;

		// check for array instances
		for (Entry<Long, Objekt> entryHeap : s.getHeap().entrySet()) {
			Objekt heapObject = entryHeap.getValue();
			if (heapObject.getOrigin() == null)
				continue;
			String valueOrigin = heapObject.getOrigin().toString().replace("{ROOT}:this", "");

			if (heapObject instanceof Array && pOrigin.contains(valueOrigin)) {
				if (valueOrigin.length() > length) {
					type = heapObject.getType();
					length = valueOrigin.length();
				}
			}
		}
		if (type.isEmpty())
			throw new CouldNotFindTypeException(pOrigin);

		return type;

	}

	private void formatFunctionApplicationForPathCondition(State s, FunctionApplication a, HashSet<String> done) {
		for (Primitive p : a.getArgs()) {
			formatPrimitiveForPathCondition(s, p, done);
		}
	}
}
