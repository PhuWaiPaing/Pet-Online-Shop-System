package com.ci6225.springboot.shoppingcart.dao;

import java.io.IOException;
import java.util.Date;

import javax.persistence.NoResultException;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ci6225.springboot.shoppingcart.entity.Puppy;
import com.ci6225.springboot.shoppingcart.form.ProductForm;
import com.ci6225.springboot.shoppingcart.model.ProductInfo;
import com.ci6225.springboot.shoppingcart.pagination.Pagination;

@Transactional
@Repository
public class ProductDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
    public Puppy findProduct(String code) {
        try {
            String sql = "Select e from " + Puppy.class.getName() + " e Where e.code =:code ";
 
            Session session = this.sessionFactory.getCurrentSession();
            Query<Puppy> query = session.createQuery(sql, Puppy.class);
            query.setParameter("code", code);
            return (Puppy) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
	
	public ProductInfo findProductInfo(String code) {
		Puppy puppy = this.findProduct(code);
		if (puppy == null) {
			return null;
		}
		return new ProductInfo(puppy.getCode(),puppy.getName(),puppy.getGender(), puppy.getPrice());
	}
	
	@Transactional
	public void save(ProductForm productForm) {
		Session session = this.sessionFactory.getCurrentSession();
		String code = productForm.getPuppyCode();
		Puppy puppy = null;
		
		boolean isNew = false;
		if (code != null) {
			puppy = this.findProduct(code); 
		}
		if (puppy == null) {
			isNew = true;
			puppy = new Puppy();
			puppy.setCreateDate(new Date());
		}
		puppy.setCode(code);
		puppy.setName(productForm.getPuppyName());
		puppy.setGender(productForm.getPuppyGender());
		puppy.setPrice(productForm.getPuppyPrice());
		
		if (productForm.getFile() != null) {
			byte[] image = null;
	        try {
				image = productForm.getFile().getBytes();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	            if (image != null && image.length > 0) {
	                puppy.setImage(image);
	            }
	        }
	        if (isNew) {
	            session.persist(puppy);
	        }
		session.flush();
	}
	
	public Pagination<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage,
            String likeName) {
        String sql = "Select new " + ProductInfo.class.getName()
                + "(p.code, p.name, p.gender, p.price) " + " from "
                + Puppy.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        sql += " order by p.createDate desc ";
        //
        Session session = this.sessionFactory.getCurrentSession();
 
        Query<ProductInfo> query = session.createQuery(sql,ProductInfo.class);
        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new Pagination<ProductInfo>(query, page, maxResult, maxNavigationPage);
    }
 
    public Pagination<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage) {
        return queryProducts(page, maxResult, maxNavigationPage, null);
    }

}


