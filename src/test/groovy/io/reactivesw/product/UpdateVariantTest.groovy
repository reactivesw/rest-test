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
 * Update product variant test.
 */
class UpdateVariantTest extends Specification {

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

    def "Test1: add variant, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def addVariant = ProductUpdateDataFactory.getAddVariant()
        addVariant["version"] = createdProduct.data.version

        def skuName = addVariant["actions"][0]["sku"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: addVariant)

        then: "should return 200 ok and product view with new variant"
        response.status == 200
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.variants.size >= 1
        response.data.masterData.staged.variants.find { it -> it.sku == skuName } != null
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test2: change master variant, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def changeMasterVariant = ProductUpdateDataFactory.getChangeMasterVariant()
        changeMasterVariant["version"] = createdProduct.data.version

        def oldMasterVariantSku = createdProduct.data.masterData.staged.masterVariant.sku
        def oldVariant = createdProduct.data.masterData.staged.variants.find { it -> it.id == changeMasterVariant["actions"][0]["variantId"] }

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: changeMasterVariant)

        then: "should return 200 ok and product view with new variant"
        response.status == 200
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.variants.find { it -> it.sku == oldMasterVariantSku } != null
        response.data.masterData.staged.variants.find { it -> it.sku == oldVariant.sku } == null
        response.data.masterData.staged.masterVariant.sku == oldVariant.sku
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test3: remove variant, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def removeVariant = ProductUpdateDataFactory.getRemoveVariant()
        removeVariant["version"] = createdProduct.data.version

        def removedVariant = createdProduct.data.masterData.staged.variants.find { it -> it.id == removeVariant["actions"][0]["variantId"] }

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: removeVariant)

        then: "should return 200 ok and product view with new variant"
        response.status == 200
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.variants.find { it -> it.sku == removedVariant.sku } == null
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test4: set variant sku, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def setSku = ProductUpdateDataFactory.getSetSku()
        setSku["version"] = createdProduct.data.version

        def sku = setSku["actions"][0]["sku"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setSku)

        then: "should return 200 ok and product view with new variant sku"
        response.status == 200
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.masterVariant.sku == sku
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test5: set variant key, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def setVariantKey = ProductUpdateDataFactory.getSetVariantKey()
        setVariantKey["version"] = createdProduct.data.version

        def key = setVariantKey["actions"][0]["key"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setVariantKey)

        then: "should return 200 ok and product view with new variant key"
        response.status == 200
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.masterVariant.key == key
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def cleanup() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, categoryCleanupMap)
        CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, productTypeCleanupMap)
        CleanupUtil.cleanup(ProductConfig.rootURL, cleanupMap)
    }
}
