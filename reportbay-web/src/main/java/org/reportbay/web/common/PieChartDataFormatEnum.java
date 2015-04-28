package org.reportbay.web.common;


/**
 * 
 * Pie chart data format type enum with mapping to primefaces PieModel.dataFormat 
 *
 */
public enum PieChartDataFormatEnum{
	
	VALUE("value"),
	PERCENT("percent");
	
	private String code;
	
	/**
	 * 
	 * @param code
	 */
	private PieChartDataFormatEnum(String code){
		this.code = code;
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
	public String getLabel(){
		return this.name();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static PieChartDataFormatEnum fromName(String name){
		PieChartDataFormatEnum enumValue = null;
				
		if(name!=null){
			for(PieChartDataFormatEnum ref: PieChartDataFormatEnum.values()){
				if(name.equals(ref.name())){
					enumValue = ref;
					break;
				}
			}
		}
				
		return enumValue;
	}
}