package com.patient.framework.utils;

import java.util.Random;
import java.util.regex.Pattern;

public class Generator {

    Random random = new Random();

    public Object getRand(String keyword) {
        Object result = null;
        keyword = keyword.toLowerCase().replace(" ", "").replace("_", "");
        if ("age".equals(keyword) || "pa".equals(keyword) || "patientage".equals(keyword)) {
            result = randomPA();
        } else if ("sign".equals(keyword) || "ps".equals(keyword) || "patientsign".equals(keyword)) {
            result = randomPSI();
        } else if ("servetime".equals(keyword) || "servicetime".equals(keyword) ||
                "spentwithdoctor".equals(keyword) || "swd".equals(keyword)) {
            result = randSWD();
        }
        return result;
    }

    public Object getRand(String keyword, Object factor) {
        Object result = null;
        keyword = keyword.toLowerCase().replace(" ", "").replace("_", "");
        if ("age".equals(keyword) || "pa".equals(keyword) || "patientage".equals(keyword)) {
            result = randomPA();
        } else if(factor instanceof String){
            Pattern pattern = Pattern.compile("^[+]{0,1}(\\d+)$|^[+]{0,1}(\\d+\\.\\d+)$");
            if ("age".equals(keyword) || "pa".equals(keyword) || "patientage".equals(keyword)) {
                result = randomPA();
            } else if (pattern.matcher((String) factor).matches()) {
                if ("sign".equals(keyword) || "ps".equals(keyword) || "patientsign".equals(keyword) ||
                        "patientsignindex".equals(keyword)) {
                    result = randomPSI(Integer.parseInt((String) factor));
                } else if ("servetime".equals(keyword) || "servicetime".equals(keyword) ||
                        "spentwithdoctor".equals(keyword) || "swd".equals(keyword)) {
                    result = randSWD(Double.parseDouble((String) factor));
                }
            }
        } else if (factor instanceof Integer) {
            if ("sign".equals(keyword) || "ps".equals(keyword) || "patientsign".equals(keyword) ||
                    "patientsignindex".equals(keyword)) {
                result = randomPSI((int) factor);
            }
        } else if (factor instanceof Double){
            if ("servetime".equals(keyword) || "servicetime".equals(keyword) ||
                    "spentwithdoctor".equals(keyword) || "swd".equals(keyword)) {
                result = randSWD((double) factor);
            }
        }
        return result;
    }

    private int randomPA(){
        int age = 18;
        double serveTime = random.nextDouble() * 25.000 + 5.000;
        if ((serveTime >= 5.000) && (serveTime < 10.000)) {
            age = random.nextInt(12) + 18;
        } else if ((serveTime >= 10) && (serveTime < 15.000)) {
            age = random.nextInt(12) + 28;
        } else if ((serveTime >= 15) && (serveTime < 20.000)) {
            age = random.nextInt(25) + 25;
        } else if ((serveTime >= 20.000) && (serveTime < 25.000)) {
            age = random.nextInt(40) + 25;
        } else if ((serveTime >= 25.000) && (serveTime < 30.000)) {
            age = random.nextInt(45) + 35;
        }
        return age;
    }

    private double randomPSI(){
        return random.nextDouble() * 0.999 + 0.001;
    }


    private double randomPSI(int age){
        double sign = 0.800;
        if ((age >= 18) && (age < 20)) {
            sign = random.nextDouble() * 0.600 + 0.400;
        } else if ((age >= 20) && (age < 30)) {
            sign = random.nextDouble() * 0.500 + 0.500;
        } else if ((age >= 30) && (age < 40)) {
            sign = random.nextDouble() * 0.600 + 0.350;
        } else if ((age >= 40) && (age < 50)) {
            sign = random.nextDouble() * 0.700 + 0.200;
        } else if ((age >= 50) && (age < 60)) {
            sign = random.nextDouble() * 0.849 + 0.001;
        } else if ((age >= 60) && (age < 80)) {
            sign = random.nextDouble() * 0.799 + 0.001;
        }
        return sign;
    }

    private double randSWD(){
        return random.nextDouble() * 25.000 + 5.000;
    }

    private double randSWD(double sign){
        Double spentWithDoctor = 15.000;
        if ((sign >= 0.900) && (sign < 1.000)) {
            spentWithDoctor = random.nextDouble() * 10.000 + 5.000;
        } else if ((sign >= 0.750) && (sign < 0.900)) {
            spentWithDoctor = random.nextDouble() * 15.000 + 5.000;
        } else if ((sign >= 0.500) && (sign < 0.750)) {
            spentWithDoctor = random.nextDouble() * 15.000 + 10.000;
        } else if ((sign >= 0.200) && (sign < 0.500)) {
            spentWithDoctor = random.nextDouble() * 20.000 + 10.000;
        } else if ((sign >= 0.001) && (sign < 0.200)) {
            spentWithDoctor = random.nextDouble() * 15.00 + 15.000;
        }
        return spentWithDoctor;
    }


}
