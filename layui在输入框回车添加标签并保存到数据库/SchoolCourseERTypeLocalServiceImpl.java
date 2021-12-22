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

package com.hwadee.cb.learnplan.service.impl;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hwadee.cb.learnplan.model.SchoolCourseERType;
import com.hwadee.cb.learnplan.model.SchoolCourseERType;
import com.hwadee.cb.learnplan.service.SchoolCourseERTypeLocalServiceUtil;
import com.hwadee.cb.learnplan.service.base.SchoolCourseERTypeLocalServiceBaseImpl;
import com.hwadee.cb.learnplan.service.persistence.SchoolCourseERTypeUtil;
import com.hwadee.cb.learnplan.service.persistence.SchoolCourseERTypeUtil;
import com.hwadee.cb.school.util.DataResult;
import com.hwadee.liferay.basic.common.util.UserCurrentOrganization;
import com.hwadee.liferay.kernel.CurrentUser;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;

/**
 * The implementation of the 瀛︽牎璇剧▼閫変慨蹇呬慨绫诲埆 local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.hwadee.cb.learnplan.service.SchoolCourseERTypeLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author hwadee
 * @see com.hwadee.cb.learnplan.service.base.SchoolCourseERTypeLocalServiceBaseImpl
 * @see com.hwadee.cb.learnplan.service.SchoolCourseERTypeLocalServiceUtil
 */
public class SchoolCourseERTypeLocalServiceImpl
	extends SchoolCourseERTypeLocalServiceBaseImpl {
	public DataResult<JSONArray> getSchoolOwnERType(String SchoolId){
		long SchoolIdLong = Long.valueOf(SchoolId);
		JSONArray couArray = new JSONArray();
		int total=0;
		try {
			List<SchoolCourseERType> links = SchoolCourseERTypeUtil.findByschoolId(SchoolIdLong, false);
			total = SchoolCourseERTypeUtil.countByschoolId(SchoolIdLong, false);
			couArray=JSONArray.parseArray(JSON.toJSONString(links));
		} catch ( IllegalArgumentException | SystemException e) {
			e.printStackTrace();
		}
		return DataResult.successByTotalData(couArray, (long)total);
	}
	public String addERType(String typeName) throws SystemException{
		JSONObject object = new JSONObject();
		object.put("stat", false);
		String organizationId = UserCurrentOrganization.getCurrentOrganization();
//		String organizationId = "158870";
		if(Validator.isNotNull(organizationId) && !organizationId.equals("0")){
			SchoolCourseERType cat = SchoolCourseERTypeUtil.fetchByschoolIdAndTypeName(Long.valueOf(organizationId), typeName, false);
			if(cat!=null){
				object.put("message", "类别已存在！");
			}else {
				cat= SchoolCourseERTypeLocalServiceUtil.createSchoolCourseERType(counterLocalService.increment(SchoolCourseERType.class.getName()));
				cat.setIsDeleted(false);
				cat.setTypeName(typeName);
				cat.setSchoolId(Long.valueOf(organizationId));
				cat.setCreatorId(CurrentUser.getUserId());
				cat.setCreatorName(CurrentUser.getUserName());
				cat.setCreateDate(new Date());
				SchoolCourseERTypeLocalServiceUtil.updateSchoolCourseERType(cat);
				object.put("stat", true);
				object.put("message", "添加成功！");
			}
		}
		return object.toString();
	}
	public String delERType(String typeId) throws SystemException{
		JSONObject object = new JSONObject();
		object.put("stat", false);
		SchoolCourseERType cat = SchoolCourseERTypeUtil.fetchBytypeId(Long.valueOf(typeId), false);
		if(cat==null){
			object.put("message", "类别不存在！");
		}else {
			cat.setIsDeleted(true);
			SchoolCourseERTypeLocalServiceUtil.updateSchoolCourseERType(cat);
			object.put("stat", true);
			object.put("message", "删除成功！");
		}
		return object.toString();
	}
}