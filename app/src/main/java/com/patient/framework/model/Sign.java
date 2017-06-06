package com.patient.framework.model;

public class Sign {
    int sid;
    int pid;
    float height;
    float weight;
    float temp;
    float breath;
    float pulse;
    float pressure;
    float blsugar;
    String more;

    public Sign() {
    }

    public Sign(int sid, int pid, float height, float weight, float temp, float breath, float pulse, float pressure, float blsugar, String more) {
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

    public int getUid() {
        return pid;
    }

    public void setUid(int pid) {
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

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
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
        return "Sign{" +
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
        return new String[]{Integer.toString(pid),Float.toString(height),Float.toString(weight),Float.toString(temp),Float.toString(breath),Float.toString(pulse),Float.toString(pressure),Float.toString(blsugar),more};
    }

    public String[] getInfo(){
        return new String[]{Integer.toString(pid),Float.toString(height),Float.toString(weight),Float.toString(temp),Float.toString(breath),Float.toString(pulse),Float.toString(pressure),Float.toString(blsugar),more};
    }
}
