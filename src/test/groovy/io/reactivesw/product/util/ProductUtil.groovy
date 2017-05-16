package io.reactivesw.product.util

/**
 * Util class for product.
 */
class ProductUtil {
    def static void setProductTypeAndCategory(def product, def productType, def category){
        product['categories'][0]['id'] = category.data.id
        product['productType']['id'] = productType.data.id
    }
}
