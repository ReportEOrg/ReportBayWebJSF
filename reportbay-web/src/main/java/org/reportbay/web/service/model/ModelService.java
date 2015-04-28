package org.reportbay.web.service.model;

import java.io.Serializable;
import java.util.List;

import org.reportbay.web.dto.model.Model;
import org.reportbay.web.dto.model.ModelPreviewResult;
import org.reportbay.web.service.model.exception.ModelServiceException;


public interface ModelService extends Serializable{
	
	/**
	 * 
	 * @param model
	 * @return
	 * @throws ModelServiceException if there is any problem during the process.
	 * @throws IllegalArgumentException if <code>model</code> is <code>null</code>.
	 */
	Model save(Model model) throws ModelServiceException;
	/**
	 * 
	 * @param model
	 * @return
	 * @throws ModelServiceException if there is any problem during the process.
	 * @throws IllegalArgumentException if <code>model</code> is <code>null</code>.
	 */
	Model update(Model model) throws ModelServiceException;
	/**
	 * 
	 * @param model
	 * @return
	 * @throws ModelServiceException if there is any problem during the process.
	 * @throws IllegalArgumentException if <code>model</code> is <code>null</code>.
	 */
	boolean delete(Model model) throws ModelServiceException;
	/**
	 * 
	 * @return
	 * @throws ModelServiceException
	 */
	List<Model> findAll() throws ModelServiceException;
	
	/**
	 * 
	 * @return
	 * @throws ModelServiceException
	 */
	List<Model> findAllOrderByDatasourceName() throws ModelServiceException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws ModelServiceException
	 */
	Model find(int id) throws ModelServiceException;
	
	/**
	 * 
	 * @param model
	 * @return
	 * @throws ModelServiceException
	 */
	Model deriveModelQueryAttributes(Model model) throws ModelServiceException;
	
	/**
	 * 
	 * @param model
	 * @param maxRow
	 * @return
	 * @throws ModelServiceException
	 */
	ModelPreviewResult generateModelPreview(Model model, int maxRow) throws ModelServiceException;
	
	/**
	 * 
	 * @param modelId
	 * @param fieldName
	 * @return
	 * @throws ModelServiceException
	 */
	List<String> getModelFieldUniqueValue(int modelId, String fieldName) throws ModelServiceException;
}