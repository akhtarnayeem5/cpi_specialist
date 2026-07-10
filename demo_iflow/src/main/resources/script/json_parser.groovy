import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper;
import groovy.json.JsonOutput;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String)
    def jsonSlurper = new JsonSlurper()
    def json = jsonSlurper.parseText(body)
    def employee = json.EmployeeDetails.Employee 
    def emp_names = employee.findAll{it -> it.designation == "Senior Manager"}.collect{it.name}

    println(employee.findAll{true}.collect{it.name}.join("_"))
    
    
    return message
}
