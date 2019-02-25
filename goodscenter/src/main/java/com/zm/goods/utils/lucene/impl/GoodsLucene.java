package com.zm.goods.utils.lucene.impl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.BooleanFilter;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.util.BytesRef;

import com.zm.goods.annotation.SearchCondition;
import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.base.SortModel;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.pojo.dto.GuideProperty;
import com.zm.goods.utils.DateUtil;
import com.zm.goods.utils.lucene.AbstractLucene;
import com.zm.goods.utils.lucene.SplitAnalyzer;

/**
 * ClassName: CommodityLucene <br/>
 * Function: 商品lucene工具类. <br/>
 * date: Aug 8, 2017 8:16:15 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
public class GoodsLucene extends AbstractLucene {

	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";

	private static String decimalFormat = "000000000";

	// private SplitAnalyzer splitAnalyzer = new SplitAnalyzer(",");// 按逗号分词

	@Override
	public <T> void writerIndex(List<T> objList) {
		Document doc;
		DecimalFormat df2 = (DecimalFormat) DecimalFormat.getInstance();
		df2.applyPattern(decimalFormat);
		for (Object obj : objList) {
			doc = packDocument(df2, obj);
			try {
				indexWriter.addDocument(doc);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Document packDocument(DecimalFormat df2, Object obj) {
		Document doc;
		long time = 0;
		GoodsSearch model = (GoodsSearch) obj;
		// 初始化导购信息
		model.initGuideProperty();
		doc = new Document();
		doc.add(new StringField("specsTpId", model.getSpecsTpId(), Store.YES));

		// 商品名称设置权重
		TextField commodityName = new TextField("goodsName", model.getGoodsName() == null ? "" : model.getGoodsName(),
				Store.NO);
		doc.add(commodityName);

		doc.add(new IntField("ratio", model.getRatio() == null ? 0 : model.getRatio(), Store.NO));
		doc.add(new IntField("welfare", model.getWelfare() == null ? 0 : model.getRatio(), Store.NO));
		doc.add(new IntField("saleNum", model.getSaleNum() == null ? 0 : model.getSaleNum(), Store.NO));
		doc.add(new StringField("upShelves", "1", Store.NO));
		doc.add(new StringField("firstCategory", model.getFirstCategory().trim(), Store.NO));
		doc.add(new StringField("secondCategory", model.getSecondCategory().trim(), Store.NO));
		doc.add(new StringField("thirdCategory", model.getThirdCategory().trim(), Store.NO));
		doc.add(new StringField("price", model.getPrice() == null ? "0" : df2.format((model.getPrice() * 100)) + "",
				Store.NO));
		if (model.getGuideList() != null) {
			for (GuideProperty property : model.getGuideList()) {
				doc.add(new StoredField(property.getName(), property.getValue()));
			}
		}
		try {
			time = DateUtil.stringToLong(model.getUpshelfTime(), dateFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		doc.add(new LongField("upshelfTime", time, Store.NO));
		return doc;
	}

	@Override
	public <T> void updateIndex(List<T> objList) {
		Document doc = null;

		if (objList == null || objList.size() == 0) {
			return;
		}
		DecimalFormat df2 = (DecimalFormat) DecimalFormat.getInstance();
		df2.applyPattern(decimalFormat);
		for (Object obj : objList) {
			GoodsSearch model = (GoodsSearch) obj;
			String specsTpId = model.getSpecsTpId();
			doc = packDocument(df2, obj);
			try {
				indexWriter.updateDocument(new Term("specsTpId", specsTpId), doc);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> packageData(Highlighter highlighter, TopDocs results, ScoreDoc[] hits,
			TopDocs allResults) throws IOException, InvalidTokenOffsetsException {

		Document doc1;
		Map<String, Object> result = new HashMap<String, Object>(16);
		List<String> commodityIdList = new ArrayList<String>();
		for (ScoreDoc hit : hits) {
			doc1 = indexSearch.doc(hit.doc);
			String res = doc1.get("specsTpId");
			if (res != null) {
				commodityIdList.add(res);
			}
		}
		Set<String> tmpSet = null;
		IndexableField idxField = null;
		for (ScoreDoc hit : allResults.scoreDocs) {
			doc1 = indexSearch.doc(hit.doc);
			Iterator<IndexableField> it = doc1.iterator();
			while (it.hasNext()) {
				idxField = it.next();
				if (idxField.fieldType().stored()) {
					if (!idxField.name().equals("specsTpId")) {
						if (result.get(idxField.name()) == null) {
							tmpSet = new HashSet<>();
							tmpSet.add(idxField.stringValue());
							result.put(idxField.name(), tmpSet);
						} else {
							Set<String> set = (Set<String>) result.get(idxField.name());
							set.add(idxField.stringValue());
						}
					}
				}
			}
		}
		result.put(Constants.TOTAL, results.totalHits);
		result.put(Constants.ID_LIST, commodityIdList);

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void renderParameter(Map<String, String> searchPara, Map<String, String> accuratePara, Object obj) {

		Object o = null;

		GoodsSearch commodityInfo = (GoodsSearch) obj;
		Class clazz = commodityInfo.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			PropertyDescriptor pd = null;
			try {
				pd = new PropertyDescriptor(field.getName(), clazz);
			} catch (IntrospectionException e) {
				e.printStackTrace();
			}
			SearchCondition searchCondition = field.getAnnotation(SearchCondition.class);
			if (searchCondition == null) {
				continue;
			}
			Method getMethod = pd.getReadMethod();// 获得get方法
			try {
				o = getMethod.invoke(commodityInfo);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			if (o != null) {
				if (SearchCondition.FILTER == searchCondition.value()) {
					if (field.getName().equals("guideList")) {
						List<GuideProperty> list = (List<GuideProperty>) o;
						for (GuideProperty p : list) {
							accuratePara.put(p.getName(), p.getValue());
						}
					} else {
						accuratePara.put(field.getName(), o + "");
					}
				} else if (SearchCondition.SEARCH == searchCondition.value()) {
					searchPara.put(field.getName(), o + "");
				}
			}
		}

	}

	@Override
	public Sort renderSortParameter(SortModelList sortList) {
		List<SortField> sortFieldList = new ArrayList<SortField>();
		if (sortList != null && sortList.getSortList() != null && sortList.getSortList().size() > 0) {
			List<SortModel> list = sortList.getSortList();
			SortField sortField = null;
			for (SortModel model : list) {
				if ("createTime".equals(model.getSortField())) {
					sortField = new SortField(model.getSortField(), Type.LONG,
							"desc".equals(model.getSortRule()) ? true : false);
				} else {
					sortField = new SortField(model.getSortField(), Type.STRING,
							"desc".equals(model.getSortRule()) ? true : false);
				}
				sortFieldList.add(sortField);
			}
		} else {
			SortField sortField = new SortField("ratio", Type.INT, true);
			sortFieldList.add(sortField);
			SortField sortField_1 = new SortField("thirdCategory", Type.STRING_VAL, true);
			sortFieldList.add(sortField_1);
		}
		sortFieldList.add(SortField.FIELD_SCORE);
		Sort sort = new Sort(sortFieldList.toArray(new SortField[sortFieldList.size()]));
		return sort;
	}

	@Override
	public Query packageQuery(Map<String, String> searchPara) throws IOException {
		BooleanQuery query = new BooleanQuery();
		TokenStream stream = null;
		for (Map.Entry<String, String> entry : searchPara.entrySet()) {
			if (entry.getKey().contains("Category") || entry.getKey().contains("upShelves")) {
				Term tm = new Term(entry.getKey(), entry.getValue());
				Query termQuery = new TermQuery(tm);
				return termQuery;
			}
			BooleanQuery query1 = new BooleanQuery();

			stream = analyzer.tokenStream(entry.getKey(), new StringReader(entry.getValue()));

			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			while (stream.incrementToken()) {
				System.out.println(cta.toString());
				Term tm = new Term(entry.getKey(), cta.toString());

				TermQuery parser = new TermQuery(tm);
				query1.add(parser, BooleanClause.Occur.MUST);
			}
			stream.end();
			stream.close();

			query.add(query1, BooleanClause.Occur.MUST);
		}
		return query;
	}

	public GoodsLucene(Integer centerId) {
		init(centerId);
	}

	private void init(Integer centerId) {
		filePath = GoodsLucene.class.getClassLoader().getResource("").getPath() + "product/" + centerId;
		getIndexWriter();
	}

	@Override
	public BooleanFilter accurateQuery(Map<String, String> accuratePara) {
		if (accuratePara != null && accuratePara.size() > 0) {
			BooleanQuery query = null;
			BooleanFilter booleanFilter = new BooleanFilter();
			if (accuratePara.get("priceMin") != null && accuratePara.get("priceMax") != null) {
				DecimalFormat df2 = (DecimalFormat) DecimalFormat.getInstance();
				df2.applyPattern(decimalFormat);
				String priceMin = df2.format((Double.valueOf(accuratePara.get("priceMin")) * 100)) + "";
				String priceMax = df2.format((Double.valueOf(accuratePara.get("priceMax")) * 100)) + "";
				Filter filter = new TermRangeFilter("price", new BytesRef(priceMin), new BytesRef(priceMax), true,
						true);
				booleanFilter.add(filter, Occur.MUST);
			}
			for (Map.Entry<String, String> entry : accuratePara.entrySet()) {
				if (!"priceMin".equals(entry.getKey()) && !"priceMax".equals(entry.getKey())) {
					query = new BooleanQuery();
					String[] strArr = entry.getValue().split(",");
					for (String s : strArr) {
						Term term = new Term(entry.getKey(), s);// 添加term
						query.add(new TermQuery(term), Occur.SHOULD);
					}
					QueryWrapperFilter filter = new QueryWrapperFilter(query);// 添加过滤器
					booleanFilter.add(filter, Occur.MUST);
				}
				// if ("status".equals(entry.getKey())) {
				// Term term = new Term(entry.getKey(), entry.getValue());//
				// 添加term
				// QueryWrapperFilter filter = new QueryWrapperFilter(new
				// TermQuery(term));// 添加过滤器
				// booleanFilter.add(filter, Occur.MUST);
				// }
			}
			return booleanFilter;
		}
		return null;
	}

	@Override
	public void deleteIndex(List<String> list) {
		try {
			if (list != null && list.size() > 0) {
				for (String specsTpId : list) {
					indexWriter.deleteDocuments(new Term("specsTpId", specsTpId));
				}
			}
			indexWriter.commit();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		String str = "爆款,ce,shi";
		TokenStream stream = null;
		@SuppressWarnings("resource")
		SplitAnalyzer an = new SplitAnalyzer(",");
		try {
			stream = an.tokenStream("测试", new StringReader(str));
			CharTermAttribute attr = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			while (stream.incrementToken()) {
				System.out.println(attr.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
