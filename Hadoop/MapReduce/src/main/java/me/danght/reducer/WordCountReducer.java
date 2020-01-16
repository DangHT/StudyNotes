package me.danght.reducer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author DangHT
 * @date 16/01/2020
 */
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    int sum = 0;
    IntWritable v = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        // 1.累加求和
        sum = 0;
        for (IntWritable count : values) {
            sum += count.get();
        }

        // 2.输出
        v.set(sum);
        context.write(key, v);
    }

}
