package com.zm.goods.utils.lucene;

import java.io.Reader;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.pattern.PatternTokenizer;

public class SplitAnalyzer extends Analyzer {

	String regex;//使用的正则拆分式

    public SplitAnalyzer(String regex) {

         this.regex=regex;

    }
 

    @Override

    protected TokenStreamComponents createComponents(String arg0, Reader arg1) {

        return new TokenStreamComponents(new PatternTokenizer(arg1, Pattern.compile(regex),-1));

    }
}
