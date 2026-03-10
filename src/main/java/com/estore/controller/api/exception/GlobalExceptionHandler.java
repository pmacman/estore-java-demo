package com.estore.controller.api.exception;

import com.estore.model.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.util.Collections;
import java.util.List;

/**
 * References:
 * https://github.com/jovannypcg/exception_handler
 * https://medium.com/@jovannypcg/understanding-springs-controlleradvice-cd96a364033f
 */
@ControllerAdvice
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class GlobalExceptionHandler {
    /**
     * Provides handling for exceptions throughout this service.
     *
     * @param ex The target exception
     * @param request The current request
     */
    @ExceptionHandler({
            CartNotFoundException.class,
            CustomerNotFoundException.class,
            CountryNotFoundException.class,
            OrderNotFoundException.class,
            PartnerNotFoundException.class,
            PaymentUnsuccessfulException.class,
            ProductNotFoundException.class,
            ProductOutOfStockException.class,
            ReviewNotFoundException.class,
            UserNotFoundException.class
    })
    @Nullable
    public final ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof CartNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            CartNotFoundException nfe = (CartNotFoundException) ex;
            return handleCartNotFoundException(nfe, headers, status, request);
        } else if (ex instanceof CustomerNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            CustomerNotFoundException nfe = (CustomerNotFoundException) ex;
            return handleCustomerNotFoundException(nfe, headers, status, request);
        } else if (ex instanceof CountryNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            CountryNotFoundException nfe = (CountryNotFoundException) ex;
            return handleCountryNotFoundException(nfe, headers, status, request);
        } else if (ex instanceof OrderNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            OrderNotFoundException nfe = (OrderNotFoundException) ex;
            return handleOrderNotFoundException(nfe, headers, status, request);
        }  else if (ex instanceof PartnerNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            PartnerNotFoundException nfe = (PartnerNotFoundException) ex;
            return handlePartnerNotFoundException(nfe, headers, status, request);
        }  else if (ex instanceof PaymentUnsuccessfulException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            PaymentUnsuccessfulException nfe = (PaymentUnsuccessfulException) ex;
            return handlePaymentUnsuccessfulException(nfe, headers, status, request);
        }  else if (ex instanceof ProductNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ProductNotFoundException nfe = (ProductNotFoundException) ex;
            return handleProductNotFoundException(nfe, headers, status, request);
        }  else if (ex instanceof ProductOutOfStockException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            ProductOutOfStockException nfe = (ProductOutOfStockException) ex;
            return handleProductOutOfStockException(nfe, headers, status, request);
        }  else if (ex instanceof ReviewNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ReviewNotFoundException nfe = (ReviewNotFoundException) ex;
            return handleReviewNotFoundException(nfe, headers, status, request);
        }  else if (ex instanceof UserNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            UserNotFoundException nfe = (UserNotFoundException) ex;
            return handleUserNotFoundException(nfe, headers, status, request);
        } else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
        }
    }

    protected ResponseEntity<ApiError> handleCartNotFoundException(CartNotFoundException ex, HttpHeaders headers,
                                                                       HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    protected ResponseEntity<ApiError> handleCustomerNotFoundException(CustomerNotFoundException ex, HttpHeaders headers,
                                                                      HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    protected ResponseEntity<ApiError> handleCountryNotFoundException(CountryNotFoundException ex, HttpHeaders headers,
                                                                      HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    protected ResponseEntity<ApiError> handleOrderNotFoundException(OrderNotFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    protected ResponseEntity<ApiError> handlePartnerNotFoundException(PartnerNotFoundException ex, HttpHeaders headers,
                                                                      HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    protected ResponseEntity<ApiError> handlePaymentUnsuccessfulException(PaymentUnsuccessfulException ex, HttpHeaders headers,
                                                                      HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    protected ResponseEntity<ApiError> handleProductNotFoundException(ProductNotFoundException ex, HttpHeaders headers,
                                                                           HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    protected ResponseEntity<ApiError> handleProductOutOfStockException(ProductOutOfStockException ex, HttpHeaders headers,
                                                                      HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    protected ResponseEntity<ApiError> handleReviewNotFoundException(ReviewNotFoundException ex, HttpHeaders headers,
                                                                      HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    protected ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex, HttpHeaders headers,
                                                                     HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    /**
     * A single place to customize the response body of all Exception types.
     *
     * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
     * request attribute and creates a {@link ResponseEntity} from the given
     * body, headers, and status.
     *
     * @param ex The exception
     * @param body The body for the response
     * @param headers The headers for the response
     * @param status The response status
     * @param request The current request
     */
    protected ResponseEntity<ApiError> handleExceptionInternal(Exception ex, @Nullable ApiError body,
                                                               HttpHeaders headers, HttpStatus status,
                                                               WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }
}