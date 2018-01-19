package com.zm.goods.utils.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.BooleanFilter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.base.Pagination;
import com.zm.goods.pojo.base.SortModelList;

/**
 * ClassName: AbstractLuceneUtil <br/>
 * Function: lucene抽象类，商品lucene搜索统一继承自该类. <br/>
 * date: Aug 8, 2017 2:00:32 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
public abstract class AbstractLucene {

	protected Analyzer analyzer = new IKAnalyzer();

	private Directory directory;

	protected String filePath;

	protected IndexWriter indexWriter;

	protected IndexSearcher indexSearch;

	private static final int TEXT_MAX_LENGTH = 2000;

	private static final String PREFIX_HTML = "<font color='red'>";

	private static final String SUFFIX_HTML = "</font>";

	private DirectoryReader reader;

	/**
	 * 
	 * deleteIndex:根据ID删除索引. <br/>
	 * 
	 * @author wqy
	 * @param id
	 * @since JDK 1.7
	 */
	public abstract void deleteIndex(List<String> list);

	/**
	 * 
	 * search:搜索统一入口. <br/>
	 * 
	 * @author wqy
	 * @param obj
	 * @param pagination
	 * @param sortList
	 * @return
	 * @throws IOException
	 * @throws InvalidTokenOffsetsException
	 * @since JDK 1.7
	 */
	public Map<String, Object> search(Object obj, Pagination pagination, SortModelList sortList)
			throws IOException, InvalidTokenOffsetsException {

		List<String> keyWordsList = new ArrayList<String>();
		List<String> filedsList = new ArrayList<String>();
		Map<String, String> accuratePara = new HashMap<String, String>();

		getIndexSearch();

		// 封装查询参数
		renderParameter(keyWordsList, filedsList, accuratePara, obj);

		if (keyWordsList.size() == 0 && accuratePara.size() == 0) {
			filedsList.add("upShelves");
			keyWordsList.add("1");
		} 
		
		return queryWithPara(pagination, sortList, keyWordsList, filedsList, accuratePara);

	}

	/**
	 * 
	 * getIndexWriter:初始化indexWriter对象. <br/>
	 * 
	 * @author wqy
	 * @since JDK 1.7
	 */
	protected void getIndexWriter() {
		// 3.创建IndexWriterConfig
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		// 4.创建IndexWriter
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			directory = FSDirectory.open(file);
			// 创建writer
			indexWriter = new IndexWriter(directory, iwc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * getIndexSearch:初始化indexSearch对象. <br/>
	 * 
	 * @author wqy
	 * @since JDK 1.7
	 */
	protected void getIndexSearch() {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		if (indexSearch == null) {
			try {
				directory = FSDirectory.open(file);
				reader = DirectoryReader.open(directory);
				indexSearch = new IndexSearcher(reader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (reader != null) {
				try {
					directory = FSDirectory.open(file);
					DirectoryReader newReader = DirectoryReader.openIfChanged(reader);
					if (newReader != null) {
						reader.close();
						reader = newReader;
						indexSearch = new IndexSearcher(reader);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * writerIndex:创建索引. <br/>
	 * 
	 * @author wqy
	 * @param objList
	 * @since JDK 1.7
	 */
	public abstract <T> void writerIndex(List<T> objList);

	/**
	 * 
	 * updateIndex:更新索引 <br/>
	 * 
	 * @author wqy
	 * @param param
	 * @since JDK 1.7
	 */
	public abstract void updateIndex(Map<String, String> param);

	/**
	 * 
	 * packageData:封装自己需要的内容并返回. <br/>
	 * 
	 * @author wqy
	 * @param highlighter
	 *            高亮对象
	 * @param results
	 *            查询结果
	 * @param hits
	 *            碰撞结果
	 * @return Map对象
	 * @since JDK 1.7
	 */
	public abstract Map<String, Object> packageData(Highlighter highlighter, TopDocs results, ScoreDoc[] hits, TopDocs allResults)
			throws IOException, InvalidTokenOffsetsException;

	/**
	 * 
	 * renderParameter:lucene搜索条件渲染. <br/>
	 * 
	 * @author wqy
	 * @param keyWordsList
	 *            搜索条件
	 * @param filedsList
	 *            搜索域
	 * @param occurList
	 *            搜索逻辑（and/or/not in）
	 * @param accuratePara
	 *            精确搜索条件
	 * @param obj
	 *            对象
	 * @since JDK 1.7
	 */
	public abstract void renderParameter(List<String> keyWordsList, List<String> filedsList,
			Map<String, String> accuratePara, Object obj);

	/**
	 * 
	 * renderSortParameter:lucene排序条件渲染. <br/>
	 * 
	 * @author wqy
	 * @param sortList
	 * @return
	 * @since JDK 1.7
	 */
	public abstract Sort renderSortParameter(SortModelList sortList);
	
	/**
	 * 
	 * packageQuery:封装query. <br/>
	 * 
	 * @author wqy
	 * @param keyWordsList
	 * @param filedsList
	 * @return BooleanQuery
	 * @since JDK 1.7
	 */
	public abstract Query packageQuery(List<String> keyWordsList, List<String> filedsList) throws IOException ;
	

	/**
	 * 
	 * accurateQuery:过滤器,精确查找. <br/>
	 * 
	 * @author wqy
	 * @param accuratePara
	 * @param query
	 * @param booleanFilter
	 * @return
	 * @since JDK 1.7
	 */
	public abstract BooleanFilter accurateQuery(Map<String, String> accuratePara); 

	/**
	 * 
	 * queryWithPara:根据条件搜索. <br/>
	 * 
	 * @author wqy
	 * @param pagination
	 *            分页对象
	 * @param sortList
	 *            排序对象
	 * @param keyWordsList
	 *            搜索条件
	 * @param filedsList
	 *            搜索域
	 * @param occurList
	 *            搜索逻辑（and/or/not in）
	 * @param accuratePara
	 *            精确搜索条件
	 * @return
	 * @throws IOException
	 * @throws InvalidTokenOffsetsException
	 * @since JDK 1.7
	 */
	private Map<String, Object> queryWithPara(Pagination pagination, SortModelList sortList, List<String> keyWordsList,
			List<String> filedsList, Map<String, String> accuratePara)
					throws IOException, InvalidTokenOffsetsException {

		Map<String, Object> result = new HashMap<String, Object>(16);

		Query query = packageQuery(keyWordsList, filedsList);

		// 封装排序参数
		Sort sort = renderSortParameter(sortList);

		// 是否需要精确查找
		BooleanFilter booleanFilter = accurateQuery(accuratePara); 

		// 获取高亮对象
		Highlighter highlighter = getHighlighter(query);
		
		ScoreDoc scoreDoc = getLastScoreDoc(pagination.getCurrentPage(), pagination.getNumPerPage(), query,
				booleanFilter, indexSearch, sort);
		TopDocs results = indexSearch.searchAfter(scoreDoc, query, booleanFilter, pagination.getNumPerPage(), sort);
		System.out.println("Total match：" + results.totalHits);
		ScoreDoc[] hits = results.scoreDocs;
		
		TopDocs allResults = indexSearch.search(query, booleanFilter, reader.maxDoc());

		result = packageData(highlighter, results, hits, allResults);
		

		return result;
	}

	
	/**
	 * 
	 * queryWithOutPara:查询所有. <br/>
	 * 
	 * @author wqy
	 * @param pagination
	 *            分页对象
	 * @return
	 * @throws IOException
	 * @since JDK 1.7
	 */
	private Map<String, Object> queryWithOutPara(Pagination pagination) throws IOException {
		Document doc1;
		Map<String, Object> result = new HashMap<String, Object>(16);
		List<String> idList = new ArrayList<String>();

		int count = reader.maxDoc();
		int start = (pagination.getCurrentPage() - 1) * pagination.getNumPerPage();
		int end = pagination.getCurrentPage() * pagination.getNumPerPage();
		if(end >= count){
			end = count;
		}
		for (int i = start; i < end; i++) {
			doc1 = indexSearch.doc(i);
			String res = doc1.get("goodsId");
			if (res != null) {
				idList.add(res);
			}
		}
		result.put(Constants.TOTAL, count);
		result.put(Constants.ID_LIST, idList);

		return result;
	}

	/**
	 * 
	 * getHighlighter:获取高亮对象. <br/>
	 * 
	 * @author wqy
	 * @param query
	 * @return
	 * @since JDK 1.7
	 */
	private Highlighter getHighlighter(Query query) {
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(PREFIX_HTML, SUFFIX_HTML);
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));
		highlighter.setTextFragmenter(new SimpleFragmenter(TEXT_MAX_LENGTH));
		return highlighter;
	}

	/**
	 * 
	 * getLastScoreDoc:分页查询获取上一页的最大索引ID. <br/>
	 * 
	 * @author wqy
	 * @param pageIndex
	 *            第几页
	 * @param pageSize
	 *            每页数量
	 * @param query
	 *            搜索器
	 * @param booleanFilter
	 *            过滤器
	 * @param searcher
	 *            搜索对象
	 * @param sort
	 *            排序对象
	 * @return
	 * @throws IOException
	 * @since JDK 1.7
	 */
	private ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, BooleanFilter booleanFilter,
			IndexSearcher searcher, Sort sort) throws IOException {
		if (pageIndex == 1)
			return null;// 如果是第一页就返回空
		int num = pageSize * (pageIndex - 1);// 获取上一页的最后数量
		TopDocs tds = searcher.search(query, booleanFilter, num, sort);
		return tds.scoreDocs[num - 1];
	}

}
