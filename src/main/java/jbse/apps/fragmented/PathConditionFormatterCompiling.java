package jbse.apps.fragmented;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import jbse.apps.Formatter;
import jbse.mem.Clause;
import jbse.mem.ClauseAssume;
import jbse.mem.ClauseAssumeReferenceSymbolic;
import jbse.mem.State;
import jbse.mem.fragmented.PathConditionFragmented;
import jbse.val.MemoryPath;
import jbse.val.Primitive;
import jbse.val.ReferenceSymbolic;
import jbse.val.Value;

public class PathConditionFormatterCompiling implements Formatter {

	protected String output = "";
	protected static HashSet<String> symbolsInPathCondition = new HashSet<String>();

	@Override
	public void cleanup() {
		this.output = "";
	}

	@Override
	public String emit() {
		return this.output;
	}

	public void formatPathConditionFragmented(PathConditionFragmented pcf, Map<String, SymbolInformation> symbols) {
		this.output = formatPathConditionFragmentedCondition(pcf, symbols);
	}

	public void formatSymbols(Map<String, SymbolInformation> symbols) {
		String symbolsWithType = "";

		Iterator<String> iter = symbolsInPathCondition.iterator();

		while (iter.hasNext()) {
			String iterSymbol = iter.next();
			for (Entry<String, SymbolInformation> entry : symbols.entrySet()) {
				SymbolInformation value = entry.getValue();
				if (value.getId().equals(iterSymbol)) {
					symbolsWithType += value.getId();
					if (iter.hasNext()) {
						symbolsWithType += ", ";
					}
				}
			}
		}
		symbolsInPathCondition.clear();
		this.output = symbolsWithType;
	}

	public void formatSymbolsWithType(Map<String, SymbolInformation> symbols) {
		String symbolsWithType = "";

		Iterator<String> iter = symbolsInPathCondition.iterator();

		while (iter.hasNext()) {
			String iterSymbol = iter.next();
			for (Entry<String, SymbolInformation> entry : symbols.entrySet()) {
				SymbolInformation value = entry.getValue();
				if (value.getId().equals(iterSymbol)) {
					symbolsWithType += value.getTypeAsObject() + " " + value.getId();
					if (iter.hasNext()) {
						symbolsWithType += ", ";
					}
				}
			}
		}
		this.output = symbolsWithType;
	}

	public void formatCheckForNullSymbols() {
		String checkForNullSymbols = "";

		Iterator<String> iter = symbolsInPathCondition.iterator();

		while (iter.hasNext()) {
			checkForNullSymbols += iter.next() + " == null";
			if (iter.hasNext()) {
				checkForNullSymbols += " || ";
			}
		}
		symbolsInPathCondition.clear();

		this.output = checkForNullSymbols;
	}

	private static String formatPathConditionFragmentedCondition(PathConditionFragmented pcf,
			Map<String, SymbolInformation> symbols) {
		String expression = "";
		boolean doneFirstExpression = false;

		State s = pcf.getState();
		for (Clause c : pcf.getClausesOfPathCondition()) {

			expression += (doneFirstExpression ? (" && ") : "");
			doneFirstExpression = true;
			if (c instanceof ClauseAssume) {
				final Primitive cond = ((ClauseAssume) c).getCondition();
				expression += formatValue(s, cond, symbols);
			} else if (c instanceof ClauseAssumeReferenceSymbolic) {
				final ReferenceSymbolic ref = ((ClauseAssumeReferenceSymbolic) c).getReference();
				expression += ref.toString() + " == ";
				if (s.isNull(ref)) {
					expression += "null";
				} else {
					final MemoryPath tgtOrigin = s.getObject(ref).getOrigin();
					expression += "Object[" + s.getResolution(ref) + "] ("
							+ (ref.getOrigin().equals(tgtOrigin) ? "fresh" : ("aliases " + tgtOrigin)) + ")";
				}
			} else { // (c instanceof ClauseAssumeClassInitialized) || (c instanceof
						// ClauseAssumeClassNotInitialized)
				expression += c.toString();
			}
		}
		return (expression.equals("") ? "" : expression);
	}

	/**
	 * This transforms the JBSE object to String
	 * 
	 * @param s
	 * @param val
	 * @param symbols
	 * @return
	 */
	private static String formatValue(State s, Value val, Map<String, SymbolInformation> symbols) {
		String tmp = formatSymbols(val.originFragmented(), symbols);

		if (val instanceof ReferenceSymbolic) {
			ReferenceSymbolic ref = (ReferenceSymbolic) val;
			if (s.resolved(ref)) {
				if (s.isNull(ref)) {
					tmp += " == null";
				} else {
					tmp += " == Object[" + s.getResolution(ref) + "]";
				}
			}
		}
		return tmp;
	}

	/**
	 * Replace the original variable name for the one stored in the symbol map
	 * 
	 * @param originFragmented
	 * @param symbols
	 * @return
	 */
	private static String formatSymbols(String originFragmented, Map<String, SymbolInformation> symbols) {
		for (Entry<String, SymbolInformation> entry : symbols.entrySet()) {
			SymbolInformation value = entry.getValue();
			if (originFragmented.contains(value.getName())) {
				symbolsInPathCondition.add(value.getId());
				originFragmented = originFragmented.replace(value.getName(), value.getId());
			}
		}
		return originFragmented;
	}

	@Override
	public void formatState(State s) {
		// TODO Auto-generated method stub
		
	}

}
