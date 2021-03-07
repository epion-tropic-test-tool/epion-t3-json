/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.json.command.model;

import com.epion_t3.core.common.annotation.CommandDefinition;
import com.epion_t3.core.common.bean.scenario.Command;
import com.epion_t3.json.command.runner.ExtractValue4JsonFileEvidenceRunner;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CommandDefinition(id = "ExtractValue4JsonFileEvidence", runner = ExtractValue4JsonFileEvidenceRunner.class)
public class ExtractValue4JsonFileEvidence extends Command {
    /**
     * 抽出パス.
     */
    private String path;
    /**
     * 対象とするFlowID.
     */
    private String targetFlowId;
}
