package me.danght.bean;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 账单 bean
 * 实现 Writable 接口
 * @author DangHT
 * @date 17/01/2020
 */
public class Bill implements Writable {

    private int cost;
    private int income;
    private int sum;

    public Bill() {}

    public Bill(int cost, int income) {
        this.cost = cost;
        this.income = income;
        this.sum = income - cost;
    }

    /**
     * 序列化方法
     * @param out
     * @throws IOException
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(cost);
        out.writeInt(income);
        out.writeInt(sum);
    }

    /**
     * 反序列化方法（与序列化顺序一致）
     * @param in
     * @throws IOException
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        this.cost = in.readInt();
        this.income = in.readInt();
        this.sum = in.readInt();
    }

    @Override
    public String toString() {
        return cost + "\t" + income + "\t" + sum;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
