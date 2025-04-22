package one.wcy.ebookloaningtool.xdz.controller;

import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.xdz.pojo.TopUpRequest;
import one.wcy.ebookloaningtool.xdz.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/topup")
    public Response topUp(@RequestBody TopUpRequest request) {
        return paymentService.topUp(request);
    }

    @PostMapping("/history")
    public Response getPaymentHistory() {
        return paymentService.getPaymentHistory();
    }
}
