package io.reactivesw.product

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.config.ProductTypeConfig
import io.reactivesw.product.config.ProductConfig
import io.reactivesw.util.CategoryDataFactory
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.ProductDataFactory
import io.reactivesw.util.ProductTypeDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

/**
 * Create product test.
 */
class CreateProductTest extends Specification {
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

    @Shared
    CleanupMap categoryCleanupMap = new CleanupMap()

    @Shared
    CleanupMap productTypeCleanupMap = new CleanupMap()

    def client = RestClientFactory.getJsonClient(ProductConfig.rootURL)
    def categoryClient = RestClientFactory.getJsonClient(CategoryConfig.rootURL)
    def productTypeClient = RestClientFactory.getJsonClient(ProductTypeConfig.ROOTURL)


    def "Test1: create product with category and product type, should return 200 ok and product view"() {
        setup: "create category and product type"
        def category = categoryClient.post(CategoryDataFactory.getCategoryForProduct())
        def productType = productTypeClient.post(ProductTypeDataFactory.getProductTypeForProduct())

        given: "prepare  data"
        def product = ProductDataFactory.getProduct()
        product['categories'][0]['id'] = category.id
        product['productType']['id'] = productType.id

        when: "call product api to create product"
        def response = client.post(body: product)

        then: "should return 200 ok and product view"
        response.status == 200
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def cleanupSpec() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, categoryCleanupMap)
        CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, productTypeCleanupMap)
        CleanupUtil.cleanup(ProductConfig.rootURL, cleanupMap)
    }
}
