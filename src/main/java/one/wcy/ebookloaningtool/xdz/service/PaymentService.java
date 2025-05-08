package one.wcy.ebookloaningtool.xdz.service;

import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.xdz.pojo.TopUpRequest;

/**
 * Service interface for managing payment operations.
 * Provides methods for handling user payments and retrieving payment history.
 */
public interface PaymentService {
    /**
     * Processes a top-up request to add funds to the user's account.
     * @param request The top-up request containing payment details
     * @return Response indicating success or failure of the operation
     */
    Response topUp(TopUpRequest request);

    /**
     * Retrieves the payment history for the current user.
     * @return Response containing the user's payment history
     */
    Response getPaymentHistory();
}