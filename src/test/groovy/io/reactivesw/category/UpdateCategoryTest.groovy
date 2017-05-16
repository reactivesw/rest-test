package io.reactivesw.category

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.util.CategoryDataFactory
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

/**
 category update test
 */
class UpdateCategoryTest extends Specification {
    @Shared
    def id
    @Shared
    def version
    @Shared
    def category
    @Shared
    def client
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

    def setupSpec() {
        category = CategoryDataFactory.getCategory().validCategory1
        client = RestClientFactory.getJsonClient(CategoryConfig.rootURL)
        def response = client.post(body: category)
        id = response.data.id
        version = response.data.version
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test1: update category name, should return 200 ok and category view"() {
        given: "prepare data"
        def setName = CategoryDataFactory.getSetNameAction()
        setName['version'] = cleanupMap.allObjects[id]

        when: "call api to update category name"
        def response = client.put([path: id, body: setName])

        then: "should return 200 ok and category view"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.name == setName.actions[0].name

    }

    def "Test2: update category slug, should return 200 ok and category view"() {
        given: "prepare data"
        def setSlug = CategoryDataFactory.getSetSlugAction()
        setSlug['version'] = cleanupMap.allObjects[id]

        when: "call api to update category slug"
        def response = client.put([path: id, body: setSlug])

        then: "should return 200 and category view"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.slug == setSlug.actions[0].slug
    }

    def "Test3: update category description, should return 200 ok and category view"() {
        given: "prepare data"
        def setDescription = CategoryDataFactory.getSetDescriptionAction()
        setDescription['version'] = cleanupMap.allObjects[id]

        when: "call api to update category description"
        def response = client.put([path: id, body: setDescription])

        then: "should return 200 ok and category view"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.description == setDescription.actions[0].description
    }

    def "Test4: multi update action, should return 200 ok category view"() {
        given: "prepare data"
        def multiAction = CategoryDataFactory.getMultiUpdateAction()
        multiAction['version'] = cleanupMap.allObjects[id]

        when: "call api to update category description"
        def response = client.put([path: id, body: multiAction])

        then: "should return 200 and category view"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.name == multiAction.actions[0].name
        response.data.slug == multiAction.actions[1].slug
    }

    def "Test5: update with invalid actions, should return 400 bad request"() {
        given: "prepare data"
        def invalidAction = CategoryDataFactory.getInvalidCategoryUpdate().invalidAction
        invalidAction['version'] = cleanupMap.allObjects[id]

        when: "call api to update category"
        def response = client.put([path: id, body: invalidAction])

        then: "should return 400 bad request"
        response == 400
    }

    def "Test6: update with invalid version(string), should return 400 bad request"() {
        given: "prepare data"
        def invalidVersionWithString = CategoryDataFactory.getInvalidCategoryUpdate().invalidVersionWithString

        when: "call api to upate category"
        def response = client.put([path: id, body: invalidVersionWithString])

        then: "should return 400 bad request"
        response == 400
    }

    def "Test7: update with big integer version, should return 400 bad request"() {
        given: "prepare data"
        def invalidVersionWithBigInteger = CategoryDataFactory.getInvalidCategoryUpdate().invalidVersionWithBigInteger

        when: "call api to update category"
        def response = client.put([path: id, body: invalidVersionWithBigInteger])

        then: "should return 400 bad request"
        response == 400
    }

    def "Test8: update parent, should return 200 ok and category view"() {
        given: "prepare data"
        def subCategory = CategoryDataFactory.getCategory().validCategory2
        def subCategoryResponse = client.post(body: subCategory)
        def subCategoryId = subCategoryResponse.data.id
        def subCategoryVersion = subCategoryResponse.data.version
        def setParent = CategoryDataFactory.getSetParent()
        setParent['version'] = subCategoryVersion
        setParent.actions[0].parent.id = id

        when: "call api to update parent"
        def response = client.put([path: subCategoryId, body: setParent, requestContentType: "application/json"])

        then: "should return 200 ok and category view "
        response.status == 200
        response.data.parent == setParent.actions[0].parent
        response.data.id == subCategoryId
    }

    def "Test9: update external ID, should return 200 ok and category view"() {
        given: "prepare data"
        def setExternalID = CategoryDataFactory.getSetExternalID()
        setExternalID['version'] = cleanupMap.allObjects[id]

        when: "call api to update category description"
        def response = client.put([path: id, body: setExternalID])

        then: "should return 200 ok and category view"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.externalId == setExternalID.actions[0].externalId
    }

    def "Test10: update meta description, should return 200 ok and category view"() {
        given: "prepare data"
        def setMetaDescription = CategoryDataFactory.getSetMetaDescription()
        setMetaDescription['version'] = cleanupMap.allObjects[id]

        when: "call api to update meta description"
        def response = client.put([path: id, body: setMetaDescription])

        then: "should return 200 ok and category view"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.metaDescription == setMetaDescription.actions[0].metaDescription
    }

    def "Test11: update meta keywords, should return 200 ok and category view"() {
        given: "prepare data"
        def setMetaKeywords = CategoryDataFactory.getSetMetaKeywords()
        setMetaKeywords['version'] = cleanupMap.allObjects[id]

        when: "call api to update meta keywords"
        def response = client.put([path: id, body: setMetaKeywords])

        then: "should return 200 ok and category view"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.metaKeywords == setMetaKeywords.actions[0].metaKeywords
    }

    def "Test12: update meta title, should return 200 ok and category view"() {
        given: "prepare data"
        def setMetaTitle = CategoryDataFactory.getSetMetaTitle()
        setMetaTitle['version'] = cleanupMap.allObjects[id]

        when: "call api to update meta title"
        def response = client.put([path: id, body: setMetaTitle])

        then: "should return 200 ok and category view"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.metaTitle == setMetaTitle.actions[0].metaTitle
    }


    def "Test13: update parent with invalid parent, should return 404 not found"() {
        given: "prepare data"
        def subCategory = CategoryDataFactory.getCategory().validCategory3
        def subCategoryResponse = client.post(body: subCategory)
        def subCategoryId = subCategoryResponse.data.id
        def subCategoryVersion = subCategoryResponse.data.version
        def setParent = CategoryDataFactory.getSetParent()
        setParent['version'] = subCategoryVersion
        setParent.actions[0].parent.id = "this is an invalid parent id"

        when: "call api to update parent"
        def response = client.put([path: subCategoryId, body: setParent, requestContentType: "application/json"])

        then: "should return 404 not found"
        cleanupMap.addObject(subCategoryId, subCategoryVersion)
        response == 404

    }

    def "Test14: update category slug with wrong version, should return 409 conflict"() {
        given: "prepare data"
        def setSlug = CategoryDataFactory.getSetSlugAction()
        setSlug['version'] = 42342342

        when: "call api to update category slug"
        def response = client.put([path: id, body: setSlug])

        then: "should return 409 conflict"
        response == 409
    }
    def "Test15: update order hint, should return 200 ok and category view"() {
        given: "prepare data"
        def setOrderHint = CategoryDataFactory.getSetOrderHint()
        def previousOrderHint = setOrderHint.actions[0].previousOrderHint as Double
        def nextOrderHint = setOrderHint.actions[0].nextOrderHint as Double
        def medianOfTwoOrderHint = ((previousOrderHint + nextOrderHint) / 2).toString()
        setOrderHint['version'] = cleanupMap.allObjects[id]


        when: "call api to update order hint"
        def response = client.put([path: id, body: setOrderHint])

        then: "should return 200 ok and category view, new order hint should be average of two given order hint"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.orderHint == medianOfTwoOrderHint
    }

    def "Test16: update order hint with previous order hint and an empty next order hint, should return 200 ok and category view"() {
        given: "prepare data"
        def setOrderHint = CategoryDataFactory.getSetOrderHint()
        setOrderHint['nextOrderHint'] = ""
        setOrderHint['version'] = cleanupMap.allObjects[id]

        when: "call api to update order hint"
        def response = client.put([path: id, body: setOrderHint])

        then: "should return 200 ok and category view with a regenerated order hint"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
    }

    def "Test17: update order hint with invalid previous order hint, should return 400 bad request"() {
        given: "prepare data"
        def setOrderHint = CategoryDataFactory.getInvalidUpdateOrderHint()
        setOrderHint['version'] = cleanupMap.allObjects[id]

        when: "call api to update order hint"
        def response = client.put([path: id, body: setOrderHint])

        then: "should return 400 bad request"
        response == 400

    }

    def cleanupSpec() {
        if (!cleanupMap.isEmpty()) {
            CleanupUtil.cleanup(CategoryConfig.rootURL, cleanupMap)
        }
    }
}
