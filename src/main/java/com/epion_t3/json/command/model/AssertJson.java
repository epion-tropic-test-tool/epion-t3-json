/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.json.command.model;

import com.epion_t3.core.common.annotation.CommandDefinition;
import com.epion_t3.core.common.bean.scenario.Command;
import com.epion_t3.json.command.reporter.AssertJsonReporter;
import com.epion_t3.json.command.runner.AssertJsonRunner;
import lombok.Getter;
import lombok.Setter;
import org.apache.bval.constraints.NotEmpty;

import java.util.List;

@Getter
@Setter
@CommandDefinition(id = "AssertJson", runner = AssertJsonRunner.class, reporter = AssertJsonReporter.class, assertCommand = true)
public class AssertJson extends Command {

    /**
     * 期待値JSONのパス.
     */
    @NotEmpty
    private String expectedPath;

    /**
     * 結果値を取得したFlowID.
     */
    @NotEmpty
    private String actualFlowId;

    /**
     * 比較対象外リスト.
     */
    private List<String> ignores;

}
