package com.ape.business.abstracts;

import com.ape.entity.concrete.AddressEntity;
import com.ape.entity.dto.OrderDTO;
import com.ape.entity.dto.request.OrderRequest;
import com.ape.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderDTO getOrderByIdAndUser(Long id);
    Page<OrderDTO> getAuthOrdersWithPage(Pageable pageable);
    PageImpl<OrderDTO> getAllOrdersWithFilterAndPage(String query, List<OrderStatus> status, String startDate, String endDate, Pageable pageable);
    OrderDTO createOrder(String cartUUID, OrderRequest orderRequest);
    PageImpl<OrderDTO> getOrdersWithUserIdAndPage(Long userId, String startDate, String endDate, List<OrderStatus> status, Pageable pageable);
    OrderDTO getOrderById(Long id);
    OrderDTO updateOrderStatus(Long orderId, OrderStatus status);
    long countOrderRecords();
    boolean existsByInvoiceAddress(AddressEntity address);
    boolean existsByShippingAddress(AddressEntity address);
}
