package org.reportbay.web.common;

import java.sql.Types;

/**
 * 
 * Java JDBC API Sql type int constant to String enum 
 *
 */
public enum SqlTypeEnum {
	ARRAY(Types.ARRAY), 
	BIGINT(Types.BIGINT), 
	BINARY(Types.BINARY), 
	BIT(Types.BIT), 
	BLOB(Types.BLOB), 
	BOOLEAN(Types.BOOLEAN), 
	CHAR(Types.CHAR), 
	CLOB(Types.CLOB), 
	DATALINK(Types.DATALINK), 
	DATE(Types.DATE), 
	DECIMAL(Types.DECIMAL), 
	DISTINCT(Types.DISTINCT), 
	DOUBLE(Types.DOUBLE), 
	FLOAT(Types.FLOAT), 
	INTEGER(Types.INTEGER), 
	JAVA_OBJECT(Types.JAVA_OBJECT), 
	LONGNVARCHAR(Types.LONGNVARCHAR), 
	LONGVARBINARY(Types.LONGVARBINARY), 
	LONGVARCHAR(Types.LONGNVARCHAR), 
	NCHAR(Types.NCHAR), 
	NCLOB(Types.NCLOB), 
	NULL(Types.NULL), 
	NUMERIC(Types.NUMERIC), 
	NVARCHAR(Types.NVARCHAR), 
	OTHER(Types.OTHER), 
	REAL(Types.REAL), 
	REF(Types.REF), 
	ROWID(Types.ROWID), 
	SMALLINT(Types.SMALLINT), 
	SQLXML(Types.SQLXML), 
	STRUCT(Types.STRUCT),
	TIME(Types.TIME), 
	TIMESTAMP(Types.TIMESTAMP), 
	TINYINT(Types.TINYINT), 
	VARBINARY(Types.VARBINARY), 
	VARCHAR(Types.VARCHAR);

	private int sqlTypeConstant;

	/**
	 * 
	 * @param sqlTypeValue
	 */
	private SqlTypeEnum(int sqlTypeValue) {
		this.sqlTypeConstant = sqlTypeValue;
	}

	/**
	 * 
	 * @return
	 */
	public int getSqlTypeValue() {
		return sqlTypeConstant;
	}

	/**
	 * convert to SqlTypeEnum by name
	 * @param name
	 * @return
	 */
	public static SqlTypeEnum fromString(String name){
		SqlTypeEnum enumValue = null;
		
		if(name!=null){
			for(SqlTypeEnum ref : SqlTypeEnum.values()){
				if(name.equals(ref.name())){
					enumValue = ref;
					break;
				}
			}
		}
		
		return enumValue;
	}
	
	/**
	 * convert to SqlTypeEnum by name
	 * @param name
	 * @return
	 */
	public static SqlTypeEnum fromJavaSqlType(int javaSqlType){
		SqlTypeEnum enumValue = null;
		
		for(SqlTypeEnum ref : SqlTypeEnum.values()){
			if(ref.getSqlTypeValue() == javaSqlType){
				enumValue = ref;
				break;
			}
		}
		
		return enumValue;
	}
}