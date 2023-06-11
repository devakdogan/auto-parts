package com.ape.controller;

import com.ape.business.abstracts.OrderService;
import com.ape.business.concretes.OrderManager;
import com.ape.entity.dto.OrderDTO;
import com.ape.entity.dto.request.OrderRequest;
import com.ape.entity.dto.response.DataResponse;
import com.ape.entity.dto.response.Response;
import com.ape.entity.dto.response.ResponseMessage;
import com.ape.entity.enums.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;


    @GetMapping("/auth/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Get authenticated user order with order ID")
    public ResponseEntity<OrderDTO> getAuthOrderById(@PathVariable("orderId") Long id) {
        OrderDTO orderDTO = orderService.getOrderByIdAndUser(id);
        return ResponseEntity.ok(orderDTO);
    }



    @GetMapping("/auth")
    @Operation(summary = "Get authenticated user all orders with filter and page")
    public ResponseEntity<Page<OrderDTO>> getAuthOrdersWithPage(@RequestParam("page") int page,
                                                                @RequestParam("size") int size,
                                                                @RequestParam("sort") String prop,
                                                                @RequestParam(value = "direction",
                                                                        required = false, defaultValue = "DESC")Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<OrderDTO> pageDTO = orderService.getAuthOrdersWithPage(pageable);
        return ResponseEntity.ok(pageDTO);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin getting all orders with filter and page")
    public ResponseEntity<PageImpl<OrderDTO>> getAllOrdersWithFilterAndPage(
            @RequestParam(value = "q",required = false) String query,
            @RequestParam(value = "status",required = false) List<OrderStatus> status,
            @RequestParam(value = "startDate",required = false) String startDate,
            @RequestParam(value = "endDate",required = false) String endDate,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam(value = "direction", required = false, defaultValue = "DESC")
            Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        PageImpl<OrderDTO> pageDTO = orderService.getAllOrdersWithFilterAndPage(query, status, startDate, endDate, pageable);
        return ResponseEntity.ok(pageDTO);
    }

    @PostMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Create an order in database")
    public ResponseEntity<Response> createOrder(@RequestHeader("cartUUID") String cartUUID, @Valid @RequestBody OrderRequest orderRequest){
        OrderDTO orderDTO=  orderService.createOrder(cartUUID,orderRequest);
        Response response=new DataResponse<>(ResponseMessage.ORDER_CREATE_RESPONSE,
                true,orderDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/admin/user/{id}")
    @Operation(summary = "Admin getting user orders with user ID, filters and page")
    public ResponseEntity<PageImpl<OrderDTO>> getOrdersWithUserIdAndPage
            (@PathVariable("id") Long userId,
             @RequestParam(value = "status",required = false) List<OrderStatus> status,
             @RequestParam(value = "date1",required = false) String startDate,
             @RequestParam(value = "date2",required = false) String endDate,
             @RequestParam("page") int page,
             @RequestParam("size") int size,
             @RequestParam("sort") String prop,
             @RequestParam(value = "direction", required = false, defaultValue = "DESC")
             Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        PageImpl<OrderDTO> pageDTO = orderService.getOrdersWithUserIdAndPage(userId, startDate, endDate, status, pageable);
        return ResponseEntity.ok(pageDTO);
    }

    @GetMapping("/admin/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin getting order info with order ID")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("orderId") Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/auth/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin updating order status")
    public ResponseEntity<Response> updateOrderStatus(@PathVariable ("orderId") Long orderId, @RequestParam("status") OrderStatus status){
        OrderDTO orderDTO=  orderService.updateOrderStatus(orderId,status);
        Response response=new DataResponse<>(ResponseMessage.ORDER_UPDATE_RESPONSE,true,orderDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
