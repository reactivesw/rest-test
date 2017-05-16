package io.reactivesw.product

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.config.ProductTypeConfig
import io.reactivesw.product.config.ProductConfig
import io.reactivesw.product.util.ProductDataFactory
import io.reactivesw.product.util.ProductUpdateDataFactory
import io.reactivesw.product.util.ProductUtil
import io.reactivesw.util.*
import spock.lang.Shared
import spock.lang.Specification

/**
 * Update product image test.
 */
class UpdateImageTest extends Specification {

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

    @Shared
    def createdProduct

    def setup() {
        def categoryClient = RestClientFactory.getJsonClient(CategoryConfig.rootURL)
        def productTypeClient = RestClientFactory.getJsonClient(ProductTypeConfig.ROOTURL)

        category = categoryClient.post(body: CategoryDataFactory.getCategoryForProduct())
        categoryCleanupMap.addObject(category.data.id, category.data.version)
        productType = productTypeClient.post(body: ProductTypeDataFactory.getProductTypeForProduct())
        productTypeCleanupMap.addObject(productType.data.id, productType.data.version)

        def product = ProductDataFactory.getProduct()
        ProductUtil.setProductTypeAndCategory(product, productType, category)
        createdProduct = client.post(body: product)
        cleanupMap.addObject(createdProduct.data.id, createdProduct.data.version)
    }

    def "Test1: add external image to product, should return 200 ok and updated product view"() {
        setup: "prepare update request"

        def addExternalImage = ProductUpdateDataFactory.getAddExternalImage()
        addExternalImage["version"] = createdProduct.data.version

        def image = addExternalImage.actions[0].image

        when: "call product api to publish product"
        def response = client.put(path: createdProduct.data.id, body: addExternalImage)

        then: "should return 200 ok and product view with staged info and the same info in current"
        response.status == 200
        response.data != null
        response.data.masterData.staged != null
        response.data.masterData.staged.masterVariant.images.find { it -> it.label == image.label && it.url == image.url } != null
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test2: remove image from product, should return 200 ok and updated product view"() {
        setup: "prepare update request"
        def removeImage = ProductUpdateDataFactory.getRemoveImage()
        removeImage["version"] = createdProduct.data.version
        def imageUrl = createdProduct.data.masterData.staged.masterVariant.images[0].url
        removeImage["actions"][0]["imageUrl"] = imageUrl

        when: "call product api to publish product"
        def response = client.put(path: createdProduct.data.id, body: removeImage)

        then: "should return 200 ok and product view with staged info and the same info in current"
        response.status == 200
        response.data != null
        response.data.masterData.staged != null
        response.data.masterData.staged.masterVariant.images.find { it -> it.url == imageUrl } == null
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def cleanup() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, categoryCleanupMap)
        CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, productTypeCleanupMap)
        CleanupUtil.cleanup(ProductConfig.rootURL, cleanupMap)
    }
}