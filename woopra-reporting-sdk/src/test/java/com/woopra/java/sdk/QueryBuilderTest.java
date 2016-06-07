package com.woopra.java.sdk;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.woopra.java.sdk.WoopraQueryBuilder.Query;
import com.woopra.java.sdk.conf.WoopraConfig;

/**
 * 
 * @author abdulhafeth
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WoopraConfig.class })
public class QueryBuilderTest {

	@Autowired
	private WoopraQueryBuilder queryBuilder;

	@Test
	public void testBuildingQuery() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("website", "test.com");
		params.put("startDate", "01/1/2015");
		params.put("endDate", "06/17/2015");
		params.put("limit", "100");
		params.put("offset", "0");
		params.put("isDesc", "true");
		params.put("icon", "misc/chart-3.png");
		params.put("reportLimit", "100");
		params.put("orderBy", "Visits");
		params.put("title", "Referrer Hosts");
		params.put("menu", "Referrers");
		queryBuilder.buildMainQuery(params, Query.MAIN);
	}
}

