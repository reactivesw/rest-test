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

    def "Test1: create product with category and product type, should return 200 ok and product view"() {
        given: "prepare data"
        def product = ProductDataFactory.getProduct()
        ProductUtil.setProductTypeAndCategory(product, productType, category)

        when: "call product api to create product"
        def response = client.post(body: product)

        then: "should return 200 ok and product view"
        response.status == 200
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test2: create product and required attribute is lack, should return 400 bad request"() {
        given: "prepare data"
        def product = ProductDataFactory.getLackRequiredAttributeProduct()
        ProductUtil.setProductTypeAndCategory(product, productType, category)

        when: "call product api to create product"
        def response = client.post(body: product)

        then: "should return 400 bad request"
        response == 400
    }

    def "Test3: create product and unique attribute is not unique, should return 400 bad request"() {
        given: "prepare data"
        def product = ProductDataFactory.getNotUniqueAttributeProduct()
        ProductUtil.setProductTypeAndCategory(product, productType, category)

        when: "call product api to create product"
        def response = client.post(body: product)

        then: "should return 400 bad request"
        response == 400
    }

    def "Test4: create product and combination unique attribute is not unique, should return 400 bad request"() {
        given: "prepare data"
        def product = ProductDataFactory.getNotCombinationUniqueAttributeProduct()
        ProductUtil.setProductTypeAndCategory(product, productType, category)

        when: "call product api to create product"
        def response = client.post(body: product)

        then: "should return 400 bad request"
        response == 400
    }

    def "Test5: create product and same for all attribute is not same, should return 400 bad request"() {
        given: "prepare data"
        def product = ProductDataFactory.getNotSameForAllAttributeProductt()
        ProductUtil.setProductTypeAndCategory(product, productType, category)

        when: "call product api to create product"
        def response = client.post(body: product)

        then: "should return 400 bad request"
        response == 400
    }

    def "Test6: create product with same sku name, should return 400 bad request"() {
        given: "prepare data"
        def product = ProductDataFactory.getSameSkuNameProduct()
        ProductUtil.setProductTypeAndCategory(product, productType, category)

        when: "call product api to create product"
        def response = client.post(body: product)

        then: "should return 400 bad request"
        response == 400
    }

    def "Test7: create product with exist sku name, should return 409 conflict"() {
        setup: "create a product"
        def basicProduct = ProductDataFactory.getExistSkuProduct().basicProduct
        ProductUtil.setProductTypeAndCategory(basicProduct, productType, category)
        def createdProduct = client.post(body: basicProduct)
        cleanupMap.addObject(createdProduct.data.id, createdProduct.data.version)

        def existSkuProduct = ProductDataFactory.getExistSkuProduct().existSkuProduct
        ProductUtil.setProductTypeAndCategory(existSkuProduct, productType, category)

        when: "call product api to create product"
        def response = client.post(body: existSkuProduct)

        then: "should return 409 conflict"
        response == 409
    }

    def "Test8: create product with exist slug, should return 409 conflict"() {
        setup: "create a product"
        def basicProduct = ProductDataFactory.getExistSlugProduct().basicProduct
        ProductUtil.setProductTypeAndCategory(basicProduct, productType, category)
        def createdProduct = client.post(body: basicProduct)
        cleanupMap.addObject(createdProduct.data.id, createdProduct.data.version)

        def existSlugProduct = ProductDataFactory.getExistSlugProduct().existSlugProduct
        ProductUtil.setProductTypeAndCategory(existSlugProduct, productType, category)

        when: "call product api to create product"
        def response = client.post(body: existSlugProduct)

        then: "should return 409 conflict"
        response == 409
    }

    def cleanupSpec() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, categoryCleanupMap)
        CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, productTypeCleanupMap)
        CleanupUtil.cleanup(ProductConfig.rootURL, cleanupMap)
    }
}
