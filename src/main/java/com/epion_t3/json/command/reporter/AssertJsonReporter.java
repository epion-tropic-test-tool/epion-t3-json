/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.json.command.reporter;

import com.epion_t3.core.command.reporter.impl.AbstractThymeleafCommandReporter;
import com.epion_t3.core.common.bean.ExecuteCommand;
import com.epion_t3.core.common.bean.ExecuteFlow;
import com.epion_t3.core.common.bean.ExecuteScenario;
import com.epion_t3.core.common.context.ExecuteContext;
import com.epion_t3.json.bean.AssertJsonResult;
import com.epion_t3.json.command.model.AssertJson;

import java.util.Map;

/**
 * AssertJsonコマンドのカスタムレポートクラス.
 *
 * @author Nozomu.Takashima
 */
public class AssertJsonReporter extends AbstractThymeleafCommandReporter<AssertJson, AssertJsonResult> {

    /**
     * {@inheritDoc}}
     */
    @Override
    public String templatePath() {
        return "assert-json-report";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVariables(Map<String, Object> variable, AssertJson command, AssertJsonResult assertCommandResult,
            ExecuteContext executeContext, ExecuteScenario executeScenario, ExecuteFlow executeFlow,
            ExecuteCommand executeCommand) {

        // Expectedの格納
        variable.put("expected", assertCommandResult.getExpected());

        // Actualの格納
        variable.put("actual", assertCommandResult.getActual());

        // Json比較対象外リストの格納
        variable.put("ignores", command.getIgnores());

        // Assertion結果の詳細情報の格納
        variable.put("assertDetails", assertCommandResult.getJsonDiffList());
    }

}
