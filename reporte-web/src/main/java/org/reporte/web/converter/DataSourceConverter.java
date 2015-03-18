package org.reporte.web.converter;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

import org.reporte.web.dto.datasource.DataSource;

@Named
@ApplicationScoped
public class DataSourceConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String submittedValue) {
		DataSource selectedDataSource = null;
		
		if (submittedValue == null || submittedValue.isEmpty()) {
			return null;
		}

		//convert select value to integer
		int selectedId = Integer.valueOf(submittedValue);
		
		//uiComponent = selectOneMenu
		if(uiComponent!=null){
			List<UIComponent> childs = uiComponent.getChildren();
			
			UISelectItems items = null;
			
			//if contain option(s) under the selectOneMenu component
			if (childs != null) {
				//loop through each component
                for (UIComponent ui : childs) {
                	
                	if (ui instanceof UISelectItems) {
                		items = (UISelectItems) ui;
                        break;
                    }
                	//if it is a selectItem component
                	else if (ui instanceof UISelectItem) {
                        UISelectItem item = (UISelectItem) ui;
                        try {
                            Object value = item.getItemValue();
                            
                            if(value instanceof DataSource){
                            	DataSource refValue = (DataSource)value;
                            	
                            	if(selectedId == refValue.getId()){
                            		//match and exit
                            		selectedDataSource = refValue;
                            		break;
                            	}
                            }
                        } 
                        catch (Exception e) {
                        	//TODO:
                        }
                	}
                } //for-loop
			}
			
			//if found selectitems component
			if(items!=null){
				try {
					@SuppressWarnings("unchecked")
					List<DataSource> dataSources = (List<DataSource>)items.getValue();
				
					if (dataSources  != null) {
						for(DataSource datasource: dataSources){
							if(datasource!=null && selectedId == datasource.getId()){
								//match and exit
                        		selectedDataSource = datasource;
                        		break;
                        	}
						}
					}
				} 
				catch (Exception e) {
                 	//TODO:
				}
			}
		}
		
		return selectedDataSource;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object selectedOption) {
		if (selectedOption == null) {
			return "";
		}

		if (selectedOption instanceof DataSource) {
			return String.valueOf(((DataSource) selectedOption).getId());
		} else {
			throw new ConverterException(new FacesMessage(String.format("%s is not a valid Datasource.", selectedOption)));
		}
	}

}
