package evaluator.evaluators;

import data.EvaluatedRule;

public interface RuleEvaluatedHandler {
    void onEvaluateRule(EvaluatedRule evaluatedRule);
}
