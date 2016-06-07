package com.woopra.java.sdk;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woopra.java.sdk.entities.Aggregation;
import com.woopra.java.sdk.entities.Column;
import com.woopra.java.sdk.entities.Constraints;
import com.woopra.java.sdk.entities.Did;
import com.woopra.java.sdk.entities.DidFilter;
import com.woopra.java.sdk.entities.Filter;
import com.woopra.java.sdk.entities.Goal;
import com.woopra.java.sdk.entities.GroupBy;
import com.woopra.java.sdk.entities.Segment;
import com.woopra.java.sdk.entities.Timeframe;
import com.woopra.java.sdk.entities.Transform;
import com.woopra.java.sdk.entities.WoopraFunnelRequest;
import com.woopra.java.sdk.entities.WoopraReportRequest;

/**
 * 
 * @author abdulhafeth
 *
 */
@Service
public class WoopraQueryBuilder {

	@Autowired
	private VelocityEngine velocityEngine;

	@SuppressWarnings("unchecked")
	public String buildQuery(Map<Query, Object> quries) {

		Map<String, Object> mainQueryParams = (Map<String, Object>) quries
				.get(Query.MAIN);

		for (Query key : quries.keySet()) {
			if (key.equals(Query.MAIN)) {
				continue;
			}

			Object params = quries.get(key);
			if (params instanceof List) {
				List<Map<String, Object>> castedListParams = (List<Map<String, Object>>) params;
				List<String> properties = new ArrayList<String>();

				for (Map<String, Object> param : castedListParams) {
					/* next, get the Template */
					Template t = velocityEngine.getTemplate(key.getPath());

					/* create a context and add data */
					VelocityContext context = map(param);

					/* now render the template into a StringWriter */
					StringWriter writer = new StringWriter();
					t.merge(context, writer);
					properties.add(writer.toString());
				}
				mainQueryParams.put(key.getName(), properties);

			} else {
				Map<String, Object> castedMapParams = (Map<String, Object>) params;
				/* next, get the Template */
				Template t = velocityEngine.getTemplate(key.getPath());

				/* create a context and add data */
				VelocityContext context = map(castedMapParams);

				/* now render the template into a StringWriter */
				StringWriter writer = new StringWriter();
				t.merge(context, writer);
				mainQueryParams.put(key.getName(), writer.toString());
			}
		}

		return buildMainQuery(mainQueryParams, Query.MAIN);
	}

	public String buildMainQuery(Map<String, Object> params, Query query) {

		/* next, get the Template */
		Template t = velocityEngine.getTemplate(query.getPath());

		/* create a context and add data */
		VelocityContext context = map(params);

		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		/* show the World */
		System.out.println(writer.toString());
		return writer.toString();
	}

	private VelocityContext map(Map<String, Object> params) {
		VelocityContext context = new VelocityContext();

		params = mapNullProperties(params);
		for (String key : params.keySet()) {
			context.put(key, params.get(key));
		}
		return context;
	}

	private Map<String, Object> mapNullProperties(Map<String, Object> params) {

		for (QueryProperties property : QueryProperties.values()) {
			if (!params.containsKey(property.getValue())) {
				params.put(property.getValue(), "");
			}
		}
		return params;
	}

	private List<String> buildColumnsQuery(List<Column> columns) {

		List<String> columnProperties = new ArrayList<String>();

		for (Column column : columns) {
			columnProperties.add(buildColumnQuery(column));
		}

		return columnProperties;
	}

	private String buildColumnQuery(Column column) {
		/* next, get the Template */
		Template t = velocityEngine.getTemplate(Query.COLUMNS.getPath());

		/* create a context and add data */
		VelocityContext context = new VelocityContext(); // map(params);

		context.put(QueryProperties.METHOD.getValue(),
				column.getMethod() == null ? "" : column.getMethod());
		context.put(QueryProperties.SCOPE.getValue(),
				column.getScope() == null ? "" : column.getScope());
		context.put(QueryProperties.NAME.getValue(),
				column.getName() == null ? "" : column.getName());
		context.put(QueryProperties.FORMAT.getValue(),
				column.getFormat() == null ? "" : column.getFormat());
		context.put(QueryProperties.RENDER.getValue(),
				column.getRender() == null ? "" : column.getRender());
		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	private List<String> buildTransformsQuery(List<Transform> transforms) {

		List<String> transformProperties = new ArrayList<String>();

		for (Transform transform : transforms) {
			transformProperties.add(buildTransformQuery(transform));
		}

		return transformProperties;
	}

	private String buildTransformQuery(Transform transfrom) {
		/* next, get the Template */
		Template t = velocityEngine.getTemplate(Query.TRANSFORM.getPath());

		/* create a context and add data */
		VelocityContext context = new VelocityContext(); // map(params);

		context.put(QueryProperties.FUNCTION.getValue(),
				transfrom.getFunction() == null ? "" : transfrom.getFunction());
		context.put(QueryProperties.PARAMS.getValue(),
				transfrom.getParams() == null ? "" : transfrom.getParams());

		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	private List<String> buildGroupByItemsQuery(List<GroupBy> groupByItems) {
		List<String> groupByProperties = new ArrayList<String>();

		for (GroupBy groupByItem : groupByItems) {
			groupByProperties.add(buildGroupByQuery(groupByItem));
		}

		return groupByProperties;
	}

	private String buildGroupByQuery(GroupBy groupByItem) {
		/* next, get the Template */
		Template t = velocityEngine.getTemplate(Query.GROUP_BY_ITEMS.getPath());

		/* create a context and add data */
		VelocityContext context = new VelocityContext(); // map(params);

		context.put(QueryProperties.SCOPE.getValue(),
				groupByItem.getScope() == null ? "" : groupByItem.getScope());
		context.put(QueryProperties.KEY.getValue(),
				groupByItem.getKey() == null ? "" : groupByItem.getKey());
		context.put(QueryProperties.TRANSFROMS.getValue(), groupByItem
				.getTransforms() == null ? ""
				: buildTransformsQuery(groupByItem.getTransforms()));
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	private List<String> buildFiltersQuery(List<Filter> filters) {
		List<String> filterProperties = new ArrayList<String>();

		for (Filter filter : filters) {
			filterProperties.add(buildFilterQuery(filter));
		}

		return filterProperties;
	}

	private String buildFilterQuery(Filter filter) {
		/* next, get the Template */
		Template t = velocityEngine.getTemplate(Query.FILTERS.getPath());

		/* create a context and add data */
		VelocityContext context = new VelocityContext(); // map(params);

		context.put(QueryProperties.SCOPE.getValue(),
				filter.getScope() == null ? "" : filter.getScope());
		context.put(QueryProperties.KEY.getValue(),
				filter.getKey() == null ? "" : filter.getKey());
		context.put(QueryProperties.MATCH.getValue(),
				filter.getMatch() == null ? "" : filter.getMatch());
		context.put(QueryProperties.VALUE.getValue(),
				filter.getValue() == null ? "" : filter.getValue());
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	private List<String> buildSegmentsQuery(List<Segment> segments) {
		if (segments == null) {
			return new ArrayList<String>();
		}

		List<String> segmentProperties = new ArrayList<String>();

		for (Segment segment : segments) {
			segmentProperties.add(buildSegmentQuery(segment));
		}

		return segmentProperties;
	}

	private List<String> buildDidFiltersQuery(List<DidFilter> didFilters) {
		List<String> didFilterProperties = new ArrayList<String>();

		for (DidFilter didFilter : didFilters) {
			didFilterProperties.add(buildDidFilterQuery(didFilter));
		}

		return didFilterProperties;
	}

	private String buildTimeframeQuery(Timeframe timeframe) {
		/* next, get the Template */
		Template t = velocityEngine.getTemplate(Query.TIMEFRAME.getPath());

		/* create a context and add data */
		VelocityContext context = new VelocityContext(); // map(params);

		context.put(QueryProperties.METHOD.getValue(),
				timeframe.getMethod() == null ? "" : timeframe.getMethod());
		context.put(QueryProperties.FROM.getValue(),
				timeframe.getFrom() == null ? "" : timeframe.getFrom());
		context.put(QueryProperties.TO.getValue(),
				timeframe.getTo() == null ? "" : timeframe.getTo());
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	private String buildAggregationQuery(Aggregation aggregation) {
		/* next, get the Template */
		Template t = velocityEngine.getTemplate(Query.AGGREGATION.getPath());

		/* create a context and add data */
		VelocityContext context = new VelocityContext(); // map(params);

		context.put(QueryProperties.METHOD.getValue(),
				aggregation.getMethod() == null ? "" : aggregation.getMethod());
		context.put(QueryProperties.BY.getValue(),
				aggregation.getBy() == null ? "" : aggregation.getBy());
		context.put(QueryProperties.MATCH.getValue(),
				aggregation.getMatch() == null ? "" : aggregation.getMatch());
		context.put(QueryProperties.VALUE.getValue(),
				aggregation.getValue() == null ? "" : aggregation.getValue());
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	private String buildDidFilterQuery(DidFilter didFilter) {
		/* next, get the Template */
		Template t = velocityEngine.getTemplate(Query.DID_FILTER.getPath());

		/* create a context and add data */
		VelocityContext context = new VelocityContext(); // map(params);

		context.put(QueryProperties.ACTION.getValue(),
				didFilter.getAction() == null ? ""
						: buildConstraintsQuery(didFilter.getAction()));
		context.put(QueryProperties.VISIT.getValue(),
				didFilter.getVisit() == null ? ""
						: buildConstraintsQuery(didFilter.getVisit()));
		context.put(QueryProperties.TIMEFRAME.getValue(),
				didFilter.getTimeframe() == null ? ""
						: buildTimeframeQuery(didFilter.getTimeframe()));
		context.put(QueryProperties.AGGREGATION.getValue(), didFilter
				.getAggregation() == null ? ""
				: buildAggregationQuery(didFilter.getAggregation()));
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	private String buildDidQuery(Did did) {
		/* next, get the Template */
		Template t = velocityEngine.getTemplate(Query.DID.getPath());

		/* create a context and add data */
		VelocityContext context = new VelocityContext(); // map(params);

		context.put(
				QueryProperties.FILTERS.getValue(),
				did.getFilters() == null ? "" : buildDidFiltersQuery(did
						.getFilters()));
		context.put(QueryProperties.OPERATOR.getValue(),
				did.getOperator() == null ? "" : did.getOperator());
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	private String buildSegmentQuery(Segment segment) {
		/* next, get the Template */
		Template t = velocityEngine.getTemplate(Query.SEGMENT.getPath());

		/* create a context and add data */
		VelocityContext context = new VelocityContext(); // map(params);

		context.put(
				QueryProperties.ARE.getValue(),
				segment.getAre() == null ? "" : buildConstraintsQuery(segment
						.getAre()));
		context.put(QueryProperties.DID.getValue(),
				segment.getDid() == null ? "" : ",did:{"
						+ buildDidQuery(segment.getDid()) + "}");
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	private String buildConstraintsQuery(Constraints constraints) {
		if (constraints == null) {
			return null;
		}

		/* next, get the Template */
		Template t = velocityEngine.getTemplate(Query.CONSTRAINTS.getPath());

		/* create a context and add data */
		VelocityContext context = new VelocityContext(); // map(params);

		context.put(QueryProperties.FILTERS.getValue(),
				constraints.getFilters() == null ? ""
						: buildFiltersQuery(constraints.getFilters()));
		context.put(QueryProperties.OPERATOR.getValue(), constraints
				.getOperator() == null ? "" : constraints.getOperator());
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	private String buildGoalQuery(Goal goal) {
		/* next, get the Template */
		Template t = velocityEngine.getTemplate(Query.GOAL.getPath());

		/* create a context and add data */
		VelocityContext context = new VelocityContext(); // map(params);

		context.put(QueryProperties.OPERATOR.getValue(),
				goal.getOperator() == null ? "" : goal.getOperator());
		context.put(QueryProperties.NAME.getValue(),
				goal.getName() == null ? "" : goal.getName());
		context.put(
				QueryProperties.FILTERS.getValue(),
				goal.getFilters() == null ? "" : buildFiltersQuery(goal
						.getFilters()));
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	private List<String> buildGoalsQuery(List<Goal> goals) {
		if (goals == null) {
			return new ArrayList<String>();
		}

		List<String> segmentProperties = new ArrayList<String>();

		for (Goal goal : goals) {
			segmentProperties.add(buildGoalQuery(goal));
		}

		return segmentProperties;
	}

	public String buildQuery(@Valid @NotNull WoopraReportRequest woopraRequest) {
		Map<String, Object> params = new HashMap<String, Object>();

		// building columns
		List<String> columns = buildColumnsQuery(woopraRequest.getColumns());
		if (columns.isEmpty()) {
			params.put(QueryProperties.COLUMNS.getValue(), "");
		} else {
			params.put(QueryProperties.COLUMNS.getValue(), columns);
		}

		// building groupBy
		List<String> groupByItems = buildGroupByItemsQuery(woopraRequest
				.getGroupByItems());
		if (groupByItems.isEmpty()) {
			params.put(QueryProperties.GROUPBY_ITEMS.getValue(), "");
		} else {
			params.put(QueryProperties.GROUPBY_ITEMS.getValue(), groupByItems);
		}

		// building segments query
		List<String> segments = buildSegmentsQuery(woopraRequest.getSegments());
		if (segments.isEmpty()) {
			params.put(QueryProperties.SEGMENTS.getValue(), "");
		} else {
			params.put(QueryProperties.SEGMENTS.getValue(), segments);
		}

		// building Constraints
		String constraints = buildConstraintsQuery(woopraRequest
				.getConstraints());
		params.put(QueryProperties.CONSTRAINTS.getValue(),
				constraints == null ? "" : ", constraints:" + constraints);

		params.put(QueryProperties.ORDERBY.getValue(),
				woopraRequest.getOrderBy());
		params.put(QueryProperties.WEBSITE.getValue(),
				woopraRequest.getWebsite());
		params.put(QueryProperties.DATE_FORMAT.getValue(),
				woopraRequest.getDateFormat());
		params.put(QueryProperties.START_DATE.getValue(),
				woopraRequest.getStartDate());
		params.put(QueryProperties.END_DATE.getValue(),
				woopraRequest.getEndDate());
		params.put(QueryProperties.LIMIT.getValue(), woopraRequest.getLimit());
		params.put(QueryProperties.OFFSET.getValue(), woopraRequest.getOffset());
		params.put(QueryProperties.RENDER.getValue(), woopraRequest.getRender());
		params.put(QueryProperties.TITLE.getValue(), woopraRequest.getTitle());
		params.put(QueryProperties.MENU.getValue(), woopraRequest.getMenu());
		params.put(QueryProperties.ICON.getValue(), woopraRequest.getIcon());
		params.put(QueryProperties.REPORT_LIMIT.getValue(),
				woopraRequest.getReportLimit());
		params.put(QueryProperties.IS_DESC.getValue(),
				woopraRequest.getIsDesc());

		return buildMainQuery(params, Query.MAIN);
	}

	public String buildQuery(@Valid @NotNull WoopraFunnelRequest woopraRequest) {
		Map<String, Object> params = new HashMap<String, Object>();

		// building groupBy
		String groupByItems = buildGroupByQuery(woopraRequest.getGroupBy());
		params.put(QueryProperties.GROUPBY_ITEMS.getValue(), groupByItems);

		// building segments query
		List<String> segments = buildSegmentsQuery(woopraRequest.getSegments());
		if (segments.isEmpty()) {
			params.put(QueryProperties.SEGMENTS.getValue(), "");
		} else {
			params.put(QueryProperties.SEGMENTS.getValue(), segments);
		}

		// building Constraints
		List<String> goals = buildGoalsQuery(woopraRequest.getGoals());
		params.put(QueryProperties.GOALS.getValue(), goals == null ? "" : goals);

		params.put(QueryProperties.ORDERBY.getValue(),
				woopraRequest.getOrderBy());
		params.put(QueryProperties.WEBSITE.getValue(),
				woopraRequest.getWebsite());
		params.put(QueryProperties.DATE_FORMAT.getValue(),
				woopraRequest.getDateFormat());
		params.put(QueryProperties.START_DATE.getValue(),
				woopraRequest.getStartDate());
		params.put(QueryProperties.END_DATE.getValue(),
				woopraRequest.getEndDate());
		params.put(QueryProperties.LIMIT.getValue(), woopraRequest.getLimit());

		return buildMainQuery(params, Query.FUNNEL);
	}

	/**
	 * Enum that has all the attributes that we should replace in velocity
	 * template
	 * 
	 * @author abdulhafeth
	 *
	 */
	public enum QueryProperties {
		START_DATE("startDate"), END_DATE("endDate"), LIMIT("limit"), OFFSET(
				"offset"), SEGMENTS("segments"), IS_DESC("isDesc"), COLUMNS(
				"columns"), ICON("icon"), REPORT_LIMIT("reportLimit"), GROUPBY_ITEMS(
				"groupByItems"), ORDERBY("orderBy"), TITLE("title"), MENU(
				"menu"), METHOD("method"), SCOPE("scope"), NAME("name"), FORMAT(
				"format"), RENDER("render"), KEY("key"), TRANSFROMS(
				"transforms"), FUNCTION("function"), PARAMS("params"), WEBSITE(
				"website"), DATE_FORMAT("dateFormat"), FILTERS("filters"), OPERATOR(
				"operator"), MATCH("match"), VALUE("value"), CONSTRAINTS(
				"constraints"), ARE("are"), DID("did"), ACTION("action"), VISIT(
				"visit"), TIMEFRAME("timeframe"),  AGGREGATION("aggregation"), FROM("from"), TO("to"), BY(
				"by"), GOALS("goals");

		private String value;

		QueryProperties(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	public enum Query {
		COLUMNS("columns", "woopra/column.vm"), CONSTRAINTS("constraints",
				"woopra/constraints.vm"), FILTERS("filter", "woopra/filter.vm"), GROUP_BY_ITEMS(
				"groupByItems", "woopra/group-by-item.vm"), MAIN("main",
				"woopra/main.vm"), TRANSFORM("transforms",
				"woopra/transform.vm"), SEGMENT("segments", "woopra/segment.vm"), DID_FILTER(
				"filters", "woopra/did-filter.vm"), AGGREGATION("aggregation",
				"woopra/aggregation.vm"), DID("did", "woopra/did.vm"), TIMEFRAME(
				"timeframe", "woopra/timeframe.vm"), GOAL("goal",
				"woopra/goal.vm"), FUNNEL("funnel", "woopra/funnel.vm");
		private String name;
		private String path;

		Query(String name, String path) {
			this.name = name;
			this.path = path;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the path
		 */
		public String getPath() {
			return path;
		}

	}
}
