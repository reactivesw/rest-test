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
 * Update product price test.
 */
class UpdatePriceTest extends Specification {
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

    def "Test1: add price to master variant, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def addPrice = ProductUpdateDataFactory.getAddPrice()
        addPrice["version"] = createdProduct.data.version

        def price = addPrice["actions"][0]["price"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: addPrice)

        then: "should return 200 ok and product view with new price"
        response.status == 200
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.masterVariant.prices.find { it -> it.country == price["country"] && it.value.currencyCode == price["value"]["currencyCode"] && it.value.centAmount == price["value"]["centAmount"] } != null
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test2: change price, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def changePrice = ProductUpdateDataFactory.getChangePrice()
        changePrice["version"] = createdProduct.data.version
        changePrice["actions"][0]["priceId"] = createdProduct.data.masterData.staged.masterVariant.prices[0].id

        def price = changePrice["actions"][0]["price"]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: changePrice)

        then: "should return 200 ok and product view with new price"
        response.status == 200
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.masterVariant.prices.find { it -> it.country == price["country"] && it.value.currencyCode == price["value"]["currencyCode"] && it.value.centAmount == price["value"]["centAmount"] } != null
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test3: remove price, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def removePrice = ProductUpdateDataFactory.getRemovePrice()
        removePrice["version"] = createdProduct.data.version
        removePrice["actions"][0]["priceId"] = createdProduct.data.masterData.staged.masterVariant.prices[0].id

        def price = createdProduct.data.masterData.staged.masterVariant.prices[0]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: removePrice)

        then: "should return 200 ok and product view without this price"
        response.status == 200
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.masterVariant.prices.find { it -> it.country == price["country"] && it.value.currencyCode == price["value"]["currencyCode"] && it.value.centAmount == price["value"]["centAmount"] } == null
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test4: set prices, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def setPrices = ProductUpdateDataFactory.getSetPrices()
        setPrices["version"] = createdProduct.data.version

        def price = setPrices["actions"][0]["prices"][0]

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setPrices)

        then: "should return 200 ok and product view with new prices"
        response.status == 200
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged.masterVariant.prices.find { it -> it.country == price["country"] && it.value.currencyCode == price["value"]["currencyCode"] && it.value.centAmount == price["value"]["centAmount"] } != null
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def cleanup() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, categoryCleanupMap)
        CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, productTypeCleanupMap)
        CleanupUtil.cleanup(ProductConfig.rootURL, cleanupMap)
    }
}
