package com.inn.foresight.core.infra.rowprefix;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class RowPrefixRange {

    private Integer min;

    private Integer max;

    public RowPrefixRange(Integer min, Integer max) {
        super();
        this.min = min;
        this.max = max;
    }

    public String startRow() {
        return HBaseRowPrefixUtils.getAlphaNumericPrefix(min);
    }

    public String endRow() {
        return HBaseRowPrefixUtils.getAlphaNumericPrefix(max + 1);
    }
    
}