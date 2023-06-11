package com.ape.business.concretes;

import com.ape.business.abstracts.*;
import com.ape.entity.concrete.*;
import com.ape.entity.dao.*;
import com.ape.entity.dto.OrderDTO;
import com.ape.entity.dto.request.OrderRequest;
import com.ape.entity.enums.OrderStatus;
import com.ape.entity.enums.PaymentStatus;
import com.ape.entity.enums.TransactionStatus;
import com.ape.exception.BadRequestException;
import com.ape.exception.ErrorMessage;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.OrderMapper;
import com.ape.utility.UniqueIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderManager implements OrderService {
    private final OrderDao orderDao;
    private final OrderMapper orderMapper;
    private final UserService userService;
    private final AddressService addressService;
    private final ShoppingCartService shoppingCartService;
    private final OrderItemDao orderItemDao;
    private final EntityManager entityManager;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final EmailManager emailManager;
    private final EmailService emailService;
    private final CreditCardService creditCardService;


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    Predicate finalPredicate = null;
    private final ProductDao productDao;
    private final UserDao userDao;
    private final TransactionDao transactionDao;
    private final PaymentDao paymentDao;

    @Override
    public OrderDTO getOrderByIdAndUser(Long id) {
        UserEntity user = userService.getCurrentUser();
        OrderEntity order = orderDao.findByIdAndUserId(id,user.getId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
        return orderMapper.entityToDTO(order);
    }

    @Override
    public Page<OrderDTO> getAuthOrdersWithPage(Pageable pageable) {
        UserEntity user = userService.getCurrentUser();
        Page<OrderEntity> orderPage = orderDao.findAllByUserId(user.getId(),pageable);
        return orderPage.map(orderMapper::entityToDTO);
    }

    @Override
    public PageImpl<OrderDTO> getAllOrdersWithFilterAndPage(String query, List<OrderStatus> status, String date1, String date2, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> criteriaQuery = cb.createQuery(OrderEntity.class);
        Root<OrderEntity> root = criteriaQuery.from(OrderEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (query != null && !query.isEmpty()){
            String likeSearchText = "%" + query.toLowerCase(Locale.US) + "%";
            Predicate searchByOrderCode = cb.like(cb.lower(root.get("code")),likeSearchText);
            Predicate searchByContactName = cb.like(cb.lower(root.get("contactName")),likeSearchText);
            predicates.add(cb.or(searchByOrderCode,searchByContactName));
        }
        if (status != null && !status.isEmpty()){
            predicates.add(root.get("status").in(status));
        }
        if (date1 != null && date2 != null){
            LocalDateTime startDate = LocalDate.parse(date1, formatter).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(date2, formatter).atStartOfDay();
            predicates.add(cb.between(root.get("createAt"),startDate,endDate));
        }else{
            if (date1 != null){
                LocalDateTime startDate = LocalDate.parse(date1, formatter).atStartOfDay();
                predicates.add(cb.greaterThan(root.get("createAt"),startDate));
            }
            if (date2 != null) {
                LocalDateTime endDate = LocalDate.parse(date2, formatter).atStartOfDay();
                predicates.add(cb.lessThan(root.get("createAt"), endDate));
            }
        }


        finalPredicate = cb.and(predicates.toArray(new Predicate[0]));

        criteriaQuery.orderBy(pageable.getSort().stream()
                .map(order -> {
                    if (order.isAscending()) {
                        return cb.asc(root.get(order.getProperty()));
                    } else {
                        return cb.desc(root.get(order.getProperty()));
                    }
                })
                .collect(Collectors.toList()));

        criteriaQuery.where(finalPredicate);

        TypedQuery<OrderEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int)pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(OrderEntity.class)));
        countQuery.where(finalPredicate);
        Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        List<OrderDTO> orderDTOList = orderMapper.map(typedQuery.getResultList());

        return new PageImpl<>(orderDTOList,pageable,totalRecords);
    }


    @Override
    public OrderDTO createOrder(String cartUUID, OrderRequest orderRequest) {
        ShoppingCartEntity shoppingCart = shoppingCartService.findCartByUUID(cartUUID);
        List<ShoppingCartItemEntity> shoppingCartItemList = shoppingCart.getShoppingCartItems();
        DecimalFormat df = new DecimalFormat("#.##");
        OrderEntity order = new OrderEntity();
        PaymentEntity payment = new PaymentEntity();
        TransactionEntity transaction = new TransactionEntity();
        UserEntity user = userService.getCurrentUser();
        AddressEntity shippingAddress = addressService.
                getAddressById(orderRequest.getShippingAddressId());
        AddressEntity invoiceAddress = addressService.
                getAddressById(orderRequest.getInvoiceAddressId());

        if (shoppingCartItemList.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessage.UUID_NOT_FOUND_MESSAGE);
        }
        String[] shippingCompany = {"UPS","FedEx","Amazon Logistics","USPS","DHL Express","OnTrac","Purolator","LaserShip","Aramex","ShipBob"};
        String[] provider = {"PayPal","Stripe", "Square", "Authorize.net","Braintree", "Dwolla", "Amazon Pay", "Google Pay", "Apple Pay", "Visa Checkout"};
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            digits.add(i);
        }
        Collections.shuffle(digits);
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            randomNumber.append(digits.get((int) ((Math.random() * 10))));
        }

        double discount = 0.0;
        double tax = calculateTaxCost(shoppingCartItemList);
        double subTotal = 0.0;

        for (ShoppingCartItemEntity each : shoppingCartItemList) {
            ProductEntity product = each.getProduct();
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setProduct(product);
            orderItem.setSku(product.getSku());
            if (each.getQuantity() > each.getProduct().getStockAmount()){
                throw new BadRequestException(String.format(ErrorMessage.PRODUCT_OUT_OF_STOCK_MESSAGE,each.getProduct().getId()));
            }
            orderItem.setQuantity(each.getQuantity());
            orderItem.setDiscount(product.getDiscount());
            orderItem.setTax(product.getTax());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setSubTotal(each.getProduct().getPrice()* each.getQuantity());
            orderItemDao.save(orderItem);
            order.getOrderItems().add(orderItem);

            discount+=((product.getPrice()-product.getDiscountedPrice()) * each.getQuantity());
            subTotal+= product.getPrice()*each.getQuantity();


            Integer newStockAmount = product.getStockAmount() - each.getQuantity();
            product.setStockAmount(newStockAmount);
            productDao.save(product);
        }

        double grandTotal = Double.parseDouble(df.format(grandTotalCalculator(subTotal,discount,tax)).replaceAll(",","."));
        double shippingCost = calculateShippingCost(grandTotal);



        transaction.setTransaction(TransactionStatus.CREATED);
        user.getTransactions().add(transaction);
        user.getOrders().add(order);
        payment.setAmount(grandTotal+shippingCost);
        payment.setProvider(provider[(int)(Math.random()*shippingCompany.length)]);
        payment.setStatus(PaymentStatus.COMPLETED);
        transactionDao.save(transaction);
        userDao.save(user);
        paymentDao.save(payment);



        order.setCode(uniqueIdGenerator.generateUniqueId(8));
        order.setContactName(orderRequest.getContactName());
        order.setContactPhone(orderRequest.getPhoneNumber());
        order.setGrandTotal(grandTotal+shippingCost);
        order.setShippingCost(calculateShippingCost(order.getGrandTotal()));
        order.setStatus(OrderStatus.PENDING);
        order.setInvoiceAddress(invoiceAddress);
        order.setShippingAddress(shippingAddress);
        order.setTax(Double.parseDouble(df.format(tax).replaceAll(",",".")));
        order.setDiscount(Double.parseDouble(df.format(discount).replaceAll(",",".")));
        order.setSubTotal(Double.parseDouble(df.format(subTotal).replaceAll(",",".")));
        order.setUser(user);
        order.setShippingDetails(shippingCompany[(int)(Math.random()*shippingCompany.length)] + " : "+ randomNumber);
        order.getTransactions().add(transaction);
        order.getPayments().add(payment);
        orderDao.save(order);

        if (orderRequest.isSaveCart()){
            CreditCardEntity creditCard = new CreditCardEntity();
            creditCard.setNameOnCard(orderRequest.getNameOnCard());
            creditCard.setCardNumber(orderRequest.getCardNo());
            creditCard.setExpirationDate(orderRequest.getExpireDate());
            creditCard.setTitle(orderRequest.getTitle());
            creditCard.setCvc(orderRequest.getCvc());
            creditCard.setUser(user);
            creditCardService.createPaymentInfo(creditCard);
        }

        emailService.send(
                user.getEmail(),
                emailManager.buildOrderMail(order)
        );

        shoppingCartService.cleanShoppingCart(cartUUID);
        return orderMapper.entityToDTO(order);
    }

    @Override
    public PageImpl<OrderDTO> getOrdersWithUserIdAndPage(Long userId, String startDate, String endDate, List<OrderStatus> status, Pageable pageable) {
        return null;
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        return orderMapper.entityToDTO(orderDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.ORDER_NOT_FOUND_MESSAGE, id))));
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        return null;
    }

    public boolean existsByInvoiceAddress(AddressEntity userAddress) {
        return orderDao.existsByInvoiceAddress(userAddress);
    }

    public boolean existsByShippingAddress(AddressEntity userAddress) {
        return orderDao.existsByShippingAddress(userAddress);
    }

    public long countOrderRecords() {
        return orderDao.count();
    }

    private double grandTotalCalculator(double subTotal,double discount, double tax) {
        return (subTotal-discount)+tax;
    }

    private double calculateTaxCost(List<ShoppingCartItemEntity> shoppingCartItemList) {
        double taxCost = 0.0;
        for (ShoppingCartItemEntity each:shoppingCartItemList) {
            taxCost+= (each.getProduct().getDiscountedPrice()* each.getQuantity()*(each.getProduct().getTax()))/100;
        }
        return taxCost;
    }

    private double calculateShippingCost(double orderGrandTotal) {
        double shippingCost = 0.0;

        if (orderGrandTotal<=750){
            shippingCost = 5.0;
        } else if (orderGrandTotal <= 1500.0){
            shippingCost = 15.0;
        } else if (orderGrandTotal <= 3000.0) {
            shippingCost = 25.0;
        } else if (orderGrandTotal <= 5000.0) {
            shippingCost = 35.0;
        }else return shippingCost;

        return shippingCost;
    }
}
