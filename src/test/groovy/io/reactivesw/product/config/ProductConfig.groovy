package io.reactivesw.product.config

import io.reactivesw.config.GlobalConfig

/**
 * Configuration of category service.
 */
class ProductConfig {
    static final String path = "products"

//    static final String rootURL = GlobalConfig.host + path + "/"
    static final String rootURL = "http://localhost:8088/"
}
