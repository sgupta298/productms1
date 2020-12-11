package com.product.service;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.product.dto.ProductDTO;
import com.product.entity.ProductEntity;
import com.product.repository.ProductRepository;

@Service
public class ProductService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
	ProductRepository prodrepo;
    
    public List<ProductDTO> getProducts(){
    	logger.info("request for product details");
    	List<ProductEntity> proList=prodrepo.findAll();
    	List<ProductDTO> pl=new ArrayList<ProductDTO>();
    	for (ProductEntity p : proList) {
			pl.add(ProductDTO.valueOf(p));
		}
		logger.info("product details", proList);

		return pl;
    }
    
    public List<ProductDTO> getProductsByCategory(@PathVariable String category){
    	logger.info("request for product details by category");
    	List<ProductEntity> proList=prodrepo.findByCategory(category);
    	List<ProductDTO> pl=new ArrayList<ProductDTO>();
    	for (ProductEntity p : proList) {
			pl.add(ProductDTO.valueOf(p));
		}
		logger.info("product details by category", proList);

		return pl;
    }
    
    public List<ProductDTO> getProductsByProductName(@PathVariable String productName){
    	logger.info("request for product details by product name");
    	List<ProductEntity> proList=prodrepo.findByProductname(productName);
    	List<ProductDTO> pl=new ArrayList<ProductDTO>();
    	for (ProductEntity p : proList) {
			pl.add(ProductDTO.valueOf(p));
		}
		logger.info("product details", proList);

		return pl;
    }
    public Boolean addProduct(ProductDTO proDto) {
        ProductEntity p=ProductDTO.creatEntity(proDto);
        ProductEntity prod=prodrepo.save(p);
        if(prod==null) {
        	return false;
        }
        return true;

    }
    public boolean removeProduct(Integer sellerId,Integer prodId) {
    	List<ProductEntity> productEntities=prodrepo.findAll();
    	for(ProductEntity prod:productEntities) {
    		if(prod.getProdid()==prodId) {
    			if(prod.getSellerid()==sellerId) {
    				prodrepo.delete(prod);
    				return true;
    			}
    		}
    	}
    	return false;
    }
    public List<ProductDTO> getProductbyIds(List<Integer> productIds) {
        	List<ProductEntity> productEntities=prodrepo.findAll();
        	List<ProductDTO> productDTOs=new ArrayList<ProductDTO>();
        	for(ProductEntity proEntity:productEntities) {
        		if(productIds.contains(proEntity.getProdid())) {
        			productDTOs.add(ProductDTO.valueOf(proEntity));
        			
        		}
        	}
        	return productDTOs;
    }
}
