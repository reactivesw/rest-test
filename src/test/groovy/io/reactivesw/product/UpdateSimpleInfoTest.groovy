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
 * Update product simple information test.
 */
class UpdateSimpleInfoTest extends Specification {

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

    def "Test1: set product description, should return 200 ok and updated product view"() {
        setup: "prepare update request"

        def setDescription = ProductUpdateDataFactory.getSetDescription()
        setDescription["version"] = createdProduct.data.version

        def description = setDescription["actions"][0]["description"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setDescription)

        then: "should return 200 ok and product view with new description"
        response.status == 200
        response.data != null
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged != null
        response.data.masterData.staged.description == description
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test2: set product key, should return 200 ok and updated product view"() {
        setup: "prepare update request"

        def setKey = ProductUpdateDataFactory.getSetKey()
        setKey["version"] = createdProduct.data.version

        def key = setKey["actions"][0]["key"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setKey)

        then: "should return 200 ok and product view with new description"
        response.status == 200
        response.data != null
        response.data.key == key
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test3: set product meta description, should return 200 ok and updated product view"() {
        setup: "prepare update request"
        def setMetaDescription = ProductUpdateDataFactory.getSetMetaDescription()
        setMetaDescription["version"] = createdProduct.data.version

        def metaDescription = setMetaDescription["actions"][0]["metaDescription"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setMetaDescription)

        then: "should return 200 ok and product view with new metaDescription"
        response.status == 200
        response.data != null
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.metaDescription == metaDescription
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test4: set product meta keywords, should return 200 ok and updated product view"() {
        setup: "prepare update request"
        def setMetaKeywords = ProductUpdateDataFactory.getSetMetaKeywords()
        setMetaKeywords["version"] = createdProduct.data.version

        def metaKeywords = setMetaKeywords["actions"][0]["metaKeywords"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setMetaKeywords)

        then: "should return 200 ok and product view with new metaKeywords"
        response.status == 200
        response.data != null
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.metaKeywords == metaKeywords
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test5: set product meta title, should return 200 ok and updated product view"() {
        setup: "prepare update request"
        def setMetaTitle = ProductUpdateDataFactory.getSetMetaTitle()
        setMetaTitle["version"] = createdProduct.data.version

        def metaTitle = setMetaTitle["actions"][0]["metaTitle"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setMetaTitle)

        then: "should return 200 ok and product view with new metaKeywords"
        response.status == 200
        response.data != null
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.metaTitle == metaTitle
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test6: set product name, should return 200 ok and updated product view"() {
        setup: "prepare update request"

        def setName = ProductUpdateDataFactory.getSetName()
        setName["version"] = createdProduct.data.version

        def name = setName["actions"][0]["name"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setName)

        then: "should return 200 ok and product view with new name"
        response.status == 200
        response.data != null
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.name == name
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test7: set product search keyword, should return 200 ok and updated product view"() {
        setup: "prepare update request"
        def setSearchKeywords = ProductUpdateDataFactory.getSetSearchKeywords()
        setSearchKeywords["version"] = createdProduct.data.version

        def searchKeywords = setSearchKeywords["actions"][0]["searchKeywords"]["text"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setSearchKeywords)

        then: "should return 200 ok and product view with new name"
        response.status == 200
        response.data != null
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.searchKeyword["text"] == searchKeywords
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test8: set product slug, should return 200 ok and updated product view"() {
        setup: "prepare update request"
        def setSlug = ProductUpdateDataFactory.getSetSlug()
        setSlug["version"] = createdProduct.data.version

        def slug = setSlug["actions"][0]["slug"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setSlug)

        then: "should return 200 ok and product view with new slug"
        response.status == 200
        response.data != null
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.slug == slug
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def cleanup() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, categoryCleanupMap)
        CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, productTypeCleanupMap)
        CleanupUtil.cleanup(ProductConfig.rootURL, cleanupMap)
    }
}
