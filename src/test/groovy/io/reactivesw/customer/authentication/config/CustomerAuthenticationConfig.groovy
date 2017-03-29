package io.reactivesw.customer.authentication.config

import io.reactivesw.config.GlobalConfig

/**
 * the configuration for customer authentication test
 */
class CustomerAuthenticationConfig{

    static final String PATH = "auth"

    static final String ROOTURL = GlobalConfig.host + PATH + "/"
}