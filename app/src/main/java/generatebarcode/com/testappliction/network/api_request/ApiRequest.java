package generatebarcode.com.testappliction.network.api_request;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by punamchand on 18-Oct-18.
 */

public class ApiRequest {
   /* Map<String,String> map;

    public ApiRequest(Context context){
        map = new HashMap<>();
        map.put("image","");
    }*/

    Map<String,String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
