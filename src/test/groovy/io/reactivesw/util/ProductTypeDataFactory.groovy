package io.reactivesw.util

class ProductTypeDataFactory {
    static def slurper = new groovy.json.JsonSlurper()

    public static def getCreateProductType() {
        return slurper.parse(new FileReader('src/test/resources/productType/CreateProductType.json'))
    }
}