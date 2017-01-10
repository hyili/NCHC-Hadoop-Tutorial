package org.nchc.train.mr.sshversion;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.*;

import org.json.JSONObject;


public  class sshVersionMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    private static Logger logger = Logger.getLogger(sshVersionMapper.class);

    private Pattern http = Pattern.compile("[Hh][Tt][Tt][Pp]/");
    private Pattern ftp = Pattern.compile("([Ff][Tt][Pp])|(220)");


    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
      JSONObject documentObj = null;

      try {
        logger.info("input sentence " + value.toString());
        documentObj = new JSONObject(value.toString());
      } catch (Exception e) {
        throw new RuntimeException("Failed to convert JSON String document into a JSON Object.", e);
      }

      try {
        String _word;
        _word = documentObj.getJSONObject("data").getJSONObject("ssh").getJSONObject("server_protocol").getString("raw_banner").replace("\n", "").replace("\r", "");

        // Regex Matcher: find out http and ftp little bug
        Matcher m = null;

        m = http.matcher(_word);
        if (m.find()) {
          word.set("HTTP");
          context.write(word, one);
          return;
        }
        m = ftp.matcher(_word);
        if (m.find()) {
          word.set("FTP");
          context.write(word, one);
          return;
        }

        word.set(_word);
        context.write(word, one);
      } catch (Exception e) {
        String _word;
        _word = "invalid";

        word.set(_word);
        context.write(word, one);
      }

    }
}
