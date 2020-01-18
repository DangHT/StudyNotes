package me.danght.mapper;

import me.danght.bean.Bill;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 计算账单，账单数据示例：
 *  ---------------------------------------------
 * |id      |name       |cost       |income     |
 * ----------------------------------------------
 * |1       |DangHT     |1800       |2500       |
 * ----------------------------------------------
 * @author DangHT
 * @date 17/01/2020
 */
public class BillCountMapper extends Mapper<LongWritable, Text, Text, Bill> {

    Text k = new Text();
    Bill v = new Bill();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1. 获取一行
        String line = value.toString();

        // 2. 切割字段
        String[] fields = line.split("\t");

        // 3. 封装对象
        String name = fields[1];
        int cost = Integer.parseInt(fields[2]);
        int income = Integer.parseInt(fields[3]);

        k.set(name);
        v.setCost(cost);
        v.setIncome(income);

        // 4.写出
        context.write(k, v);
    }

}
