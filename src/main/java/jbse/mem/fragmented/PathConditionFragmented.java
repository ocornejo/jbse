package jbse.mem.fragmented;

import java.io.Serializable;
import java.util.List;

import jbse.mem.Clause;
import jbse.mem.PathCondition;
import jbse.mem.State;

public class PathConditionFragmented implements Serializable {

	private static final long serialVersionUID = 1L;
	private PathCondition pathCondition;
    private State state;
    private String method;
    private int count;
    private int hashCode;
    
    public int getHashCode() {
    	return hashCode;
    }
    
    public String getMethod() {
		return method;
	}

	public int getCount() {
		return count;
	}
	
	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}
	
	public void setMethod(String method) {
    	this.method = method;
    }
    
    public void setCount(int count) {
    	this.count = count;
    }
    
	@Override
	public String toString() {
		return "PathConditionFragmented [method=" + method + ", count=" + count + "]";
	}

	public PathConditionFragmented(State state){
        this.pathCondition = new PathCondition();
        this.state = state;
    }     
    
    public State getState() {
        return this.state;
    }
    
    public List<Clause> getClausesOfPathCondition(){
        return this.pathCondition.getClauses();
    }
    
    public String getPathConditionAsString() {
       return this.pathCondition.toString();
    }
    
    public void addClauseToPathCondition(Clause c) {
        this.pathCondition.addClause(c);
    }
    
    public boolean isEmpty(){
        return pathCondition.isEmpty();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.pathCondition == null)
        ? 0
        : this.pathCondition.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
            return true;
        if (obj == null) 
            return false;
        if (getClass() != obj.getClass()) 
            return false;
        PathConditionFragmented other = (PathConditionFragmented) obj;
       
        if (this.pathCondition == null) {
            if (other.pathCondition != null) 
                return false;
        } else if (!this.pathCondition.equals(other.pathCondition))
            return false;
        return true;
    }

}
