import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.logging.Log
import java.util.HashMap

def Message processData(Message message) {
    // Read body
    def body = message.getBody(String)
    
    // Read headers and properties
    def headers = message.getHeaders()
    def properties = message.getProperties()

    // Get iFlow name from property (set in CPI) or use fallback
    def iflowName = properties.get("CamelSapMessageContextId") ?: 
                    properties.get("SAP_ApplicationID") ?: 
                    "Unknown_iFlow"

    // Add custom header/property
    message.setHeader("iflow_name", iflowName)
    message.setProperty("iflow_name", iflowName)

    // Log incoming payload
    def messageLog = messageLogFactory?.getMessageLog(message)
    if (messageLog != null) {
        messageLog.addAttachmentAsString("IncomingPayload", body, "text/plain")
        messageLog.addAttachmentAsString("Headers", headers.toString(), "text/plain")
        messageLog.addAttachmentAsString("Properties", properties.toString(), "text/plain")
    }

    // Read throwError header and normalize to boolean
    def throwErrorVal = headers.get("throwError")
    def shouldThrowError = false

    if (throwErrorVal != null) {
        shouldThrowError = throwErrorVal.toString().equalsIgnoreCase("true")
    }

    // Throw runtime exception if requested
    if (shouldThrowError) {
        throw new RuntimeException("Intentional error triggered by header throwError=true")
    }

    // Set body back if needed
    message.setBody(body)

    return message
}