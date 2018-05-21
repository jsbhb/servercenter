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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
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

import com.zm.goods.constants.Constants;
import com.zm.goods.pojo.base.SortModel;
import com.zm.goods.pojo.base.SortModelList;
import com.zm.goods.pojo.dto.GoodsSearch;
import com.zm.goods.utils.DateUtil;
import com.zm.goods.utils.lucene.AbstractLucene;

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

	@Override
	public <T> void writerIndex(List<T> objList) {
		Document doc;
		long time = 0;
		DecimalFormat df2 = (DecimalFormat) DecimalFormat.getInstance();
		df2.applyPattern(decimalFormat);
		for (Object obj : objList) {

			GoodsSearch model = (GoodsSearch) obj;

			doc = new Document();
			doc.add(new StringField("goodsId", model.getGoodsId(), Store.YES));

			// 商品名称设置权重
			TextField commodityName = new TextField("goodsName",
					model.getGoodsName() == null ? "" : model.getGoodsName(), Store.YES);
			doc.add(commodityName);

			doc.add(new TextField("specs", model.getSpecs() == null ? "" : model.getSpecs(), Store.YES));

			doc.add(new StringField("brand", model.getBrand() == null ? "" : model.getBrand() + "", Store.YES));
			doc.add(new StringField("popular", model.getPopular() == null ? "0" : model.getPopular() + "", Store.NO));
			doc.add(new StringField("type", model.getType() == null ? "0" : model.getType() + "", Store.NO));
			doc.add(new StringField("upShelves", "1", Store.NO));
			doc.add(new StringField("firstCategory", model.getFirstCategory().trim(), Store.NO));
			doc.add(new StringField("secondCategory", model.getSecondCategory().trim(), Store.NO));
			doc.add(new StringField("thirdCategory", model.getThirdCategory().trim(), Store.NO));
			doc.add(new StringField("origin", model.getOrigin() == null ? "" : model.getOrigin(), Store.YES));
			doc.add(new StringField("price", model.getPrice() == null ? "0" : df2.format((model.getPrice() * 100)) + "",
					Store.NO));
			try {
				time = DateUtil.stringToLong(model.getCreateTime(), dateFormat);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			doc.add(new LongField("createTime", time, Store.NO));
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

	@Override
	public void updateIndex(Map<String, String> param) {
		Document doc = new Document();

		for (Map.Entry<String, String> entry : param.entrySet()) {
			if ("goodsName".equals(entry.getKey())) {
				TextField commodityName = new TextField("goodsName", entry.getValue() == null ? "" : entry.getValue(),
						Store.YES);
				doc.add(commodityName);
			} else if ("specs".equals(entry.getKey()) || "brand".equals(entry.getKey())
					|| "origin".equals(entry.getKey())) {
				doc.add(new StringField(entry.getKey(), entry.getValue() == null ? "" : entry.getValue() + "",
						Store.YES));
			} else {
				doc.add(new StringField(entry.getKey(), entry.getValue() == null ? "" : entry.getValue() + "",
						Store.NO));
			}
		}
		try {
			indexWriter.updateDocument(new Term("goodsId", param.get("goodsId")), doc);
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Map<String, Object> packageData(Highlighter highlighter, TopDocs results, ScoreDoc[] hits,
			TopDocs allResults) throws IOException, InvalidTokenOffsetsException {

		Document doc1;
		GoodsSearch info = null;
		Map<String, Object> result = new HashMap<String, Object>(16);
		List<String> commodityIdList = new ArrayList<String>();

		Map<String, GoodsSearch> highlighterModel = new HashMap<String, GoodsSearch>();
		for (ScoreDoc hit : hits) {
			doc1 = indexSearch.doc(hit.doc);
			String res = doc1.get("goodsId");
			if (res != null) {
				info = new GoodsSearch();
				info.setGoodsName(highlighter.getBestFragment(analyzer, "goodsName", doc1.get("goodsName")));
				highlighterModel.put(res, info);
				commodityIdList.add(res);
			}
		}
		Set<String> brandSet = new HashSet<String>();
		Set<String> originSet = new HashSet<String>();
		for (ScoreDoc hit : allResults.scoreDocs) {
			doc1 = indexSearch.doc(hit.doc);
			String res = doc1.get("brand");
			if (res != null) {
				brandSet.add(res);
			}
			res = doc1.get("origin");
			if (res != null) {
				originSet.add(res);
			}

		}
		result.put(Constants.TOTAL, results.totalHits);
		result.put(Constants.ID_LIST, commodityIdList);
		result.put(Constants.HIGHLIGHTER_MODEL, highlighterModel);
		result.put(Constants.BRAND, brandSet);
		result.put(Constants.ORIGIN, originSet);

		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void renderParameter(List<String> keyWordsList, List<String> filedsList, Map<String, String> accuratePara,
			Object obj) {

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
			Method getMethod = pd.getReadMethod();// 获得get方法
			try {
				o = getMethod.invoke(commodityInfo);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			if (o != null) {
				if ("brand".equals(field.getName()) || "origin".equals(field.getName())
						|| "priceMin".equals(field.getName()) || "priceMax".equals(field.getName())
						|| "type".equals(field.getName())) {
					accuratePara.put(field.getName(), o + "");
				} else if (!"centerId".equals(field.getName())) {
					keyWordsList.add(o + "");
					filedsList.add(field.getName());
				}
			}
		}

	}

	@Override
	public Sort renderSortParameter(SortModelList sortList) {
		List<SortField> sortFieldList = new ArrayList<SortField>();
		if (sortList != null && sortList.getSortList() != null && sortList.getSortList().size() > 0) {
			List<SortModel> list = sortList.getSortList();
			SortField s1 = new SortField("popular", Type.STRING, true);
			sortFieldList.add(s1);
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
		}
		sortFieldList.add(SortField.FIELD_SCORE);
		Sort sort = new Sort(sortFieldList.toArray(new SortField[sortFieldList.size()]));
		return sort;
	}

	@Override
	public Query packageQuery(List<String> keyWordsList, List<String> filedsList) throws IOException {
		BooleanQuery query = new BooleanQuery();
		TokenStream stream = null;
		for (int i = 0; i < keyWordsList.size(); i++) {
			if (filedsList.get(i).contains("Category") || filedsList.get(i).contains("upShelves")) {
				Term tm = new Term(filedsList.get(i), keyWordsList.get(i));
				Query termQuery = new TermQuery(tm);
				return termQuery;
			}
			BooleanQuery query1 = new BooleanQuery();

			stream = analyzer.tokenStream(filedsList.get(i), new StringReader(keyWordsList.get(i)));

			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			while (stream.incrementToken()) {
				System.out.println(cta.toString());
				Term tm = new Term(filedsList.get(i), cta.toString());

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
				for (String goodsId : list) {
					indexWriter.deleteDocuments(new Term("goodsId", goodsId));
				}
			}
			indexWriter.commit();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
