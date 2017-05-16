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
 * Update product category test.
 */
class UpdateCategoryTest extends Specification {
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

    def "Test1: add product to category, should return 200 ok and updated product view"() {
        setup: "prepare update request"

        def addToCategory = ProductUpdateDataFactory.getAddToCategory()
        addToCategory["version"] = createdProduct.data.version

        def categoryId = addToCategory["actions"][0]["category"].id

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: addToCategory)

        then: "should return 200 ok and product view with new category"
        response.status == 200
        response.data != null
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged != null
        response.data.masterData.staged.categories.find{it -> it.id == categoryId} != null
        response.data.masterData.staged.categoryOrderHints.find{it -> it.key == categoryId } != null
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test2: remove product from category, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def categoryId = createdProduct.data.masterData.staged.categories[0].id

        def removeFromCategory = ProductUpdateDataFactory.getRemoveFromCategory()
        removeFromCategory["version"] = createdProduct.data.version
        removeFromCategory["actions"][0]["category"].id = categoryId

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: removeFromCategory)

        then: "should return 200 ok and product view without this category"
        response.status == 200
        response.data != null
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged != null
        response.data.masterData.staged.categories.find{it -> it.id == categoryId} == null
        response.data.masterData.staged.categoryOrderHints.find{it -> it.key == categoryId } == null
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test3: set category order hint, should return 200 ok and updated product view"() {
        given: "prepare update request"
        def categoryId = createdProduct.data.masterData.staged.categories[0].id

        def setCategoryOrderHint = ProductUpdateDataFactory.getSetCategoryOrderHint()
        setCategoryOrderHint["version"] = createdProduct.data.version
        setCategoryOrderHint["actions"][0]["categoryId"] = categoryId

        def orderHint = new BigDecimal(setCategoryOrderHint["actions"][0]["previousOrderHint"])
        orderHint = orderHint.add(new BigDecimal(setCategoryOrderHint["actions"][0]["nextOrderHint"])).divide(new BigDecimal(2)).toString()

        when: "call product api to update product"
        def response = client.put(path: createdProduct.data.id, body: setCategoryOrderHint)

        then: "should return 200 ok and product view without this category"
        response.status == 200
        response.data != null
        response.data.masterData.hasStagedChanges == true
        response.data.masterData.staged != null
        response.data.masterData.staged.categoryOrderHints.find{it -> it.key == categoryId && it.order == orderHint } != null
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def cleanup() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, categoryCleanupMap)
        CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, productTypeCleanupMap)
        CleanupUtil.cleanup(ProductConfig.rootURL, cleanupMap)
    }
}
