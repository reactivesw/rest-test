class ProductTypeDataFactory {
    static def slurper = new groovy.json.JsonSlurper()

    public static def getClothes() {
        return slurper.parse(new FileReader("src/test/resources/product_type/clothes.json"))
    }
    public static def getComputer(){
        return slurper.parse(new FileReader("src/test/resources/product_type/computer.json"))
    }
    public static def getPants(){
        return slurper.parse(new FileReader("src/test/resources/product_type/pants.json"))
    }
    public static def getSmartPhone(){
        return slurper.parse(new FileReader("src/test/resources/product_type/pants.json"))
    }
    public static def getGamingLaptop(){
        return slurper.parse(new FileReader("src/test/resources/product_type/gaming_lap_top.json"))
    }
}