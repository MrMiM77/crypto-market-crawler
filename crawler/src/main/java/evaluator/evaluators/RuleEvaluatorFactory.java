package evaluator.evaluators;

import data.MovingAverageRule;
import data.Rule;

import java.util.ArrayList;

public class RuleEvaluatorFactory extends Thread{
    private ArrayList<RuleEvaluator> evaluators;
    public RuleEvaluatorFactory() {
        evaluators = new ArrayList<>();
    }

    public void addRule(Rule rule, RuleEvaluatedHandler handler) {
        if(rule.getClass() == MovingAverageRule.class)
            addMovingAverageRuleEvaluator((MovingAverageRule) rule, handler);
    }
    public void addMovingAverageRuleEvaluator(MovingAverageRule rule, RuleEvaluatedHandler handler) {
        MovingAverageRuleEvaluator evaluator = new MovingAverageRuleEvaluator(rule, handler);
        evaluators.add(evaluator);
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (RuleEvaluator evaluator : evaluators)
                    evaluator.Evaluate();
                Thread.sleep(1000 * 60 * 1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
