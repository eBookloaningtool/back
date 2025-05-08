package one.wcy.ebookloaningtool.xdz.service;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.users.UserRepository;
import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import one.wcy.ebookloaningtool.xdz.mapper.PaymentMapper;
import one.wcy.ebookloaningtool.xdz.pojo.PaymentRecord;
import one.wcy.ebookloaningtool.xdz.pojo.TopUpRequest;
import one.wcy.ebookloaningtool.xdz.response.PaymentHistoryResponse;
import one.wcy.ebookloaningtool.xdz.response.TopUpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the payment service interface.
 * Handles user balance top-up operations and payment history retrieval.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private final UserRepository userRepository;

    /**
     * Constructs a new PaymentServiceImpl with the specified user repository.
     *
     * @param userRepository The repository for user-related operations
     */
    public PaymentServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Processes a top-up request for a user's balance.
     *
     * @param request The top-up request containing the amount to be added
     * @return Response containing the payment ID and updated balance, or an error message
     */
    @Override
    public Response topUp(TopUpRequest request) {
        // Parameter validation
        if (request.getAmount() <= 0) {
            return new Response("Invalid amount");
        }

        // User authentication
        Claims claims = ThreadLocalUtil.get();
        if (claims == null) {
            return new Response("Unauthorized");
        }
        String userId = claims.get("uuid").toString();

        paymentMapper.updateBalance(userId, BigDecimal.valueOf(request.getAmount()));

        // Record payment transaction
        PaymentRecord payment = new PaymentRecord();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setUuid(userId);
        payment.setAmount(BigDecimal.valueOf(request.getAmount()));
        payment.setPaymentDate(LocalDateTime.now());
        paymentMapper.insert(payment);

        BigDecimal balance = userRepository.findByUuid(userId).getBalance();

        return new TopUpResponse("success", payment.getPaymentId(), balance);
    }

    /**
     * Retrieves the payment history for the current user.
     *
     * @return Response containing the list of payment history items
     */
    @Override
    public Response getPaymentHistory() {
        // 1. Get current logged-in user
        Claims claims = ThreadLocalUtil.get();
        if (claims == null) {
            return new Response("Unauthorized");
        }
        String userId = claims.get("uuid").toString();

        // 2. Query payment history
        List<PaymentHistoryResponse.PaymentHistoryItem> history = paymentMapper.findUserPaymentHistory(userId);

        // 3. Return results
        return new PaymentHistoryResponse("success", history);
    }
}