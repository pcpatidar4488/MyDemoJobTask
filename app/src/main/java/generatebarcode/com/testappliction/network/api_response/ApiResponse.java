package generatebarcode.com.testappliction.network.api_response;

/**
 * Created by punamchand on 18-Oct-18.
 */

public class ApiResponse {
    public int status;
    public String message;
    public String imageString;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
}
