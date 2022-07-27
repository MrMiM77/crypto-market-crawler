package evaluator.evaluators;

import data.Rule;

public class RuleEvaluator {
    private Rule rule;
    RuleEvaluatedHandler handler;

    public RuleEvaluator(Rule rule, RuleEvaluatedHandler handler) {
        this.rule = rule;
        this.handler = handler;
    }
    public void Evaluate() {
    }
}
