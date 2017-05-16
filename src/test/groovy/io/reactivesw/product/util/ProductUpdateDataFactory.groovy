package io.reactivesw.product.util

/**
 * Data factory for product update action.
 */
class ProductUpdateDataFactory {
    static def slurper = new groovy.json.JsonSlurper()

    public static def getPublish() {
        return slurper.parse(new FileReader('src/test/resources/product/update/Publish.json'))
    }

    public static def getUnPublish() {
        return slurper.parse(new FileReader('src/test/resources/product/update/UnPublish.json'))
    }

    public static def getRevertStagedChanges() {
        return slurper.parse(new FileReader('src/test/resources/product/update/RevertStagedChanges.json'))
    }

    public static def getSetKey() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetKey.json'))
    }

    public static def getSetName() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetName.json'))
    }

    public static def getAddExternalImage() {
        return slurper.parse(new FileReader('src/test/resources/product/update/AddExternalImage.json'))
    }

    public static def getRemoveImage() {
        return slurper.parse(new FileReader('src/test/resources/product/update/RemoveImage.json'))
    }

    public static def getAddToCategory() {
        return slurper.parse(new FileReader('src/test/resources/product/update/AddToCategory.json'))
    }

    public static def getRemoveFromCategory() {
        return slurper.parse(new FileReader('src/test/resources/product/update/RemoveFromCategory.json'))
    }

    public static def getSetCategoryOrderHint() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetCategoryOrderHint.json'))
    }

    public static def getSetDescription() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetDescription.json'))
    }

    public static def getSetSlug() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetSlug.json'))
    }

    public static def getSetMetaDescription() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetMetaDescription.json'))
    }

    public static def getSetMetaKeywords() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetMetaKeywords.json'))
    }

    public static def getSetMetaTitle() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetMetaTitle.json'))
    }

    public static def getSetSearchKeywords() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetSearchKeywords.json'))
    }

    public static def getAddPrice() {
        return slurper.parse(new FileReader('src/test/resources/product/update/AddPrice.json'))
    }

    public static def getChangePrice() {
        return slurper.parse(new FileReader('src/test/resources/product/update/ChangePrice.json'))
    }

    public static def getRemovePrice() {
        return slurper.parse(new FileReader('src/test/resources/product/update/RemovePrice.json'))
    }

    public static def getSetPrices() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetPrices.json'))
    }

    public static def getSetAttribute() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetAttribute.json'))
    }

    public static def getSetAttributeInAllVariants() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetAttributeInAllVariants.json'))
    }

    public static def getAddVariant() {
        return slurper.parse(new FileReader('src/test/resources/product/update/AddVariant.json'))
    }

    public static def getChangeMasterVariant() {
        return slurper.parse(new FileReader('src/test/resources/product/update/ChangeMasterVariant.json'))
    }

    public static def getRemoveVariant() {
        return slurper.parse(new FileReader('src/test/resources/product/update/RemoveVariant.json'))
    }

    public static def getSetSku() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetSku.json'))
    }

    public static def getSetVariantKey() {
        return slurper.parse(new FileReader('src/test/resources/product/update/SetVariantKey.json'))
    }
}
