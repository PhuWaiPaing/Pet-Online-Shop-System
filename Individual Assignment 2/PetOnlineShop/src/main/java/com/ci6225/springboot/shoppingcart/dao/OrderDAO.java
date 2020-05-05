package com.ci6225.springboot.shoppingcart.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ci6225.springboot.shoppingcart.dao.ProductDAO;
import com.ci6225.springboot.shoppingcart.entity.Order;
import com.ci6225.springboot.shoppingcart.entity.OrderDetails;
import com.ci6225.springboot.shoppingcart.entity.Puppy;
import com.ci6225.springboot.shoppingcart.model.CartInfo;
import com.ci6225.springboot.shoppingcart.model.CartLineInfo;
import com.ci6225.springboot.shoppingcart.model.CustomerInfo;
import com.ci6225.springboot.shoppingcart.model.OrderDetailsInfo;
import com.ci6225.springboot.shoppingcart.model.OrderInfo;
import com.ci6225.springboot.shoppingcart.pagination.Pagination;


@Transactional
@Repository
public class OrderDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ProductDAO productDAO;
	
    private int getMaxOrderNum() {
        String sql = "Select max(o.orderNum) from " + Order.class.getName() + " o ";
        Session session = this.sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery(sql,Integer.class);
        Integer value = (Integer) query.uniqueResult();
        if (value == null) {
            return 0;
        }
        return value;
    }
    public void saveOrder(CartInfo cartInfo) {
        Session session = this.sessionFactory.getCurrentSession();
 
        int orderNum = this.getMaxOrderNum() + 1;
        Order order = new Order();
 
        order.setId(UUID.randomUUID().toString());
        order.setOrderNum(orderNum);
        order.setOrderDate(new Date());
        order.setAmount(cartInfo.getAmountTotal());
 
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        order.setCustomerName(customerInfo.getName());
        order.setCustomerEmail(customerInfo.getEmail());
        order.setCustomerPhone(customerInfo.getPhone());
        order.setCustomerAddress(customerInfo.getAddress());
 
        session.persist(order);
 
        List<CartLineInfo> lines = cartInfo.getCart();
 
        for (CartLineInfo line : lines) {
            OrderDetails detail = new OrderDetails();
            detail.setId(UUID.randomUUID().toString());
            detail.setOrder(order);
            detail.setAmount(line.getAmount());
            detail.setPrice(line.getProductInfo().getPuppyPrice());
            detail.setQuantity(line.getQuantity());
 
            String code = line.getProductInfo().getPuppyCode();
            Puppy puppy = this.productDAO.findProduct(code);
            detail.setPuppy(puppy);
 
            session.persist(detail);
        }
 
        // Set OrderNum for report.
        cartInfo.setOrderNum(orderNum);
    }
	
	   public Pagination<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {
	        String sql = "Select new " + OrderInfo.class.getName()
	                + "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
	                + " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone) " + " from "
	                + Order.class.getName() + " ord "
	                + " order by ord.orderNum desc";
	        Session session = this.sessionFactory.getCurrentSession();
	 
	        Query<OrderInfo> query = session.createQuery(sql,OrderInfo.class);
	 
	        return new Pagination<OrderInfo>(query, page, maxResult, maxNavigationPage);
	    }
	   
	public Order findOrder(String orderId) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Order.class,orderId);
	}
	
	public OrderInfo getOrderInfo(String orderId) {
		Order order = this.findOrder(orderId);
		if (order == null) {
			return null;
		}
		return new OrderInfo(order.getId(), order.getOrderDate(), order.getOrderNum(),
				order.getAmount(),order.getCustomerAddress(),order.getCustomerEmail(),order.getCustomerName(),
				order.getCustomerPhone());
	}
	
	public List<OrderDetailsInfo> listOrderDetails(String orderId){
		String sql = "Select new " + OrderDetailsInfo.class.getName() + "(d.id,d.puppy.code, d.puppy.name,d.puppy.gender,d.quantity, d.price, d.amount) "
						+ " from "+ OrderDetails.class.getName()+" d " + " where d.order.id = :orderId";
		Session session = this.sessionFactory.getCurrentSession();
		Query<OrderDetailsInfo> q = session.createQuery(sql,OrderDetailsInfo.class);
		q.setParameter("orderId", orderId);
		return q.getResultList();
	}

}
