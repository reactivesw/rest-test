package io.reactivesw.customer.authentication.config

import io.reactivesw.config.GlobalConfig

/**
 * Created by samray on 3/28/17.
 */
class CustomerAuthenticationConfig{

    static final String PATH = "auth"

    static final String ROOTURL = GlobalConfig.host + PATH + "/"
}