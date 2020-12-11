package com.product.controller;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.product.dto.ProductDTO;
import com.product.dto.ProductIDsDTO;
import com.product.dto.WishlistDTO;
import com.product.service.ProductService;


@RestController
@CrossOrigin
public class ProductController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ProductService proser;
	

	@Autowired
	Environment environment;
	
	
	@GetMapping(value = "/products",  produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProductDTO> getProducts(){
		logger.info("request for products");
		return proser.getProducts();
	}
	@GetMapping(value = "/product/{category}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProductDTO> getProductsByCategory(@PathVariable String category){
		logger.info("request for products");
		return proser.getProductsByCategory(category);
	}
	@GetMapping(value = "/products/{productName}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProductDTO> getProductsByProductName(@PathVariable String productName){
		logger.info("request for products");
		return proser.getProductsByProductName(productName);
	}
	@PostMapping(value = "/productByIds")
	public ResponseEntity<ProductDTO[]> getProductsByIDs(@RequestBody ProductIDsDTO productIds){
		logger.info("request for product details from IDs");
		List<ProductDTO> productDTOs=proser.getProductbyIds(productIds.getProductIds());
		ProductDTO[] products=new ProductDTO[productDTOs.size()];
		for(int i=0;i<products.length;i++) {
			products[i]=productDTOs.get(i);
		}
		return new ResponseEntity<ProductDTO[]>(products,HttpStatus.FOUND);
	}
	@DeleteMapping(value = "/product/delete/{sellerId:.+}/{prodId}")
	public ResponseEntity<String> deleteProductId(@PathVariable("sellerId") Integer sellerId,@PathVariable("prodId") Integer prodId){
		try {
			String msg=null;
			logger.info("request from seller:{} to delete product using ProductId{}",sellerId,prodId);
			Boolean res=proser.removeProduct(sellerId,prodId);
			if(res) {
				msg=environment.getProperty("PRODUCT_REMOVED");
			}
			else {
				msg=environment.getProperty("NOT_REMOVED");
			}
			return new ResponseEntity<String>(msg,HttpStatus.OK);
		}
		catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
		}
	}
	@PostMapping(value = "/product/addProduct",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO){
		try {
			String msg=null;
			logger.info("request to addProduct ");
			Boolean check=new RestTemplate().getForObject("http://localhost:8200//seller/"+productDTO.getSellerid(),Boolean.class);
			if(check) {
				Boolean res=proser.addProduct(productDTO);
				if(res) {
					msg=environment.getProperty("PRODUCT_ADDED");
					return new ResponseEntity<String>(msg,HttpStatus.CREATED);
				}
				else {
					msg=environment.getProperty("ERROR_OCCURED");
					return new ResponseEntity<String>(msg,HttpStatus.BAD_REQUEST);
				}
			}
			else {
				msg=environment.getProperty("NOT_ALLOWED");
				return new ResponseEntity<String>(msg,HttpStatus.BAD_REQUEST);
			}
		}
		catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
		}
	}
	@PostMapping(value="/product/addproductwishlist", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addProductToWishList(@RequestBody WishlistDTO wishDTO){
		try {
			logger.info("request for add product to wishlist");
			String responseWish=new RestTemplate().postForObject("http://localhost:8200/product/addwishlist",wishDTO, String.class);
			return new ResponseEntity<String>(responseWish,HttpStatus.CREATED);
		}
		catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,environment.getProperty(e.getMessage()),e);
		}
	}
	@PostMapping(value="/product/removeproductwishlist", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> removeProductToWishList(@RequestBody WishlistDTO wishDTO){
		try {
			logger.info("request for remove product to wishlist");
			String responseWish=new RestTemplate().postForObject("http://localhost:8200/product/removewishlist",wishDTO, String.class);
			return new ResponseEntity<String>(responseWish,HttpStatus.CREATED);
		}
		catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,environment.getProperty(e.getMessage()),e);
		}
	}
}
