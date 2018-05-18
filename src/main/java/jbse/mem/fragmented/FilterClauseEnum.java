package jbse.mem.fragmented;

import jbse.mem.ClauseAssumeNull;
import jbse.mem.ClauseAssumeExpands;
import jbse.mem.ClauseAssumeClassInitialized;
import jbse.mem.ClauseAssumeClassNotInitialized;
import jbse.mem.ClauseAssumeReferenceSymbolic;
import jbse.mem.ClauseAssumeAliases;

public enum FilterClauseEnum{
    CLAUSE_ASSUME_NULL(ClauseAssumeNull.class), // null
    CLAUSE_ASSUME_EXPANDS(ClauseAssumeExpands.class), // fresh
    CLASS_INITIALIZED(ClauseAssumeClassInitialized.class), // pre_init
    CLASS_NOT_INITIALIZED(ClauseAssumeClassNotInitialized.class), // not pre_init
    CLASS_ASSUME_REFERENCE_SYMBOLIC(ClauseAssumeReferenceSymbolic.class),
    CLASS_ASSUME_ALIASES(ClauseAssumeAliases.class);
    
                                                                 
    private Class<?> cls;

    private FilterClauseEnum(Class<?> cls) {
        this.cls = cls;
    }

    public Class<?> getCls() {
        return cls;
    }
    

}