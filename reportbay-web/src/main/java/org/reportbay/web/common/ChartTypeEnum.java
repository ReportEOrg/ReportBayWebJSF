package org.reportbay.web.common;


/**
 * 
 * Chart type enum with mapping to primefaces <p:chart type value 
 *
 */
public enum ChartTypeEnum{
	
	AREA("line","Area Chart"),
	BAR("bar", "Bar Chart"),
	COLUMN("bar", "Column Chart"),
	LINE("line", "Line Chart"),
	PIE("pie", "Pie Chart");
	
	private String code;
	private String description;
	
	/**
	 * 
	 * @param code
	 * @param description
	 */
	private ChartTypeEnum(String code, String description){
		this.code = code;
		this.description = description;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCode(){
		return code;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel(){
		return this.name();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static ChartTypeEnum fromName(String name){
		ChartTypeEnum enumValue = null;
				
		if(name!=null){
			for(ChartTypeEnum ref: ChartTypeEnum.values()){
				if(name.equals(ref.name())){
					enumValue = ref;
					break;
				}
			}
		}
				
		return enumValue;
	}
}