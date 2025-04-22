package one.wcy.ebookloaningtool.xdz.service;

import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.xdz.pojo.TopUpRequest;

public interface PaymentService {
    Response topUp(TopUpRequest request);

    Response getPaymentHistory();
}