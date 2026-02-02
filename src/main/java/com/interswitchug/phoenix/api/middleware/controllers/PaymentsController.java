package com.interswitchug.phoenix.api.middleware.controllers;


import com.interswitchug.phoenix.api.middleware.dto.PaymentRequest;
import com.interswitchug.phoenix.api.middleware.services.PaymentsService;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("isw/payments")
public class PaymentsController {

    private final PaymentsService paymentsService;

    public PaymentsController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @PostMapping("/validation")
    public String validateCustomer(@RequestBody PaymentRequest request,
                                   @RequestHeader("AuthToken") String authToken,
                                   @RequestHeader("TerminalKey") String terminalKey) throws Exception {

        return paymentsService.validateCustomer(request, authToken, terminalKey);
    }

    @PostMapping("/pay")
    public String doPayment(@RequestBody PaymentRequest registrationDetail,
                           @RequestHeader("AuthToken") String authToken,
                           @RequestHeader("TerminalKey") String terminalKey) throws Exception {

        return paymentsService.makePayment(registrationDetail, authToken, terminalKey);
    }

    @GetMapping("/checkStatus")
    public String checkStatus(@PathParam("requestReference") String requestReference,
                             @RequestHeader("AuthToken") String authToken,
                             @RequestHeader("TerminalKey") String terminalKey) throws Exception {

        return paymentsService.checkStatus(requestReference, authToken, terminalKey);
    }

    @GetMapping("/balance")
    public String getBalance(@RequestHeader("AuthToken") String authToken,
                            @RequestHeader("TerminalKey") String terminalKey) throws Exception {

        return paymentsService.fetchBalance(authToken, terminalKey);
    }

    @GetMapping("/billerCategories")
    public String getBillerCategories(@RequestHeader("AuthToken") String authToken,
                                     @RequestHeader("TerminalKey") String terminalKey) throws Exception {

        return paymentsService.getCategories(authToken, terminalKey);
    }

    @GetMapping("/categoryBillers")
    public String getBillersByCategory(@PathParam("categoryId") String categoryId,
                                      @RequestHeader("AuthToken") String authToken,
                                      @RequestHeader("TerminalKey") String terminalKey) throws Exception {

        return paymentsService.getCategoryBillers(categoryId, authToken, terminalKey);
    }

    @GetMapping("/billerItems")
    public String getPaymentItemsByBiller(@PathParam("billerId") String billerId,
                                         @RequestHeader("AuthToken") String authToken,
                                         @RequestHeader("TerminalKey") String terminalKey) throws Exception {

        return paymentsService.getBillerItems(billerId, authToken, terminalKey);
    }

}
