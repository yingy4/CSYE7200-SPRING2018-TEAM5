package retrieval;

import java.util.HashMap;
import java.util.Map;


/*
 * This class shows how to make a simple authenticated call to the
 * Amazon Product Advertising API.
 *
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
public class JavaCodeSnippet {

    /*
     * Your Access Key ID, as taken from the Your Account page.
     */
    private static final String ACCESS_KEY_ID = "AKIAJVADVVC5WAOOAQHA";

    /*
     * Your Secret Key corresponding to the above ID, as taken from the
     * Your Account page.
     */
    private static final String SECRET_KEY = "9MA5mQrkHgkK2g+MtPIrQucz5sGo0URy6gVPPeWT";

    /*
     * Use the end-point according to the region you are interested in.
     */
    private static final String ENDPOINT = "webservices.amazon.com";

    public String generateUrl() {

        /*
         * Set up the signed requests helper.
         */
        SignedRequestsHelper helper;
        try {
            helper = new SignedRequestsHelper(ENDPOINT, ACCESS_KEY_ID, SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String requestUrl = null;

        Map<String, String> params = new HashMap<String, String>();

        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemSearch");
        params.put("AWSAccessKeyId", "AKIAJVADVVC5WAOOAQHA");
        params.put("AssociateTag", "scalaproject-20");
        params.put("SearchIndex", "All");
        params.put("Keywords", "Boots");
        params.put("ResponseGroup", "Images,ItemAttributes");

        requestUrl = helper.sign(params);
        //System.out.println("Signed URL: \"" + requestUrl + "\"");
        //System.out.println(requestUrl);
        return requestUrl;
    }
}

