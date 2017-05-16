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
 * Update product attribute test.
 */
class UpdateAttributeTest extends Specification {
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

    def "Test1: set attribute, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def setAttribute = ProductUpdateDataFactory.getSetAttribute()
        setAttribute["version"] = createdProduct.data.version

        def attributeName = setAttribute["actions"][0]["name"]
        def attributeValue = setAttribute["actions"][0]["value"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setAttribute)

        then: "should return 200 ok and product view with new attribute"
        response.status == 200
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.masterVariant.attributes.find { it -> it.name == attributeName && it.value == attributeValue } != null
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test2: set attribute in all variants, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def setAttributeInAllVariants = ProductUpdateDataFactory.getSetAttributeInAllVariants()
        setAttributeInAllVariants["version"] = createdProduct.data.version

        def attributeName = setAttributeInAllVariants["actions"][0]["name"]
        def attributeValue = setAttributeInAllVariants["actions"][0]["value"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setAttributeInAllVariants)

        then: "should return 200 ok and product view with new attribute"
        response.status == 200
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.masterVariant.attributes.find { it -> it.name == attributeName && it.value == attributeValue } != null
        response.data.masterData.staged.variants.forEach {
            it.attributes.find {
                attribute -> attribute.name == attributeName && attribute.value == attributeValue
            } != null
        }
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def cleanup() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, categoryCleanupMap)
        CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, productTypeCleanupMap)
        CleanupUtil.cleanup(ProductConfig.rootURL, cleanupMap)
    }
}
