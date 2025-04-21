package one.wcy.ebookloaningtool.xdz.service;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import one.wcy.ebookloaningtool.xdz.mapper.PaymentMapper;
import one.wcy.ebookloaningtool.xdz.pojo.PaymentRecord;
import one.wcy.ebookloaningtool.xdz.pojo.TopUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

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
        payment.setUserUUID(userId);
        payment.setAmount(BigDecimal.valueOf(request.getAmount()));
        payment.setPaymentDate(LocalDateTime.now());
        paymentMapper.insert(payment);

        return new Response("success");
    }
}