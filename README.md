# Rest Test
This repo is for microservice REST API testing
## How to run test
Taking security into account, so we have avoid writing all sensitive information into configuration file
and get it from environment variable. In this case, if you want to run  test, you have to 
set the environment variable `TEST_SERVER` first.
for Linux/Unix, you could run the below command in terminal or put it in `.bashrc`
```
export TEST_SERVER="your test server ip"
```
In this project , our build tool is gradle, so if you want to run test, 
you have to install `gradle` first. For OS X, you can just install gradle via homebrew
```
brew install gradle
```
and then run test:
```
gradle test
```
## Test Case
A test case should contains the following properties
1. description: The description of test case, because of feature provided by spock, so you can write your description into your function name 
2. requestData: Include url, apiName, parameter, method
3. configuration: An array of setups or configuration use for test case
4. expectedResult: The expected response of the request. The required result is 
http code, if a `data` is presented, the data should satisfy expectation
5.result: pass or failure
## Test module
Test case should be organized into test module which is dependent on what kind of service need to test
you could name a test module with the service you want to test. for example, if you want to test
 `customer-authentication` service, you could create a test module named `customer-authentication`

# The file structure
```
 src
    └── test //directory of test case
        ├── groovy
        │   └── io
        │       └── reactivesw
        │           ├── category //module needs to test
        │           │   └── config //configuration of module
        │           ├── config  //global configuration
        │           ├── customer //module needs to test
        │           │   └── authentication
        │           │       └── config //configuration of module
        │           └── util //some useful function
        └── resources //resource of test case
            ├── category // directory for test data (such as json file) 
            └── customer // directory for test data for customer service
                └── authentication

```

## How to write test
### Test Framework
we use spock to write test case instead of junit because spock is more elegant. Spock is simple and beautiful, 
but it is a little bit different from Junit, so if you have no clue about what exactly spock is, you should read 
the [Spock Framework Reference Documentation](http://spockframework.org/spock/docs/1.1-rc-3/index.html)
### StartUp
This Project is for restful api test,so you should write your test based on the restful api document. In this case, 
take customer-authentication service for example, first of all, you have to check [customer-authentication-service api document](https://github.com/reactivesw/customer-authentication/blob/master/docs/api.md)
Then, in the document, you could find :
1. Url of the target api
2. required parameter
3. http method (GET, PUT, DELETE, PUT, POST)
4. response result

According to the given method, you are able to write you own restful api test which should be:
1. test different situation of one method
2. provide some specific arguments to that method
3. test that the result is as expected
### Test Data
For some test, you may need  some mock data, so you should put your data in corresponding directory as
 `The file structure` describe above. Assuming you have some test data (in this case, json file), you should
read it and parse it for test. Taking `customer-authentication` for example, 
 ```java
    public static def getSignup(){
        return slurper.parse(new FileReader("src/test/resources/customer/authentication/SignUp.json"))
    }
 ```
 and then use it for in your test:
 ```java
    def "Test1: sign in with email and password should return customerView and the status of response should be 200"() {
        given: "prepare data that customer needs to sign in"
        def signinInformation = CustomerAuthenticationDataFactory.getSignin().validCustomer
        println CustomerAuthenticationConfig.ROOTURL

        when: "call signin api to sign in"
        def response = client.post(path: "signin", body: signinInformation, requestContentType: "application/json")

        then: "response status should be 200, and email should be equal to given signinInformation"
        with(response) {
            status == 200
            data.customerView.email == signinInformation.email
        }
 ```
### Attention, Please
In some test, maybe you have to post some mock data to your rest api, which will change status of your
service. For example, in the `category` service, if you add some mock category, when you finish your
test, you should clean up your mock data. Don't you hope to represent some mock data to your real customers
in the future?

in some way, the goal of writing test is to find some potential bugs, so you should think about a lot
of different situation to dig out bugs (but afraid that your developer will not be pleased with bugs)