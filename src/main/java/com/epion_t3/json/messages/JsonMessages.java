/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.json.messages;

import com.epion_t3.core.message.Messages;

/**
 * json用メッセージ定義Enum.<br>
 *
 * @author epion-t3-devtools
 */
public enum JsonMessages implements Messages {

    /** JSONの比較に失敗しました。 */
    JSON_ERR_9005("com.epion_t3.json.err.9005"),

    /** JSONは一致しません. */
    JSON_ERR_9004("com.epion_t3.json.err.9004"),

    /** 結果値のJSONが見つかりません. パス:{0}, FlowId:{1} */
    JSON_ERR_9003("com.epion_t3.json.err.9003"),

    /** 期待値のJSONが見つかりません. パス:{0} */
    JSON_ERR_9002("com.epion_t3.json.err.9002"),

    /** JSONは一致します. */
    JSON_INF_1001("com.epion_t3.json.inf.1001"),

    /** JSON解析に失敗しました. */
    JSON_ERR_9001("com.epion_t3.json.err.9001");

    /** メッセージコード */
    private final String messageCode;

    /**
     * プライベートコンストラクタ<br>
     *
     * @param messageCode メッセージコード
     */
    private JsonMessages(final String messageCode) {
        this.messageCode = messageCode;
    }

    /**
     * messageCodeを取得します.<br>
     *
     * @return messageCode
     */
    public String getMessageCode() {
        return this.messageCode;
    }
}
