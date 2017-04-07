import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.util.CategoryDataFactory
import io.reactivesw.util.RestClientFactory

class AddMockData {
    static def clothesCategoryId

    public static def addCategoryData() {
        def client = RestClientFactory.getClient(CategoryConfig.rootURL)
        def clothesCategory = CategoryDataFactory.getClothes()
        def response = client.post(body: clothesCategory, requestContentType: "application/json")
        clothesCategoryId = response.data.categoryView.id
        println response.data.categoryView.id
    }


    public static void main(String[] args) {
        addCategoryData()
    }
}