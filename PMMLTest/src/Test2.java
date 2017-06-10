import weka.classifiers.pmml.consumer.PMMLClassifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.DenseInstance;
import weka.core.pmml.PMMLFactory;
import weka.core.pmml.PMMLModel;
import weka.core.pmml.PMMLUtils;

import java.util.ArrayList;

public class Test2 {
/*
*   features=['PatientId','PatientAge','PatientSign','Remain','SpentWithDoctor',
*               'MonthOfYear','DayOfMonth','HourOfDay','DayOfWeek']
*    outputs=['SpentOnQueue']
*/
    public static void main(String[] args) throws Throwable{
        String path="GBRT.pmml";
        Instance instance;
        PMMLModel model= PMMLFactory.getPMMLModel(path);
        PMMLClassifier classifier=(PMMLClassifier) model;

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

        double result=0;
        instance=new DenseInstance(inputs.size());
        for(int i=0;i<inputs.size();i++){
            instance.setValue(i,(float)inputs.get(i));
        }

        result=classifier.classifyInstance(instance);
//        result=classifier.distributionForInstance(instance);

        System.out.println(result);
    }

}
