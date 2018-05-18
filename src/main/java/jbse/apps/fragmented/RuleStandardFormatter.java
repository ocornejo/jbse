package jbse.apps.fragmented;

import static jbse.apps.Util.LINE_SEP;

import jbse.apps.Formatter;
import jbse.mem.State;

public class RuleStandardFormatter implements Formatter {

	protected String output = "";

	@Override
	public void cleanup() {
		this.output = "";
	}

	@Override
	public String emit() {
		return this.output;
	}

	public void formatRule(String klass, String subklass, boolean lastOne) {
		this.output = klass + ";" + subklass + LINE_SEP;
	}

	@Override
	public void formatState(State s) {
		// TODO Auto-generated method stub
		
	}

}
