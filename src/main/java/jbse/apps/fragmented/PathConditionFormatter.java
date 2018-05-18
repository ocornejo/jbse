package jbse.apps.fragmented;

import static jbse.apps.Util.LINE_SEP;

import java.util.HashSet;

import jbse.apps.Formatter;
import jbse.mem.Clause;
import jbse.mem.ClauseAssume;
import jbse.mem.ClauseAssumeReferenceSymbolic;
import jbse.mem.State;
import jbse.mem.fragmented.PathConditionFragmented;
import jbse.val.Expression;
import jbse.val.FunctionApplication;
import jbse.val.MemoryPath;
import jbse.val.NarrowingConversion;
import jbse.val.Primitive;
import jbse.val.PrimitiveSymbolic;
import jbse.val.ReferenceSymbolic;
import jbse.val.Value;
import jbse.val.WideningConversion;

public class PathConditionFormatter implements Formatter{
        
    protected String output = "";
    
    @Override   
    public void cleanup() {
        this.output = "";
    }
    
    @Override 
    public String emit() {
        return this.output;
    }

    public void formatPathConditionFragmented(PathConditionFragmented pcf) {
        this.output = formatPathConditionFragmented(pcf,true, "\t", "") + LINE_SEP;
    }
    
    private static String formatPathConditionFragmented(PathConditionFragmented pcf,boolean breakLines, String indentTxt, String indentCurrent){
        final String lineSep = (breakLines ? LINE_SEP : "");
        String expression = "";
        String where = "";
        boolean doneFirstExpression = false;
        boolean doneFirstWhere = false;
        HashSet<String> doneSymbols = new HashSet<String>();
        State s = pcf.getState();
        
        for (Clause c : pcf.getClausesOfPathCondition()) {
            expression += (doneFirstExpression ? (" &&" + lineSep) : "") + indentCurrent;
            doneFirstExpression = true;
            
            if (c instanceof ClauseAssume) {
                final Primitive cond = ((ClauseAssume) c).getCondition();
                expression += formatValue(s, cond);
                final String expressionFormatted = formatPrimitiveForPathCondition(cond, breakLines, indentTxt, indentCurrent, doneSymbols);
                if (expressionFormatted.equals("")) {
                    //does nothing
                } else {
                    where += (doneFirstWhere ? (" &&" + lineSep) : "") + indentCurrent + expressionFormatted;
                    doneFirstWhere = true;
                }
            } else if (c instanceof ClauseAssumeReferenceSymbolic) {
                final ReferenceSymbolic ref = ((ClauseAssumeReferenceSymbolic) c).getReference(); 
                expression += ref.toString() + " == ";
                if (s.isNull(ref)) {
                    expression += "null";
                } else {
                    final MemoryPath tgtOrigin = s.getObject(ref).getOrigin();
                    expression += "Object[" + s.getResolution(ref) + "] (" + (ref.getOrigin().equals(tgtOrigin) ? "fresh" : ("aliases " + tgtOrigin)) + ")";
                }
                final String referenceFormatted = formatReferenceForPathCondition(ref, doneSymbols); 
                if (referenceFormatted.equals("")) {
                    //does nothing
                } else {
                    where += (doneFirstWhere ? (" &&" + lineSep ) : "") + indentCurrent + referenceFormatted;
                    doneFirstWhere = true;
                }
            } else { //(c instanceof ClauseAssumeClassInitialized) || (c instanceof ClauseAssumeClassNotInitialized)
                expression += c.toString();
            }
        
        }
        return (expression.equals("") ? "" : (lineSep + expression)) + (where.equals("") ? "" : (lineSep + indentCurrent + "where:" + lineSep  + where));
    }
    
    private static String formatReferenceForPathCondition(ReferenceSymbolic r, HashSet<String> done) {
        if (done.contains(r.toString())) {
            return "";
        } else {
            return r.toString() + " == " + r.getOrigin();
        }
    }

    private static String formatValue(State s, Value val) {
        String tmp = val.toString();
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
    
    private static String formatExpressionForPathCondition(Expression e, boolean breakLines, String indentTxt, String indentCurrent, HashSet<String> done) {
        final Primitive firstOp = e.getFirstOperand();
        final Primitive secondOp = e.getSecondOperand();
        String retVal = "";
        if (firstOp != null) {
            retVal = formatPrimitiveForPathCondition(firstOp, breakLines, indentTxt, indentCurrent, done);
        }
        final String secondOpFormatted = formatPrimitiveForPathCondition(secondOp, breakLines, indentTxt, indentCurrent, done);
        if (retVal.equals("") || secondOpFormatted.equals("")) {
            //does nothing
        } else {
            final String lineSep = (breakLines ? LINE_SEP : "");
            retVal += " &&" + lineSep + indentCurrent;
        }
        retVal += secondOpFormatted;
        return retVal;
    }
    
    
    private static String formatPrimitiveForPathCondition(Primitive p, boolean breakLines, String indentTxt, String indentCurrent, HashSet<String> done) {
        if (p instanceof Expression) {
            return formatExpressionForPathCondition((Expression) p, breakLines, indentTxt, indentCurrent, done);
        } else if (p instanceof PrimitiveSymbolic) {
            if (done.contains(p.toString())) {
                return "";
            } else {
                done.add(p.toString());
                return p.toString() + " == " + ((PrimitiveSymbolic) p).getOrigin();
            }
        } else if (p instanceof FunctionApplication) {
            return formatFunctionApplicationForPathCondition((FunctionApplication) p, breakLines, indentTxt, indentCurrent, done);
        } else if (p instanceof WideningConversion) {
            final WideningConversion pWiden = (WideningConversion) p;
            return formatPrimitiveForPathCondition(pWiden.getArg(), breakLines, indentTxt, indentCurrent, done);
        } else if (p instanceof NarrowingConversion) {
            final NarrowingConversion pNarrow = (NarrowingConversion) p;
            return formatPrimitiveForPathCondition(pNarrow.getArg(), breakLines, indentTxt, indentCurrent, done);
        } else { //(p instanceof Any || p instanceof Simplex)
            return "";
        }
    }
    
    private static String formatFunctionApplicationForPathCondition(FunctionApplication a, boolean breakLines, String indentTxt, String indentCurrent, HashSet<String> done) {
        String retVal = "";
        boolean first = true;
        for (Primitive p : a.getArgs()) {
            String argFormatted = formatPrimitiveForPathCondition(p, breakLines, indentTxt, indentCurrent, done);
            if (argFormatted.equals("")) {
                //does nothing
            } else { 
                final String lineSep = (breakLines ? LINE_SEP : "");
                retVal += (first ? "" : " &&" + lineSep + indentCurrent) + argFormatted;
                first = false;
            }
        }
        return retVal;
    }

	@Override
	public void formatState(State s) {
		// TODO Auto-generated method stub
		
	}    

}
