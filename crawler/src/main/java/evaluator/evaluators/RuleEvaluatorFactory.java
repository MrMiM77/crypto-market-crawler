package evaluator.evaluators;

import java.util.ArrayList;

public class RuleEvaluatorFactory extends Thread{
    private ArrayList<RuleEvaluator> evaluators;
    public RuleEvaluatorFactory() {
        evaluators = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (RuleEvaluator evaluator : evaluators)
                    evaluator.Evaluate();
                Thread.sleep(1000 * 60 * 10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
