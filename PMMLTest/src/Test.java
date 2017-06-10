import org.dmg.pmml.*;
import org.jpmml.evaluator.*;
import org.jpmml.evaluator.OutputField;
import org.jpmml.model.PMMLUtil;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Test {
    private PMML pmml;
    private Evaluator evaluator;
    List<InputField> inputFields;
    List<TargetField> targetFields;
    List<OutputField> outputFields;
    private final String path = "GBRT.pmml";

    public Test() {
        try {
            this.pmml = PMMLUtil.unmarshal(new FileInputStream(path));
            ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();
            ModelEvaluator<?> modelEvaluator = modelEvaluatorFactory.newModelEvaluator(pmml);
            evaluator = (Evaluator) modelEvaluator;
            inputFields = evaluator.getInputFields();
            targetFields = evaluator.getTargetFields();
            outputFields = evaluator.getOutputFields();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initiate(){

    }

    public Object predict(List<?> inputs){
        Object targetFieldValue;
        ArrayList<Object> outputs = new ArrayList<>();
        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
        List<InputField> inputFields = evaluator.getInputFields();

        for(InputField inputField : inputFields){
            FieldName inputFieldName = inputField.getName();
            // The raw (ie. user-supplied) value could be any Java primitive value
            Object rawValue = inputs;
            FieldValue inputFieldValue = inputField.prepare(rawValue);
            arguments.put(inputFieldName, inputFieldValue);
        }

        Map<FieldName, ?> results = evaluator.evaluate(arguments);

        List<TargetField> targetFields = evaluator.getTargetFields();
        for(TargetField targetField : targetFields){
            FieldName targetFieldName = targetField.getName();
            targetFieldValue = results.get(targetFieldName);
            if(targetFieldValue instanceof Computable){
                Computable computable = (Computable) targetFieldValue;
                targetFieldValue = computable.getResult();
            }
        }

        List<OutputField> outputFields = evaluator.getOutputFields();
        for(OutputField outputField : outputFields){
            FieldName outputFieldName = outputField.getName();
            outputs.add(results.get(outputFieldName));
        }

        return outputs;
    }

    public static void main(String[] args){
        Test test=new Test();
        ArrayList<Object> inputs=new ArrayList<>();
        inputs.add(1150);
        inputs.add(23);
        inputs.add(0.7712736);
        inputs.add(0);
        inputs.add(17.54465819);
        inputs.add(3);
        inputs.add(1);
        inputs.add(9);
        inputs.add(1);

//        Object res=test.predict(inputs);
        System.out.println(inputs);
    }

}

