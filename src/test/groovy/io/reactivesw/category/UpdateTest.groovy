package io.reactivesw.category

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.util.CategoryDataFactory
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by Davis on 17/3/10.
 */
class UpdateTest extends Specification {
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

    def "Test1: update category name, should return 200 ok"() {
        given: "prepare data"
        def setName = CategoryDataFactory.getSetNameAction()
        setName['version'] = cleanupMap.allObjects[id]

        when: "call api to update ctegory name"
        def response = client.put([path: id, body: setName])

        then: "return 200 and category with new name"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.name == setName.actions[0].name

    }

    def "Test2: update category slug, should return 200 ok"() {
        given: "prepare data"
        def setSlug = CategoryDataFactory.getSetSlugAction()
        setSlug['version'] = cleanupMap.allObjects[id]

        when: "call api to update category slug"
        def response = client.put([path: id, body: setSlug])

        then: "return 200 and category with new slug"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.slug == setSlug.actions[0].slug
    }

    def "Test3: update category description, should return 200 ok"() {
        given: "prepare data"
        def setDescription = CategoryDataFactory.getSetDescriptionAction()
        setDescription['version'] = cleanupMap.allObjects[id]

        when: "call api to update category description"
        def response = client.put([path: id, body: setDescription])

        then: "return 200 and category with new description"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.description == setDescription.actions[0].description
    }

    def "Test4: multi update action, should return 200 ok"() {
        given: "prepare data"
        def multiAction = CategoryDataFactory.getMultiUpdateAction()
        multiAction['version'] = cleanupMap.allObjects[id]

        when: "call api to update category description"
        def response = client.put([path: id, body: multiAction])

        then: "return 200 and category with new params"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.name == multiAction.actions[0].name
        response.data.slug == multiAction.actions[1].slug
    }


    def "Test5: update with invalid actions, should return 400 bad request"() {
        given: "prepare invalid action"
        def invalidAction = CategoryDataFactory.getInvalidCategoryUpdate().invalidAction
        invalidAction['version'] = cleanupMap.allObjects[id]

        when: "call api to update category"
        def response = client.put([path: id, body: invalidAction])

        then: "response should be 400"
        response == 400
    }

    def "Test6: update with invalid version(String), should return 400 bad request"() {
        given: "prepare invalid version(string) "
        def invalidVersionWithString = CategoryDataFactory.getInvalidCategoryUpdate().invalidVersionWithString

        when: "call api to upate category"
        def response = client.put([path: id, body: invalidVersionWithString])

        then: "response should be 400"
        response == 400
    }

    def "Test7: update with big integer version, should return 400 bad request"() {
        given: "prepare invalid version (big integer)"
        def invalidVersionWithBigInteger = CategoryDataFactory.getInvalidCategoryUpdate().invalidVersionWithBigInteger

        when: "call api to update category"
        def response = client.put([path: id, body: invalidVersionWithBigInteger])

        then: "response should be 400"
        response == 400
    }

    def "Test9: update external ID, should return 200 ok and category view"() {
        given: "prepare externalID"
        def setExternalID = CategoryDataFactory.getSetExternalID()
        setExternalID['version'] = cleanupMap.allObjects[id]

        when: "call api to update category description"
        def response = client.put([path: id, body: setExternalID])

        then: "the status of response should be 200,and with new externalID"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.externalId == setExternalID.actions[0].externalId
    }

    def "Test10: update metadescription, should return 200 ok and category view"() {
        given: "prepare meta description"
        def setMetaDescription = CategoryDataFactory.getSetMetaDescription()
        setMetaDescription['version'] = cleanupMap.allObjects[id]

        when: "call api to update meta description"
        def response = client.put([path: id, body: setMetaDescription])

        then: "the status of response should be 200,and with new meta description"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.metaDescription == setMetaDescription.actions[0].metaDescription
    }

    def "Test11: update meta keywords, should return 200 ok and category view"() {
        given: "prepare meta keywords"
        def setMetaKeywords = CategoryDataFactory.getSetMetaKeywords()
        setMetaKeywords['version'] = cleanupMap.allObjects[id]

        when: "call api to update meta keywords"
        def response = client.put([path: id, body: setMetaKeywords])

        then: "the status of response should be 200,and with new meta description"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.metaKeywords == setMetaKeywords.actions[0].metaKeywords
    }

    def "Test12: update meta title, should return 200 ok and category view"() {
        given: "prepare meta title"
        def setMetaTitle = CategoryDataFactory.getSetMetaTitle()
        setMetaTitle['version'] = cleanupMap.allObjects[id]

        when: "call api to update meta title"
        def response = client.put([path: id, body: setMetaTitle])

        then: "the status of response should be 200,and with new meta title "
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.metaTitle == setMetaTitle.actions[0].metaTitle
    }

    def "Test13: update parent,should return 200 ok and category view"() {
        given: "prepare  sub category  data"
        def subCategory = CategoryDataFactory.getCategory().validCategory2
        def subCategoryResponse = client.post(body: subCategory)
        def subCategoryId = subCategoryResponse.data.id
        def subCategoryVersion = subCategoryResponse.data.version
        def setParent = CategoryDataFactory.getSetParent()
        setParent['version'] = subCategoryVersion
        setParent.actions[0].parent.id = id

        when: "call api to update parent"
        def response = client.put([path: subCategoryId, body: setParent, requestContentType: "application/json"])

        then: "the status of response should be 200,and category view "
        response.status == 200
        response.data.parent == setParent.actions[0].parent
        response.data.id == subCategoryId
    }

    def cleanupSpec() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, cleanupMap)
    }
}
