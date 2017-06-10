//package com.patient.framework.service;
//
//import org.dmg.pmml.*;
//import org.jpmml.evaluator.*;
//import org.jpmml.evaluator.OutputField;
//import org.jpmml.model.PMMLUtil;
//
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//public class PreparedModel {
//    private PMML pmml;
//    private Evaluator evaluator;
//    List<InputField> inputFields;
//    List<TargetField> targetFields;
//    List<OutputField> outputFields;
//    private final String path = "GBRT.pmml";
//
//    public PreparedModel() {
//        try {
//            this.pmml = PMMLUtil.unmarshal(new FileInputStream(path));
//            ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();
//            ModelEvaluator<?> modelEvaluator = modelEvaluatorFactory.newModelEvaluator(pmml);
//            evaluator = (Evaluator) modelEvaluator;
//            inputFields = evaluator.getInputFields();
//            targetFields = evaluator.getTargetFields();
//            outputFields = evaluator.getOutputFields();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private void initiate(){
//
//    }
//
//    public Object predict(List<?> inputs){
//        Object targetFieldValue;
//        ArrayList<Object> outputs = new ArrayList<>();
//        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
//        List<InputField> inputFields = evaluator.getInputFields();
//
//        for(InputField inputField : inputFields){
//            FieldName inputFieldName = inputField.getName();
//            // The raw (ie. user-supplied) value could be any Java primitive value
//            Object rawValue = inputs;
//            FieldValue inputFieldValue = inputField.prepare(rawValue);
//            arguments.put(inputFieldName, inputFieldValue);
//        }
//
//        Map<FieldName, ?> results = evaluator.evaluate(arguments);
//
//        List<TargetField> targetFields = evaluator.getTargetFields();
//        for(TargetField targetField : targetFields){
//            FieldName targetFieldName = targetField.getName();
//            targetFieldValue = results.get(targetFieldName);
//            if(targetFieldValue instanceof Computable){
//                Computable computable = (Computable) targetFieldValue;
//                targetFieldValue = computable.getResult();
//            }
//        }
//
//        List<OutputField> outputFields = evaluator.getOutputFields();
//        for(OutputField outputField : outputFields){
//            FieldName outputFieldName = outputField.getName();
//            outputs.add(results.get(outputFieldName));
//        }
//
//        return outputs;
//    }
//
//}
