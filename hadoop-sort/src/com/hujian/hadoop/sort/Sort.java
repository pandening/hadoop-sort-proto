package com.hujian.hadoop.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;
/*
* just sort the data set.
* */
public class Sort {
    public static class TokenizerMapper extends Mapper<Object, Text, IntWritable, Text> {
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            if (value.toString().length() != 0) {
                context.write(new IntWritable(Integer.parseInt(value.toString())), new Text(""));
            }
        }
    }
    public static class IntSumReducer extends Reducer<IntWritable,Text,IntWritable,Text> {
        public void reduce(IntWritable key, Iterable<Text> values, Context context
        ) throws IOException, InterruptedException {
            for(Text n:values) {
                context.write(key, new Text(""));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "Sort");
        job.setJarByClass(Sort.class);
        job.setMapperClass(TokenizerMapper.class);

        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);

        //reducer out type,mapper need to same as reducer(out-part).
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);
        //set the in/out path
        FileInputFormat.addInputPath(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}