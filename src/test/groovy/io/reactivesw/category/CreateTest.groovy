package io.reactivesw.category

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.util.CategoryDataFactory
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by Davis on 17/3/9.
 */
class CreateTest extends Specification {
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

    def client = RestClientFactory.getJsonClient(CategoryConfig.rootURL)

    def "Test1 : create category with name and slug, should return 200 ok and new category"() {
        given: "prepare category data"
        def category = CategoryDataFactory.getCategory().validCategory1

        when: "call category api to create category"
        def response = client.post(body: category)

        then: "response status should be 200, name and slug should be equal to given category"
        response.status == 200
        response.data.name == category.name
        response.data.slug == category.slug
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test2 : create category with all parameter, should return 200 ok and new category"() {
        given: "prepare category data"
        def category = CategoryDataFactory.getCategoryWithAllParams()

        when: "call category api to create category"
        def response = client.post(body: category, requestContentType: "application/json")

        then: "response status should be 200, params should be equal to given category"
        response.status == 200
        def newCategory = response.data
        category.name == newCategory.name
        category.slug == newCategory.slug
        category.description == newCategory.description
        category.externalId == newCategory.externalId
        category.metaDescription == newCategory.metaDescription
        category.metaKeywords == newCategory.metaKeywords
        category.metaTitle == newCategory.metaTitle
        category.orderHint == newCategory.orderHint

        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test3: create category with invalid char in slug, should return 400 bad request"() {
        given: "prepare category data that include invalid char in slug"
        def includeInvalidCharCategory = CategoryDataFactory.getCategory().includeInvalidCharCategory

        when: "call category api to create category"
        def response = client.post(body: includeInvalidCharCategory)

        then: "response status should be 400"
        response == 400
    }

    def "Test4: create category with 1-char length(less than minimum size) slug, should return 400 bad request"() {
        given: "prepare category data including 1-char length slug"
        def lessThanMinimumSizeCategory = CategoryDataFactory.getCategory().lessThanMinimumSizeCategory

        when: "call category api to create category"
        def response = client.post(body: lessThanMinimumSizeCategory)

        then: "the status of response should be 400"
        response == 400
    }

    def "Test5: create category with 277-char length(great than maximum size) slug, should return 400 bad request"() {
        given: "prepare category data including 277-char length slug"
        def greatThanMaximumSizeCategory = CategoryDataFactory.getCategory().greatThanMaximumSizeCategory

        when: "call category api to create category"
        def response = client.post(body: greatThanMaximumSizeCategory)

        then: "the status of response should be 400"
        response == 400
    }

    def "Test6: create category without slug, should return 400 bad request"() {
        given: "prepare category data without slug"
        def withoutSlugCategory = CategoryDataFactory.getCategory().withoutSlugCategory

        when: "call category api to create category"
        def response = client.post(body: withoutSlugCategory)

        then: "the status of response should be 400"
        response == 400
    }

    def "Test7: create category without name, should return 400 bad request"() {
        given: "prepare category data without name"
        def withoutNameCategory = CategoryDataFactory.getCategory().withoutNameCategory

        when: "call category api to create category"
        def response = client.post(body: withoutNameCategory)

        then: "the status of response should be 400"
        response == 400
    }

    def cleanupSpec() {
        def url = CategoryConfig.rootURL
        CleanupUtil.cleanup(url, cleanupMap)
    }
}
