package jbse.apps.fragmented;

import static jbse.apps.Util.LINE_SEP;

import jbse.apps.Formatter;
import jbse.mem.State;

public class RuleFormatter implements Formatter {

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
        String lineSep = (lastOne ? LINE_SEP : ("," + LINE_SEP));
        this.output = "instanceof "+ klass + " expands to instanceof " + subklass + lineSep; 
    }

	@Override
	public void formatState(State s) {
		// TODO Auto-generated method stub

	}

}
