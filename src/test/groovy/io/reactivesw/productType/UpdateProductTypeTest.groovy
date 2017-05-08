import io.reactivesw.config.ProductTypeConfig
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.ProductTypeDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

class UpdateProductTypeTest extends Specification {
    @Shared
    def id
    @Shared
    def version
    @Shared
    def client
    @Shared
    def productType
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

    def setupSpec() {
        productType = ProductTypeDataFactory.getCreateProductType().validProductType1
        client = RestClientFactory.getClient(ProductTypeConfig.ROOTURL)
        def response = client.post(body: productType, requestContentType: "application/json")
        id = response.data.id
        version = response.data.version
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test1: update product type with valid name, should return 200 ok and productTypeView"() {

        given: "prepare data"
        def setName = ProductTypeDataFactory.getUpdateProductType().validSetName
        setName['version'] = cleanupMap.allObjects[id]

        when: "call function to update product type name"
        def response = client.put([path: id, body: setName, requestContentType: "application/json"])

        then: "should return 200 ok and productTypeView"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.name == setName.actions[0].name
    }

    def "Test2: update product type with empty name, should return 400 bad request"() {
        given: "prepare data"
        def invalidSetName = ProductTypeDataFactory.getUpdateProductType().invalidSetName
        invalidSetName['version'] = cleanupMap.allObjects[id]

        when: "call function to update product type name"
        def response = client.put([path: id, body: invalidSetName, requestContentType: "application/json"])

        then: "should return 400 bad request"
        response == 400
    }

    def "Test3: update product type with valid description, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def setDescription = ProductTypeDataFactory.getUpdateProductType().validSetDescription
        setDescription['version'] = cleanupMap.allObjects[id]

        when: "call function to update product type description"
        def response = client.put([path: id, body: setDescription, requestContentType: "application/json"])

        then: "should return 200 ok and productTypeView"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        response.data.description == setDescription.actions[0].description
    }

    def "Test4: update product type with incorrect version, should return 409 conflict"() {
        given: "prepare data"
        def setDescription = ProductTypeDataFactory.getUpdateProductType().validSetDescription
        setDescription['version'] = 28883838

        when: "call function to update product type description"
        def response = client.put([path: id, body: setDescription, requestContentType: "application/json"])

        then: "should return 409 conflict"
        response == 409
    }

    def "Test5: update product type with invalid attribute, should return 400 bad request and productTypeView"() {
        given: "prepare data"
        def invalidAddAttributeDefinition = ProductTypeDataFactory.getUpdateProductType().invalidAddAttributeDefinition
        invalidAddAttributeDefinition['version'] = cleanupMap.allObjects[id]

        when: "call function to update product type attributes"
        def response = client.put([path: id, body: invalidAddAttributeDefinition, requestContentType: "application/json"])

        then: "should return 400 bad request"
        response == 400
    }

    def "Test6: update product type with valid attribute, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def validAddAttributeDefinition = ProductTypeDataFactory.getUpdateProductType().validAddAttributeDefinition
        validAddAttributeDefinition['version'] = cleanupMap.allObjects[id]

        when: "call function to update product type attributes"
        def response = client.put([path: id, body: validAddAttributeDefinition, requestContentType: "application/json"])

        then: "should return 200 ok and productTypeView"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200

    }

    def "Test7: update product type with valid remove attribute definition, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def validRemoveAttributeDefinition = ProductTypeDataFactory.getUpdateProductType().validRemoveAttributeDefinition
        validRemoveAttributeDefinition['version'] = cleanupMap.allObjects[id]

        when: "call function to remove attribute definition"
        def response = client.put([path: id, body: validRemoveAttributeDefinition, requestContentType: "application/json"])

        then: "should return 200 ok and productTypeView"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
    }

    def "Test8: update product type with wrong attribute name, should return 404 not found"() {
        given: "prepare data"
        def invalidRemoveAttributeDefinition = ProductTypeDataFactory.getUpdateProductType().invalidRemoveAttributeDefinition
        invalidRemoveAttributeDefinition['version'] = cleanupMap.allObjects[id]

        when: "call function to remove attribute definition"
        def response = client.put([path: id, body: invalidRemoveAttributeDefinition, requestContentType: "application/json"])

        then: "should return 404 not found"
        response == 404
    }

    def "Test9: update product type with valid attribute label, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def validSetAttributeLabel = ProductTypeDataFactory.getUpdateProductType().validSetAttributeLabel
        validSetAttributeLabel['version'] = cleanupMap.allObjects[id]

        when: "call function to set attribute label"
        def response = client.put([path: id, body: validSetAttributeLabel, requestContentType: "application/json"])

        then: "should return 200 ok and productTypeView"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
    }

    def "Test10: update product type with valid attribute inputTip, should return 200 and productTypeView"() {
        given: "prepare data"
        def validSetAttributeInputTip = ProductTypeDataFactory.getUpdateProductType().validSetAttributeInputTip
        validSetAttributeInputTip['version'] = cleanupMap.allObjects[id]

        when: "call function to set attribute inputTip"
        def response = client.put([path: id, body: validSetAttributeInputTip, requestContentType: "application/json"])

        then: "should return 200 ok and productTypeView"
        cleanupMap.addObject(response.data.id, response.data.version)
        with(response) {
            status == 200
        }
    }

    def "Test11: update product type with attribute searchable, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def setSearchable = ProductTypeDataFactory.getUpdateProductType().validSetSearchable
        setSearchable['version'] = cleanupMap.allObjects[id]

        when: "call function to set attribute searchable"
        def response = client.put([path: id, body: setSearchable, requestContentType: "application/json"])

        then: "should return 200 ok and productTypeView"
        cleanupMap.addObject(response.data.id, response.data.version)
        with(response) {
            status == 200
        }
    }

    def "Test12: update product type with invalid attribute searchable, should return 400 bad request"() {
        given: "prepare data"
        def invalidSetSearchable = ProductTypeDataFactory.getUpdateProductType().invalidSetSearchable
        invalidSetSearchable['version'] = cleanupMap.allObjects[id]

        when: "call function to set attribute searchable"
        def response = client.put([path: id, body: invalidSetSearchable, requestContentType: "application/json"])

        then: "should return 400 bad request"
        response == 400
    }

    def "Test13: update product type with plain enum value, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def validSetSearchable = ProductTypeDataFactory.getUpdateProductType().validAddPlainEnumValue
        validSetSearchable['version'] = cleanupMap.allObjects[id]

        when: "call function to set attribute searchable"
        def response = client.put([path: id, body: validSetSearchable, requestContentType: "application/json"])

        then: "should return 200 ok"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200

    }

    def "Test14: update attribute order of product type, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def validSetAttributeOrder = ProductTypeDataFactory.updateProductType.validSetAttributeOrder
        def originalProductType = client.get(path: id)
        validSetAttributeOrder['version'] = cleanupMap.allObjects[id]
        def attributes = originalProductType.data.attributes
        Collections.shuffle(attributes)
        validSetAttributeOrder.actions[0].attributes = attributes

        when: "call function to update attribute order"
        def response = client.put([path: id, body: validSetAttributeOrder, requestContentType: "application/json"])

        then: "should return 200 ok and productTypeView"
        println response
        cleanupMap.addObject(response.data.id, response.data.version)
        attributes == response.data.attributes
        response.status == 200
    }

    def "Test15: update product type with adding LocalizedEnumValue, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def validAddLocalizedEnumValue = ProductTypeDataFactory.updateProductType.validAddLocalizedEnumValue
        validAddLocalizedEnumValue['version'] = cleanupMap.allObjects[id]

        when: "call function to add localizedEnumValue"
        def response = client.put([path: id, body: validAddLocalizedEnumValue, requestContentType: "application/json"])

        then: "should return 200 ok"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
    }

    def "Test16: update plain enum value order of product type, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def createProductType = ProductTypeDataFactory.getCreateProductType().validProductType2
        def createResponse = client.post(body: createProductType, requestContentType: "application/json")
        id = createResponse.data.id
        def validSetPlainEnumValueOrder = ProductTypeDataFactory.updateProductType.validSetPlainEnumValueOrder
        def originalProductType = client.get(path: id)
        validSetPlainEnumValueOrder['version'] = createResponse.data.version
        def values = originalProductType.data.attributes[2].type.values
        Collections.shuffle(values)
        validSetPlainEnumValueOrder.actions[0].values = values

        when: "call function to update plainEnumValueOrder"
        def response = client.put([path: id, body: validSetPlainEnumValueOrder, requestContentType: "application/json"])

        then: "should return 200 ok and productTypeView"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        values == response.data.attributes[2].type.values
    }

    def "Test17: update localized enum value order of product type, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def validSetLocalizedEnumValueOrder = ProductTypeDataFactory.updateProductType.validSetLocalizedEnumValueOrder
        def originalProductType = client.get(path: id)
        validSetLocalizedEnumValueOrder['version'] = cleanupMap.allObjects[id]
        def values = originalProductType.data.attributes[3].type.values
        Collections.shuffle(values)
        validSetLocalizedEnumValueOrder.actions[0].values = values

        when: "call function to update localizedEnumValueOrder"
        def response = client.put([path: id, body: validSetLocalizedEnumValueOrder, requestContentType: "application/json"])

        then: "should return 200 ok and productTypeView"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
        values == response.data.attributes[3].type.values
    }

    def "Test18: update plain enum value label of product type, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def validSetPlainEnumValueLabel = ProductTypeDataFactory.updateProductType.validSetPlainEnumValueLabel
        validSetPlainEnumValueLabel['version'] = cleanupMap.allObjects[id]

        when: "call function to set plain enum value label"
        def response = client.put([path: id, body: validSetPlainEnumValueLabel, requestContentType: "application/json"])

        then: "should return 200 ok"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
    }

    def "Test19: update localized enum value label, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def validSetLocalizedEnumValueLabel = ProductTypeDataFactory.updateProductType.validSetLocalizedEnumValueLabel
        validSetLocalizedEnumValueLabel['version'] = cleanupMap.allObjects[id]

        when: "call function to set localized enum value label"
        def response = client.put([path: id, body: validSetLocalizedEnumValueLabel, requestContentType: "application/json"])

        then: "should return 200 ok"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
    }

    def "Test20: update product type with multiple actions, should return 200 ok and productTypeView"() {
        given: "prepare data"
        def multiUpdate = ProductTypeDataFactory.updateProductType.multiUpdate
        multiUpdate['version'] = cleanupMap.allObjects[id]

        when: "call function to update product type"
        def response = client.put([path: id, body: multiUpdate, requestContentType: "application/json"])

        then: "should return 200 ok"
        cleanupMap.addObject(response.data.id, response.data.version)
        response.status == 200
    }

    def cleanupSpec() {
        if (!cleanupMap.isEmpty()) {
            CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, cleanupMap)
        }
    }
}