package io.reactivesw.product

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.config.ProductTypeConfig
import io.reactivesw.product.config.ProductConfig
import io.reactivesw.product.util.ProductDataFactory
import io.reactivesw.product.util.ProductUpdateDataFactory
import io.reactivesw.product.util.ProductUtil
import io.reactivesw.util.CategoryDataFactory
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.ProductTypeDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

/**
 * Test for RevertStagedChanges update action.
 */
class RevertStagedChangesTest extends Specification {
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

    def "Test1: public product after created, should return 200 ok and updated product view"() {
        setup: "create and publish a product, prepare update request"
        def product = ProductDataFactory.getProduct()
        ProductUtil.setProductTypeAndCategory(product, productType, category)
        def createdProduct = client.post(body: product)
        def productId = createdProduct.data.id
        cleanupMap.addObject(productId, createdProduct.data.version)

        def publish = ProductUpdateDataFactory.getPublish()
        publish["version"] = createdProduct.data.version
        def publishedProduct = client.put(path: productId, body: publish)

        def setName = ProductUpdateDataFactory.getSetName()
        setName["version"] = createdProduct.data.version

        def updatedProduct = client.put(path: productId, body: setName)

        def revertStagedChanges = ProductUpdateDataFactory.getRevertStagedChanges()
        revertStagedChanges["version"] = updatedProduct.data.version

        when: "call product api to publish product"
        def response = client.put(path: productId, body: revertStagedChanges)

        then: "should return 200 ok and product view with staged info and the same info in current"
        response.status == 200
        response.data != null
        response.data.masterData.published == true
        response.data.masterData.hasStagedChanges == false
        response.data.masterData.current != null
        response.data.masterData.staged != null
        response.data.masterData.current.name == response.data.masterData.staged.name
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def cleanupSpec() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, categoryCleanupMap)
        CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, productTypeCleanupMap)
        CleanupUtil.cleanup(ProductConfig.rootURL, cleanupMap)
    }
}