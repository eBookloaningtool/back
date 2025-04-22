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

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private final UserRepository userRepository;

    public PaymentServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Response topUp(TopUpRequest request) {
        // 参数校验
        if (request.getAmount() <= 0) {
            return new Response("Invalid amount");
        }

        // 用户认证
        Claims claims = ThreadLocalUtil.get();
        if (claims == null) {
            return new Response("Unauthorized");
        }
        String userId = claims.get("uuid").toString();


        paymentMapper.updateBalance(userId, BigDecimal.valueOf(request.getAmount()));

        // 记录支付流水
        PaymentRecord payment = new PaymentRecord();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setUserUUID(userId);
        payment.setAmount(BigDecimal.valueOf(request.getAmount()));
        payment.setPaymentDate(LocalDateTime.now());
        paymentMapper.insert(payment);

        double balance = userRepository.findByUuid(userId).getBalance();

        return new TopUpResponse("success", payment.getPaymentId(), balance);
    }

    @Override
    public Response getPaymentHistory() {
        // 1. 获取当前登录用户
        Claims claims = ThreadLocalUtil.get();
        if (claims == null) {
            return new Response("Unauthorized");
        }
        String userId = claims.get("uuid").toString();

        // 2. 查询充值历史
        List<PaymentHistoryResponse.PaymentHistoryItem> history = paymentMapper.findUserPaymentHistory(userId);

        // 3. 返回结果
        return new PaymentHistoryResponse("success", history);
    }
}