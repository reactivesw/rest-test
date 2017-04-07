package io.reactivesw.payment.config

import io.reactivesw.config.GlobalConfig

/**
 * Configuration class of payment service
 */
class PaymentConfig {
    static final String PATH = "payments"
    static final String ROOTURL = GlobalConfig.host + PATH + "/"
}
