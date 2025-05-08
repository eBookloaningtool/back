package one.wcy.ebookloaningtool.xdz.controller;

import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.xdz.pojo.TopUpRequest;
import one.wcy.ebookloaningtool.xdz.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for managing payment operations.
 * Provides endpoints for handling account top-ups and viewing payment history.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    /**
     * Service for handling payment operations
     */
    @Autowired
    private PaymentService paymentService;

    /**
     * Processes a top-up request to add funds to a user's account.
     * @param request The top-up request containing the amount to add
     * @return Response indicating success or failure of the operation
     */
    @PostMapping("/topup")
    public Response topUp(@RequestBody TopUpRequest request) {
        return paymentService.topUp(request);
    }

    /**
     * Retrieves the payment history for the current user.
     * @return Response containing the user's payment history
     */
    @PostMapping("/history")
    public Response getPaymentHistory() {
        return paymentService.getPaymentHistory();
    }
}
