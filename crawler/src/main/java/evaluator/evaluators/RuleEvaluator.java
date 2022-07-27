package evaluator.evaluators;

import data.Rule;

public abstract class RuleEvaluator {
    RuleEvaluatedHandler handler;

    public abstract void Evaluate();
    public abstract void cleanData();

}
