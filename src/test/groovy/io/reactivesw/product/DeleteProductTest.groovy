package io.reactivesw.product

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.config.ProductTypeConfig
import io.reactivesw.product.config.ProductConfig
import io.reactivesw.product.util.ProductDataFactory
import io.reactivesw.product.util.ProductUtil
import io.reactivesw.util.*
import spock.lang.Shared
import spock.lang.Specification

/**
 * Delete product test.
 */
class DeleteProductTest extends Specification {
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

    @Shared
    CleanupMap categoryCleanupMap = new CleanupMap()

    @Shared
    CleanupMap productTypeCleanupMap = new CleanupMap()

    def client = RestClientFactory.getJsonClient(ProductConfig.rootURL)

    @Shared
    def category

    @Shared
    def productType

    def setupSpec() {
        def categoryClient = RestClientFactory.getJsonClient(CategoryConfig.rootURL)
        def productTypeClient = RestClientFactory.getJsonClient(ProductTypeConfig.ROOTURL)

        category = categoryClient.post(body: CategoryDataFactory.getCategoryForProduct())
        categoryCleanupMap.addObject(category.data.id, category.data.version)
        productType = productTypeClient.post(body: ProductTypeDataFactory.getProductTypeForProduct())
        productTypeCleanupMap.addObject(productType.data.id, productType.data.version)
    }

    def "Test1: delete product with id and version, should return 200 ok"() {
        given: "prepare data"
        def product = ProductDataFactory.getProduct()
        ProductUtil.setProductTypeAndCategory(product, productType, category)
        def createdProduct = client.post(body: product)

        def productId = createdProduct.data.id
        def version = createdProduct.data.version
        def requestVersion = ['version': version]

        client = RestClientFactory.getClient(ProductConfig.rootURL)

        when: "call api to delete product"
        def response = client.delete(path: productId, query: requestVersion)

        then: "should return 200 ok"
        response.status == 200
    }

    def "Test2: delete product with not exist id, should return 404 not found"() {
        given: "prepare data"
        def productId = "not_exist_product_id"
        def requestVersion = ['version': 1]

        when: "call api to delete product"
        def response = client.delete(path: productId, query: requestVersion)

        then: "should return 404 not found"
        response == 404
    }

    def "Test3: delete product with id and wrong version, should return 409 conflict"() {
        given: "prepare data"
        def product = ProductDataFactory.getProduct()
        ProductUtil.setProductTypeAndCategory(product, productType, category)
        def createdProduct = client.post(body: product)

        cleanupMap.addObject(createdProduct.data.id, createdProduct.data.version)

        def productId = createdProduct.data.id
        def version = createdProduct.data.version + 1
        def requestVersion = ['version': version]

        when: "call api to delete product"
        def response = client.delete(path: productId, query: requestVersion)

        then: "should return 200 ok"
        response == 409
    }

    def cleanupSpec() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, categoryCleanupMap)
        CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, productTypeCleanupMap)
        CleanupUtil.cleanup(ProductConfig.rootURL, cleanupMap)
    }
}
