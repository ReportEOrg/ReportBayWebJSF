package org.reporte.web.dto.reporttemplate;

import java.io.Serializable;

import org.reporte.web.common.SqlTypeEnum;

public class CrossTabTemplateDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String attributeDisplayName;
	private int attributeDisplaySequence;
	private FieldTypeEnum fieldType;
	private GroupAggregateEnum groupOrAggregate;
	private String modelAttributeName;
	private SqlFunctionEnum sqlFunction;
	private SqlTypeEnum sqltype;
	
	/**
	 * @return the attributeDisplayName
	 */
	public String getAttributeDisplayName() {
		return attributeDisplayName;
	}

	/**
	 * @param attributeDisplayName the attributeDisplayName to set
	 */
	public void setAttributeDisplayName(String attributeDisplayName) {
		this.attributeDisplayName = attributeDisplayName;
	}

	/**
	 * @return the attributeDisplaySequence
	 */
	public int getAttributeDisplaySequence() {
		return attributeDisplaySequence;
	}

	/**
	 * @param attributeDisplaySequence the attributeDisplaySequence to set
	 */
	public void setAttributeDisplaySequence(int attributeDisplaySequence) {
		this.attributeDisplaySequence = attributeDisplaySequence;
	}

	/**
	 * @return the fieldType
	 */
	public FieldTypeEnum getFieldType() {
		return fieldType;
	}

	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(FieldTypeEnum fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * @return the groupOrAggregate
	 */
	public GroupAggregateEnum getGroupOrAggregate() {
		return groupOrAggregate;
	}

	/**
	 * @param groupOrAggregate the groupOrAggregate to set
	 */
	public void setGroupOrAggregate(GroupAggregateEnum groupOrAggregate) {
		this.groupOrAggregate = groupOrAggregate;
	}

	/**
	 * @return the modelAttributeName
	 */
	public String getModelAttributeName() {
		return modelAttributeName;
	}

	/**
	 * @param modelAttributeName the modelAttributeName to set
	 */
	public void setModelAttributeName(String modelAttributeName) {
		this.modelAttributeName = modelAttributeName;
	}

	/**
	 * @return the sqlFunction
	 */
	public SqlFunctionEnum getSqlFunction() {
		return sqlFunction;
	}

	/**
	 * @param sqlFunction the sqlFunction to set
	 */
	public void setSqlFunction(SqlFunctionEnum sqlFunction) {
		this.sqlFunction = sqlFunction;
	}

	/**
	 * @return the sqltype
	 */
	public SqlTypeEnum getSqltype() {
		return sqltype;
	}

	/**
	 * @param sqltype the sqltype to set
	 */
	public void setSqltype(SqlTypeEnum sqltype) {
		this.sqltype = sqltype;
	}
	
	public enum FieldTypeEnum{
		ROW("Row"),
		COLUMN("Column");

		private String type;
		
		private FieldTypeEnum(String type){
			this.type=type;
		}
		
		public String getType(){
			return this.type;
		}
	}
	
	public enum GroupAggregateEnum{
		GROUPING("grouping"),
		AGGREGATE("aggregate");
		
		private String value;
		
		private GroupAggregateEnum(String value){
			this.value = value;
		}
		
		public String getValue(){
			return this.value;
		}
	}
	
	public enum SqlFunctionEnum{
		GROUPBY("groupby"),
		AVG("avg"),
		COUNT("count"),
		FIRST("first"),
		LAST("last"),
		MAX("max"),
		MIN("min"),
		SUM("sum");
		
		private String value;
		
		private SqlFunctionEnum(String value){
			this.value=value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
}