package evaluator.database;

import data.EvaluatedRule;
import evaluator.evaluators.RuleEvaluatedHandler;

public class RuleHandler implements RuleEvaluatedHandler {
    @Override
    public void onEvaluateRule(EvaluatedRule evaluatedRule) {
        Database database = Database.getInstance();
        database.insertRule(evaluatedRule);
    }
}
