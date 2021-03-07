/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.json.command.runner;

import com.epion_t3.core.command.bean.CommandResult;
import com.epion_t3.core.command.runner.impl.AbstractCommandRunner;
import com.epion_t3.json.command.model.ExtractValue4JsonFileEvidence;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;

/**
 *
 */
public class ExtractValue4JsonFileEvidenceRunner extends AbstractCommandRunner<ExtractValue4JsonFileEvidence> {
    @Override
    public CommandResult execute(ExtractValue4JsonFileEvidence command, Logger logger) throws Exception {
        var evidenceFilePath = referFileEvidence(command.getTargetFlowId());
        var ctx = JsonPath.parse(evidenceFilePath.toFile());
        var extractValue = ctx.read(command.getPath());
        logger.info(collectLoggingMarker(), "extract value : {}", extractValue);
        setVariable(command.getTarget(), extractValue);
        return CommandResult.getSuccess();
    }
}
