package io.reactivesw.category.config

import io.reactivesw.config.GlobalConfig

/**
 * configuration of category service
 */
class CategoryConfig {
    static final String path = "categories"

//    static final String rootURL = GlobalConfig.host + path + "/"
    static final String rootURL = "http://localhost:8082/"
}
