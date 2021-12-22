/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.hwadee.cb.bigdata.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hwadee.cb.bigdata.service.base.SomeStatisticLocalServiceBaseImpl;
import com.hwadee.cb.bigdata.utils.DataResult;
import com.hwadee.cb.bigdata.utils.OrganizationCurrent;
import com.hwadee.cb.common.CodeTypeQuerier;
import com.hwadee.cb.common.CqlcbStatus;
import com.hwadee.cb.dictionary.model.DictionaryItem;
import com.hwadee.cb.dictionary.service.DictionaryItemLocalServiceUtil;
import com.hwadee.liferay.kernel.DBUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.sun.swing.internal.plaf.basic.resources.basic;

/**
 * The implementation of the some statistic local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.hwadee.cb.bigdata.service.SomeStatisticLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author hwadee
 * @see com.hwadee.cb.bigdata.service.base.SomeStatisticLocalServiceBaseImpl
 * @see com.hwadee.cb.bigdata.service.SomeStatisticLocalServiceUtil
 */
public class SomeStatisticLocalServiceImpl
	extends SomeStatisticLocalServiceBaseImpl {
	
	/**
	 * 机构分布
	 */
	public DataResult<String> platformOrganizationDistribution(String  type,long superiorId) {
		String superiorLevel="";
		String nowLevel=type;
		if (CodeTypeQuerier.TYPE_CODE_CITY.equals(nowLevel)) {//"city"
			superiorLevel="province";
		}else if (CodeTypeQuerier.TYPE_CODE_AREA.equals(nowLevel)) {//"area",
			nowLevel="district";//前端传来的字段和数据库字段不同
			superiorLevel="city";
		}
		try {
			StringBuilder sqlBuilder =new StringBuilder("SELECT nowLevel,GROUP_CONCAT(organizationType,':',count) from (SELECT nowLevel,SUM(count)as count,organizationType FROM `cb_statistic_platform_region_distribution`").append(superiorId==0 ? "" : " where superiorLevel=? ").append(" GROUP BY nowLevel,organizationType ORDER BY SUM(count))as t GROUP BY nowLevel;");
			List<Object> serializable=new ArrayList<>();
			if(superiorId!=0){
				serializable.add(superiorId);
			}
			String sql = sqlBuilder.toString().replaceAll("superiorLevel", superiorLevel);
			sql = sql.replaceAll("nowLevel", nowLevel);
			List<Object[]> results=new ArrayList<Object[]>();
			Serializable[] params=serializable.toArray(new Serializable[serializable.size()]);
			results = DBUtil.executeSQLQuery(sql.toString(),params);
			JSONArray result = new JSONArray();
			for(Object[] obj:results){
				String contString = Optional.ofNullable(obj[1]).orElse("").toString();
				String []strArray = contString.split(",");
				int total=0;
				JSONObject dataJson = new JSONObject();
				JSONObject detail = new JSONObject();
				for(String s:strArray){
					String []countArray = s.split(":");
					total+=Integer.valueOf(countArray[1].toString());
					if(CqlcbStatus.ORG_TYPE_ENTERPRISE==Integer.valueOf(countArray[0].toString())){
						detail.put("enterprise", countArray[1]);
					}else{
						detail.put("government", countArray[1]);
					}
				}
				dataJson.put("num", total);
				dataJson.put("detail", detail);
				dataJson.put("dicItemId", obj[0]);
				result.add(dataJson);
			}
			return DataResult.successByDatas(JSONObject.toJSONString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DataResult.err();
	}
	
}