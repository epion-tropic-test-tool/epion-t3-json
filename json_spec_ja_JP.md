#  カスタム機能ドキュメント

このドキュメントは、 のカスタム機能が提供する、
Flow、コマンド、設定定義についての説明及び出力するメッセージの定義について説明する。

- Contents
  - [Information](#Information)
  - [Description](#Description)
  - [Flow List](#Flow-List)
  - [Command List](#Command-List)
  - [Configuration List](#Configuration-List)
  - [Message List](#Message-List)

## Information

本カスタム機能の基本情報は以下の通り。

JSONを扱う機能を提供します。

- Name : `json`
- Custom Package : `com.epion_t3.json`

## Description
JSONを扱う機能を提供します。

## Flow List

本カスタム機能が提供するFlowの一覧及び詳細。

|Name|Summary|
|:---|:---|


## Command List

本カスタム機能が提供するコマンドの一覧及び詳細。

|Name|Summary|Assert|Evidence|
|:---|:---|:---|:---|
|[AssertJson](#AssertJson)|JSONを比較します。  |X||
|[ExtractValue4JsonFileEvidence](#ExtractValue4JsonFileEvidence)|JSONから値を抽出して変数に設定します。  |||

------

### AssertJson
JSONを比較します。
#### Command Type
- Assert : __Yes__
- Evidence : No

#### Functions
- JSONを比較します。
- 比較したくない要素は、ignoreに指定が可能です。
- 比較したくない要素の指定方法は、 要素名をドットで連結したパスとしてください。

#### Structure
```yaml
commands : 
  id : コマンドのID
  command : 「AssertJson」固定
  summary : コマンドの概要（任意）
  description : コマンドの詳細（任意）
  expectedPath : 期待値のファイルパスを指定します。
  actualFlowId : 結果値をエビデンスとして取得したFlowIdを指定します。
  ignores : 比較を行いたくない要素をリストで設定します。比較したくない要素の指定方法は、 要素名をドットで連結したパスとしてください。

```

------

### ExtractValue4JsonFileEvidence
JSONから値を抽出して変数に設定します。
#### Command Type
- Assert : No
- Evidence : No

#### Functions
- JSONから値を抽出して変数に設定します。
- パスの指定方法は、 [JsonPath](https://github.com/json-path/JsonPath) を参照してください。ライブラリとして利用しています。
- 指定されたFlowIDがファイルエビデンスを保存していない場合絵エラーとなります。

#### Structure
```yaml
commands : 
  id : コマンドのID
  command : 「ExtractValue4JsonFileEvidence」固定
  summary : コマンドの概要（任意）
  description : コマンドの詳細（任意）
  target : 抽出した値を設定する変数名を指定します。
  targetFlowId : 抽出対象のJSONファイルエビデンスを取得したFlowIDを指定します。
  path : JSONから値を抽出するためのパスを指定します。記載方法は [JsonPath](https://github.com/json-path/JsonPath) を参照してください。

```


## Configuration List

本カスタム機能が提供する設定定義の一覧及び詳細。

|Name|Summary|
|:---|:---|


## Message List

本カスタム機能が出力するメッセージの一覧及び内容。

|MessageID|MessageContents|
|:---|:---|
|com.epion_t3.json.err.9005|JSONの比較に失敗しました。|
|com.epion_t3.json.err.9004|JSONは一致しません.|
|com.epion_t3.json.err.9003|結果値のJSONが見つかりません.　パス:{0}, FlowId:{1}|
|com.epion_t3.json.err.9002|期待値のJSONが見つかりません.　パス:{0}|
|com.epion_t3.json.inf.1001|JSONは一致します.|
|com.epion_t3.json.err.9001|JSON解析に失敗しました.|
