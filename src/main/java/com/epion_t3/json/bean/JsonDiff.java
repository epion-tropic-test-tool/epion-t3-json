/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.json.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonDiff {

    /**
     * Jsonパス(ドット表記).
     */
    private String pathName;

    /**
     * 期待値
     */
    private Object expected;

    /**
     * 対象値
     */
    private Object actual;

    /**
     * アサート結果
     */
    private boolean success;

    /**
     * ignore状態
     */
    private boolean ignore;
}
