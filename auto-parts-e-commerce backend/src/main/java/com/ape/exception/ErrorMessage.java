package com.ape.exception;

public class ErrorMessage {
    // ****** USER ******
    public final static String USER_NOT_FOUND_MESSAGE = "Email or password doesn't matched.";
    public final static String PRINCIPAL_NOT_FOUND_MESSAGE= "User not found";
    // ****** BRAND ******
    public final static String BRAND_NOT_FOUND_MESSAGE = "Brand id: %d not found";
    public final static String BRAND_CONFLICT_EXCEPTION = "Brand '%s' is already exist";
    public final static String BRAND_CAN_NOT_DELETE_EXCEPTION = "Brand can not be deleted";
    // ****** CATEGORY ******
    public final static String CATEGORY_USED_EXCEPTION = "Category '%s' is already exist";
    public final static String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";
    public final static String CATEGORY_CAN_NOT_DELETE_EXCEPTION = "Category can not be deleted";
    // ****** PRODUCT ******
    public final static String PRODUCT_NOT_FOUND_MESSAGE = "Product with id %d not found";
    public final static String PRODUCT_OUT_OF_STOCK_MESSAGE = "The product with id %d is out of stock";
    public final static String PRODUCT_USED_BY_ORDER_MESSAGE = "Product couldn't be deleted while it have order";
    // ****** EMAIL ******
    public final static String EMAIL_ALREADY_CONFIRMED_MESSAGE = "Email already confirmed";
    public final static String EMAIL_NOT_CONFIRMED_MESSAGE = "Email %s need to be confirmed";
    public final static String EMAIL_ALREADY_EXIST_MESSAGE = "Email: %s already exist";
    public final static String FAILED_TO_SEND_EMAIL_MESSAGE = "Failed to send email";
    // ****** TOKEN ******
    public final static String TOKEN_EXPIRED_MESSAGE = "Confirmation Token Expired";
    // ****** ROLE ******
    public final static String ROLE_NOT_FOUND_MESSAGE = "Role : %s not found";
    // ****** RESOURCE ******
    public final static String RESOURCE_NOT_FOUND_MESSAGE = "Resource with id %s not found";
    // ****** IMAGE ******
    public final static String IMAGE_NOT_FOUND_MESSAGE = "ImageFile with id %s not found";
    public final static String IMAGE_USED_MESSAGE = "Image already used";
    public final static String IMAGE_ALREADY_EXIST_MESSAGE = "Image: %s already exist";
    // ****** ADDRESS ******
    public final static String ADDRESS_NOT_FOUND_MESSAGE = "Address not found";
    public final static String ADDRESS_USED_MESSAGE = "User address is used by order";
    // ****** ORDER ******
    public final static String ORDER_NOT_FOUND_MESSAGE = "Order with id: %d not found";
    // ****** SHOPPING_CART ******
    public static final String UUID_NOT_FOUND_MESSAGE = "Uuid could not found with given id";
    // ****** CREDIT_CARD ******
    public static final String CREDIT_CARD_NOT_FOUND_MESSAGE = "Credit card not found";
    public static final String CREDIT_CARD_TITLE_FOUND_MESSAGE = "Credit card with title %s already exist";
}
