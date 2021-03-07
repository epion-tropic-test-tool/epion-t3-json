/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.json.bean;

import com.epion_t3.core.command.bean.AssertCommandResult;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssertJsonResult extends AssertCommandResult {

    /**
     * default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Json差分の結果.
     */
    private List<JsonDiff> jsonDiffList;

    /**
     * 期待値文字列.
     */
    private String expectedString;

    /**
     * 結果値文字列.
     */
    private String actualString;

}
