package com.airline.management.service.factory;

import com.airline.management.model.Payment;
import com.airline.management.service.payment.CreditCardPaymentProcessor;
import com.airline.management.service.payment.DebitCardPaymentProcessor;
import com.airline.management.service.payment.NetBankingPaymentProcessor;
import com.airline.management.service.payment.PaymentProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Factory Pattern: Creates appropriate payment processor based on payment method
@Component
public class PaymentProcessorFactory {
    
    private final CreditCardPaymentProcessor creditCardProcessor;
    private final DebitCardPaymentProcessor debitCardProcessor;
    private final NetBankingPaymentProcessor netBankingProcessor;
    
    @Autowired
    public PaymentProcessorFactory(
            CreditCardPaymentProcessor creditCardProcessor,
            DebitCardPaymentProcessor debitCardProcessor,
            NetBankingPaymentProcessor netBankingProcessor) {
        this.creditCardProcessor = creditCardProcessor;
        this.debitCardProcessor = debitCardProcessor;
        this.netBankingProcessor = netBankingProcessor;
    }
    
    public PaymentProcessor getProcessor(Payment.PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case CREDIT_CARD:
                return creditCardProcessor;
            case DEBIT_CARD:
                return debitCardProcessor;
            case NET_BANKING:
                return netBankingProcessor;
            default:
                throw new RuntimeException("Unsupported payment method");
        }
    }
}
