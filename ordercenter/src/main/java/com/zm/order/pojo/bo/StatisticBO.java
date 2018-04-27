/**  
 * Project Name:cardmanager  
 * File Name:StatisticPojo.java  
 * Package Name:com.card.manager.factory.auth.model  
 * Date:Apr 27, 20183:54:05 PM  
 *  
 */
package com.zm.order.pojo.bo;

import java.util.List;

/**
 * ClassName: StatisticPojo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Apr 27, 2018 3:54:05 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class StatisticBO {

	private List<DiagramBO> diagramData;
	private List<DiagramBO> chartData;

	public List<DiagramBO> getDiagramData() {
		return diagramData;
	}

	public void setDiagramData(List<DiagramBO> diagramData) {
		this.diagramData = diagramData;
	}

	public List<DiagramBO> getChartData() {
		return chartData;
	}

	public void setChartData(List<DiagramBO> chartData) {
		this.chartData = chartData;
	}

}
