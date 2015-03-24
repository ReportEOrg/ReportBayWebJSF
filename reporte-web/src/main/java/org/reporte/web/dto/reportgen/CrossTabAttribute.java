package org.reporte.web.dto.reportgen;

import java.io.Serializable;
import static org.reporte.web.dto.reporttemplate.CrossTabTemplateDetail.GroupAggregateEnum;
import static org.reporte.web.dto.reporttemplate.CrossTabTemplateDetail.FieldTypeEnum;
import org.reporte.web.common.SqlTypeEnum;


public class CrossTabAttribute implements Serializable{
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GroupAggregateEnum groupOrAggregate;
	private FieldTypeEnum fieldType;
	private int attributeDisplaySequence;
	private SqlTypeEnum type;
	private ColumnMetaData metaData;
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
	 * @return the type
	 */
	public SqlTypeEnum getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(SqlTypeEnum type) {
		this.type = type;
	}
	/**
	 * @return the metaData
	 */
	public ColumnMetaData getMetaData() {
		return metaData;
	}
	/**
	 * @param metaData the metaData to set
	 */
	public void setMetaData(ColumnMetaData metaData) {
		this.metaData = metaData;
	}
}