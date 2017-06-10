package com.patient.framework.model;

public class PatientSign {
    int sid;
    int pid;
    float height;
    float weight;
    float temp;
    float breath;
    float pulse;
    float blsugar;
    String pressure;
    String more;

    public PatientSign() {
    }

    public PatientSign(int pid,float height, float weight, float temp, float breath, float pulse, String pressure, float blsugar, String more) {
        this.pid = pid;
        this.height = height;
        this.weight = weight;
        this.temp = temp;
        this.breath = breath;
        this.pulse = pulse;
        this.pressure = pressure;
        this.blsugar = blsugar;
        this.more = more;
    }

    public PatientSign(int sid, int pid, float height, float weight, float temp, float breath, float pulse, String pressure, float blsugar, String more) {
        this.sid = sid;
        this.pid = pid;
        this.height = height;
        this.weight = weight;
        this.temp = temp;
        this.breath = breath;
        this.pulse = pulse;
        this.pressure = pressure;
        this.blsugar = blsugar;
        this.more = more;
    }

    public int getId() {
        return sid;
    }

    public void setId(int sid) {
        this.sid = sid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getBreath() {
        return breath;
    }

    public void setBreath(float breath) {
        this.breath = breath;
    }

    public float getPulse() {
        return pulse;
    }

    public void setPulse(float pulse) {
        this.pulse = pulse;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public float getBlsugar() {
        return blsugar;
    }

    public void setBlsugar(float blsugar) {
        this.blsugar = blsugar;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    @Override
    public String toString() {
        return "PatientSign{" +
                "sid=" + sid +
                ", pid=" + pid +
                ", height=" + height +
                ", weight=" + weight +
                ", temp=" + temp +
                ", breath=" + breath +
                ", pulse=" + pulse +
                ", pressure=" + pressure +
                ", blsugar=" + blsugar +
                ", more='" + more + '\'' +
                '}';
    }

    public String[] getAll(){
        return new String[]{Integer.toString(pid),Float.toString(height),Float.toString(weight),Float.toString(temp),Float.toString(breath),Float.toString(pulse),pressure,Float.toString(blsugar),more};
    }

    public String[] getInfo(){
        return new String[]{Integer.toString(sid),Integer.toString(pid),Float.toString(height),Float.toString(weight),Float.toString(temp),Float.toString(breath),Float.toString(pulse),pressure,Float.toString(blsugar),more};
    }
}
