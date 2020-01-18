package me.danght.reducer;

import me.danght.bean.Bill;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author DangHT
 * @date 17/01/2020
 */
public class BillCountReducer extends Reducer<Text, Bill, Text, Bill> {

    @Override
    protected void reduce(Text key, Iterable<Bill> values, Context context) throws IOException, InterruptedException {
        int sumCost = 0;
        int sumIncome = 0;

        // 1.遍历 Bill, 累加 cost 和 income
        for (Bill bill : values) {
            sumCost += bill.getCost();
            sumIncome += bill.getIncome();
        }

        // 2.封装对象
        Bill bill = new Bill(sumCost, sumIncome);

        // 3.写出
        context.write(key, bill);
    }

}
