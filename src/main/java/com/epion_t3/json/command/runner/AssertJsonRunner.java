/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.json.command.runner;

import com.epion_t3.core.command.bean.CommandResult;
import com.epion_t3.core.command.runner.impl.AbstractCommandRunner;
import com.epion_t3.core.common.type.AssertStatus;
import com.epion_t3.core.exception.SystemException;
import com.epion_t3.core.message.MessageManager;
import com.epion_t3.json.bean.AssertJsonResult;
import com.epion_t3.json.bean.JsonDiff;
import com.epion_t3.json.command.model.AssertJson;
import com.epion_t3.json.helper.CreateJsonDiffListHelper;
import com.epion_t3.json.messages.JsonMessages;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONParser;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssertJsonRunner extends AbstractCommandRunner<AssertJson> {

    @Override
    public CommandResult execute(AssertJson command, Logger logger) throws Exception {

        // リザルトオブジェクトの生成
        var commandResult = new AssertJsonResult();

        // 期待値のパスを取得
        var expectedPath = Paths.get(getCommandBelongScenarioDirectory(), command.getExpectedPath());
        // 期待値の配置パスが存在しなかった場合はエラー
        if (Files.notExists(expectedPath)) {
            throw new SystemException(JsonMessages.JSON_ERR_9002, expectedPath.toString());
        }
        // ファイル内容を読み込み
        var expected = Files.readString(expectedPath);
        if (StringUtils.isNotEmpty(expected)) {
            commandResult.setExpectedString(expected);
        }

        // 結果値のパスを取得（エビデンスより）
        var actualPath = referFileEvidence(command.getActualFlowId());
        // 結果値のパスが存在しなかった場合はエラー
        if (Files.notExists(actualPath)) {
            throw new SystemException(JsonMessages.JSON_ERR_9003, actualPath.toString());
        }
        // ファイル内容を読み込み
        var actual = Files.readString(actualPath);
        if (StringUtils.isNotEmpty(actual)) {
            commandResult.setActualString(actual);
        }

        try {
            // JSONObjectに変換(順序整形)
            JSONObject expectedJsonObject = new JSONObject("{ \"assertTarget\" : " + expected + "}");
            JSONObject actualJsonObject = new JSONObject("{ \"assertTarget\" : " + actual + "}");

            // 結果に格納する
            commandResult.setActual(actualJsonObject);
            commandResult.setExpected(expectedJsonObject);

            // Json比較対象外リストの作成
            List<Customization> ignoreList = new ArrayList<>();

            if (command.getIgnores() != null) {
                for (String ignore : command.getIgnores()) {
                    // ignore指定したものは判定を強制的にtrueにする
                    ignoreList.add(new Customization(ignore, (o1, o2) -> true));
                }
            }

            try {
                // ignoreがあるかないかで分岐
                if (!ignoreList.isEmpty()) {
                    Customization[] ignores = ignoreList.toArray(new Customization[ignoreList.size()]);
                    CustomComparator customComparator = new CustomComparator(JSONCompareMode.STRICT, ignores);

                    JSONAssert.assertEquals(expectedJsonObject.toString(), actualJsonObject.toString(),
                            customComparator);
                } else {
                    JSONAssert.assertEquals(expectedJsonObject.toString(), actualJsonObject.toString(),
                            JSONCompareMode.STRICT);
                }

                // 正常終了時
                commandResult.setAssertStatus(AssertStatus.OK);
                commandResult.setMessage(MessageManager.getInstance().getMessage(JsonMessages.JSON_INF_1001));
                commandResult.setJsonDiffList(
                        createJsonDiffList("", expectedJsonObject, actualJsonObject, command.getIgnores()));
            } catch (AssertionError e) {
                // アサートエラー発生時
                commandResult.setAssertStatus(AssertStatus.NG);
                commandResult.setMessage(MessageManager.getInstance().getMessage(JsonMessages.JSON_ERR_9004));
                commandResult.setJsonDiffList(
                        createJsonDiffList(e.getMessage(), expectedJsonObject, actualJsonObject, command.getIgnores()));
            }
        } catch (JSONException e) {
            // 実行中のエラー
            commandResult.setAssertStatus(AssertStatus.NG);
            commandResult.setMessage(MessageManager.getInstance().getMessage(JsonMessages.JSON_ERR_9005));
        }

        return commandResult;
    }

    /**
     * Jsonの比較結果を作成する.
     *
     * @param assertErrorMessage アサートエラーメッセージ
     * @param expected 期待値Jsonオブジェクト
     * @param actual 取得値Jsonオブジェクト
     * @param ignoreStrList ignoreの正規表現文字列一覧
     * @return Jsonの比較結果
     * @throws JSONException
     */
    private static List<JsonDiff> createJsonDiffList(String assertErrorMessage, JSONObject expected, JSONObject actual,
            List<String> ignoreStrList) throws JSONException {
        // エラーのマップ
        Map<String, JsonDiff> jsonDiffErrorMap = new HashMap<>();

        // AssertionErrorの出力からアサート失敗結果を取得する
        if (!assertErrorMessage.isEmpty()) {
            String[] diffMessageList = assertErrorMessage.split("; ");

            for (String diffMessage : diffMessageList) {
                // AssertionErrorのフォーマットをもとに文字列解析を行う
                List<String> diffInfoList = Arrays.asList(diffMessage.split("\r\n|\r|\n|: "));
                JsonDiff jsonDiff = new JsonDiff();
                if (diffInfoList.contains("Unexpected")) {
                    // Expectedになくて、Actualにある場合のエラー
                    if (diffInfoList.get(0).isEmpty()) {
                        // Jsonのルート階層の場合
                        jsonDiff.setPathName(diffInfoList.get(2));
                    } else {
                        jsonDiff.setPathName(diffInfoList.get(0) + "." + diffInfoList.get(2));
                    }
                    jsonDiff.setExpected("This path doesn't exist at the expected");
                    jsonDiff.setActual("");
                } else if (diffInfoList.contains("     but none found")) {
                    // Expectedにあって、Actualにない場合のエラー
                    if (diffInfoList.get(0).isEmpty()) {
                        // Jsonのルート階層の場合
                        jsonDiff.setPathName(diffInfoList.get(2));
                    } else {
                        jsonDiff.setPathName(diffInfoList.get(0) + "." + diffInfoList.get(2));
                    }
                    jsonDiff.setExpected("");
                    jsonDiff.setActual("This path doesn't exist at the actual");
                } else if (diffInfoList.contains("Expected") && diffInfoList.contains("     got")) {
                    // 値比較エラー
                    jsonDiff.setPathName(diffInfoList.get(0));
                    jsonDiff.setExpected(diffInfoList.get(2));
                    jsonDiff.setActual(diffInfoList.get(4));
                } else {
                    // 配列要素数不一致エラー
                    String[] listSizeErrorStr = diffInfoList.get(1).split(" ");
                    jsonDiff.setPathName(diffInfoList.get(0));
                    jsonDiff.setExpected("List size is " + listSizeErrorStr[1]);
                    jsonDiff.setActual("List size is " + listSizeErrorStr[5]);
                }

                // エラーマップに格納する
                jsonDiffErrorMap.put(jsonDiff.getPathName(), jsonDiff);
            }
        }

        // JSON比較結果作成インスタンスの生成
        CreateJsonDiffListHelper cjd = new CreateJsonDiffListHelper(jsonDiffErrorMap, ignoreStrList);
        // JSON比較結果を作成する
        cjd.compareJSON("", expected, actual);

        return cjd.getAllDiffList();
    }
}
