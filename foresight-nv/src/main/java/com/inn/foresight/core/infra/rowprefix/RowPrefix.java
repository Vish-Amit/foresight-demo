package com.inn.foresight.core.infra.rowprefix;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class RowPrefix {

    private Integer prefix;

    public RowPrefix(Integer prefix) {
        super();
        this.prefix = prefix;
    }

    public String prefix() {
        return HBaseRowPrefixUtils.getAlphaNumericPrefix(prefix);
    }

}